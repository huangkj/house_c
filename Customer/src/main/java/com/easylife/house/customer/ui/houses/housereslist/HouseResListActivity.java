package com.easylife.house.customer.ui.houses.housereslist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ExpandableItemSaleAdapter;
import com.easylife.house.customer.bean.SalingBean;
import com.easylife.house.customer.bean.SalingPersonItem;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.houseresource.HouseResourceActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 房源列表页
 * zgm
 */
@RuntimePermissions
public class HouseResListActivity extends MVPBaseActivity<HouseResListContract.View, HouseResListPresenter> implements HouseResListContract.View {

    @Bind(R.id.tv_build)
    TextView tvBuild;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_floor)
    TextView tvFloor;
    @Bind(R.id.tv_satus)
    TextView tvSatus;
    @Bind(R.id.ll_top)
    LinearLayout llTop;
    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.rl_ask)
    RelativeLayout rlAsk;
    @Bind(R.id.tv_look)
    TextView tvLook;
    @Bind(R.id.tv_call)
    TextView tvCall;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_house_res_list, null);
    }

    @Override
    protected void initView() {

        ArrayList<MultiItemEntity> list = new ArrayList<>();

        SalingBean bean = new SalingBean();
        bean.count = "2";
        bean.simple = "明厨明卫";
        bean.salingTitle = "3室1厅2卫";
        bean.farea = "120.8-142.7㎡";
        bean.price = "365.8万元/套";


        for (int i = 0; i < 2; i++) {
            SalingPersonItem item = new SalingPersonItem();
            item.title = "2#" + (i + 1) + "单元-" + (i + 2) + "02";
            item.area = "123.45㎡";
            item.price = "580.2";
            item.sum = 2;
            bean.addSubItem(item);
        }
        list.add(bean);


        final ExpandableItemSaleAdapter mAdapter = new ExpandableItemSaleAdapter(list);

        RecyclerView.LayoutManager llManager = new LinearLayoutManager(this);
        recycle.setAdapter(mAdapter);
        recycle.setLayoutManager(llManager);

        mAdapter.setChildItemClickListenear(new ExpandableItemSaleAdapter.ChildItemClickListenear() {
            @Override
            public void click(MultiItemEntity item) {
                HouseResourceActivity.startActivity(HouseResListActivity.this, ((SalingPersonItem) item).title, ((SalingPersonItem) item).id, 0);
            }
        });

    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("合生  中山  房源");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    @OnClick({R.id.tv_build, R.id.tv_type, R.id.tv_floor, R.id.tv_satus, R.id.rl_ask, R.id.tv_look, R.id.tv_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_build:
                break;
            case R.id.tv_type:
                break;
            case R.id.tv_floor:
                break;
            case R.id.tv_satus:
                break;
            case R.id.rl_ask:
                CustomerUtils.showTip(this, "敬请期待");
                break;
            case R.id.tv_look:
                break;
            case R.id.tv_call:
                call("15011380929");
                break;
        }
    }

    /**
     * 显示拨号提示框
     *
     * @param phone
     */
    public void call(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(this, "手机号错误");
            return;
        }
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否拨打 " + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HouseResListActivityPermissionsDispatcher.jumpCallPhoneWithCheck(HouseResListActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HouseResListActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
