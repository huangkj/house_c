package com.easylife.house.customer.bean;

public class DiscountListBean {

    /**
     * disType : member
     * scope : 一居
     * count : 19
     * privilege : 0.12万抵120万
     * seqence : 0
     */

    private String disType;
    private String scope;
    private int count;
    private String privilege;
    private int seqence;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisType() {
        return disType;
    }

    public void setDisType(String disType) {
        this.disType = disType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public int getSeqence() {
        return seqence;
    }

    public void setSeqence(int seqence) {
        this.seqence = seqence;
    }
}
