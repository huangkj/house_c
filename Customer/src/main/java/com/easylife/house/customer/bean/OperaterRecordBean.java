package com.easylife.house.customer.bean;

/**
 * - @Description:  操作记录
 * - @Author:  hkj
 * - @Time:  2018/7/10 16:56
 */

public class OperaterRecordBean {


    /**
     * applyId : 4
     * remark :
     * operatorRoleType : 1
     * createTime : 1542352513249
     * operatorRoleText : 经纪公司
     * id : 5bee6e818d6bde190c060832
     * type : 2
     * operation : 1
     * operatorId : 27476
     * submitPersonName : 13133333333
     * createTimeStr : 2018-11-16
     * operationText : 提交审核
     */

    private String applyId;
    private String remark;
    private int operatorRoleType;
    private String createTime;
    private String operatorRoleText;
    private String id;
    private int type;
    private int operation;// 1, "待审核",2, "运营审核通过",3, "财务审核通过" 4, "运营审核拒绝" 5, "财务审          核拒绝"


    private String auditStatus;
    private int operatorId;
    private String submitPersonName;
    private String createTimeStr;
    private String operationText;

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    private String checkStatus;
    private String system;

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    private String stateStr;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getRemark() {
        return remark;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOperatorRoleType() {
        return operatorRoleType;
    }

    public void setOperatorRoleType(int operatorRoleType) {
        this.operatorRoleType = operatorRoleType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperatorRoleText() {
        return operatorRoleText;
    }

    public void setOperatorRoleText(String operatorRoleText) {
        this.operatorRoleText = operatorRoleText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getSubmitPersonName() {
        return submitPersonName;
    }

    public void setSubmitPersonName(String submitPersonName) {
        this.submitPersonName = submitPersonName;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getOperationText() {
        return operationText;
    }

    public void setOperationText(String operationText) {
        this.operationText = operationText;
    }
}
