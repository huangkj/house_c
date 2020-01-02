package com.easylife.house.customer.base;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.easylife.house.customer.R;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/30/030.
 */

public abstract class BaseFragment extends Fragment {

    public View root;
    public AlertDialog dialog;
    public LayoutInflater inflater;
    protected int page = Constants.PAGE_START;
    protected ServerDao mDao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View root = inflater.inflate(getLayout(), container, false);
        this.root = root;
        ButterKnife.bind(this, root);
        mDao = new ServerDao(getActivity());
        initViews();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void showLoading() {
        showProgress();
    }

    public void cancelLoading() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void showProgress() {
        dialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.progress_dialog);
        window.setDimAmount(0);
        SpinKitView progress = (SpinKitView) window.findViewById(R.id.progressBar);
        Circle circle = new Circle();
        progress.setIndeterminateDrawable(circle);
        dialog.setCanceledOnTouchOutside(false);
        progress.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    public abstract int getLayout();

    public abstract void initViews();

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面("MainScreen"为页面名称，可自定义)
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}
