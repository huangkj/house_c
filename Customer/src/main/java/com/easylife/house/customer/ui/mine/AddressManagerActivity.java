package com.easylife.house.customer.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.AddressListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.AddressBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 地址管理
 */
public class AddressManagerActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.recyclerAddress)
    RecyclerView recyclerAddress;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, AddressManagerActivity.class), requestCode);
    }

    public static void startActivity(Context activity) {
        activity.startActivity(new Intent(activity, AddressManagerActivity.class));
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_address_manager, null);
    }

    private AddressListAdapter adapter;

    @Override
    protected void initView() {

        adapter = new AddressListAdapter(activity, mDao, R.layout.item_address, null, this);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_pub, null);
        ((TextView) emptyView.findViewById(R.id.tvEmpty)).setText("暂无地址,去新增收货地址");
        adapter.setEmptyView(emptyView);
        recyclerAddress.setLayoutManager(new LinearLayoutManager(this));
        recyclerAddress.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter a, View view, final int position) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(AddressManagerActivity.this);
                builder.content("确定删除").positiveText("确定").negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mDao.deleteAddress(2, adapter.getItem(position).getId(), AddressManagerActivity.this);
                    }
                });
                builder.show();


                return true;
            }
        });
        getNetData();

    }


    @Override
    protected void setActionBarDetail() {

    }

    private void getNetData() {
        mDao.addressList(1, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                List<AddressBean> data = new Gson().fromJson(response, new TypeToken<List<AddressBean>>() {
                }.getType());
                adapter.setNewData(data);
                break;
            case 2:
                ToastUtils.showShort(activity, "成功");
                getNetData();
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        switch (requestType) {
            case 2:
                ToastUtils.showShort(activity, code.msg);
                break;
        }
    }

    @OnClick(R.id.btnAdd)
    public void onViewClicked() {
        AddressEditActivity.startActivity(activity, null, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getNetData();
        }
    }
}
