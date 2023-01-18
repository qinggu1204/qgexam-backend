package com.qgexam.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.FaceErrorCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.AddFaceDTO;
import com.qgexam.user.pojo.DTO.ExamFaceComparisonDTO;
import com.qgexam.user.pojo.DTO.SearchFaceDTO;
import com.qgexam.user.pojo.PO.*;
import com.qgexam.user.pojo.VO.FaceSearchResVO;
import com.qgexam.user.service.FaceEngineService;
import com.qgexam.user.service.StudentExamActionService;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@Validated
@RequestMapping("/stu")
public class FaceController extends BaseController {

    public final static Logger logger = LoggerFactory.getLogger(FaceController.class);


    @DubboReference
    private FaceEngineService faceEngineService;

    @DubboReference
    private StudentInfoService studentInfoService;

    @DubboReference
    private StudentExamActionService studentExamActionService;

    /*
    人脸添加
     */
    @PutMapping("/addFace")
    public ResponseResult addFace(@RequestBody @Validated AddFaceDTO addFaceDTO) {
        try {
            byte[] decode = Base64.decode(base64Process(addFaceDTO.getFile()));
            ImageInfo imageInfo = ImageFactory.getRGBData(decode);

            //人脸特征获取
            byte[] bytes = faceEngineService.extractFaceFeature(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));
            if (bytes == null) {
                return ResponseResult.errorResult(FaceErrorCodeEnum.NO_FACE_DETECTED.getCode(),
                        FaceErrorCodeEnum.NO_FACE_DETECTED.getDescription());
            }

            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setStudentId(getStudentId());
            studentInfo.setFaceFeature(bytes);

            //人脸特征插入到数据库
            studentInfoService.updateById(studentInfo);

            return ResponseResult.okResult();
        } catch (Exception e) {
            logger.error("", e);
        }
        return ResponseResult.errorResult(FaceErrorCodeEnum.UNKNOWN.getCode(), FaceErrorCodeEnum.UNKNOWN.getDescription());
    }

    /*
    人脸识别
     */
    @PostMapping("/searchFace")
    public ResponseResult searchFace(@RequestBody @Validated SearchFaceDTO searchFaceDTO) throws Exception {
        byte[] decode = Base64.decode(base64Process(searchFaceDTO.getFile()));
        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(decode));
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);

        //人脸特征获取
        byte[] bytes = faceEngineService.extractFaceFeature(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));
        if (bytes == null) {
            return ResponseResult.errorResult(FaceErrorCodeEnum.NO_FACE_DETECTED.getCode(),
                    FaceErrorCodeEnum.NO_FACE_DETECTED.getDescription());
        }
        //人脸比对，获取比对结果
        List<FaceUserInfo> userFaceInfoList = faceEngineService.compareFaceFeature(getStudentId(),bytes);

        if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
            FaceUserInfo faceUserInfo = userFaceInfoList.get(0);
            FaceSearchResVO faceSearchResVO = new FaceSearchResVO();
            BeanUtil.copyProperties(faceUserInfo, faceSearchResVO);
            List<ProcessInfo> processInfoList = faceEngineService.process(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));
            if (CollectionUtil.isNotEmpty(processInfoList)) {
                //人脸检测
                List<FaceInfo2> faceInfoList = faceEngineService.detectFaces(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));
                int left = faceInfoList.get(0).getRect().getLeft();
                int top = faceInfoList.get(0).getRect().getTop();
                int width = faceInfoList.get(0).getRect().getRight() - left;
                int height = faceInfoList.get(0).getRect().getBottom() - top;

                Graphics2D graphics2D = bufImage.createGraphics();
                graphics2D.setColor(Color.RED);//红色
                BasicStroke stroke = new BasicStroke(5f);
                graphics2D.setStroke(stroke);
                graphics2D.drawRect(left, top, width, height);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufImage, "jpg", outputStream);
                byte[] bytes1 = outputStream.toByteArray();
                faceSearchResVO.setImage("data:image/jpeg;base64," + Base64Utils.encodeToString(bytes1));
                faceSearchResVO.setAge(processInfoList.get(0).getAge());
                faceSearchResVO.setGender(processInfoList.get(0).getGender().equals(1) ? "女" : "男");

            }

            return ResponseResult.okResult(faceSearchResVO);
        }
        return ResponseResult.errorResult(FaceErrorCodeEnum.FACE_DOES_NOT_MATCH.getCode(),
                FaceErrorCodeEnum.FACE_DOES_NOT_MATCH.getDescription());
    }

    /**
     * 考试过程中人脸比对
     * @param examFaceComparisonDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/examFaceComparison")
    public ResponseResult examFaceComparison(@RequestBody @Validated ExamFaceComparisonDTO examFaceComparisonDTO) throws Exception {
        byte[] decode = Base64.decode(base64Process(examFaceComparisonDTO.getFile()));
        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(decode));
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);

        //人脸特征获取
        byte[] bytes = faceEngineService.extractFaceFeature(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));
        if (bytes == null) {
            return ResponseResult.errorResult(FaceErrorCodeEnum.NO_FACE_DETECTED.getCode(),
                    FaceErrorCodeEnum.NO_FACE_DETECTED.getDescription());
        }
        //人脸比对，获取比对结果
        List<FaceUserInfo> userFaceInfoList = faceEngineService.compareFaceFeature(getStudentId(),bytes);

        if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
            List<ProcessInfo> processInfoList = faceEngineService.process(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));
            if (CollectionUtil.isNotEmpty(processInfoList)) {
                //人脸检测
                List<FaceInfo2> faceInfoList = faceEngineService.detectFaces(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));
                int left = faceInfoList.get(0).getRect().getLeft();
                int top = faceInfoList.get(0).getRect().getTop();
                int width = faceInfoList.get(0).getRect().getRight() - left;
                int height = faceInfoList.get(0).getRect().getBottom() - top;

                Graphics2D graphics2D = bufImage.createGraphics();
                graphics2D.setColor(Color.RED);//红色
                BasicStroke stroke = new BasicStroke(5f);
                graphics2D.setStroke(stroke);
                graphics2D.drawRect(left, top, width, height);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufImage, "jpg", outputStream);
            }

            return ResponseResult.okResult();
        }
        //若不合格,添加进考试行为表
        StudentExamAction studentExamAction = new StudentExamAction();
        studentExamAction.setStudentId(getStudentId());
        studentExamAction.setExaminationId(examFaceComparisonDTO.getExaminationId());
        studentExamAction.setType(ExamConstants.ACTION_TYPE_FACE);
        boolean succ = studentExamActionService.save(studentExamAction);
        if (!succ) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        return ResponseResult.errorResult(FaceErrorCodeEnum.FACE_DOES_NOT_MATCH.getCode(),
                FaceErrorCodeEnum.FACE_DOES_NOT_MATCH.getDescription());
    }

    @PostMapping("/detectFaces")
    public List<FaceInfo2> detectFaces(String image) throws IOException {
        byte[] decode = Base64.decode(image);
        InputStream inputStream = new ByteArrayInputStream(decode);
        ImageInfo imageInfo = ImageFactory.getRGBData(inputStream);

        if (inputStream != null) {
            inputStream.close();
        }
        List<FaceInfo2> faceInfoList = faceEngineService.detectFaces(BeanCopyUtils.copyBean(imageInfo, ImageInfo2.class));

        return faceInfoList;
    }


    private String base64Process(String base64Str) {
        if (!StringUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }

            return base64Str;
        } else {
            return "";
        }
    }
}

