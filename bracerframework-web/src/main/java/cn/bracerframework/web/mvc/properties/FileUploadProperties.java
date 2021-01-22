package cn.bracerframework.web.mvc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置属性
 *
 * @author Lifeng.Lin
 */
@ConfigurationProperties(prefix = FileUploadProperties.PREFIX)
public class FileUploadProperties {
    public static final String PREFIX = "bracerframework.commons-multipart";

    /**
     * 文件编码
     */
    private String encoding = "UTF-8";
    /**
     * 单个文件允许的文件大小
     */
    private Long maxUploadSizePerFile = 1L * 1024 * 1024;
    /**
     * 文件上传的最大值
     */
    private Long maxUploadSize = 5L * 1024 * 1024;
    /**
     * 文件上传时写入内存的最大值
     */
    private Integer maxInMemorySize = 10 * 1024;
    /**
     * 上传文件临时目录
     */
    private String uploadTempDir;
    /**
     * 是否保留客户端发送的文件名
     */
    private boolean preserveFilename = false;
    /**
     * 是否延迟文件解析
     */
    private boolean resolveLazily = false;

    /**
     * @return 文件编码
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding 文件编码
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return 单个文件允许的文件大小
     */
    public Long getMaxUploadSizePerFile() {
        return maxUploadSizePerFile;
    }

    /**
     * @param maxUploadSizePerFile 单个文件允许的文件大小
     */
    public void setMaxUploadSizePerFile(Long maxUploadSizePerFile) {
        this.maxUploadSizePerFile = maxUploadSizePerFile;
    }

    /**
     * @return 文件上传的最大值
     */
    public Long getMaxUploadSize() {
        return maxUploadSize;
    }

    /**
     * @param maxUploadSize 文件上传的最大值
     */
    public void setMaxUploadSize(Long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    /**
     * @return 文件上传时写入内存的最大值
     */
    public Integer getMaxInMemorySize() {
        return maxInMemorySize;
    }

    /**
     * @param maxInMemorySize 文件上传时写入内存的最大值
     */
    public void setMaxInMemorySize(Integer maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    /**
     * @return 上传文件临时目录
     */
    public String getUploadTempDir() {
        return uploadTempDir;
    }

    /**
     * @param uploadTempDir 上传文件临时目录
     */
    public void setUploadTempDir(String uploadTempDir) {
        this.uploadTempDir = uploadTempDir;
    }

    /**
     * @return 是否保留客户端发送的文件名
     */
    public boolean isPreserveFilename() {
        return preserveFilename;
    }

    /**
     * @param preserveFilename 是否保留客户端发送的文件名
     */
    public void setPreserveFilename(boolean preserveFilename) {
        this.preserveFilename = preserveFilename;
    }

    /**
     * @return 是否延迟文件解析
     */
    public boolean isResolveLazily() {
        return resolveLazily;
    }

    /**
     * @param resolveLazily 是否延迟文件解析
     */
    public void setResolveLazily(boolean resolveLazily) {
        this.resolveLazily = resolveLazily;
    }

}