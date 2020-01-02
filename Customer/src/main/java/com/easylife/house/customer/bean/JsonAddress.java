package com.easylife.house.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 本地城市数据
 */
public class JsonAddress {

    public ArrayList<Data> RECORDS;

    public static class Data implements Serializable, IPickerViewData, Parcelable {

        /**
         * "id":"24",
         * "provinceid":"520000",
         * "province":"贵州省"
         * <p>
         * "id":"341",
         * "cityid":"653200",
         * "city":"和田地区",
         * "provinceid":"650000"
         * <p>
         * "id":"19",
         * "areaid":"120101",
         * "area":"和平区",
         * "cityid":"120100"
         */

        public String id;

        public String province;
        public String provinceid;

        public String city;
        public String cityid;

        public String area;
        public String areaid;

        @Override
        public String getPickerViewText() {
            if (!TextUtils.isEmpty(area)) {
                return area;
            }
            if (!TextUtils.isEmpty(city)) {
                return city;
            }
            if (!TextUtils.isEmpty(province)) {
                return province;
            }
            return null;
        }

        public String getPickerViewId() {
            if (!TextUtils.isEmpty(area)) {
                return areaid;
            }
            if (!TextUtils.isEmpty(city)) {
                return cityid;
            }
            if (!TextUtils.isEmpty(province)) {
                return provinceid;
            }
            return null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.province);
            dest.writeString(this.provinceid);
            dest.writeString(this.city);
            dest.writeString(this.cityid);
            dest.writeString(this.area);
            dest.writeString(this.areaid);
        }

        public Data() {
        }

        protected Data(Parcel in) {
            this.id = in.readString();
            this.province = in.readString();
            this.provinceid = in.readString();
            this.city = in.readString();
            this.cityid = in.readString();
            this.area = in.readString();
            this.areaid = in.readString();
        }

        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }

}
