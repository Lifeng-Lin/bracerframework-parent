package cn.bracerframework.core.util;

import cn.bracerframework.core.exception.OperationException;
import cn.bracerframework.core.exception.SystemException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具类<br/>
 * 扩展自 {@link cn.hutool.http.HttpUtil}
 * 此工具部分方法依赖第三方 httpclient 包
 *
 * @author Lifeng.Lin
 */
public class HttpUtil extends cn.hutool.http.HttpUtil {

    public static final String HTTP = "http";
    public static final String URL_SPLIT = "?";

    /**
     * 创建 {@link UrlEncodedFormEntity} 实体对象
     *
     * @param parameters 参数数据 {@link NameValuePairPost}
     * @return UrlEncodedFormEntity 实体对象
     */
    public static HttpEntity createEntity(List<NameValuePairPost> parameters) {
        try {
            return new UrlEncodedFormEntity(parameters);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("创建实体对象 UrlEncodedFormEntity 失败", e);
        }
    }

    /**
     * 创建 {@link StringEntity} 实体对象</br>
     * 通过 {@link JsonUtil} 对对象转 Json 操作
     *
     * @param o 参数数据
     * @return StringEntity 实体对象
     */
    public static HttpEntity createEntityWithJson(Object o) {
        return new StringEntity(JsonUtil.toStrContainNull(o), Charset.defaultCharset());
    }

    /**
     * 以字符串形式获取实体内容
     *
     * @param entity 实体对象
     * @return 字符串实体内容
     */
    public static String getString(HttpEntity entity) {
        try {
            return EntityUtils.toString(entity, Charset.defaultCharset());
        } catch (ParseException | IOException e) {
            throw new RuntimeException("以字符串形式获取实体 HttpEntity 文本数据失败", e);
        }
    }

    /**
     * 以对象形式获取实体内容<br/>
     * 通过 {@link JsonUtil} 对返回的 Json 字符串转对象操作
     *
     * @param entity 实体对象
     * @param t      内容对应对象类型
     * @return 对象实体内容
     */
    public static <T> T getObject(HttpEntity entity, Class<T> t) {
        return JsonUtil.toBean(getString(entity), t);
    }

    /**
     * 以 {@link Map} 形式获取实体对象
     *
     * @param entity 实体对象
     * @return Map 实体内容
     */
    public static Map<?, ?> getMap(HttpEntity entity) {
        return getObject(entity, HashMap.class);
    }

    /**
     * 发送 Get 请求
     *
     * @param url 地址
     * @return 请求结果
     */
    public static HttpResponse doHttpGet(String url) {
        return doHttpGet(url, null);
    }

    /**
     * 发送 Get 请求
     *
     * @param url    地址
     * @param params 请求参数
     * @return 请求结果
     */
    public static HttpResponse doHttpGet(String url, Map<String, String> params) {
        if (!Validator.isUrl(url)) {
            throw new OperationException("无效 URL 【" + url + "】");
        }
        List<NameValuePair> formparams = new ArrayList<>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpGet get = new HttpGet(url + URL_SPLIT + URLEncodedUtils.format(formparams, Charset.defaultCharset()));
        try {
            CloseableHttpResponse rep = HttpClients.createDefault().execute(get);
            return new HttpResponse(rep.getStatusLine().getStatusCode(), HttpUtil.getString(rep.getEntity()));
        } catch (IOException e) {
            throw new OperationException("创建 Get 请求失败【" + url + "】", e);
        }
    }

    /**
     * 发送 POST 请求
     *
     * @param url 地址
     * @return 请求结果
     */
    public static HttpResponse doHttpPost(String url) {
        return doHttpPost(url, null);
    }

