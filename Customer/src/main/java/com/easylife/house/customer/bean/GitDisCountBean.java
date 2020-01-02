package com.easylife.house.customer.bean;

import java.util.List;

/**
 * Created by zgm on 2017/4/24.
 */

public class GitDisCountBean {

    /**
     * data : [{"estateProjectDevId":0,"num":50000,"scope":"2居","id":1,"privilege":"缴纳五万抵十万","operator":"大鹏","status":"0"}]
     * count : 1
     * status : {"msg":"成功","code":"1000"}
     */

    public String count;
    public StatusBean status;
    public List<DataBean> data;

    public static class StatusBean {
        /**
         * msg : 成功
         * code : 1000
         */

        public String msg;
        public String code;
    }

    public static class DataBean {
        /**
         * estateProjectDevId : 0
         * num : 50000
         * scope : 2居
         * id : 1
         * privilege : 缴纳五万抵十万
         * operator : 大鹏
         * status : 0
         */

        public String estateProjectDevId;
        public String num;
        public String scope;
        public String id;
        public String privilege;
        public String operator;
        public String status;
    }
}
