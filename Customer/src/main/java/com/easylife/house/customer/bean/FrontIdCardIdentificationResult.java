package com.easylife.house.customer.bean;

import java.util.List;

/**
 * ocr身份识别 正面结果
 */
public class FrontIdCardIdentificationResult {

    /**
     * address : 河北省任丘市梁召镇北坞村429号
     * birth : 19930711
     * config_str : {"side":"face"}
     * <p>
     * face_rect : {"angle":-89.82334899902344,"center":{"x":2636.42578125,"y":2118.509033203125},"size":{"height":405.9999694824219,"width":405.9998474121094}}
     * face_rect_vertices : [{"x":2838.798828125,"y":2322.134033203125},{"x":2432.80078125,"y":2320.882080078125},{"x":2434.052734375,"y":1914.884033203125},{"x":2840.05078125,"y":1916.135986328125}]
     * name : 张宇
     * nationality : 汉
     * num : 130982199307115712
     * request_id : 20181107152301_bc717a8f7c53d21b5763b50d45c09be1
     * sex : 男
     * success : true
     */

    private String address;
    private String birth;
    private String config_str;
    private FaceRectBean face_rect;
    private String name;
    private String nationality;
    private String num;
    private String request_id;
    private String sex;
    private boolean success;
    private List<FaceRectVerticesBean> face_rect_vertices;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getConfig_str() {
        return config_str;
    }

    public void setConfig_str(String config_str) {
        this.config_str = config_str;
    }

    public FaceRectBean getFace_rect() {
        return face_rect;
    }

    public void setFace_rect(FaceRectBean face_rect) {
        this.face_rect = face_rect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<FaceRectVerticesBean> getFace_rect_vertices() {
        return face_rect_vertices;
    }

    public void setFace_rect_vertices(List<FaceRectVerticesBean> face_rect_vertices) {
        this.face_rect_vertices = face_rect_vertices;
    }

    public static class FaceRectBean {
        /**
         * angle : -89.82334899902344
         * center : {"x":2636.42578125,"y":2118.509033203125}
         * size : {"height":405.9999694824219,"width":405.9998474121094}
         */

        private double angle;
        private CenterBean center;
        private SizeBean size;

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

        public CenterBean getCenter() {
            return center;
        }

        public void setCenter(CenterBean center) {
            this.center = center;
        }

        public SizeBean getSize() {
            return size;
        }

        public void setSize(SizeBean size) {
            this.size = size;
        }

        public static class CenterBean {
            /**
             * x : 2636.42578125
             * y : 2118.509033203125
             */

            private double x;
            private double y;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }
        }

        public static class SizeBean {
            /**
             * height : 405.9999694824219
             * width : 405.9998474121094
             */

            private double height;
            private double width;

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getWidth() {
                return width;
            }

            public void setWidth(double width) {
                this.width = width;
            }
        }
    }

    public static class FaceRectVerticesBean {
        /**
         * x : 2838.798828125
         * y : 2322.134033203125
         */

        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
