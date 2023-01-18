package com.qgexam.user.pojo.PO;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 人脸信息类
 * @author peter guo
 * @date 2023/01/06 20:33:44
 */

@Data
public class FaceInfo2 implements Serializable {
    /**
     * 人脸位置信息
     */
    Rect2 rect;
    /**
     * 人脸角度
     */
    int orient;

    /**
     * faceId，IMAGE模式下不返回faceId
     */
    int faceId = -1;

    /**
     * 根据人脸位置信息和人脸角度创建一个人脸信息对象
     *
     * @param rect   人脸位置信息
     * @param orient 人脸角度
     */
    public FaceInfo2(Rect2 rect, int orient) {
        this.rect = new Rect2(rect);
        this.orient = orient;
    }

    /**
     * 根据传入的人脸信息对象创建一个新的人脸信息对象
     *
     * @param obj 人脸信息对象
     */
    public FaceInfo2(FaceInfo2 obj) {

        this.rect = new Rect2(obj.getRect());
        this.orient = obj.getOrient();
        this.faceId = obj.getFaceId();
    }

    /**
     * 创建一个新的人脸信息对象
     */
    public FaceInfo2() {
        rect = new Rect2();
        orient = 0;
    }

    /**
     * 获取格式化的人脸信息
     *
     * @return 格式化的人脸信息
     */
    @Override
    public String toString() {
        return this.rect.toString() + "," + this.orient;
    }

    /**
     * 获取当前对象的深拷贝
     *
     * @return 当前对象的深拷贝
     */
    @Override
    public FaceInfo2 clone() {
        return new FaceInfo2(this);
    }
}

