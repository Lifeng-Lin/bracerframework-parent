package cn.bracerframework.redis;

import cn.bracerframework.core.util.JsonUtil;
import cn.bracerframework.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 字符串键值数据操作类
 *
 * @author Lifeng.Lin
 */
public class StringRedisService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * redis 模板
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 保存</br>
     * 会将对象 JSON 化存储起来
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean save(String key, T value) {
        return save(key, JsonUtil.toStrContainNull(value));
    }

    /**
     * 保存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean save(String key, String value) {
        return save(key, value, -1);
    }

    /**
     * 保存
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间，<= 0 时永久生效，单位秒
     * @return
     */
    public <T> boolean save(String key, T value, long time) {
        return save(key, JsonUtil.toStrContainNull(value), time);
    }

    /**
     * 保存
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间，<= 0 时永久生效，单位秒
     * @return
     */
    public boolean save(String key, String value, long time) {
        try {
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            valueOperations.set(key, value);

            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            } else {
                long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
                if (expire > 0) {
                    stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
                }
            }

            return true;
        } catch (Throwable e) {
            logger.error("redis 存储【 " + key + " - " + value + " 】失败", e);
            return false;
        }
    }

    /**
     * 获取有效期</br>
     * 单位：秒
     *
     * @param key 键
     * @return
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 更新有效期
     *
     * @param key     键
     * @param timeout 有效期：秒
     * @return
     */
    public boolean expire(String key, long timeout) {
        return stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return
     */
    public String get(String key) {
        try {
            ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
            return valueOps.get(key);
        } catch (Throwable e) {
            logger.error("redis获取【 " + key + " 】失败", e);
            return null;
        }
    }

    /**
     * 获取对象
     *
     * @param key   键
     * @param clazz 数据对象类型
     * @return
     */
    public <T> T getObj(String key, Class<T> clazz) {
        String data = get(key);
        if (StrUtil.isEmpty(data)) {
            return null;
        }
        return JsonUtil.toBean(data, clazz);
    }

    /**
     * 获取List指定对象
     *
     * @param key   键
     * @param clazz List 内数据对象类型
     * @return
     */
    public <T> List<T> getArrayObj(String key, Class<T> clazz) {
        String data = get(key);
        if (StrUtil.isEmpty(data)) {
            return null;
        }
        return JsonUtil.toList(data, clazz);
    }

    /**
     * 获取对象
     *
     * @param key           键
     * @param typeReference 数据对象类型
     * @return
     */
    public <T> T getObj(String key, TypeReference<T> typeReference) {
        String data = get(key);
        if (StrUtil.isEmpty(data)) {
            return null;
        }
        return JsonUtil.parseObject(data, typeReference);
    }

    /**
     * 删除指定键
     *
     * @param key 键
     * @return
     */
    public boolean delete(String key) {
        try {
            stringRedisTemplate.delete(key);
            return true;
        } catch (Throwable e) {
            logger.error("redis 删除【 " + key + " 】失败", e);
            return false;
        }
    }

    /**
     * redis 判断键是否存在
     *
     * @param key 键
     * @return
     */
    public boolean hasKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Throwable e) {
            logger.error("redis 无法判断键【 " + key + " 】是否存在", e);
            return false;
        }
    }

    /**
     * 查询 Redis 所有键列表
     *
     * @return
     */
    public Set<String> list() {
        return list(null);
    }

    /**
     * 模糊查询 Redis 键列表
     *
     * @param key 键
     * @return
     */
    public Set<String> list(String key) {
        if (StrUtil.isEmpty(key)) {
            return stringRedisTemplate.keys("*");
        } else {
            return stringRedisTemplate.keys("*" + key + "*");
        }
    }

    /**
     * 模糊查询 Redis 值列表
     *
     * @param search key查询条件
     * @param clazz  返回列表对象类型
     * @param <T>    对象类型
     * @return
     */
    public <T> List<T> listVal(String search, Class<T> clazz) {
        Set<String> keys = new HashSet<>(0);
        if (StrUtil.isEmpty(search)) {
            keys = stringRedisTemplate.keys("*");
        } else {
            keys = stringRedisTemplate.keys("*" + search + "*");
        }
        List<T> ls = new ArrayList<>();
        for (String key : keys) {
            ls.add(this.getObj(key, clazz));
        }
        return ls;
    }

    /**
     * 删除指定前缀的键
     *
     * @param prefix 键前缀
     * @return 删除失败的键
     */
    public List<String> clear(String prefix) {
        List<String> msg = new ArrayList<>();
        Set<String> keys = stringRedisTemplate.keys(prefix + "*");
        for (String key : keys) {
            if (!delete(key)) {
                msg.add(key);
            }
        }
        return msg;
    }

}