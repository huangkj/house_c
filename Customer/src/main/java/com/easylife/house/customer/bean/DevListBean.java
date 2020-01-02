package com.easylife.house.customer.bean;

import com.easylife.house.customer.view.popwindow.MultiChooseListPopWindow;

import java.util.List;

public class DevListBean {

    /**
     * msg : 成功
     * code : 200
     * data : [{"devId":"1315","cityName":"北京市","devName":"公园懿府"}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements MultiChooseListPopWindow.TextImp {


        /**
         * devId : 1315
         * cityName : 北京市
         * devName : 公园懿府
         */

        private String devId;
        private String cityName;
        private String devName;
        private boolean isSelect;


        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getDevName() {
            return devName;
        }

        public void setDevName(String devName) {
            this.devName = devName;
        }

        @Override
        public String getText() {
            return devName;
        }

        @Override
        public boolean isSelect() {
            return isSelect;
        }

        @Override
        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
