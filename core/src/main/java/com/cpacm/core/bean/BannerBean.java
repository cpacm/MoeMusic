package com.cpacm.core.bean;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption: banner实体类
 */

public class BannerBean {

    private String name;//主题

    private String banner;//图片链接

    private String keyword;//关键词，用于搜索

    private String description;//描述语

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
