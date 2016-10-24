package com.cpacm.core.bean;

/**
 * @author: cpacm
 * @date: 2016/10/24
 * @desciption:
 */

public class VersionBean {

    /**
     * version : 1.0.0
     * version_code : 1
     * apk : https://raw.githubusercontent.com/cpacm
     * description : 更新版本
     */

    private String version;
    private int version_code;
    private String apk;
    private String description;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
