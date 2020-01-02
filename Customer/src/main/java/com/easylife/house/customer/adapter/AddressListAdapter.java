package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.AddressBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.AddressEditActivity;
import com.easylife.house.customer.ui.mine.AddressManagerActivity;

import java.util.List;

/**
 * 收货地址列表
 */
public class AddressListAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {
    private Activity activity;
    private ServerDao mDao;
    private RequestManagerImpl requestManager;

    public AddressListAdapter(int layoutResId, @Nullable List<AddressBean> data) {
        super(layoutResId, data);
    }

    public AddressListAdapter(Activity activity, ServerDao mDao, int layoutResId, @Nullable List<AddressBean> data, RequestManagerImpl requestManager) {
        super(layoutResId, data);
        this.activity = activity;
        this.mDao = mDao;
        this.requestManager = requestManager;
    }

    @Override
    protected void convert(BaseViewHolder helper, final AddressBean item) {
        helper.setText(R.id.tvName, item.getUserName());
        helper.setText(R.id.tvPhone, item.getPhoneNum());
        helper.setText(R.id.tvDetail, item.getAddressFull());
        helper.setText(R.id.cbDefault, "1".equals(item.getIsDefault()) ? "  默认地址" : "   设为默认地址");
        helper.setChecked(R.id.cbDefault, "1".equals(item.getIsDefault()));
        if (TextUtils.isEmpty(item.getTagName())) {
            helper.setGone(R.id.tvTag, false);
        } else {
            helper.setGone(R.id.tvTag, true);
            helper.setText(R.id.tvTag, item.getTagName());
        }

        helper.setOnClickListener(R.id.imgEdit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressEditActivity.startActivity(activity, item.getId(), 1);
            }
        });

        helper.setOnClickListener(R.id.cbDefault, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDao.editAddress(2, item.getId(), item.getUserName(), item.getAddrProvince(), item.getAddrCity(), item.getAddrCounty(),
                        item.getAddrProvinceId(), item.getAddrCityId(), item.getAddrCountyId(), item.getAddressFull(),
                        item.getTagName(), "1".equals(item.getIsDefault()) ? "0" : "1", item.getPhoneNum(), requestManager);
            }
        });
    }
}
