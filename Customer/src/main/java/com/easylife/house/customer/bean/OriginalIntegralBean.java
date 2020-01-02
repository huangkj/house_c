package com.easylife.house.customer.bean;

import java.util.List;

public class OriginalIntegralBean {


    /**
     * expire_point : 30
     * point_details : [{"xh":"201810","date":"2018年10月","detail":[{"behavior_name":"签到","state":0,"time":1538381898000,"behavior":2,"balance_point":30,"point":10,"m_id":518},{"behavior_name":"注册","state":0,"time":1538381799000,"behavior":0,"balance_point":20,"point":10,"m_id":518}]}]
     * expire_time : 1540974392000
     * point : 0
     * m_id : 518
     */

    private int expire_point;
    private long expire_time;
    private int point;
    private int m_id;
    private List<PointDetailsBean> point_details;

    public int getExpire_point() {
        return expire_point;
    }

    public void setExpire_point(int expire_point) {
        this.expire_point = expire_point;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public List<PointDetailsBean> getPoint_details() {
        return point_details;
    }

    public void setPoint_details(List<PointDetailsBean> point_details) {
        this.point_details = point_details;
    }

    public static class PointDetailsBean {
        /**
         * xh : 201810
         * date : 2018年10月
         * detail : [{"behavior_name":"签到","state":0,"time":1538381898000,"behavior":2,"balance_point":30,"point":10,"m_id":518},{"behavior_name":"注册","state":0,"time":1538381799000,"behavior":0,"balance_point":20,"point":10,"m_id":518}]
         */

        private String xh;
        private String date;
        private List<DetailBean> detail;

        public String getXh() {
            return xh;
        }

        public void setXh(String xh) {
            this.xh = xh;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<DetailBean> getDetail() {
            return detail;
        }

        public void setDetail(List<DetailBean> detail) {
            this.detail = detail;
        }

        public static class DetailBean {
            /**
             * behavior_name : 签到
             * state : 0
             * time : 1538381898000
             * behavior : 2
             * balance_point : 30
             * point : 10
             * m_id : 518
             */

            private String behavior_name;
            private int state;
            private long time;
            private int behavior;
            private int balance_point;
            private int point;
            private int m_id;

            public String getBehavior_name() {
                return behavior_name;
            }

            public void setBehavior_name(String behavior_name) {
                this.behavior_name = behavior_name;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getBehavior() {
                return behavior;
            }

            public void setBehavior(int behavior) {
                this.behavior = behavior;
            }

            public int getBalance_point() {
                return balance_point;
            }

            public void setBalance_point(int balance_point) {
                this.balance_point = balance_point;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public int getM_id() {
                return m_id;
            }

            public void setM_id(int m_id) {
                this.m_id = m_id;
            }
        }
    }
}
