package com.easylife.house.customer.ui.c2b;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.MainPagerAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Mars on 2017/10/18 15:21.
 * 描述：我的推荐
 */
@RuntimePermissions
public class MyRecommendActivity extends BaseActivity {
    @Bind(R.id.btnRecommend)
    RadioButton btnRecommend;
    @Bind(R.id.btnArrived)
    RadioButton btnArrived;
    @Bind(R.id.btnPaid)
    RadioButton btnPaid;
    @Bind(R.id.btnHadSign)
    RadioButton btnHadSign;
    @Bind(R.id.btnRefund)
    RadioButton btnRefund;
    @Bind(R.id.layContent)
    ViewPager layContent;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, MyRecommendActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_recommend, null);
    }

    private MainPagerAdapter adapter;

    @Override
    protected void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(RecordListFragment.newInstance(Customer.TYPE_RECORD_RECOMMEND));
        fragments.add(RecordListFragment.newInstance(Customer.TYPE_RECORD_ARRIVED));
        fragments.add(RecordListFragment.newInstance(Customer.TYPE_RECORD_PAID));
        fragments.add(RecordListFragment.newInstance(Customer.TYPE_RECORD_SIGNED));
        fragments.add(RecordListFragment.newInstance(Customer.TYPE_RECORD_REFUND));
        adapter = new MainPagerAdapter(fragments, getSupportFragmentManager());
        layContent.setAdapter(adapter);
        layContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        btnRecommend.setChecked(true);
                        break;
                    case 1:
                        btnArrived.setChecked(true);
                        break;
                    case 2:
                        btnPaid.setChecked(true);
                        break;
                    case 3:
                        btnHadSign.setChecked(true);
                        break;
                    case 4:
                        btnRefund.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setActionBarDetail() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.mipmap.img_add);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 推荐新的客户
            }
        });
    }

    @OnClick({R.id.btnRecommend, R.id.btnArrived, R.id.btnPaid, R.id.btnHadSign, R.id.btnRefund})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRecommend:
                // 推荐
                layContent.setCurrentItem(0);
                break;
            case R.id.btnArrived:
                // 已到访
                layContent.setCurrentItem(1);
                break;
            case R.id.btnPaid:
                // 已认购
                layContent.setCurrentItem(2);
                break;
            case R.id.btnHadSign:
                // 已签约
                layContent.setCurrentItem(3);
                break;
            case R.id.btnRefund:
                // 退房
                layContent.setCurrentItem(4);
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
                        MyRecommendActivityPermissionsDispatcher.jumpCallPhoneWithCheck(MyRecommendActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone) {
//        mDao.pointCall( dev_id, getDevName(), phone);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
