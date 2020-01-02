package com.easylife.house.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 银行支行
 */
public class BeanBankSub implements Serializable, Parcelable {
    public String bankBranchName; // 支行名称
    public String linkNumber; // 联行号

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankBranchName);
        dest.writeString(this.linkNumber);
    }

    public BeanBankSub() {
    }

    protected BeanBankSub(Parcel in) {
        this.bankBranchName = in.readString();
        this.linkNumber = in.readString();
    }

    public static final Parcelable.Creator<BeanBankSub> CREATOR = new Parcelable.Creator<BeanBankSub>() {
        @Override
        public BeanBankSub createFromParcel(Parcel source) {
            return new BeanBankSub(source);
        }

        @Override
        public BeanBankSub[] newArray(int size) {
            return new BeanBankSub[size];
        }
    };
}
