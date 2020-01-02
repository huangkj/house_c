package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * 退认筹中的文件
 */
public class BeanFile implements Serializable {
    public String name;
    public String url;
    public String size;

    public boolean isAdd;

    public String type = "0"; // Integer 是 0为图片、1为PDF
}
