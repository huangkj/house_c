package com.easylife.house.customer.bean;

/**
 * Created by Administrator on 2017/4/26/026.
 */

public class HouseSignBean {

    /**
     * realName : fygjgk
     * phone : 17600331021
     * effect : {"thumbnailImage":"http://olpdjpvtn.bkt.clouddn.com/Fi4JYs7US0SZnY4D9moXzTFc-7i8","url":"http://olpdjpvtn.bkt.clouddn.com/Fi4JYs7US0SZnY4D9moXzTFc-7i8"}
     * count : 9
     * devName : 合生帝景苑1
     * brokerName : 张宇
     */

    public String realName;
    public String phone;
    public EffectBean effect;
    public int count;
    public String devName;
    public String brokerName;

    public static class EffectBean {
        /**
         * thumbnailImage : http://olpdjpvtn.bkt.clouddn.com/Fi4JYs7US0SZnY4D9moXzTFc-7i8
         * url : http://olpdjpvtn.bkt.clouddn.com/Fi4JYs7US0SZnY4D9moXzTFc-7i8
         */

        public String thumbnailImage;
        public String url;
    }
}
