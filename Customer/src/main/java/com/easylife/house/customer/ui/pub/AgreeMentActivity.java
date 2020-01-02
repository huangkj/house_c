package com.easylife.house.customer.ui.pub;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;

import butterknife.Bind;

/**
 * 用户协议
 */
public class AgreeMentActivity extends BaseActivity {

    @Bind(R.id.btn_read)
    Button btnRead;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_agree_ment, null);
    }

    @Override
    protected void initView() {
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void setActionBarDetail() {
        layActionBar.setBackgroundColor(Color.WHITE);
    }
}
