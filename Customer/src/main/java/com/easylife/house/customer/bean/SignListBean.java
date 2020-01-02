package com.easylife.house.customer.bean;

import java.util.List;

public class SignListBean {

    /**
     * times : 1
     * point_details : [{"behavior_name":"签到","state":0,"time":1539328378000,"behavior":2,"balance_point":10,"point":10,"m_id":555}]
     * point : 10
     */

    public int times;
    public int point;
    public List<PointDetailsBean> point_details;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public List<PointDetailsBean> getPoint_details() {
        return point_details;
    }

    public void setPoint_details(List<PointDetailsBean> point_details) {
        this.point_details = point_details;
    }

    public static class PointDetailsBean {
        /**
         * behavior_name : 签到
         * state : 0
         * time : 1539328378000
         * behavior : 2
         * balance_point : 10
         * point : 10
         * m_id : 555
         */

        public String behavior_name;
        public int state;
        public long time;
        public int behavior;
        public int balance_point;
        public int point;
        public int m_id;

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
