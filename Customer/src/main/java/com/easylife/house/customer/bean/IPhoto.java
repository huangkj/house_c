package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * 图片信息
 */
public interface IPhoto extends Serializable{
	/**
	 * 获取图片路径
	 * @return
	 */
	public String getPhotoPath();
	/**
	 * 项目里的图片路径
	 * @return
	 */
	public int getPhotoRes();
}
