package com.easylife.house.customer.ui.homefragment.homeindex;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HomeAdapter;
import com.easylife.house.customer.adapter.HomeIndexItemTopAdapter;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.HomeBean;
import com.easylife.house.customer.bean.ItemBean;
import com.easylife.house.customer.bean.MultipleItemBean;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.homesearch.allhouse.AllHouseActivity;
import com.easylife.house.customer.ui.homesearch.brandland.BrandLandActivity;
import com.easylife.house.customer.ui.homesearch.buget.BugetActivity;
import com.easylife.house.customer.ui.homesearch.homearea.HomeAreaActivity;
import com.easylife.house.customer.ui.homesearch.shoprent.ShopRentActivity;
import com.easylife.house.customer.ui.homesearch.wherebuy.WhereBuyActivity;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.ui.qrhousesignup.HouseSignUpActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.ScrollSpeedLinearLayoutManger;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION;
import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN;
import static com.easylife.house.customer.event.MessageEvent.UPDATE_SEARCH_DATA;

/**
 * 首页
 */
@RuntimePermissions
public class HomeIndexFragment extends MVPBaseFragment<HomeIndexContract.View, HomeIndexPresenter>
        implements HomeIndexContract.View, View.OnClickListener {

    @Bind(R.id.imageview)
    ImageView mImageview;
    @Bind(R.id.iv_arrow)
    ImageView mIvArrow;
    @Bind(R.id.ll_title)
    RelativeLayout mLlTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ll_content)
    LinearLayout mLlContent;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarlayout;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.home_recycle)
    RecyclerView homeRecycle;
    CollapsingToolbarLayoutState mLayoutState = CollapsingToolbarLayoutState.EXPANDED;
    @Bind(R.id.coord)
    CoordinatorLayout coord;
    @Bind(R.id.tvContent1)
    TextView tvContent1;
    @Bind(R.id.tvContent2)
    TextView tvContent2;
    @Bind(R.id.tvContent3)
    TextView tvContent3;
    @Bind(R.id.tv_clear)
    TextView tvClear;
    @Bind(R.id.tv_single_search)
    TextView tvSingleSearch;
    @Bind(R.id.rl_clear)
    RelativeLayout rlClear;
    @Bind(R.id.rl_content_empty)
    RelativeLayout rlContentEmpty;
    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private AnimationSet mHideSet;
    private AnimationSet mShowSet;
    private List<MultipleItemBean> beanList = new ArrayList<>();
    private HomeAdapter mAdapter;
    private boolean isExpand = true;
    private float pageMargin260;
    private float pageMargin114;
    private float pageMargin96;
    private float pageMargin56;
    private float pageMargin20;
    private float tvOffset1;
    private float tvOffset2;
    private float tvOffset3;

    private static final int REQUEST_CODE = 10;
    private SearchSingleton singletonSearch;

    @Override
    public void showSuccess(List<HomeBean> homeList) {
        cancelLoading();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (mAdapter != null) {
            if (homeList != null) {
                Collections.sort(homeList, new Comparator<HomeBean>() {
                    @Override
                    public int compare(HomeBean o1, HomeBean o2) {
                        return o1.position.compareTo(o2.position);
                    }
                });
                beanList.clear();
                MultipleItemBean bean = new MultipleItemBean(10);
                bean.beanList = homeList;
                beanList.add(bean);
                mAdapter.setType(false);
                mAdapter.setNewData(beanList);
            } else {
                beanList.clear();
                MultipleItemBean bean = new MultipleItemBean(-1);
                beanList.add(bean);
                mAdapter.setType(true);
                mAdapter.setNewData(beanList);
            }
        }
    }

    @Override
    public void showFail(String msg) {
//        CustomerUtils.showTip(getActivity(), msg);
        cancelLoading();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void showCollectList(List<CollectionListBean> collectionList) {

        for (CollectionListBean bean : collectionList) {
            singletonSearch.collectList.add(bean.devId);
        }
        mAdapter.setCollectionList(singletonSearch.collectList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 收藏楼盘
     */
    @Override
    public void showCollect() {
        CustomerUtils.showTip(getActivity(), "收藏成功");
    }

    @Override
    public void showDelCollectSucc() {
        CustomerUtils.showTip(getActivity(), "取消收藏");
    }

    @Override
    public void jumpToCommitUserInfo(String params) {
        startActivity(new Intent(getActivity(), HouseSignUpActivity.class).putExtra("QR_STR", params));
    }


    @OnClick({R.id.iv_arrow, R.id.ll_title, R.id.tvContent1, R.id.tvContent2, R.id.tvContent3, R.id.tv_clear, R.id.iv_qr, R.id.tv_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                SearchSingleton singleton = SearchSingleton.getIstance();
                singleton.buyWhere = "";
                singleton.budget = "";
                singleton.openTime = "";
                singleton.whereHouse = "";
                singleton.minBugetValue = 0;
                singleton.maxBugetValue = 0;
                singleton.sort = "0";
                singleton.minBugetValueArea = 0;
                singleton.maxBugetValueArea = 2000;
                if (singleton.chooseSet != null) {
                    singleton.chooseSet.clear();
                    singleton.chooseSet = null;
                }

                if (singleton.chooseSetArea != null) {
                    singleton.chooseSetArea.clear();
                    singleton.chooseSetArea = null;
                }

                if (mDao.localDao != null) {
                    singleton.city = mDao.localDao.getCity();
                    singleton.cityId = mDao.localDao.getCityId();
                    singleton.buyWhere = mDao.localDao.getCity();
                    singleton.whereHouse = "";
                } else {
                    singleton.cityId = "110100";
                    singleton.city = "北京市";
                    singleton.buyWhere = "北京市";
                    singleton.whereHouse = "";
                }

                tvContent1.setText(singleton.city);
                tvContent2.setText("预算多少");
                tvContent3.setText("家的大小");
                tvSingleSearch.setText(singleton.city + "·预算多少·家的大小");
                break;
            case R.id.iv_arrow:
                mAppBarLayout.setExpanded(false);
                break;
            case R.id.ll_title:
                mAppBarLayout.setExpanded(true);
                break;
            case R.id.tv_qr:
            case R.id.iv_qr:
                if (mDao.isLogin()) {
                    HomeIndexFragmentPermissionsDispatcher.jumpQRWithCheck(this);
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 0);
                }
                break;
            case R.id.tvContent1:
                singletonSearch.searchtype = "0";
                singletonSearch.isIndexHome = true;
                startActivity(new Intent(getActivity(), WhereBuyActivity.class));
                break;
            case R.id.tvContent2:
                singletonSearch.searchtype = "0";
                singletonSearch.isIndexHome = true;
                startActivity(new Intent(getActivity(), BugetActivity.class));
                break;
            case R.id.tvContent3:
                singletonSearch.searchtype = "0";
                singletonSearch.isIndexHome = true;
//                startActivity(new Intent(getActivity(), OpenTimeActivity.class));
                startActivity(new Intent(getActivity(), HomeAreaActivity.class));
                break;
//            case R.id.ll_1:
//                singletonSearch.isIndexHome = false;
//                SearchRequestBean requestBean = new SearchRequestBean();
//                singletonSearch.searchtype = "2";
//
//                startActivity(new Intent(getActivity(), SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, requestBean));
//                break;
//            case R.id.ll_2:
//                singletonSearch.isIndexHome = false;
//                SearchRequestBean requestBean1 = new SearchRequestBean();
//                singletonSearch.searchtype = "1";
//                startActivity(new Intent(getActivity(), SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, requestBean1));
//                break;
//            case R.id.ll_3:
//                startActivity(new Intent(getActivity(), MapFindHouseActivity.class));
//                break;
//            case R.id.ll_4:
//                startActivity(new Intent(getActivity(), CalculatorActivity.class));
//                break;
        }
    }

    @NeedsPermission((Manifest.permission.CAMERA))
    public void jumpQR() {
        //跳转二维码扫描
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
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
        HomeIndexFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == UPDATE_SEARCH_DATA) {
            SearchSingleton searchSingleton = (SearchSingleton) event.obj;
            setSearchData(searchSingleton);

        } else if (event.MsgType == HOUSES_INDEXT_COLLECTION_LOGIN) {
            if (mDao.isLogin())
                mPresenter.collectHouseList(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
        } else if (event.MsgType == HOUSES_INDEXT_COLLECTION) {
            //楼盘详情 收藏楼盘 首页在此刷新
            mAdapter.setCollectionList(singletonSearch.collectList);
            mAdapter.notifyDataSetChanged();
        } else {
            tvContent3.setText("");
            tvSingleSearch.setText("");
        }
    }

    public void setSearchData(SearchSingleton searchSingleton) {
        if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
            if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                tvContent1.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse);
            } else {
                tvContent1.setText(searchSingleton.buyWhere);
            }
            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.budget)) {

                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                    tvContent2.setText(searchSingleton.budget);
                    tvContent3.setText(searchSingleton.openTime);
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·家的大小");
                    }

                    tvContent2.setText(searchSingleton.budget);
                }
            } else {
                tvContent2.setText("预算多少");
                if (!TextUtils.isEmpty(searchSingleton.openTime)) {

                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少" + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·预算多少" + "·" + searchSingleton.openTime);
                    }

                    tvContent3.setText(searchSingleton.openTime);
                } else {
                    tvContent3.setText("家的大小");
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少·家的大小");
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·预算多少·家的大小");
                    }
                }
            }
        } else if (!TextUtils.isEmpty(searchSingleton.budget)) {
            tvContent2.setText(searchSingleton.budget);

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {

                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }

                    tvContent1.setText(searchSingleton.buyWhere);
                    tvContent3.setText(searchSingleton.openTime);
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·家的大小");
                    }
                    tvContent1.setText(searchSingleton.buyWhere);
                }

            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.city + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }

                    tvContent3.setText(searchSingleton.openTime);
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleSearch.setText(searchSingleton.city + "·" + searchSingleton.budget + "·家的大小");
                    }
                }
            }
        } else if (!TextUtils.isEmpty(searchSingleton.openTime)) {
            tvContent3.setText(searchSingleton.openTime);

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                if (!TextUtils.isEmpty(searchSingleton.budget)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }

                    tvContent1.setText(searchSingleton.buyWhere);
                    tvContent2.setText(searchSingleton.budget);
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·预算多少·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "·预算多少·" + searchSingleton.openTime);
                    }
                    tvContent1.setText(searchSingleton.buyWhere);
                }

            } else {
                if (!TextUtils.isEmpty(searchSingleton.budget)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.city + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                    tvContent2.setText(searchSingleton.budget);
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·预算多少·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.city + "·预算多少·" + searchSingleton.openTime);
                    }
                }
            }
        } else if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
            if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                tvContent1.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse);
            } else {
                tvContent1.setText(searchSingleton.whereHouse);
            }

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.budget)) {

                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                    tvContent2.setText(searchSingleton.budget);
                    tvContent3.setText(searchSingleton.openTime);
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    }

                    tvContent2.setText(searchSingleton.budget);
                }
            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTime)) {

                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少" + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·预算多少" + "·" + searchSingleton.openTime);
                    }

                    tvContent3.setText(searchSingleton.openTime);
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleSearch.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少·家的大小");
                    } else {
                        tvSingleSearch.setText(searchSingleton.whereHouse + "·预算多少·家的大小");
                    }
                }
            }
        }
    }

    /**
     * 定义出CollapsingToolbarLayout展开、折叠、中间三种状态
     */
    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDTATE
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
        Log.e("showTip", msg);
    }

    @Override
    public int getLayout() {
        return R.layout.home_fragment_home;
    }

    @Override
    public void initViews() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.gradient_end));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestIndexMsg();
                if (mDao.isLogin()) {
                    mPresenter.collectHouseList(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
                }
            }
        });
        singletonSearch = SearchSingleton.getIstance();

        if (mDao.localDao != null) {
            singletonSearch.city = mDao.localDao.getCity();
            singletonSearch.cityId = mDao.localDao.getCityId();
            singletonSearch.buyWhere = mDao.localDao.getCity();
            singletonSearch.whereHouse = "";
            tvContent1.setText(singletonSearch.city);
        } else {
            singletonSearch.cityId = "110100";
            singletonSearch.city = "北京市";
            singletonSearch.buyWhere = "北京市";
            singletonSearch.whereHouse = "";
            tvContent1.setText("北京市");
        }

        setSearchData(singletonSearch);
