package com.easylife.house.customer.bean;

import java.util.List;

/**
 * Created by Mars on 2017/3/20 18:14.
 * 描述：
 */

public class ImageResult {

    public List<DataBean> data;

    public static class DataBean {
        /**
         * msg : 成功上传
         * code : 1000
         * name : /storage/emulated/0/easylife/img/.img/uploadimg_20170320181343423.jpg
         * url : http://om4yv9x56.bkt.clouddn.com/FkMMYIDIJ-SPY8bqlKHlcZ7HAc5R
         */

        public String msg;
        public String code;
        public String name;
        public String url;

    }
}
