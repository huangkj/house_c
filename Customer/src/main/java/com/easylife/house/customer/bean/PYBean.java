package com.easylife.house.customer.bean;

/**
 * 通讯录列表基类
 *
 * @author Mars
 */
public interface PYBean {
    /**
     * id
     *
     * @return
     */
    public String getId();

    /**
     * 首字母
     *
     * @return
     */
    public String getPys();

    /**
     * 拼音
     *
     * @return
     */
    public String getPinyin();

    /**
     * 名称
     *
     * @return
     */
    public String getBeanName();


    public boolean getSelected();

}
