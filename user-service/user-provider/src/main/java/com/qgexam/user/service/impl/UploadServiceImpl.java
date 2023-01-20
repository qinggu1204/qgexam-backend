package com.qgexam.user.service.impl;

import com.google.gson.Gson;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.PathUtils;
import com.qgexam.user.pojo.DTO.ImageUploadDTO;
import com.qgexam.user.pojo.VO.UploadImgVO;
import com.qgexam.user.service.UploadService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author yzw
 * @date 2023年01月20日 14:03
 */
@DubboService
public class UploadServiceImpl implements UploadService {


    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.ossUrl}")
    private String ossUrl;

    @Override
    public UploadImgVO uploadImg(ImageUploadDTO imageUploadDTO) {
        //获取原始文件名
        String originalFilename = imageUploadDTO.getOriginalFilename();
        byte[] fileBytes = imageUploadDTO.getFileBytes();
        // 将字节流转换为输入流
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        //如果文件不是图片类型，抛出异常
        if (originalFilename.endsWith(".jpg") || originalFilename.endsWith(".png") ||
                originalFilename.endsWith(".jpeg")) {
            // 生成新的文件名和oss保存路径
            String filePath = PathUtils.generateFilePath(originalFilename, imageUploadDTO.getUserId());
            String url = uploadOss(inputStream, filePath);
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            UploadImgVO uploadImgVO = new UploadImgVO();
            uploadImgVO.setUrl(url);
            uploadImgVO.setName(fileName);
            return uploadImgVO;
        } else {
            throw new BusinessException(AppHttpCodeEnum.IMG_TYPE_ERROR);
        }

    }

    private String uploadOss(InputStream inputStream, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return ossUrl + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}
