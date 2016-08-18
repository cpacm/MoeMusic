package com.cpacm.core.bean;

import java.io.Serializable;

/**
 * @author: cpacm
 * @date: 2016/7/15
 * @desciption: 封面图片
 */
public class CoverBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String small;
    private String medium;
    private String square;
    private String large;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
