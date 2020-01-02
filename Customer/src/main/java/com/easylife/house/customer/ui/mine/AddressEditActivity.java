package com.easylife.house.customer.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.AddressBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.PubSelectAddressActivity;
import com.easylife.house.customer.util.ToastUtils;
import com.easylife.house.customer.util.ValidatorUtils;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 编辑添加收货地址
 */
public class AddressEditActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.tvUserName)
    EditText tvUserName;
    @Bind(R.id.layUserName)
    RelativeLayout layUserName;
    @Bind(R.id.tvUserPhone)
    EditText tvUserPhone;
    @Bind(R.id.layUserPhone)
    RelativeLayout layUserPhone;
    @Bind(R.id.tvArea)
    TextView tvArea;
    @Bind(R.id.layArea)
    RelativeLayout layArea;
    @Bind(R.id.edAddressDetail)
    EditText edAddressDetail;
    @Bind(R.id.layAddressDetail)
    RelativeLayout layAddressDetail;
    @Bind(R.id.groupTags)
    TagFlowLayout groupTags;
    @Bind(R.id.imgAddTag)
    ImageView imgAddTag;
    @Bind(R.id.edTagNew)
    EditText edTagNew;
    @Bind(R.id.tvTagNew)
    TextView tvTagNew;
    @Bind(R.id.layTagAdd)
    LinearLayout layTagAdd;
    @Bind(R.id.layTag)
    RelativeLayout layTag;
    @Bind(R.id.cbDefault)
    CheckBox cbDefault;

    public static void startActivity(Activity activity, String id, int requestCode) {
        activity.startActivityForResult(new Intent(activity, AddressEditActivity.class)
                .putExtra("id", id), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_address_add, null);
    }

    private String userName;
    private String addrProvinceId;
    private String addrCityId;
    private String addrCountyId;
    private String addrProvince;
    private String addrCity;
    private String addrCounty;
    private String addressFull;
    private String tagName;
    private String phoneNum;

    private String id;

    private String[] tags;
    private TagAdapter<String> adapter;

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");

        tvTitle.setText(TextUtils.isEmpty(id) ? "新增收货地址" : "编辑收货地址");

        tags = new String[]{"家", "公司", "学校"};
        adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) View.inflate(activity, R.layout.item_address_tag, null);
                tv.setText(s);
                return tv;
            }
        };
        groupTags.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (position == 3 && ((TagView) view).isChecked()) {
                    layTagAdd.setVisibility(View.VISIBLE);
                } else {
                    if (groupTags.getChildCount() == 3)
                        imgAddTag.setVisibility(View.VISIBLE);
                    layTagAdd.setVisibility(View.GONE);
                }
                return false;
            }
        });
        groupTags.setAdapter(adapter);
        getNetData();
    }

    private void getNetData() {
        if (!TextUtils.isEmpty(id)) {
            mDao.addressDetail(1, id, this);
        }
    }

    @Override
    protected void setActionBarDetail() {
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("保存");
        btnRightText.setTextColor(getResources().getColor(R.color.gradient_end));
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = tvUserName.getText().toString();
                phoneNum = tvUserPhone.getText().toString();
                addressFull = edAddressDetail.getText().toString();

                Object[] selects = groupTags.getSelectedList().toArray();
                if (selects != null && selects.length != 0) {
                    tagName = tags[(Integer) selects[0]];
                }

                if (TextUtils.isEmpty(userName)) {
                    ToastUtils.showShort(activity, "请输入收货人");
                    return;
                }
                if (!ValidatorUtils.isSpecial(userName) || userName.length() < 2) {
                    ToastUtils.showShort(activity, "请输入正确的收货人");
                    return;
                }
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtils.showShort(activity, "请输入手机号码");
                    return;
                }
                if (!ValidatorUtils.isMobile(phoneNum)) {
                    ToastUtils.showShort(activity, "请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(addrProvinceId) || TextUtils.isEmpty(addrCityId) || TextUtils.isEmpty(addrCountyId)) {
                    ToastUtils.showShort(activity, "请选择省市区");
                    return;
                }
                if (TextUtils.isEmpty(addressFull)) {
                    ToastUtils.showShort(activity, "请输入详细地址");
                    return;
                }
                if (!ValidatorUtils.isSpecial(addressFull)) {
                    ToastUtils.showShort(activity, "请输入40个字内的详细地址");
                    return;
                }
                if (TextUtils.isEmpty(id)) {
                    mDao.addAddress(2, userName, addrProvince, addrCity, addrCounty,
                            addrProvinceId, addrCityId, addrCountyId, addressFull,
                            tagName, cbDefault.isChecked() ? "1" : "0", phoneNum, AddressEditActivity.this);
                } else {
                    mDao.editAddress(2, id, userName, addrProvince, addrCity, addrCounty,
                            addrProvinceId, addrCityId, addrCountyId, addressFull,
                            tagName, cbDefault.isChecked() ? "1" : "0", phoneNum, AddressEditActivity.this);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    // 现在城市返回

                    addrProvince = PubSelectAddressActivity.getTexts(data)[0];
                    addrCity = PubSelectAddressActivity.getTexts(data)[1];
                    addrCounty = PubSelectAddressActivity.getTexts(data)[2];

                    addrProvinceId = PubSelectAddressActivity.getIds(data)[0];
                    addrCityId = PubSelectAddressActivity.getIds(data)[1];
                    addrCountyId = PubSelectAddressActivity.getIds(data)[2];

                    tvArea.setText(PubSelectAddressActivity.getText(data));
                    break;
            }
        }
    }

    @OnClick({R.id.layArea, R.id.imgAddTag, R.id.tvTagNew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layArea:
                PubSelectAddressActivity.startActivity(activity, addrProvince, addrCity, addrCounty, addrProvinceId, addrCityId, addrCountyId, 1);
                break;
            case R.id.imgAddTag:
                imgAddTag.setVisibility(View.GONE);
                layTagAdd.setVisibility(View.VISIBLE);
                break;
            case R.id.tvTagNew:
                tagName = edTagNew.getText().toString();
                if (TextUtils.isEmpty(tagName)) {
                    ToastUtils.showShort(activity, "请输入标签名称");
                    return;
                }
                imgAddTag.setVisibility(View.GONE);
                layTagAdd.setVisibility(View.GONE);
                tags = new String[]{"家", "公司", "学校", tagName};
                adapter = new TagAdapter<String>(tags) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) View.inflate(activity, R.layout.item_address_tag, null);
                        tv.setText(s);
                        return tv;
                    }
                };
                adapter.setSelectedList(new int[]{3});
                groupTags.setAdapter(adapter);

                break;
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                // 收货地址详情
                AddressBean addressBean = new Gson().fromJson(response, AddressBean.class);

                addrProvince = addressBean.getAddrProvince();
                addrCity = addressBean.getAddrCity();
                addrCounty = addressBean.getAddrCounty();

                addrProvinceId = addressBean.getAddrProvinceId();
                addrCityId = addressBean.getAddrCityId();
                addrCountyId = addressBean.getAddrCountyId();

                tvUserName.setText(addressBean.getUserName());
                tvUserPhone.setText(addressBean.getPhoneNum());
                edAddressDetail.setText(addressBean.getAddressFull());
                tvArea.setText(addressBean.getAddrProvince() + addressBean.getAddrCity() + addressBean.getAddrCounty());
                cbDefault.setChecked("1".equals(addressBean.getIsDefault()));
                if (!TextUtils.isEmpty(addressBean.getTagName())) {
                    if ("家".equals(addressBean.getTagName()) || "公司".equals(addressBean.getTagName()) || "学校".equals(addressBean.getTagName())) {
                        imgAddTag.setVisibility(View.VISIBLE);
                        tags = new String[]{"家", "公司", "学校"};
                        int index = 0;
                        for (int i = 0; i < tags.length; i++) {
                            if (tags[i].equals(addressBean.getTagName())) {
                                index = i;
                                break;
                            }
                        }
                        adapter = new TagAdapter<String>(tags) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) View.inflate(activity, R.layout.item_address_tag, null);
                                tv.setText(s);
                                return tv;
                            }
                        };
                        adapter.setSelectedList(new int[]{index});
                        groupTags.setAdapter(adapter);
                    } else {
                        imgAddTag.setVisibility(View.GONE);
                        tags = new String[]{"家", "公司", "学校", addressBean.getTagName()};
                        edTagNew.setText(addressBean.getTagName());
                        adapter = new TagAdapter<String>(tags) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) View.inflate(activity, R.layout.item_address_tag, null);
                                tv.setText(s);

                                return tv;
                            }
                        };
                        adapter.setSelectedList(new int[]{3});
                        groupTags.setAdapter(adapter);
                    }
                }
                break;
            case 2:
                // 保存或者新增收货地址
                ToastUtils.showShort(activity, "成功");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        ToastUtils.showShort(activity, code.msg);
    }
}
