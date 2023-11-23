package org.platform.vehicle.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.platform.vehicle.service.UploadFileService;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.response.ResponseEnum;

import lombok.RequiredArgsConstructor;

/**
 * 上传文件
 *
 * @since 2022-07-15
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UploadFileController {

    private final UploadFileService uploadFileService;


    /**
     * 上传图片附件
     *
     * @param uploadFiles
     * @return
     */
    @RequestMapping(value = "/uploadFile" ,method = RequestMethod.POST)
    public BaseResponse uploadFile(MultipartFile[] uploadFiles){
        if (uploadFiles.length == 0){
            return BaseResponse.failure(ResponseEnum.PICTURE_UPLOAD_NO_FILE);
        }
        return uploadFileService.uploadFile(uploadFiles);
    }

}

