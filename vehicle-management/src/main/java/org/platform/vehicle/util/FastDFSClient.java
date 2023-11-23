package org.platform.vehicle.util;

import com.github.tobato.fastdfs.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Primary
@Component
public class FastDFSClient {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     */
    public String uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            StorePath storePath = storageClient.uploadFile(inputStream, file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()), null);
            return fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        } catch (Exception e) {
            log.error("FastDFSClient uploadFile error:", e);
        }
        return "";
    }

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     */
    public String uploadFile(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            StorePath storePath = storageClient.uploadFile(inputStream, file.length(),
                    FilenameUtils.getExtension(file.getName()), null);
            return fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        } catch (Exception e) {
            log.error("FastDFSClient uploadFile error:", e);
        }
        return "";
    }

    /**
     * 上传文件
     *
     * @param inputStream 文件对象
     * @return 文件访问地址
     */
    public String uploadFile(InputStream inputStream, long fileSize, String fileSuffix) {
        try {
            StorePath storePath = storageClient.uploadFile(inputStream, fileSize, fileSuffix, null);
            return storePath.getFullPath();
        } catch (Exception e) {
            log.error("FastDFSClient uploadFile error:", e);
        }
        return "";
    }

    /**
     * 将一段字符串生成一个文件上传
     *
     * @param content       文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        try (ByteArrayInputStream stream = new ByteArrayInputStream(buff)) {
            StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension,
                    null);
            return fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        } catch (Exception e) {
            log.error("FastDFSClient uploadFile error:", e);
        }
        return "";
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        return fileUrl;
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件url
     * @return
     */
    public byte[] download(String fileUrl) {
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        byte[] bytes = storageClient.downloadFile(group, path, new DownloadByteArray());
        return bytes;
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            log.error("FastDFSClient deleteFile error:", e);
        }
    }

    /**
     * 读取文件内容
     */
    public InputStream readFileToInputStream(String filePath) {
        byte[] bytes = readFileToBytes(filePath);
        return new ByteArrayInputStream(bytes);
    }


    private byte[] readFileToBytes(String filePath) {
        try {
            StorePath storePath = StorePath.praseFromUrl(filePath);
            byte[] bytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(),
                    new DownloadByteArray());
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException("无法从FastDFS中读取文件：" + e.getMessage());
        }
    }
}
