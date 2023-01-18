package com.qgexam.user.pojo.PO;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 人脸位置信息类
 * @author peter guo
 * @date 2023/01/06 20:33:44
 */

@Data
public class Rect2 implements Serializable {
    /**
     * 人脸矩形的最左边
     */
    public int left;
    /**
     * 人脸矩形的最上边
     */
    public int top;
    /**
     * 人脸矩形的最右边
     */
    public int right;
    /**
     * 人脸矩形的最下边
     */
    public int bottom;

    public Rect2() {
    }

    /**
     * 根据传入的上下左右四个值创建一个人脸位置信息
     * @param left  人脸矩形的最左边
     * @param top   人脸矩形的最上边
     * @param right 人脸矩形的最右边
     * @param bottom  人脸矩形的最下边
     */
    public Rect2(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * 根据传入的人脸矩形创建一个新的人脸矩形对象，新创建的对象为传入对象的深拷贝
     * @param r 人脸矩形信息对象
     */
    public Rect2(Rect2 r) {
        if (r == null) {
            this.left = this.top = this.right = this.bottom = 0;
        } else {
            this.left = r.left;
            this.top = r.top;
            this.right = r.right;
            this.bottom = r.bottom;
        }

    }

    /**
     *
     * @return  格式化的人脸位置信息
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("com.arcsoft.face.Rect(");
        sb.append(this.left);
        sb.append(", ");
        sb.append(this.top);
        sb.append(" - ");
        sb.append(this.right);
        sb.append(", ");
        sb.append(this.bottom);
        sb.append(")");
        return sb.toString();
    }
}

