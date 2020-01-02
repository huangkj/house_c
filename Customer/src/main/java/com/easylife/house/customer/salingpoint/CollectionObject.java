package com.easylife.house.customer.salingpoint;

public class CollectionObject {

    String serviceId;
    String serviceName;
    String city;
    String budget;
    String ioan;
    String houseType;
    SERVICE_TYPE serviceType;

    public CollectionObject() {
        // TODO Auto-generated constructor stub
    }


    public CollectionObject(String serviceId, String serviceName,
                            SERVICE_TYPE serviceType) {
        super();
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
    }
    public CollectionObject(String serviceId, String serviceName,String city,
                            String budget,String ioan,String houseType,
                            SERVICE_TYPE serviceType) {
        super();
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.city = city;
        this.budget = budget;
        this.ioan = ioan;
        this.houseType = houseType;
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "CollectionObject{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceType=" + serviceType +
                '}';
    }

    public String getServiceId() {
        return serviceId;
    }


    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }


    public String getServiceName() {
        return serviceName;
    }


    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public SERVICE_TYPE getServiceType() {
        return serviceType;
    }


    public void setServiceType(SERVICE_TYPE serviceType) {
        this.serviceType = serviceType;
    }


    public static enum SERVICE_TYPE {
        ESTATE("楼盘", 0), HOUSE_TYPE("户型", 1), HOUSE("房源", 2), BENEFIT("优惠", 3),
        APP("APP", 4), STORE("店铺", 5);

        public final String msg;
        public final int code;


        private SERVICE_TYPE(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }
    }
}
