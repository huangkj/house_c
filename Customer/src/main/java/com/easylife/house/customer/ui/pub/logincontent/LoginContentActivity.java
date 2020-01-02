/*
package com.easylife.house.customer.ui.pub.logincontent;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import com.easylife.house.customer.App;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.im.LifeHelper;
import com.easylife.house.customer.ui.im.db.LifeDBManager;
import com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity;
import com.easylife.house.customer.ui.pub.MainActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.ui.pub.loginnormal.LoginNormalActivity;
import com.easylife.house.customer.ui.pub.register.RegisterActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.ImageViewTouch;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CODE_BIND_USER;

*/
/**
 * 第三方登录
 *//*

public class LoginContentActivity extends MVPBaseActivity<LoginContentContract.View, LoginContentPresenter> implements LoginContentContract.View, RequestManagerImpl {

    @Bind(R.id.btnVerifyCode)
    ButtonTouch btnVerifyCode;
    @Bind(R.id.btnNormal)
    ButtonTouch btnNormal;
    @Bind(R.id.btnRegiste)
    ButtonTouch btnRegiste;
    @Bind(R.id.imgQQ)
    ImageViewTouch imgQQ;
    @Bind(R.id.imgWeChat)
    ImageViewTouch imgWeChat;
    @Bind(R.id.imgMicroblog)
    ImageViewTouch imgMicroblog;

    private boolean loginByThird;

    public static void startActivity(Context activity) {
        activity.startActivity(new Intent(activity, LoginContentActivity.class));
    }

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, LoginContentActivity.class), requestCode);
    }

    public static void startActivity(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), LoginContentActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_login, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        findViewById(R.id.iconLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, MainActivity.class));
                exit();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uridata = getIntent().getData();
        if (null != uridata && dao.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setBackgroundColor(Color.WHITE);
        imgBack.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.mipmap.edit_clear);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void loginFail() {
        cancelLoading();
        showTip("登录失败");
    }

    @Override
    public void loginSucc() {
        showTip("登录成功");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_STATE_CHANGE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN));
        cancelLoading();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void loginSuccIm(String easeUsername, String easePassword) {
//        showLoading();
        loginIm(easeUsername, easePassword);
    }

    @OnClick({R.id.btnVerifyCode, R.id.btnNormal, R.id.btnRegiste
            , R.id.imgQQ, R.id.imgWeChat, R.id.imgMicroblog
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnVerifyCode:
                loginByThird = false;
                startActivityForResult(new Intent(activity, LoginByVerifyCodeActivity.class), 1);
                break;
            case R.id.btnNormal:
                loginByThird = false;
                startActivityForResult(new Intent(activity, LoginNormalActivity.class), 1);
                break;
            case R.id.btnRegiste:
                loginByThird = false;
                startActivityForResult(new Intent(activity, RegisterActivity.class), 2);
                break;
            case R.id.imgQQ:
                showLoading();
                loginByQQ(activity, this);
                break;
            case R.id.imgWeChat:
                showLoading();
                loginByWeChat(activity, this);
                break;
            case R.id.imgMicroblog:
                showLoading();
                loginByiMcroblog(activity, this);
                break;
        }
    }

    */
/**
 * 第三方登录QQ  防止mView报空
 *
 * @param activity
 * @param view
 *//*

    public void loginByQQ(final Activity activity, final LoginContentContract.View view) {
        loginByThird = true;
        UMShareAPI.get(view.getContext()).deleteOauth(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogOut.d("deleteOauth*****", "onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogOut.d("deleteOauth*****", "onComplete");
                UMShareAPI.get(view.getContext()).getPlatformInfo(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        String uid = data.get("uid");
                        String name = data.get("name");
                        String iconUrl = data.get("iconurl");

                        loginByOther(1, name, uid, iconUrl, "0", LoginContentActivity.this);
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        if (t.getMessage().contains("2008")) {
                            showTip("您未安装QQ，请先安装QQ");
                        } else {
                            showTip("QQ授权失败");
                        }
                        cancelLoading();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        cancelLoading();
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogOut.d("deleteOauth*****", "onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogOut.d("deleteOauth*****", "onCancel");
            }
        });
    }

    */
/**
 * 微博登陆
 *
 * @param activity
 * @param view
 *//*

    public void loginByiMcroblog(Activity activity, final LoginContentContract.View view) {
        loginByThird = true;
        UMShareAPI.get(view.getContext()).getPlatformInfo(activity, SHARE_MEDIA.SINA, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                showTip("微博授权成功");
                cancelLoading();
                String uid = data.get("uid");
                String name = data.get("name");
                String iconUrl = data.get("iconurl");

                loginByOther(1, name, uid, iconUrl, "2", LoginContentActivity.this);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                if (t.getMessage().contains("2008")) {
                    showTip("您未安装微博，请先安装微博");
                } else {
                    showTip("微博授权失败");
                }
                cancelLoading();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                cancelLoading();
            }
        });
    }


    */
