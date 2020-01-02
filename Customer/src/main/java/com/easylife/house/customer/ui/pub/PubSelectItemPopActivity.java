package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.interfaces.IPickerViewData;
import com.contrarywind.view.WheelView;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.config.ItemSelectManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 底部列表选择弹窗
 *
 * @author Mars
 */
public class PubSelectItemPopActivity extends Activity {

    public static void startActivity(Activity activity, int type,
                                     List<? extends IPickerViewData> data,
                                     String defaultItemText,
                                     int requestCode) {
        startActivity(activity, type, data, null, defaultItemText, requestCode);
    }

    public static void startActivity(Activity activity, int type,
                                     List<? extends IPickerViewData> data,
                                     String defaultItemId, String defaultItemText,
                                     int requestCode) {
        Intent intent = new Intent(activity, PubSelectItemPopActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("defaultItemId", defaultItemId);
        intent.putExtra("defaultItemText", defaultItemText);
        activity.startActivityForResult(intent, requestCode);
    }

    public static String getSelectItemID(Intent data) {
        if (data == null)
            return null;
        ItemSelect a = (ItemSelect) data.getSerializableExtra("selected");
        if (a == null)
            return null;
        return a.id;
    }

    public static String getSelectItemText(Intent data) {
        if (data == null)
            return null;
        ItemSelect a = (ItemSelect) data.getSerializableExtra("selected");
        if (a == null)
            return null;
        return a.text;
    }

    private WheelView mWheel;
    private TextView mTvCancel, mTvOk;

    private int type;
    private String defaultItemText;
    private String defaultItemId;
    private int defaultIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_wheeview_1);
        getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        type = getIntent().getIntExtra("type", 1);
        defaultItemText = getIntent().getStringExtra("defaultItemText");
        defaultItemId = getIntent().getStringExtra("defaultItemId");
        dataList = (ArrayList<ItemSelect>) getIntent().getSerializableExtra("data");

        mWheel = (WheelView) findViewById(R.id.wheeView_center);
        mTvCancel = (TextView) findViewById(R.id.tvcacle);
        mTvOk = (TextView) findViewById(R.id.tvok);

        mWheel.setCyclic(false);

        initData();
    }

    private ArrayWheelAdapter<ItemSelect> adapter;
    private ArrayList<ItemSelect> dataList = new ArrayList<>();


    private void initData() {
        if (dataList == null || dataList.size() == 0) {
            dataList = ItemSelectManager.getSelectItems(type);
            if (dataList == null || dataList.size() == 0) {
                finish();
                return;
            }
        }
        for (int i = 0; i < dataList.size(); i++) {
            ItemSelect a = dataList.get(i);
            if ((!TextUtils.isEmpty(defaultItemId) && defaultItemId.equals(a.getPickerViewId()))
                    || (!TextUtils.isEmpty(defaultItemText) && defaultItemText.equals(a.getPickerViewText()))) {
                defaultIndex = i;
                break;
            }
        }

        adapter = new ArrayWheelAdapter<>(dataList);
        mWheel.setAdapter(adapter);
        mWheel.setCurrentItem(defaultIndex);

        mTvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.layout_parent).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        mTvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemSelect item = (ItemSelect) adapter.getItem(mWheel
                        .getCurrentItem());
                if (item == null) {
                    return;
                }
                Intent data = new Intent();
                data.putExtra("selected", item);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}
