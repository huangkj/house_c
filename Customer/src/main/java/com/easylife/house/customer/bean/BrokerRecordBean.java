package com.easylife.house.customer.bean;

import java.util.List;

public class BrokerRecordBean {

    /**
     * openBranchBank :
     * code :
     * applyUserCardNum :
     * lastAuditorName :
     * applyBrokerage : 3000
     * networkPusherOders : ["1680"]
     * applyUserPhone :
     * cityId :
     * applyUserId : 18815
     * cardPhoto :
     * belongToBank :
     * brokerList : [{"brokerageAmount":"","frontAfterNodeAmount":"","orderId":67098,"customerFollowId":"","orderBy":"","shouldAmount":3000,"cityId":"","customerPhone":"15512345678","frontAmount":"","creatTime":1539402449000,"customerId":"","id":1680,"examineState":"","ruleId":"","devAmount":"","devId":"","brokerId":"","devIdList":"","dealAmount":"","packageId":"","confirmState":1,"shouldReturnAmount":"","brokerOders":"","devName":"公园懿府","returnMark":"","customerName":"shen-1","frontNodeAmount":"","brokerageNode":"","companyId":"","afterNodeAmount":"","estimateAmount":"","creatTimeStr":"2018-10-13","surplusAmount":"","brokerName":"方法","afterAmount":"","frontSurplusAmount":"","afterSurplusAmount":"","houseNum":"一号楼-1-106","actualReturnAmount":""}]
     * applyUserName :
     * payBrokerage :
     * cityName :
     * createTime : 1541643962000
     * lastAuditorId :
     * taxBrokerage :
     * bankCardNum :
     * oderNum : 1
     * auditStatus : 1
     * id : 1
     * expectPayTime :
     */

    private String openBranchBank;
    private String code;
    private String applyUserCardNum;
    private String lastAuditorName;
    private int applyBrokerage;
    private String networkPusherOders;
    private String applyUserPhone;
    private String cityId;
    private int applyUserId;
    private String cardPhoto;
    private String belongToBank;
    private String applyUserName;
    private String payBrokerage;
    private String cityName;
    private long createTime;
    private String lastAuditorId;
    private String taxBrokerage;
    private String bankCardNum;
    private int oderNum;
    private int auditStatus;
    private int id;
    private String expectPayTime;
    private List<BrokerListBean> brokerList;

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

    public int getApplyBrokerage() {
        return applyBrokerage;
    }

    public void setApplyBrokerage(int applyBrokerage) {
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

    public int getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(int applyUserId) {
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
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

    public int getOderNum() {
        return oderNum;
    }

    public void setOderNum(int oderNum) {
        this.oderNum = oderNum;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
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

    public List<BrokerListBean> getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(List<BrokerListBean> brokerList) {
        this.brokerList = brokerList;
    }

    public static class BrokerListBean {
        /**
         * brokerageAmount :
         * frontAfterNodeAmount :
         * orderId : 67098
         * customerFollowId :
         * orderBy :
         * shouldAmount : 3000
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
         * confirmState : 1
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
         * brokerName : 方法
         * afterAmount :
         * frontSurplusAmount :
         * afterSurplusAmount :
         * houseNum : 一号楼-1-106
         * actualReturnAmount :
         */

        private String brokerageAmount;
        private String frontAfterNodeAmount;
        private int orderId;
        private String customerFollowId;
        private String orderBy;
        private int shouldAmount;
        private String cityId;
        private String customerPhone;
        private String frontAmount;
        private long creatTime;
        private String customerId;
        private int id;
        private String examineState;
        private String ruleId;
        private String devAmount;
        private String devId;
        private String brokerId;
        private String devIdList;
        private String dealAmount;
        private String packageId;
        private int confirmState;
        private String shouldReturnAmount;
        private String brokerOders;
        private String devName;
        private String returnMark;
        private String customerName;
        private String frontNodeAmount;
        private String brokerageNode;
        private String companyId;
        private String afterNodeAmount;
        private String estimateAmount;
        private String creatTimeStr;
        private String surplusAmount;
        private String brokerName;
        private String afterAmount;
        private String frontSurplusAmount;
        private String afterSurplusAmount;
        private String houseNum;
        private String actualReturnAmount;

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

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
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

        public int getShouldAmount() {
            return shouldAmount;
        }

        public void setShouldAmount(int shouldAmount) {
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

        public int getConfirmState() {
            return confirmState;
        }

        public void setConfirmState(int confirmState) {
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
