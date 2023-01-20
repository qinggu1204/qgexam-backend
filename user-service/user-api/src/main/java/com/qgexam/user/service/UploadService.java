package com.qgexam.user.service;

import com.qgexam.user.pojo.DTO.ImageUploadDTO;
import com.qgexam.user.pojo.VO.UploadImgVO;

public interface UploadService {
    UploadImgVO uploadImg(ImageUploadDTO imageUploadDTO);
}
