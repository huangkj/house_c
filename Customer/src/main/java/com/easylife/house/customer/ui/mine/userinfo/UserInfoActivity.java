package com.easylife.house.customer.ui.mine.userinfo;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.ResultEditUserInfo;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.config.ItemSelectManager;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.mine.MineInfoEditActivity;
import com.easylife.house.customer.ui.pub.PubSelectAddressActivity;
import com.easylife.house.customer.ui.pub.PubSelectItemPopActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DateUtil;
import com.easylife.house.customer.util.ImagePrickerUtil;
import com.easylife.house.customer.util.ToastUtils;
import com.easylife.house.customer.view.photoview.RoundedImageView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.event.MessageEvent.RESTART_ACTIVITY_GAME;


/**
 * 我的账户
 */
@RuntimePermissions
public class UserInfoActivity extends MVPBaseActivity<UserInfoContract.View, UserInfoPresenter> implements UserInfoContract.View, RequestManagerImpl {

    @Bind(R.id.layUserHeader)
    RelativeLayout layUserHeader;
    @Bind(R.id.imgUserHeader)
    public RoundedImageView imgUserHeader;
    @Bind(R.id.tvUserName)
    public TextView tvUserName;
    @Bind(R.id.layUserName)
    RelativeLayout layUserName;
    @Bind(R.id.tvUserPhone)
    public TextView tvUserPhone;
    @Bind(R.id.layUserPhone)
    RelativeLayout layUserPhone;
    @Bind(R.id.tvUserNike)
    TextView tvUserNike;
    @Bind(R.id.layUserNike)
    RelativeLayout layUserNike;
    @Bind(R.id.tvUserEmail)
    TextView tvUserEmail;
    @Bind(R.id.layUserEmail)
    RelativeLayout layUserEmail;
    @Bind(R.id.layUserSex)
    RelativeLayout layUserSex;
    @Bind(R.id.tvSex)
    public TextView tvSex;
    @Bind(R.id.tvUserBirthday)
    TextView tvUserBirthday;
    @Bind(R.id.layUserBirthday)
    RelativeLayout layUserBirthday;
    @Bind(R.id.tvUserJob)
    TextView tvUserJob;
    @Bind(R.id.layUserJob)
    RelativeLayout layUserJob;
    @Bind(R.id.tvUserHome)
    TextView tvUserHome;
    @Bind(R.id.layUserHome)
    RelativeLayout layUserHome;
    @Bind(R.id.tvUserAddress)
    TextView tvUserAddress;
    @Bind(R.id.layUserAddress)
    RelativeLayout layUserAddress;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_info, null);
    }

    private Customer customer;
    private TimePickerView pvTime;

    @Override
    protected void initView() {
        mPresenter.getCacheUserInfo();
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {
        mPresenter.getCacheUserInfo();
    }

    @Override
    public void initUserInfo(Customer info) {
        if (info == null)
            return;

        customer = info;

        tvUserNike.setText(info.username);
        tvUserName.setText(info.realName);
        tvUserPhone.setText(info.phone);
        tvUserEmail.setText(info.email);
        tvUserAddress.setText(info.address);
        tvUserBirthday.setText(info.age);
        tvUserHome.setText(ItemSelectManager.getSelectItemsText(ItemSelectManager.TYPE_SELECT_INFO_FAMILY_STRUCT, info.family));
        tvUserJob.setText(ItemSelectManager.getSelectItemsText(ItemSelectManager.TYPE_SELECT_INFO_OCCUPTATION, info.profession));
        if ("0".equals(info.sex)) {
            tvSex.setText("男");
        } else if ("1".equals(info.sex)) {
            tvSex.setText("女");
        } else if ("2".equals(info.sex)) {
            tvSex.setText("其他");
        } else {
            tvSex.setText(null);
        }
        CacheManager.initImageUserHeader(activity, imgUserHeader, info.headimg);
    }

    @Override
    public void saveSucc() {
        cancelLoading();
        showTip("用户信息修改成功");
        setResult(RESULT_OK);
        initUserInfo(customer);
    }

    @Override
    public void saveFail() {
        showTip("用户头像上传失败");
    }

    @Override
    public void updateUserHeaderSucc(String imgPath) {
        CacheManager.initImageUserHeader(activity, imgUserHeader, imgPath);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void takePhoto() {
        ImagePrickerUtil.pickerImage(activity, 1, 11);
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
        UserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @OnClick({R.id.layUserHeader, R.id.layUserName, R.id.layUserHome, R.id.layUserAddress,
            R.id.layUserNike, R.id.layUserEmail, R.id.layUserSex, R.id.layUserBirthday, R.id.layUserJob})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layUserHeader:
                UserInfoActivityPermissionsDispatcher.takePhotoWithCheck(this);
                break;
            case R.id.layUserName:
                MineInfoEditActivity.startActivity(activity, 2, customer.realName, 1);
                break;
            case R.id.layUserNike:
                MineInfoEditActivity.startActivity(activity, 0, customer.username, 1);
                break;
            case R.id.layUserEmail:
                MineInfoEditActivity.startActivity(activity, 1, customer.email, 1);
                break;
            case R.id.layUserSex:
                PubSelectItemPopActivity.startActivity(activity, ItemSelectManager.TYPE_SELECT_INFO_SEX, null, ItemSelectManager.getSelectItemsText(ItemSelectManager.TYPE_SELECT_INFO_SEX, customer.sex), 2);
                break;
            case R.id.layUserBirthday:
                //    年月日选择
                if (pvTime == null) {
                    //时间选择器
                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();
                    startCalendar.set(1900, 0, 1);
                    endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));
                    //选中事件回调
                    pvTime = new TimePickerBuilder(activity, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            String birthday = DateUtil.dateToStr(date, "yyyy年MM月dd日");
                            tvUserBirthday.setText(birthday);
                            dao.editUserBirthday(11, birthday, UserInfoActivity.this);
                        }
                    }).build();
                    pvTime.setDate(Calendar.getInstance());
                }
                if (pvTime.isShowing()) {
                    pvTime.dismiss();
                } else {
                    pvTime.show();
                }
                break;
            case R.id.layUserJob:
                PubSelectItemPopActivity.startActivity(activity, ItemSelectManager.TYPE_SELECT_INFO_OCCUPTATION, null, ItemSelectManager.getSelectItemsText(ItemSelectManager.TYPE_SELECT_INFO_OCCUPTATION, customer.profession), 3);
                break;
            case R.id.layUserHome:
                PubSelectItemPopActivity.startActivity(activity, ItemSelectManager.TYPE_SELECT_INFO_FAMILY_STRUCT, null, ItemSelectManager.getSelectItemsText(ItemSelectManager.TYPE_SELECT_INFO_FAMILY_STRUCT, customer.family), 4);
                break;
            case R.id.layUserAddress:
                //   省市二级联动选择
                PubSelectAddressActivity.startActivity(activity, null, null, null, null, null, null, 5);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    int type = data.getIntExtra("type", 0);
                    String text = data.getStringExtra("text");
                    switch (type) {
                        case 1:
                            // 邮箱
                            tvUserEmail.setText(text);
                            break;
                        case 2:
                            // 姓名
                            tvUserName.setText(text);
                            break;
                        default:
                            // 用户名
                            tvUserNike.setText(text);
                            break;
                    }
                    break;
                case 2:
                    // 选择性别返回
                    String sexId = PubSelectItemPopActivity.getSelectItemID(data);
                    String sex = PubSelectItemPopActivity.getSelectItemText(data);
                    tvSex.setText(sex);
                    dao.editUserSex(11, sexId, this);
                    break;
                case 3:
                    // 选择职业返回
                    String jobId = PubSelectItemPopActivity.getSelectItemID(data);
                    String job = PubSelectItemPopActivity.getSelectItemText(data);
                    tvUserJob.setText(job);
                    dao.editUserProfession(11, jobId, this);
                    break;
                case 4:
                    // 选择家庭结构返回
                    String familyId = PubSelectItemPopActivity.getSelectItemID(data);
                    String family = PubSelectItemPopActivity.getSelectItemText(data);
                    tvUserHome.setText(family);
                    dao.editUserFamily(11, familyId, this);
                    break;
                case 5:
                    // 省市地址选择返回
                    String address = PubSelectAddressActivity.getText(data);
                    tvUserAddress.setText(address);
                    dao.editUserAddress(11, address, this);
                    break;
                case 11:
                    showLoading();
                    String path = ImagePrickerUtil.getImageFirst(data);
                    dao.updateImgSingle(1, path, path, this);
                    break;
            }
        }
    }

    public void btnBack() {
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            btnBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                // 上传头像    头像上传失败待调试
                ImageResult.DataBean data = dao.analyzeUploadSingleImageResult(response);
                if (data == null || TextUtils.isEmpty(data.url)) {
                    showTip("图片上传失败");
                    return;
                }
                CacheManager.initImageUserHeader(activity, imgUserHeader, data.url);
                dao.editUserHeader(11, data.url, this);
                break;
            case 11:
                // 用户信息编辑成功
                cancelLoading();
                ResultEditUserInfo resultEditUserInfo = new Gson().fromJson(response, ResultEditUserInfo.class);
                if (!TextUtils.isEmpty(resultEditUserInfo.msg) && resultEditUserInfo.msg.contains("恭喜你")) {
                    Customer customer = dao.getCustomer();
                    customer.authentication = true;
                    EventBus.getDefault().post(new MessageEvent(RESTART_ACTIVITY_GAME));
                    dao.saveCustomer(customer);
                    dao.getUserInfo(22, this);
                    new AlertDialog.Builder(activity)
                            .setMessage(resultEditUserInfo.msg)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    WebViewActivity.startActivity(UserInfoActivity.this, "游戏活动", Constants.ACTIVITY_GAME_URL + dao.localDao.getCustomer().id);
                                }
                            }).show();
                } else {
                    ToastUtils.showShort(activity, TextUtils.isEmpty(resultEditUserInfo.msg) ? "修改成功" : resultEditUserInfo.msg);
                }
                setResult(RESULT_OK);
                break;

            case 22:
                Customer customer = new Gson().fromJson(response, Customer.class);
                if (customer == null)
                    return;
                dao.saveCustomer(customer);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        cancelLoading();
        switch (requestType) {
            case 11:
                showTip("用户信息编辑失败");
                break;
        }
    }
}
