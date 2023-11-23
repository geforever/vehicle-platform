package org.platform.vehicle.service;

import org.platform.vehicle.entity.UploadFile;
import org.platform.vehicle.response.BaseResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author gejiawei
 * @Date 2023/8/23 08:57
 */
public interface UploadFileService {

    /**
     * 上传图片附件
     *
     * @param uploadFiles
     * @return
     */
    BaseResponse uploadFile(MultipartFile[] uploadFiles);

    /**
     * 上传文件到fastDFS
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    UploadFile uploadFastDfs(MultipartFile multipartFile) throws IOException;
}