    /**
     * 发送 POST 请求
     *
     * @param url    请求地址
     * @param entity 请求实体数据
     * @return 请求结果
     */
    public static HttpResponse doHttpPost(String url, HttpEntity entity) {
        if (!Validator.isUrl(url)) {
            throw new OperationException("无效 URL 【" + url + "】");
        }
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        try {
            CloseableHttpResponse rep = HttpClients.createDefault().execute(post);
            return new HttpResponse(rep.getStatusLine().getStatusCode(), getString(rep.getEntity()));
        } catch (IOException e) {
            throw new OperationException("创建 Post 请求失败【" + url + "】", e);
        }
    }

    /**
     * 下载文件
     *
     * @param url      请求地址
     * @param entity   请求实体数据
     * @param savePath 返回的文件流输出路径
     */
    public static void download(String url, HttpEntity entity, String savePath) {
        if (!Validator.isUrl(url)) {
            throw new OperationException("无效 URL 【" + url + "】");
        }
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        FileOutputStream outputStream = null;
        try {
            CloseableHttpResponse rep = HttpClients.createDefault().execute(post);
            outputStream = new FileOutputStream(savePath);
            rep.getEntity().writeTo(outputStream);
        } catch (IOException e) {
            IoUtil.close(outputStream);
            throw new SystemException("文件下载失败", e);
        }
    }

    /**
     * 获取 url 中参数 key 的值
     *
     * @param url 请求地址
     * @param key 参数 key
     * @return 参数值
     */
    public static String getUrlParameter(String url, String key) {
        return getUrlParameter(url).get(key);
    }

    /**
     * 获取 url 中的参数值
     *
     * @param url 请求地址
     * @return 参数值
     */
    public static Map<String, String> getUrlParameter(String url) {
        if (StrUtil.isNotEmpty(url)) {
            String param;
            if (url.contains(URL_SPLIT)) {
                param = url.substring(url.indexOf(URL_SPLIT) + 1);
            } else if (url.startsWith(HTTP)) {
                param = "";
            } else {
                param = url;
            }
            String[] keyVal = param.split("&");
            if (keyVal.length > 0) {
                Map<String, String> map = new HashMap<>(keyVal.length);
                for (String str : keyVal) {
                    String[] arr = str.split("=");
                    if (StrUtil.isNotEmpty(arr[0])) {
                        map.put(arr[0], arr[1]);
                    }
                }
                return map;
            }
        }
        return new HashMap<>(0);
    }

    /**
     * 响应数据对象
     *
     * @author Lifeng.Lin
     */
    public static class HttpResponse {

        /**
         * 响应码
         */
        private int code;
        /**
         * 返回数据
         */
        private String data;

        /**
         * 响应数据对象
         */
        public HttpResponse() {
            super();
        }

        /**
         * 响应数据对象
         *
         * @param code 响应码
         * @param data 返回数据
         */
        public HttpResponse(int code, String data) {
            super();
            this.code = code;
            this.data = data;
        }

        /**
         * @return 响应码
         */
        public int getCode() {
            return code;
        }

        /**
         * @param code 响应码
         */
        public void setCode(int code) {
            this.code = code;
        }

        /**
         * @return 返回数据
         */
        public String getData() {
            return data;
        }

        /**
         * 通过 {@link JsonUtil} 对 Json 转对象操作
         *
         * @param clazz 数据对象类型
         * @return 返回指定类型数据
         */
        public <T> T getData(Class<T> clazz) {
            return JsonUtil.toBean(data, clazz);
        }

        /**
         * @param data 返回数据
         */
        public void setData(String data) {
            this.data = data;
        }

    }

    /**
     * HTTP POST请求参数对象
     *
     * @author Lifeng.Lin
     */
    public static class NameValuePairPost implements NameValuePair {

        /**
         * 字段名
         */
        private String name;

        /**
         * 字段值
         */
        private String value;

        /**
         * @param name  字段名
         * @param value 字段值
         */
        public NameValuePairPost(String name, String value) {
            super();
            this.name = name;
            this.value = value;
        }

        /**
         * @return 字段名
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * @return 字段值
         */
        @Override
        public String getValue() {
            return value;
        }

    }

}