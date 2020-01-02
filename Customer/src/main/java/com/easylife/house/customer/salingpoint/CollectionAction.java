package com.easylife.house.customer.salingpoint;

public class CollectionAction {

    String actionId;
    String actionName;
    ACTION_TYPE actionType;
    String actionStartTime;//开始时间
    String actionEndTime;
    String duration;//行为时长

    //	String
    public CollectionAction() {
        // TODO Auto-generated constructor stub
    }


    public CollectionAction(String actionId, String actionName, ACTION_TYPE actionType,
                            String actionEndTime) {
        super();
        this.actionId = actionId;
        this.actionName = actionName;
        this.actionType = actionType;
        this.actionEndTime = actionEndTime;
    }

    @Override
    public String toString() {
        return "CollectionAction{" +
                "actionId='" + actionId + '\'' +
                ", actionName='" + actionName + '\'' +
                ", actionType=" + actionType +
                ", actionStartTime='" + actionStartTime + '\'' +
                ", actionEndTime='" + actionEndTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public String getActionId() {
        return actionId;
    }


    public void setActionId(String actionId) {
        this.actionId = actionId;
    }


    public String getActionName() {
        return actionName;
    }


    public void setActionName(String actionName) {
        this.actionName = actionName;
    }


    public ACTION_TYPE getActionType() {
        return actionType;
    }


    public void setActionType(ACTION_TYPE actionType) {
        this.actionType = actionType;
    }


    public String getActionStartTime() {
        return actionStartTime;
    }


    public void setActionStartTime(String actionStartTime) {
        this.actionStartTime = actionStartTime;
    }


    public String getActionEndTime() {
        return actionEndTime;
    }


    public void setActionEndTime(String actionEndTime) {
        this.actionEndTime = actionEndTime;
    }


    public String getDuration() {
        return duration;
    }


    public void setDuration(String duration) {
        this.duration = duration;
    }


    public static enum ACTION_TYPE {
        VIEW("查看内容", 0), SHARE("分享", 1),
        FAVORITE("收藏", 2), CANCEL_FAVORITE("取消收藏", 3),
        ENROLL("报名", 4), BUY("购买", 5), IM("IM聊天", 6),
        CALL("打电话", 7), RAISE("认筹", 9),
        REPORT("报备", 10), LOOK("带看", 11), PURCHASE("认购", 12),
        SALING("在售项目", 13), REFUND_RAISE("认筹退款", 14), REFUND_PURCHASE("认购退款", 15),
        PAID("已结佣", 16), TOBEPAID("待结佣", 17), SIGN("签约", 18),
        REGISTER("注册", 19), IBUYHOUSE("我要买房", 20);

        public final String msg;
        public final int code;


        private ACTION_TYPE(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }
    }

}