/**
 * 微信登录
 *
 * @param activity
 * @param view
 *//*

    public void loginByWeChat(final Activity activity, final LoginContentContract.View view) {
        loginByThird = true;
        UMShareAPI.get(view.getContext()).deleteOauth(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogOut.d("deleteOauth*****", "onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogOut.d("deleteOauth*****", "onComplete");
                UMShareAPI.get(view.getContext()).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        showTip("微信授权成功");
                        cancelLoading();
                        String uid = data.get("uid");
                        String name = data.get("name");
                        String iconUrl = data.get("iconurl");

                        loginByOther(1, name, uid, iconUrl, "1", LoginContentActivity.this);
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        if (t.getMessage().contains("2008")) {
                            showTip("您未安装微信，请先安装微信");
                        } else {
                            showTip("微信授权失败");
                        }
                        cancelLoading();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        cancelLoading();
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogOut.d("deleteOauth*****", "onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogOut.d("deleteOauth*****", "onCancel");
            }
        });
    }

    private void loginByOther(int requestType, String name, String tid, String headImg, String type, RequestManagerImpl requestManager) {
        String url = "customer/thirdLogin";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("name", name);
        mapData.put("tid", tid);
        mapData.put("headImg", headImg);
        mapData.put("type", type);
        mapData.put("clientId", dao.getClientID());
        dao.manager.post(requestType, url, mapData, requestManager, false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (loginByThird) {
            if (requestCode == REQUEST_CODE_BIND_USER) {
                // 第三方登录成功后注册手机号返回
                if (resultCode == RESULT_OK) {
                    loginSuccIm(easemobUserName, easemobPassword);
                } else {
                    // 绑定手机号失败，退出登录状态
                    dao.loginOut();
                }
                loginByThird = false;
            }
        } else {
            if (resultCode == RESULT_OK) {
                if (requestCode == 2) {
                    // 注册成功后返回直接登录
                    String phone = data.getStringExtra("phone");
                    String pass = data.getStringExtra("pass");
                    dao.login(2, ServerDao.TYPE_LOGIN_NORMAL, phone, null, pass, this);
                } else {
                    finish();
                }
            }
        }
    }

    private Handler handler = new Handler();

    @Override
    public void showTip(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CustomerUtils.showTip(activity, msg);
            }
        });
    }

    */
/**
 * login登录环信
 *//*

    public void loginIm(String currentUsername, String currentPassword) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        LifeDBManager.getInstance().closeDB();

        // reset current user name before login
        LifeHelper.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // call login method
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d("login", "login: onSuccess");
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        App.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

//                }
                // get user's info (this should be get from App's server or 3rd party service)
                LifeHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                loginSucc();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("login", "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d("login", "login: onError: " + code);
                runOnUiThread(new Runnable() {
                    public void run() {
                        cancelLoading();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String easemobUserName, easemobPassword;

    @Override
    public void onSuccess(String response, int requestType) {
        cancelLoading();
        switch (requestType) {
            case 1:
                //  注册成功后返回直接登录，微信QQ登录成功
                if (!TextUtils.isEmpty(response)) {
                    dao.saveLoginCache(response);
                    LoginResult result = new Gson().fromJson(response, LoginResult.class);
                    Customer customer = new Customer();
                    customer.userCode = result.userCode;
                    customer.token = result.token;
                    customer.easemobUserName = result.easemobUserName;

                    dao.saveCustomer(customer);
                    easemobUserName = result.easemobUserName;
                    easemobPassword = result.easemobPassword;

                    if (TextUtils.isEmpty(result.easemobUserName)) {
                        dao.loginOut();
                    } else {
                        if (loginByThird) {
                            //     跳转绑定手机号
                            dao.getUserInfo(2, LoginContentActivity.this);
                        } else {
                            loginSuccIm(easemobUserName, easemobPassword);
                        }
                    }
                }
                break;
            case 2:
                Customer customer = new Gson().fromJson(response, Customer.class);
                if (customer != null && !TextUtils.isEmpty(customer.phone)) {
                    loginSuccIm(easemobUserName, easemobPassword);
                } else {
                    // TODO: 2019-08-28 记得改回来
//                    loginSuccIm(easemobUserName, easemobPassword);
                    BindMobileActivity.startActivity(activity, REQUEST_CODE_BIND_USER);
                }
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        cancelLoading();
    }

}
*/
