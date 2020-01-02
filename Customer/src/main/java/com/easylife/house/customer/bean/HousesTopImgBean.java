package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zgm on 2017/3/22.
 * 大图浏览
 */

public class HousesTopImgBean implements Serializable{


    /**
     * img : [{"thumbnailImage":"http://e.hiphotos.baidu.com/image/pic/item/18d8bc3eb13533fa3e01489aadd3fd1f40345bcb.jpg","url":"http://e.hiphotos.baidu.com/image/pic/item/18d8bc3eb13533fa3e01489aadd3fd1f40345bcb.jpg"}]
     * name : 效果图
     */

    public String name;
    public List<ImgBean> img;
    public class ImgBean implements Serializable{
        /**
         * thumbnailImage : http://e.hiphotos.baidu.com/image/pic/item/18d8bc3eb13533fa3e01489aadd3fd1f40345bcb.jpg
         * url : http://e.hiphotos.baidu.com/image/pic/item/18d8bc3eb13533fa3e01489aadd3fd1f40345bcb.jpg
         */

        public String name;
        public String thumbnailImage;
        public String url;
        public String imgUrl;
        public String vrUrl;
        public String videoUrl;
        public int position;//相册的位置
        public int currentPosition;//当前图片所在总相册浏览中的位置
        public int current;//当前图片在当前相册中的位置
    }
}
