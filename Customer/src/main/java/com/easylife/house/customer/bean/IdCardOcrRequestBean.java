package com.easylife.house.customer.bean;

public class IdCardOcrRequestBean {


    /**
     * image : 图片二进制数据的base64编码
     * configure : {"side":"face"}  face正面  back反面
     */

    private String image;
    private ConfigureBean configure;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ConfigureBean getConfigure() {
        return configure;
    }

    public void setConfigure(ConfigureBean configure) {
        this.configure = configure;
    }

    public static class ConfigureBean {

        public ConfigureBean() {
        }

        public ConfigureBean(String side) {
            this.side = side;
        }

        /**
         * side : face
         */

        private String side;

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }
    }
}
