package com.easylife.house.customer.ui.fitment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.FitmentCaseListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanFitmentCase;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 装修服务首页
 */
public class FitmentActivity extends BaseActivity implements RequestManagerImpl {


    @Bind(R.id.imageBack)
    ImageView imageBack;
    @Bind(R.id.layFitmentCase)
    TextView layFitmentCase;
    @Bind(R.id.layFitmentKnowledge)
    TextView layFitmentKnowledge;
    @Bind(R.id.layFitmentFreeOffer)
    TextView layFitmentFreeOffer;
    @Bind(R.id.layFitmentAppointment)
    TextView layFitmentAppointment;
    @Bind(R.id.tvCaseTitle)
    TextView tvCaseTitle;
    @Bind(R.id.tvCaseText)
    TextView tvCaseText;
    @Bind(R.id.imgCaseImage)
    ImageView imgCaseImage;
    @Bind(R.id.imgCaseMore)
    ImageView imgCaseMore;
    @Bind(R.id.layFitmentCaseItem)
    LinearLayout layFitmentCaseItem;
    @Bind(R.id.tvRecommendCase)
    TextView tvRecommendCase;
    @Bind(R.id.recyclerCase)
    RecyclerView recyclerCase;
    @Bind(R.id.tvRecommendRead)
    TextView tvRecommendRead;
    @Bind(R.id.tvReadTitle)
    TextView tvReadTitle;
    @Bind(R.id.imgReadImage)
    ImageView imgReadImage;
    @Bind(R.id.tvReadContent)
    TextView tvReadContent;
    @Bind(R.id.layRecommendRead)
    LinearLayout layRecommendRead;
    @Bind(R.id.btnFitmentAppointment)
    Button btnFitmentAppointment;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, FitmentActivity.class));
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.fiment_activity_index, null);
    }

    private FitmentCaseListAdapter adapter;

    @Override
    protected void initView() {
        SearchSingleton singletonSearch = SearchSingleton.getIstance();
        if (mDao.localDao != null) {
            singletonSearch.city = mDao.localDao.getCity();
            singletonSearch.cityId = mDao.localDao.getCityId();
            singletonSearch.buyWhere = mDao.localDao.getCity();
            singletonSearch.whereHouse = "";
            if (TextUtils.isEmpty(singletonSearch.city)) {
                singletonSearch.cityId = "110100";
                singletonSearch.city = "北京市";
            }
        } else {
            singletonSearch.cityId = "110100";
            singletonSearch.city = "北京市";
            singletonSearch.buyWhere = "北京市";
            singletonSearch.whereHouse = "";
        }

        adapter = new FitmentCaseListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCase.setLayoutManager(linearLayoutManager);
        recyclerCase.setAdapter(adapter);
        adapter.addData(new BeanFitmentCase());
        adapter.addData(new BeanFitmentCase());
        adapter.addData(new BeanFitmentCase());
    }

    @Override
    protected void setActionBarDetail() {
        layActionBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }


    @OnClick({R.id.imageBack, R.id.layFitmentCase, R.id.layFitmentKnowledge, R.id.layFitmentFreeOffer,
            R.id.layFitmentAppointment, R.id.layFitmentCaseItem, R.id.tvRecommendCase, R.id.tvRecommendRead,
            R.id.layFitmentReadItem, R.id.layRecommendRead, R.id.btnFitmentAppointment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageBack:
                finish();
                break;
            case R.id.layFitmentCase:
                DesignCaseListActivity.startActivity(activity);
                break;
            case R.id.layFitmentKnowledge:
                KnowledgeListActivity.startActivity(activity);
                break;
            case R.id.layFitmentFreeOffer:
                break;
            case R.id.layFitmentAppointment:
                break;
            case R.id.layFitmentCaseItem:
                MyFitmentPlanActivity.startActivity(activity);
                break;
            case R.id.tvRecommendCase:
                break;
            case R.id.tvRecommendRead:
                break;
            case R.id.layFitmentReadItem:
                break;
            case R.id.layRecommendRead:
                break;
            case R.id.btnFitmentAppointment:
                break;
        }
    }
}
