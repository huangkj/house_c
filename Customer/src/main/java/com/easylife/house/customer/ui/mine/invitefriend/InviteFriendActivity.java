package com.easylife.house.customer.ui.mine.invitefriend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.InviteCodeBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.GsonUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static com.easylife.house.customer.util.FileUtils.SDPATH;

public class InviteFriendActivity extends BaseActivity {
    @Bind(R.id.ivQrcode)
    ImageView ivQrcode;
    @Bind(R.id.tvVerifyCode)
    TextView tvVerifyCode;
    @Bind(R.id.btInviteFriend)
    Button btInviteFriend;
    private Bitmap qrCodeBitmap;
    private InviteCodeBean inviteCodeBean;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_invite_friend, null);
    }

    @Override
    protected void initView() {
        showLoading();
        mDao.invitation(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                cancleLoading();
                inviteCodeBean = GsonUtils.fromJson(response, InviteCodeBean.class);
                tvVerifyCode.setText(inviteCodeBean.getInvitationCode());
                qrCodeBitmap = CodeUtils.createImage(inviteCodeBean.getInvitationUrl(), 400, 400, null);
                ivQrcode.setImageBitmap(qrCodeBitmap);

            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                cancleLoading();
                ToastUtils.showShort(code.msg);
            }
        });

        ivQrcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(InviteFriendActivity.this).setMessage("保存二维码到本地").setPositiveButton("保存",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(SDPATH, "qrcode.jpg");
                                ImageUtils.save(qrCodeBitmap, file, JPEG, false);
                                ToastUtils.showShort("图片已保存到" + SDPATH);
                            }
                        }).create().show();


                return true;
            }
        });
    }


    @OnClick({R.id.btInviteFriend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btInviteFriend:
                share(inviteCodeBean.getInvitationUrl(), "好生活好房", "我正在好生活好房看房，更有精彩积分福利抽奖活动等您", "");
                break;

        }
    }

    @Override
    protected void setActionBarDetail() {

    }

}
