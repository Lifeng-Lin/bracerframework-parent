package cn.bracerframework.core.pojo;

import cn.bracerframework.core.util.StrUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作反馈
 *
 * @author Lifeng.Lin
 */
public class OperInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作是否成功
     */
    private boolean isOk = true;

    /**
     * 操作结果信息
     */
    private String msg = null;

    /**
     * 返回数据
     */
    private Map<String, Object> data = null;

    public OperInfo() {
        this.isOk = true;
        this.msg = "操作成功";
        this.data = new HashMap<>();
    }

    /**
     * 返回成功
     *
     * @return
     */
    public static OperInfo ok() {
        return ok(null, null);
    }

    /**
     * 返回成功并携带一个数据对象
     *
     * @param key
     * @param value
     * @return
     */
    public static OperInfo ok(String key, Object value) {
        if (StrUtil.isNotEmpty(key)) {
            Map<String, Object> data = new HashMap<>(1);
            data.put(key, value);
            return ok(data);
        }
        return ok(new HashMap<>(0));
    }

    /**
     * 返回成功并携带多个数据对象
     *
     * @param data
     * @return
     */
    public static OperInfo ok(Map<String, Object> data) {
        OperInfo operInfo = new OperInfo();
        operInfo.setData(data);
        return operInfo;
    }

    /**
     * 返回失败
     *
     * @param msg 报错信息
     * @return
     */
    public static OperInfo err(String msg) {
        OperInfo operInfo = new OperInfo();
        operInfo.setIsOk(false);
        operInfo.setMsg(msg);
        return operInfo;
    }

    /**
     * @return 操作是否成功
     */
    public boolean getIsOk() {
        return isOk;
    }

    /**
     * @param isOk 操作是否成功
     */
    public void setIsOk(boolean isOk) {
        this.isOk = isOk;
    }

    /**
     * @return 操作结果信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg 操作结果信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return 返回数据
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * @param data 返回数据
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * 添加附加数据项
     *
     * @param key   数据键
     * @param value 数据值
     */
    public void put(String key, Object value) {
        this.data.put(key, value);
    }

    /**
     * 获取字符串类型附加项
     *
     * @param key 数据键
     * @return
     */
    public String getString(String key) {
        return String.valueOf(this.data.get(key));
    }

    /**
     * 获取附加项
     *
     * @param key 数据键
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) this.data.get(key);
    }

}