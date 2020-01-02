package com.easylife.house.customer.bean;

public class PublicImage implements AddImage, IPhoto {

	private static final long serialVersionUID = 1L;

	public String cover;
	public boolean isAdd = false;

	@Override
	public boolean isAdd() {
		return isAdd;
	}

	@Override
	public String toString() {
		return "PublicImage [cover=" + cover + ", isAdd=" + isAdd + "]";
	}

	@Override
	public String getPhotoPath() {
		return cover;
	}

	@Override
	public int getPhotoRes() {
		return 0;
	}

}
