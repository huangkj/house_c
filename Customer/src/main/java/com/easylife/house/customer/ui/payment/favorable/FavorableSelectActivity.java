package com.easylife.house.customer.ui.payment.favorable;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.FavorableVipAdapter;
import com.easylife.house.customer.adapter.ItemClickListener;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Mars on 2017/4/7 16:32.
 * 描述：选择优惠
 */

public class FavorableSelectActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.lvFavorableVip)
    RecyclerView lvFavorableVip;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;
    private String selectId;

    public static void startActivity(Activity activity, HousesDetailBaseBean detail, int requestCode) {
        activity.startActivityForResult(new Intent(activity, FavorableSelectActivity.class)
                        .putExtra("detail", detail)
                , requestCode
        );
    }

    public static void startActivity(Activity activity, HousesDetailBaseBean detail, String id, int requestCode) {
        activity.startActivityForResult(new Intent(activity, FavorableSelectActivity.class)
                        .putExtra("detail", detail)
                        .putExtra("id", id)
                , requestCode
        );
    }

    public static void startActivity(Fragment fragment, HousesDetailBaseBean detail, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), FavorableSelectActivity.class)
                        .putExtra("detail", detail)
                , requestCode
        );
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_favorable_select, null);
    }

    private HousesDetailBaseBean detail;

    private FavorableVipAdapter adapter;
    private OrderParameter parameter = new OrderParameter();

    @Override
    protected void initView() {
        detail = (HousesDetailBaseBean) getIntent().getSerializableExtra("detail");
        selectId = getIntent().getStringExtra("id");
        if (detail == null)
            return;
        parameter.followType = "5";
        parameter.estateProjectDevId = detail.devId;
        parameter.estateProjectDevName = detail.devName;


        adapter = new FavorableVipAdapter(activity, R.layout.item_favorable_vip, null);
        if (!TextUtils.isEmpty(selectId)) {
            adapter.setIdSelected(selectId);
        }
        lvFavorableVip.setLayoutManager(new LinearLayoutManager(this));
        lvFavorableVip.setAdapter(adapter);
        adapter.setItemClickListener(new ItemClickListener<FavorableVip>() {
            @Override
            public void itemClick(int viewId, int position, FavorableVip data) {
                tvDesc.setText(Html.fromHtml("该优惠券适用于" +
                        detail.devName +
                        " 项目，有效期为" +
                        "<font   color=\"#FF6800\">" +
                        data.beginTime +
                        "</font>至<font   color=\"#FF6800\">" +
                        data.endTime +
                        "</font>。"));
            }
        });

        mDao.selectEstateProjectVip(1, detail.devId, this);
    }

    private void initFavorable(List<FavorableVip> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        adapter.setNewData(data);
        FavorableVip f = data.get(0);
        tvDesc.setText(Html.fromHtml("该优惠券适用于 " +
                detail.devName +
                " 项目，有效期为" +
                "<font   color=\"#FF6800\">" +
                f.beginTime +
                "</font>至<font   color=\"#FF6800\">" +
                f.endTime +
                "</font>。"));
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    setResult(resultCode);
                    finish();
                    break;
            }
        }
    }

    @OnClick({R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                FavorableVip vip = adapter.getSelectedFavorable();
                if (vip == null) {
                    CustomerUtils.showTip(activity, "请先选择优惠券");
                    return;
                }

                parameter.menberDiscountId = vip.id;
                parameter.orderLog.payOnThisTime = "0";
                parameter.orderLog.pay = vip.num + "";

                OrderParameter.Favorable favorable = new OrderParameter.Favorable();
                favorable.content = vip.privilege;
                favorable.pay = vip.num + "";
                favorable.discountType = OrderParameter.Favorable.TYPE_VIP;

                parameter.orderDiscount.clear();
                parameter.orderDiscount.add(favorable);

                FavorableBuyActivity.startActivity(activity, null, detail, parameter, vip.num + "", vip.num + "", 1);
                break;
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                // 会员优惠信息
                List<FavorableVip> list = new Gson().fromJson(response, new TypeToken<List<FavorableVip>>() {
                }.getType());
                initFavorable(list);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
