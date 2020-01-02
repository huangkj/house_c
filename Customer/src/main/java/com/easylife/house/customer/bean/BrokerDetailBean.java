package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

public class BrokerDetailBean implements Serializable {

    /**
     * PusherApply : {"openBranchBank":"","code":"","applyUserCardNum":"88888888","lastAuditorName":"","applyBrokerage":3000,"networkPusherOders":"[\"1680\"]","applyUserPhone":"","cityId":"","applyUserId":"","cardPhoto":"","belongToBank":"","brokerList":"","applyUserName":"方法","payBrokerage":"","cityName":"","createTime":"","lastAuditorId":"","taxBrokerage":"","bankCardNum":"666666","oderNum":"","auditStatus":"","id":1,"expectPayTime":""}
     * brokerOders : [{"brokerageAmount":"","frontAfterNodeAmount":"","orderId":"","customerFollowId":"","orderBy":"","shouldAmount":3000,"cityId":"","customerPhone":"15512345678","frontAmount":"","creatTime":1539402449000,"customerId":"","id":1680,"examineState":"","ruleId":"","devAmount":"","devId":"","brokerId":"","devIdList":"","dealAmount":"","packageId":"","confirmState":"","shouldReturnAmount":"","brokerOders":"","devName":"公园懿府","returnMark":"","customerName":"shen-1","frontNodeAmount":"","brokerageNode":"","companyId":"","afterNodeAmount":"","estimateAmount":"","creatTimeStr":"2018-10-13","surplusAmount":"","brokerName":"","afterAmount":"","frontSurplusAmount":"","afterSurplusAmount":"","houseNum":"一号楼-1-106","actualReturnAmount":""}]
     */

    public PusherApplyBean PusherApply;
    public List<BrokerOdersBean> brokerOders;

    public PusherApplyBean getPusherApply() {
        return PusherApply;
    }

    public void setPusherApply(PusherApplyBean PusherApply) {
        this.PusherApply = PusherApply;
    }

    public List<BrokerOdersBean> getBrokerOders() {
        return brokerOders;
    }

    public void setBrokerOders(List<BrokerOdersBean> brokerOders) {
        this.brokerOders = brokerOders;
    }

    public static class PusherApplyBean implements Serializable {
        /**
         * openBranchBank :
         * code :
         * applyUserCardNum : 88888888
         * lastAuditorName :
         * applyBrokerage : 3000.0
         * networkPusherOders : ["1680"]
         * applyUserPhone :
         * cityId :
         * applyUserId :
         * cardPhoto :
         * belongToBank :
         * brokerList :
         * applyUserName : 方法
         * payBrokerage :
         * cityName :
         * createTime :
         * lastAuditorId :
         * taxBrokerage :
         * bankCardNum : 666666
         * oderNum :
         * auditStatus :
         * id : 1
         * expectPayTime :
         */

        public String openBranchBank;
        public String code;
        public String applyUserCardNum;
        public String lastAuditorName;
        public String applyBrokerage;
        public String networkPusherOders;
        public String applyUserPhone;
        public String cityId;
        public String applyUserId;
        public String cardPhoto;
        public String belongToBank;
        public String brokerList;
        public String applyUserName;
        public String payBrokerage;
        public String cityName;
        public String createTime;
        public String lastAuditorId;
        public String taxBrokerage;
        public String bankCardNum;
        public String oderNum;
        public String auditStatus;
        public int id;
        public String expectPayTime;

        public String getStateStr() {
            return stateStr;
        }

        public void setStateStr(String stateStr) {
            this.stateStr = stateStr;
        }

        public String stateStr;

        public String getOpenBranchBank() {
            return openBranchBank;
        }