//        showLoading();
        mPresenter.getCacheData();
        mPresenter.requestIndexMsg();

        initData();
    }

    public static HomeIndexFragment newInstance() {
        Bundle args = new Bundle();
        HomeIndexFragment fragment = new HomeIndexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<ItemBean> listItem = new ArrayList<>();

    private void initData() {

        //初始化首页顶部栏的数据
        for (int i = 0; i < 5; i++) {
            ItemBean itemBean = new ItemBean();
            switch (i) {
                case 0:
                    itemBean.imgUrl = R.mipmap.index_brand;
                    itemBean.name = "品牌地产";
                    break;
                case 1:
                    itemBean.imgUrl = R.mipmap.index_recent;
                    itemBean.name = "近期开盘";
                    break;
                case 2:
                    itemBean.imgUrl = R.mipmap.online_house;
                    itemBean.name = "在线订房";
                    break;
                case 3:
                    itemBean.imgUrl = R.mipmap.index_find;
                    itemBean.name = "地图找房";
                    break;
                case 4:
                    itemBean.imgUrl = R.mipmap.index_tool;
                    itemBean.name = "房贷计算器";
                    break;
            }
            listItem.add(itemBean);
        }


        mLlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppBarLayout.setExpanded(true);
            }
        });
        pageMargin260 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources()
                .getDisplayMetrics());
        pageMargin114 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 114, getResources()
                .getDisplayMetrics());
        pageMargin96 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, getResources()
                .getDisplayMetrics());
        pageMargin56 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources()
                .getDisplayMetrics());
        pageMargin20 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                .getDisplayMetrics());
        mAdapter = new HomeAdapter(beanList);
        mAdapter.setNewData(beanList);
        homeRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        //监听recycleView滑动
        homeRecycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mAppBarLayout.setExpanded(false);
                }
            }
        });

        //顶部栏
        View headView = View.inflate(getActivity(), R.layout.index_item_top, null);
        final RecyclerView recyclerViewTop = (RecyclerView) headView.findViewById(R.id.recycle);
        ScrollSpeedLinearLayoutManger layoutManager = new ScrollSpeedLinearLayoutManger(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSpeedSlow();
        recyclerViewTop.setLayoutManager(layoutManager);
        final HomeIndexItemTopAdapter adapter = new HomeIndexItemTopAdapter(R.layout.homeindex_item_top, listItem);
        recyclerViewTop.setAdapter(adapter);
        recyclerViewTop.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                switch (position) {
                    case 0:
                        singletonSearch.isIndexHome = false;
                        SearchRequestBean requestBean = new SearchRequestBean();
                        requestBean.city = singletonSearch.city;
                        requestBean.cityId = singletonSearch.cityId;
                        singletonSearch.searchtype = "2";
//                        startActivity(new Intent(getActivity(), SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, requestBean));

                        startActivity(new Intent(getActivity(), BrandLandActivity.class));
                        break;
                    case 1://近期开盘
                        singletonSearch.isIndexHome = false;
                        SearchRequestBean requestBean1 = new SearchRequestBean();
                        singletonSearch.searchtype = "1";
//                        startActivity(new Intent(getActivity(), SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, requestBean1));
                        AllHouseActivity.startActivity(getActivity(), 1, "", singletonSearch.cityId, singletonSearch.city, "", 0);

                        break;
                    case 2://在线订房
                        singletonSearch.isIndexHome = false;
                        SearchRequestBean requestBean2 = new SearchRequestBean();
                        requestBean2.city = singletonSearch.city;
                        requestBean2.cityId = singletonSearch.cityId;
                        singletonSearch.searchtype = "0";
//                        startActivity(new Intent(getActivity(), SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, requestBean2).putExtra("isTransparent","1"));
//                        startActivity(new Intent(getActivity(), SearchHouseActivity.class));
                        AllHouseActivity.startActivity(getActivity(), 0, "", singletonSearch.cityId, singletonSearch.city, "1", 0);
                        break;
                    case 3:
                        ShopRentActivity.startActivity(getActivity(), singletonSearch.cityId, 0);
                        break;
                    case 4:
//                        startActivity(new Intent(getActivity(), CalculatorActivity.class));
//                        startActivity(new Intent(getActivity(), AllHouseActivity.class));
                        AllHouseActivity.startActivity(getActivity(), 0, "", singletonSearch.cityId, singletonSearch.city, "", 0);
                        break;
                }
            }
        });

        //判断是不是首次进入 计算器左右滑动
