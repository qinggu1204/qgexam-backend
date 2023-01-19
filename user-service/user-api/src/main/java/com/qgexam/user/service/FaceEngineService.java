package com.qgexam.user.service;

import com.arcsoft.face.FaceInfo;
import com.qgexam.user.pojo.PO.FaceInfo2;
import com.qgexam.user.pojo.PO.FaceUserInfo;
import com.qgexam.user.pojo.PO.ImageInfo2;
import com.qgexam.user.pojo.PO.ProcessInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 人脸服务接口
 *
 * @author peter guo
 * @since 2023-01-12 15:01:25
 */
public interface FaceEngineService {

    List<FaceInfo2> detectFaces(ImageInfo2 imageInfo);

    List<ProcessInfo> process(ImageInfo2 imageInfo);

    /**
     * 人脸特征
     * @param imageInfo
     * @return
     */
    byte[] extractFaceFeature(ImageInfo2 imageInfo) throws InterruptedException;

    /**
     * 人脸比对
     * @param faceFeature
     * @return
     */
    List<FaceUserInfo> compareFaceFeature(Integer studentId, byte[] faceFeature) throws InterruptedException, ExecutionException;



}
