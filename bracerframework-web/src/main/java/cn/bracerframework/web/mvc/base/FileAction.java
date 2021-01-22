package cn.bracerframework.web.mvc.base;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 文件操作 Action
 *
 * @author Lifeng.Lin
 */
public class FileAction {

    /**
     * 获取文件返回对象
     *
     * @param fileName 文件名
     * @param in       文件流
     * @return
     */
    protected ResponseEntity<byte[]> getFileResponse(String fileName, InputStream in) {
        return getFileResponse(fileName, IoUtil.readBytes(in));
    }

    /**
     * 获取文件返回对象
     *
     * @param fileName 文件名
     * @param out      文件字节流
     * @return
     */
    protected ResponseEntity<byte[]> getFileResponse(String fileName, ByteArrayOutputStream out) {
        return getFileResponse(fileName, out.toByteArray());
    }

    /**
     * 获取文件返回对象
     *
     * @param fileName 文件名
     * @param b        文件字节数组
     * @return
     */
    protected ResponseEntity<byte[]> getFileResponse(String fileName, byte[] b) {
        String mainName = FileUtil.mainName(fileName);
        String suffix = FileUtil.getSuffix(fileName);
        fileName = URLUtil.encode(mainName, "UTF-8") + "." + suffix;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccessControlExposeHeaders(Arrays.asList("Content-Disposition"));
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());

        return ResponseEntity.ok().headers(headers).body(b);
    }

}