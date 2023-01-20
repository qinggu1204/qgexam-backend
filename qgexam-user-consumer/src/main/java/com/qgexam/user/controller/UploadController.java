package com.qgexam.user.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.ImageUploadDTO;
import com.qgexam.user.pojo.VO.UploadImgVO;
import com.qgexam.user.service.UploadService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/common")
public class UploadController extends BaseController {
    @DubboReference
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile image) throws IOException {
        ImageUploadDTO imageUploadDTO = new ImageUploadDTO();
        imageUploadDTO.setOriginalFilename(image.getOriginalFilename());
        imageUploadDTO.setFileBytes(image.getBytes());
        imageUploadDTO.setUserId(getUserId());
        UploadImgVO uploadImgVO = uploadService.uploadImg(imageUploadDTO);
        return ResponseResult.okResult(uploadImgVO);
    }
}
