package org.platform.vehicle.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.conn.FdfsWebServer;
import org.platform.vehicle.entity.UploadFile;
import org.platform.vehicle.mapper.UploadFileMapper;
import org.platform.vehicle.service.UploadFileService;
import org.platform.vehicle.util.FastDFSClient;
import org.platform.vehicle.constants.BaseConstant;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.response.ResponseEnum;
import org.platform.vehicle.utils.ImageUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author gejiawei
 * @Date 2023/8/23 08:58
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements
        UploadFileService {

    private final FastDFSClient fastDFSClient;

    private final FdfsWebServer fdfsWebServer;

    @Value("${fdfs.web-server-url}")
    private String serverUrl;

    @Override
    public BaseResponse uploadFile(MultipartFile[] uploadFiles) {
        log.info("文件上传服务器===》");
        List<String> imgFilePath = new ArrayList<>();
        try {
            List<UploadFile> list = new ArrayList<>();
            for (MultipartFile multipartFile : uploadFiles) {
                UploadFile file = this.uploadFastDfs(multipartFile);
                imgFilePath.add(file.getFileUrl());
                list.add(file);
            }
            this.saveBatch(list);
            return BaseResponse.ok(imgFilePath);
        } catch (Exception e) {
            log.error("文件上传失败：", e);
            return BaseResponse.failure(ResponseEnum.PICTURE_UPLOAD_ERROR);
        }
    }

    /**
     * 上传文件到fastDFS
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @Override
    public UploadFile uploadFastDfs(MultipartFile multipartFile)
            throws IOException {
        String originalName = multipartFile.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        if (!BaseConstant.UPLOAD_SUFFIX_FORMAT.contains(suffix.toLowerCase())) {
            log.error("上传格式不合法");
            throw new BaseException("99999", "上传格式不合法");
        }
        String newName = null;
        String fileUrl = null;
        if (BaseConstant.IMG_SUFFIX_FORMAT.contains(suffix.toLowerCase())) {
            if (multipartFile.getSize() > 10485760) {
                throw new BaseException(ResponseEnum.PICTURE_UPLOAD_TOO_BIG);
            }
            InputStream in = ImageUtil.compressPic(multipartFile.getInputStream(), 0.5f);
            newName = fastDFSClient.uploadFile(in, in.available(), suffix.substring(1));
        } else if (BaseConstant.EXCEL_SUFFIX_FORMAT.contains(suffix.toLowerCase())) {
            newName = fastDFSClient.uploadFile(multipartFile);
        } else if (BaseConstant.VIDEO_SUFFIX_FORMAT.contains(suffix.toLowerCase())) {
            // 上传视频
            if (multipartFile.getSize() > 209715200) {
                throw new BaseException(ResponseEnum.VIDEO_UPLOAD_TOO_BIG);
            }
            newName = fastDFSClient.uploadFile(multipartFile);
        }
        if (null == newName) {
            throw new BaseException(ResponseEnum.PICTURE_UPLOAD_ERROR);
        }
        fileUrl = fdfsWebServer.getWebServerUrl() + newName;
        fileUrl = fileUrl.replaceAll(serverUrl, "");
        UploadFile file = new UploadFile();
        file.setFileOriginalName(originalName);
        file.setFileName(newName);
        file.setCreateTime(DateUtil.date());
        file.setFileUrl(fileUrl);
        file.setFileSuffix(suffix);
        file.setIsDelete(0);
        return file;
    }
}
