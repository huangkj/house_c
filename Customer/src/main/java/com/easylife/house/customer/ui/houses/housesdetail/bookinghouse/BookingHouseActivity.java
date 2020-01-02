package com.easylife.house.customer.ui.houses.housesdetail.bookinghouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.pub.MainActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DateUtil;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.TimeUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.LOOK_HOUSE_CHANGE;
import static com.easylife.house.customer.event.MessageEvent.LOOK_HOUSE_CHANGE_SEE;

/**
 * 预约看房
 */
public class BookingHouseActivity extends MVPBaseActivity<BookingHouseContract.View, BookingHousePresenter> implements
        BookingHouseContract.View, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.iv_house)
    ImageView ivHouse;
    @Bind(R.id.tv_house_name)
    TextView tvHouseName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tv_room)
    TextView tvRoom;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.line3)
    View line3;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.et_time)
    TextView etTime;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.cb_agreen)
    CheckBox cbAgreen;
    @Bind(R.id.tv_agree)
    TextView tvAgree;
    @Bind(R.id.tv_ok)
    TextView tvOk;
    @Bind(R.id.tv_sms)
    TextView tvSms;
    @Bind(R.id.et_sms)
    EditText etSms;
    @Bind(R.id.tv_get_sms)
    TextView tvGetSms;
    @Bind(R.id.rl_sms)
    RelativeLayout rlSms;
    private Calendar now;
    private int year;
    private int month;
    private int day;

    private String chooseTime;
    private HousesDetailBaseBean base_bean;


    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_booking_house, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        base_bean = (HousesDetailBaseBean) getIntent().getSerializableExtra("BASE_BEAN");

        if (dao.getCustomer() != null) {
            if (!TextUtils.isEmpty(dao.getCustomer().phone)) {
                rlSms.setVisibility(View.GONE);
                etPhone.setText(dao.getCustomer().phone);
                etPhone.setFocusable(false);
                etPhone.setClickable(false);
            } else {
                rlSms.setVisibility(View.VISIBLE);
                etPhone.setFocusable(true);
                etPhone.setClickable(true);
            }

            if (!TextUtils.isEmpty(dao.getCustomer().username)) {
                etName.setText(dao.getCustomer().username);
                etName.setFocusable(false);
                etName.setClickable(false);
            } else {
                etName.setFocusable(true);
                etName.setClickable(true);
            }
        }
        if (base_bean != null) {
            if (base_bean.effectId != null && base_bean.effectId.size() != 0) {
                CacheManager.initImageClientList(this, ivHouse, base_bean.effectId.get(0).url);
            }
            tvHouseName.setText(base_bean.devName);
            tvPrice.setText(base_bean.averPrice + "元/㎡");
            tvArea.setText(base_bean.devSquareMetre + "㎡");
            tvRoom.setText(base_bean.devBedroom);
            tvAddress.setText(base_bean.addressDistrict + "   " + base_bean.addressTown + "  " + base_bean.propertyType);

        }

    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("预约看房");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    @OnClick({R.id.tv_agree, R.id.tv_ok, R.id.et_time, R.id.tv_get_sms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_agree:
                WebViewActivity.startActivity(activity, "好生活新房委托协议", Constants.URL_AGREEMENT_LOOK_HOUSE, "我已阅读");
                break;
            case R.id.et_time:
                now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.setAccentColor(getResources().getColor(R.color.gradient_start));
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                break;
            case R.id.tv_ok:

                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    CustomerUtils.showTip(this, "姓名不能为空");
                    return;
                }

                if (TextUtils.isEmpty(etTime.getText().toString().trim())) {
                    CustomerUtils.showTip(this, "请选择看房时间");
                    return;
                }

                if (!ValidatorUtils.isMobile(etPhone.getText().toString().trim())) {
                    CustomerUtils.showTip(this, "请输入正确的手机号码");
                    return;
                }

                if (!cbAgreen.isChecked()) {
                    CustomerUtils.showTip(this, "请阅读并同意协议");
                    return;
                }

                if (rlSms.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(etSms.getText().toString().trim())) {
                        CustomerUtils.showTip(this, "请输入短信验证码");
                        return;
                    }
                }

                //防止登录失效
                if (dao.isLogin()) {
                    int secondsFromDate = CustomerUtils.getSecondsFromDate(chooseTime, "yyyy-MM-dd HH:mm");
                    showLoading();
                    LoginResult loginCache = dao.getLoginCache();
                    mPresenter.requestBookHouse(base_bean.devId, loginCache.userCode, etSms.getText().toString().trim(), loginCache.token,
                            etName.getText().toString().trim(), etPhone.getText().toString().trim(), secondsFromDate + "");
                } else {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
                }
                break;
            //获取手机验证码
            case R.id.tv_get_sms:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    CustomerUtils.showTip(this, "请输入手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    CustomerUtils.showTip(this, "请输入正确的手机号");
                    return;
                }
                mPresenter.getVerifyCode(etPhone.getText().toString().trim());
                TimeUtils timeUtils = new TimeUtils(tvGetSms, "获取验证码", this);
                timeUtils.RunTimer();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear + 1;
        day = dayOfMonth;

        TimePickerDialog tpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), true);
        tpd.setAccentColor(getResources().getColor(R.color.gradient_start));
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String minuteStr = "";
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = minute + "";
        }
        chooseTime = year + "-" + month + "-" + day + " " + hourOfDay + ":" + minuteStr;
        etTime.setText(chooseTime);
    }

    @Override
    public void showSuccess() {
        dao.pointSubLook(base_bean.devId, base_bean.devName, DateUtil.dateToLong(etTime.getText().toString().trim(), "yyyy-MM-dd HH:mm"), "", "");
        cancelLoading();
//        CustomerUtils.showTip(this,"预约成功");

        if (rlSms.getVisibility() == View.VISIBLE) {
            Customer customer = dao.getCustomer();
            if (customer != null) {
                customer.phone = etPhone.getText().toString().trim();
            }
            dao.saveCustomer(customer);
        }

        new AlertDialog.Builder(this).setTitle("成功预约")
                .setMessage("好生活平台即将成为您安排专属购\n房咨询服务，TA稍后与您联系。 ")
                .setPositiveButton("继续看房", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("查看看房日程", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SearchSingleton searchSingleton = SearchSingleton.getIstance();
                                searchSingleton.lookHouse.add(BookingHouseActivity.this);

                                for (int i = 0; i < searchSingleton.lookHouse.size(); i++) {
                                    searchSingleton.lookHouse.get(i).finish();
                                }
                                searchSingleton.lookHouse.clear();
                                EventBus.getDefault().post(new MessageEvent(LOOK_HOUSE_CHANGE_SEE));
                                dialog.dismiss();
                                startActivity(new Intent(BookingHouseActivity.this, MainActivity.class));
                            }
                        }
                )
                .show();
        EventBus.getDefault().post(new MessageEvent(LOOK_HOUSE_CHANGE));
    }

    @Override
    public void showFail(NetBaseStatus code) {
        cancelLoading();
        if (code != null && "9013".equals(code.code)) {
            TextView title  = new TextView(this);
            title.setGravity(Gravity.CENTER);
            title.setText("提示");
            title.setTextSize(18);
            title.setTextColor(Color.BLACK);
            title.setPadding(0,20,0,0);
            new AlertDialog.Builder(this).setCustomTitle(title)
                    .setMessage("请勿重复提交相同预约，可在看房\n中查看网上预约。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void getVerifyCodeSucc() {
        CustomerUtils.showTip(this, "获取验证码成功");
    }
}