//        if(TextUtils.isEmpty(mDao.localDao.getIsFirst()) || !"Saling_is_first".equals(mDao.localDao.getIsFirst())){
        isFirstInto(recyclerViewTop);
//            mDao.localDao.saveIsFirst("Saling_is_first");
//        }
        mAdapter.addHeaderView(headView);
        homeRecycle.setAdapter(mAdapter);

        mAdapter.setImgeViewOnclickLisenear(new HomeAdapter.ImgeViewOnclickLisenear() {
            //收藏点击事件
            @Override
            public void collectOnclick(final CheckBox imageView, final HomeBean homeBean) {
                if (mDao.isLogin()) {
                    if (imageView.isChecked()) {
                        mPresenter.collectHouse(homeBean.devId, homeBean.devName, mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0", "");
                        if (!singletonSearch.collectList.contains(homeBean.devId)) {
                            singletonSearch.collectList.add(homeBean.devId);
                        }
                    } else {
                        mPresenter.delCollectHouse(homeBean.devId, homeBean.devName, mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0", "");
                        if (singletonSearch.collectList.contains(homeBean.devId)) {
                            singletonSearch.collectList.remove(homeBean.devId);
                        }
                    }
                } else {
                    imageView.setChecked(false);
                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 1);
                }

            }

            //item点击事件
            @Override
            public void itemOnclick(HomeBean homeBean) {
                HouseDetailActivity.startActivity(getActivity(), homeBean.devId, homeBean.coOp, 0);

//                if (3 == homeBean.coOp) {
//                    startActivity(new Intent(getActivity(), HousesAndTypeSimpleActivity.class).putExtra("DEV_ID", homeBean.devId));
//                } else {
//                    startActivity(new Intent(getActivity(), HousesAndTypeActivity.class).putExtra("DEV_ID", homeBean.devId));
//                }
            }
        });

        collapsingListener();

    }

    /**
     * 是不是第一次进入
     *
     * @param recyclerViewTop
     */
    private void isFirstInto(final RecyclerView recyclerViewTop) {
        final Timer timer = new Timer();
        final TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewTop.smoothScrollToPosition(0);
                    }
                });

            }
        };

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewTop.smoothScrollToPosition(4);
                        timer.schedule(task2, 1000);
                    }
                });

            }
        };


        timer.schedule(task, 1000);
    }

    /**
     * 控制CollapsingToolbarLayout状态
     */
    private void collapsingListener() {

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


                tvContent1.setAlpha(1 - Math.abs(verticalOffset) / pageMargin114);
                mLlTitle.setAlpha(Math.abs(verticalOffset) / pageMargin114);
                mIvArrow.setAlpha(1 - Math.abs(verticalOffset) / pageMargin114);
                tvContent2.setAlpha(1 - Math.abs(verticalOffset) / pageMargin96);
                tvContent3.setAlpha(1 - Math.abs(verticalOffset) / pageMargin56);
                rlClear.setAlpha(1 - Math.abs(verticalOffset) / pageMargin20);

                if (tvContent1.getAlpha() < 0.1) {
                    tvContent1.setVisibility(View.GONE);
                    tvContent2.setVisibility(View.GONE);
                    tvContent3.setVisibility(View.GONE);
                } else {
                    tvContent1.setVisibility(View.VISIBLE);
                    tvContent2.setVisibility(View.VISIBLE);
                    tvContent3.setVisibility(View.VISIBLE);
                }

                mIvArrow.setTranslationY(-verticalOffset);
                tvClear.setTranslationY(-verticalOffset);


                if (verticalOffset != 0) {
                    tvOffset2 = Math.abs(verticalOffset) / pageMargin114 * 15;
                    tvOffset3 = Math.abs(verticalOffset) / pageMargin114 * 35;
                    tvOffset1 = Math.abs(verticalOffset) / pageMargin114 * 25;
                } else {
                    tvOffset2 = 0;
                    tvOffset3 = 0;
                }
                tvContent1.setTranslationY(-verticalOffset);
                tvContent2.setTranslationY(-verticalOffset - tvOffset2);
                tvContent3.setTranslationY(-verticalOffset - tvOffset3);
                rlClear.setTranslationY(verticalOffset + tvOffset3);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!TextUtils.isEmpty(result)) {

                        if (!NetworkUtils.isConnected(getActivity())) {
                            CustomerUtils.showTip(getActivity(), "当前网络不可用");
                            return;
                        }

                        if (result.contains(Constants.QR_URL)) {
                            mPresenter.resolveQrParams(result);
                        } else {
                            CustomerUtils.showTip(getActivity(), "二维码无效");
                        }
                    } else {
                        CustomerUtils.showTip(getActivity(), "二维码无效");
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
