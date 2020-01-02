package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

public class AdBean implements Serializable {

    /**
     * id : 1
     * appName : 经纪人端、好房端、管家端
     * appType : 0、1、2
     * userId : 279
     * userCode : null
     * userName : vivid
     * linkAddress : https://www.hao123.com/
     * executionStatus : 0
     * enableStatus : 0
     * createTime : 2019-09-19 11:34:27
     * updateTime : 2019-09-19 15:03:36
     * appImg : [{"id":1,"adId":1,"name":0,"url":"http://kfcdn.lifeat.cn/20190919112928.jpg","createTime":"2019-09-19 11:34:27","updateTime":"2019-09-19 11:34:27"},{"id":2,"adId":1,"name":1,"url":"http://kfcdn.lifeat.cn/20190919113650.png","createTime":"2019-09-19 11:34:27","updateTime":"2019-09-19 11:34:27"},{"id":3,"adId":1,"name":2,"url":"http://kfcdn.lifeat.cn/20190919113700.png","createTime":"2019-09-19 11:34:27","updateTime":"2019-09-19 11:34:27"}]
     */

    private int id;
    private String appName;
    private String appType;
    private int userId;
    private Object userCode;
    private String userName;
    private String linkAddress;
    private int executionStatus;
    private int enableStatus;
    private String createTime;
    private String updateTime;
    private List<AppImgBean> appImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Object getUserCode() {
        return userCode;
    }

    public void setUserCode(Object userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public int getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(int executionStatus) {
        this.executionStatus = executionStatus;
    }

    public int getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(int enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<AppImgBean> getAppImg() {
        return appImg;
    }

    public void setAppImg(List<AppImgBean> appImg) {
        this.appImg = appImg;
    }

    private String localImagePath;

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }


    public static class AppImgBean implements Serializable {
        /**
         * id : 1
         * adId : 1
         * name : 0
         * url : http://kfcdn.lifeat.cn/20190919112928.jpg
         * createTime : 2019-09-19 11:34:27
         * updateTime : 2019-09-19 11:34:27
         */

        private int id;
        private int adId;
        private int name;
        private String url;
        private String createTime;
        private String updateTime;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAdId() {
            return adId;
        }

        public void setAdId(int adId) {
            this.adId = adId;
        }

        public int getName() {
            return name;
        }

        public void setName(int name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }


    }

    public AppImgBean getAndroidImageBean() {
        if (appImg != null && appImg.size() > 0) {
            for (AppImgBean imgBean : appImg
            ) {
                if (imgBean.getName() == 2) {
                    return imgBean;
                }
            }
        }
        return null;
    }
}