        public void setOpenBranchBank(String openBranchBank) {
            this.openBranchBank = openBranchBank;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getApplyUserCardNum() {
            return applyUserCardNum;
        }

        public void setApplyUserCardNum(String applyUserCardNum) {
            this.applyUserCardNum = applyUserCardNum;
        }

        public String getLastAuditorName() {
            return lastAuditorName;
        }

        public void setLastAuditorName(String lastAuditorName) {
            this.lastAuditorName = lastAuditorName;
        }

        public String getApplyBrokerage() {
            return applyBrokerage;
        }

        public void setApplyBrokerage(String applyBrokerage) {
            this.applyBrokerage = applyBrokerage;
        }

        public String getNetworkPusherOders() {
            return networkPusherOders;
        }

        public void setNetworkPusherOders(String networkPusherOders) {
            this.networkPusherOders = networkPusherOders;
        }

        public String getApplyUserPhone() {
            return applyUserPhone;
        }

        public void setApplyUserPhone(String applyUserPhone) {
            this.applyUserPhone = applyUserPhone;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getApplyUserId() {
            return applyUserId;
        }

        public void setApplyUserId(String applyUserId) {
            this.applyUserId = applyUserId;
        }

        public String getCardPhoto() {
            return cardPhoto;
        }

        public void setCardPhoto(String cardPhoto) {
            this.cardPhoto = cardPhoto;
        }

        public String getBelongToBank() {
            return belongToBank;
        }

        public void setBelongToBank(String belongToBank) {
            this.belongToBank = belongToBank;
        }

        public String getBrokerList() {
            return brokerList;
        }

        public void setBrokerList(String brokerList) {
            this.brokerList = brokerList;
        }

        public String getApplyUserName() {
            return applyUserName;
        }

        public void setApplyUserName(String applyUserName) {
            this.applyUserName = applyUserName;
        }

        public String getPayBrokerage() {
            return payBrokerage;
        }

        public void setPayBrokerage(String payBrokerage) {
            this.payBrokerage = payBrokerage;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getLastAuditorId() {
            return lastAuditorId;
        }

        public void setLastAuditorId(String lastAuditorId) {
            this.lastAuditorId = lastAuditorId;
        }

        public String getTaxBrokerage() {
            return taxBrokerage;
        }

        public void setTaxBrokerage(String taxBrokerage) {
            this.taxBrokerage = taxBrokerage;
        }

        public String getBankCardNum() {
            return bankCardNum;
        }

        public void setBankCardNum(String bankCardNum) {
            this.bankCardNum = bankCardNum;
        }

        public String getOderNum() {
            return oderNum;
        }

        public void setOderNum(String oderNum) {
            this.oderNum = oderNum;
        }

        public String getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getExpectPayTime() {
            return expectPayTime;
        }

        public void setExpectPayTime(String expectPayTime) {
            this.expectPayTime = expectPayTime;
        }
    }

    public static class BrokerOdersBean implements Serializable {
        /**
         * brokerageAmount :
         * frontAfterNodeAmount :
         * orderId :
         * customerFollowId :
         * orderBy :
         * shouldAmount : 3000.0
         * cityId :
         * customerPhone : 15512345678
         * frontAmount :
         * creatTime : 1539402449000
         * customerId :
         * id : 1680
         * examineState :
         * ruleId :
         * devAmount :
         * devId :
         * brokerId :
         * devIdList :
         * dealAmount :
         * packageId :
         * confirmState :
         * shouldReturnAmount :
         * brokerOders :
         * devName : 公园懿府
         * returnMark :
         * customerName : shen-1
         * frontNodeAmount :
         * brokerageNode :
         * companyId :
         * afterNodeAmount :
         * estimateAmount :
         * creatTimeStr : 2018-10-13
         * surplusAmount :
         * brokerName :
         * afterAmount :
         * frontSurplusAmount :
         * afterSurplusAmount :
         * houseNum : 一号楼-1-106
         * actualReturnAmount :
         */

        public String brokerageAmount;
        public String frontAfterNodeAmount;
        public String orderId;
        public String customerFollowId;
        public String orderBy;
        public String shouldAmount;
        public String cityId;
        public String customerPhone;
        public String frontAmount;
        public long creatTime;
        public String customerId;
        public int id;
        public String examineState;
        public String ruleId;
        public String devAmount;
        public String devId;
        public String brokerId;
        public String devIdList;
        public String dealAmount;
        public String packageId;
        public String confirmState;
        public String shouldReturnAmount;
        public String brokerOders;
        public String devName;
        public String returnMark;
        public String customerName;
        public String frontNodeAmount;
        public String brokerageNode;
        public String companyId;
        public String afterNodeAmount;
        public String estimateAmount;
        public String creatTimeStr;
        public String surplusAmount;
        public String brokerName;
        public String afterAmount;
        public String frontSurplusAmount;
        public String afterSurplusAmount;
        public String houseNum;
        public String actualReturnAmount;

        public String getBrokerageAmount() {
            return brokerageAmount;
        }

        public void setBrokerageAmount(String brokerageAmount) {
            this.brokerageAmount = brokerageAmount;
        }

        public String getFrontAfterNodeAmount() {
            return frontAfterNodeAmount;
        }

        public void setFrontAfterNodeAmount(String frontAfterNodeAmount) {
            this.frontAfterNodeAmount = frontAfterNodeAmount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCustomerFollowId() {
            return customerFollowId;
        }

        public void setCustomerFollowId(String customerFollowId) {
            this.customerFollowId = customerFollowId;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public String getShouldAmount() {
            return shouldAmount;
        }

        public void setShouldAmount(String shouldAmount) {
            this.shouldAmount = shouldAmount;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getFrontAmount() {
            return frontAmount;
        }

        public void setFrontAmount(String frontAmount) {
            this.frontAmount = frontAmount;
        }

        public long getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(long creatTime) {
            this.creatTime = creatTime;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getExamineState() {
            return examineState;
        }

        public void setExamineState(String examineState) {
            this.examineState = examineState;
        }

        public String getRuleId() {
            return ruleId;
        }

        public void setRuleId(String ruleId) {
            this.ruleId = ruleId;
        }

        public String getDevAmount() {
            return devAmount;
        }

        public void setDevAmount(String devAmount) {
            this.devAmount = devAmount;
        }

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public String getBrokerId() {
            return brokerId;
        }

        public void setBrokerId(String brokerId) {
            this.brokerId = brokerId;
        }

        public String getDevIdList() {
            return devIdList;
        }

        public void setDevIdList(String devIdList) {
            this.devIdList = devIdList;
        }

        public String getDealAmount() {
            return dealAmount;
        }

        public void setDealAmount(String dealAmount) {
            this.dealAmount = dealAmount;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getConfirmState() {
            return confirmState;
        }

        public void setConfirmState(String confirmState) {
            this.confirmState = confirmState;
        }

        public String getShouldReturnAmount() {
            return shouldReturnAmount;
        }

        public void setShouldReturnAmount(String shouldReturnAmount) {
            this.shouldReturnAmount = shouldReturnAmount;
        }

        public String getBrokerOders() {
            return brokerOders;
        }

        public void setBrokerOders(String brokerOders) {
            this.brokerOders = brokerOders;
        }

        public String getDevName() {
            return devName;
        }

        public void setDevName(String devName) {
            this.devName = devName;
        }

        public String getReturnMark() {
            return returnMark;
        }

        public void setReturnMark(String returnMark) {
            this.returnMark = returnMark;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getFrontNodeAmount() {
            return frontNodeAmount;
        }

        public void setFrontNodeAmount(String frontNodeAmount) {
            this.frontNodeAmount = frontNodeAmount;
        }

        public String getBrokerageNode() {
            return brokerageNode;
        }

        public void setBrokerageNode(String brokerageNode) {
            this.brokerageNode = brokerageNode;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getAfterNodeAmount() {
            return afterNodeAmount;
        }

        public void setAfterNodeAmount(String afterNodeAmount) {
            this.afterNodeAmount = afterNodeAmount;
        }

        public String getEstimateAmount() {
            return estimateAmount;
        }

        public void setEstimateAmount(String estimateAmount) {
            this.estimateAmount = estimateAmount;
        }

        public String getCreatTimeStr() {
            return creatTimeStr;
        }

        public void setCreatTimeStr(String creatTimeStr) {
            this.creatTimeStr = creatTimeStr;
        }

        public String getSurplusAmount() {
            return surplusAmount;
        }

        public void setSurplusAmount(String surplusAmount) {
            this.surplusAmount = surplusAmount;
        }

        public String getBrokerName() {
            return brokerName;
        }

        public void setBrokerName(String brokerName) {
            this.brokerName = brokerName;
        }

        public String getAfterAmount() {
            return afterAmount;
        }

        public void setAfterAmount(String afterAmount) {
            this.afterAmount = afterAmount;
        }

        public String getFrontSurplusAmount() {
            return frontSurplusAmount;
        }

        public void setFrontSurplusAmount(String frontSurplusAmount) {
            this.frontSurplusAmount = frontSurplusAmount;
        }

        public String getAfterSurplusAmount() {
            return afterSurplusAmount;
        }

        public void setAfterSurplusAmount(String afterSurplusAmount) {
            this.afterSurplusAmount = afterSurplusAmount;
        }

        public String getHouseNum() {
            return houseNum;
        }

        public void setHouseNum(String houseNum) {
            this.houseNum = houseNum;
        }

        public String getActualReturnAmount() {
            return actualReturnAmount;
        }

        public void setActualReturnAmount(String actualReturnAmount) {
            this.actualReturnAmount = actualReturnAmount;
        }
    }
}
