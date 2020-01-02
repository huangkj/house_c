package com.easylife.house.customer.mvp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.easylife.house.customer.R;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;


public abstract class MVPBaseFragment<V extends BaseView, T extends BasePresenterImpl<V>> extends Fragment implements BaseView {
    public T mPresenter;
    public View root;
    public AlertDialog dialog;
    public LayoutInflater inflater;
    protected int page = Constants.PAGE_START;
    protected ServerDao mDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View root = inflater.inflate(getLayout(), container, false);
        this.root = root;
        try {
            ButterKnife.bind(this, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);
        mDao = new ServerDao(getActivity());
        initViews();
        return root;
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (root != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            root.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (root != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            root.startAnimation(fadeOut);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        showProgress();
    }

    @Override
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

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面("MainScreen"为页面名称，可自定义)
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}
