package com.easylife.house.customer.ui.homesearch.search;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.SearchHouseAdapter;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.homesearch.buget.BugetActivity;
import com.easylife.house.customer.ui.homesearch.homearea.HomeAreaActivity;
import com.easylife.house.customer.ui.homesearch.housefilter.HouseFilterActivity;
import com.easylife.house.customer.ui.homesearch.wherebuy.WhereBuyActivity;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.ui.houses.map.mapfindhouse.MapFindHouseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.UiUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_SEARCH_DATA;

/**
 * 搜索页面结果
 */
public class SearchActivity extends MVPBaseActivity<SearchContract.View, SearchPresenter> implements SearchContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.ll_title)
    RelativeLayout mLlTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_arrow)
    ImageView mIvArrow;
    @Bind(R.id.tv_clear)
    TextView tvClear;
    @Bind(R.id.tvContent1)
    TextView tvContent1;
    @Bind(R.id.tvContent2)
    TextView tvContent2;
    @Bind(R.id.tvContent3)
    TextView tvContent3;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.layBtns)
    LinearLayout layBtns;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.home_recycle)
    RecyclerView homeRecycle;
    @Bind(R.id.coord)
    CoordinatorLayout coord;
    @Bind(R.id.iv_back_search)
    ImageView ivBackSearch;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;
    @Bind(R.id.tv_single_title)
    TextView tvSingleTitle;
    @Bind(R.id.rl_clear)
    RelativeLayout rlClear;
    @Bind(R.id.tv_filter_map)
    TextView tvFilterMap;
    @Bind(R.id.iv_filter_map)
    ImageView ivFilterMap;
    @Bind(R.id.tv_filter)
    TextView tvFilter;
    @Bind(R.id.iv_filter)
    ImageView ivFilter;
    @Bind(R.id.rl_search_empty)
    RelativeLayout rlSearchEmpty;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private float pageMargin114;
    private float pageMargin96;
    private float pageMargin56;
    private float tvOffset2;
    private float tvOffset3;
    private String isTransparent = null;
    private SearchHouseAdapter mAdapter;
    private List<HousesDetailBaseBean> baseBeanList;
    private List<HousesDetailBaseBean> allBaseBeanList = new ArrayList<>();
    private SearchSingleton searchSingleton;
    private SearchRequestBean searchBean;
    private SearchRequestBean totalRequstBean;
    public String type;
    private boolean isClear = false;

    @Override
    protected View setContentLayoutView() {
        UiUtil.setTranslucent(this);
        return View.inflate(this, R.layout.activity_search, null);
    }

    @Override
    protected void initView() {
        isTransparent = getIntent().getStringExtra("isTransparent");
        searchSingleton = SearchSingleton.getIstance();
//        int type = getIntent().getIntExtra("TYPE", -1);
        searchBean = (SearchRequestBean) getIntent().getSerializableExtra(SearchRequestBean.SEARCH_BEAN);
        type = searchSingleton.searchtype;
        if (!TextUtils.isEmpty(isTransparent)) {
            type = "0";
        }
        setRequestBeanParams(searchBean);
        if (searchSingleton.isIndexHome) {
            SearchValue();
        } else {
            NoSearchValue();
        }

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.gradient_end));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mPresenter.requestSearchData(totalRequstBean, type, isTransparent, page + "");
            }
        });
        showLoading();
        mPresenter.requestSearchData(totalRequstBean, type, isTransparent, page + "");

        mAdapter = new SearchHouseAdapter(R.layout.search_item, baseBeanList);
        homeRecycle.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, homeRecycle);
        homeRecycle.setAdapter(mAdapter);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, rlSearchEmpty, false);
        mAdapter.setEmptyView(emptyView);

        pageMargin114 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 114, getResources()
                .getDisplayMetrics());
        pageMargin96 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, getResources()
                .getDisplayMetrics());
        pageMargin56 = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources()
                .getDisplayMetrics());

        homeRecycle.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (allBaseBeanList != null && allBaseBeanList.size() != 0) {
                    HousesDetailBaseBean baseBean = allBaseBeanList.get(position);
                    searchSingleton.lookHouse.add(SearchActivity.this);
                    HouseDetailActivity.startActivity(SearchActivity.this, baseBean.devId, baseBean.coOp, 0);
//                    if (baseBean.coOp == 3) {
//                        startActivity(new Intent(SearchActivity.this, HousesAndTypeSimpleActivity.class).putExtra("DEV_ID", baseBean.devId));
//                    } else {
//                        startActivity(new Intent(SearchActivity.this, HousesAndTypeActivity.class).putExtra("DEV_ID", baseBean.devId));
//                    }
                }
            }
        });
        homeRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                     /*
                new State
                0（SCROLL_STATE_IDLE）表示recyclerview是不动的（The RecyclerView is not currently scrolling.）
                1（SCROLL_STATE_DRAGGING）表示recyclerview正在被拖拽（The RecyclerView is currently being dragged by outside input such as user touch input.）
                2（SCROLL_STATE_SETTLING）表示recyclerview正在惯性下滚动（The RecyclerView is currently animating to a final position while not under outside control.）
                 */
                switch (newState) {
                    case 0:
                        layBtns.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layBtns.setVisibility(View.VISIBLE);
                            }
                        }, 200);
                        break;
                    default:
                        layBtns.setVisibility(View.GONE);
                        break;
                }
            }
        });

        collapsingListener();
    }

    /**
     * 请求数据赋值
     */
    public void setRequestBeanParams(SearchRequestBean searchBean) {
        if (searchBean == null) {
            searchBean = new SearchRequestBean();
        }

        if (totalRequstBean == null) {
            totalRequstBean = new SearchRequestBean();
        }
        if (!TextUtils.isEmpty(searchBean.userCode)) {
            totalRequstBean.userCode = searchBean.userCode;
        }


        if (!TextUtils.isEmpty(searchBean.city)) {
            totalRequstBean.city = searchBean.city;
        } else {
            totalRequstBean.city = searchSingleton.city;
//            searchSingleton.city = searchSingleton.city;
        }

        if (!TextUtils.isEmpty(searchBean.cityId)) {
            totalRequstBean.cityId = searchBean.cityId;
        } else {
            totalRequstBean.cityId = searchSingleton.cityId;
//            searchSingleton.cityId = "110100";
        }

        if (!TextUtils.isEmpty(searchBean.devName)) {
            totalRequstBean.devName = searchBean.devName;
        } else {
            totalRequstBean.devName = "";
        }

        if (searchBean.priceMapList != null) {
            totalRequstBean.priceMapList = searchBean.priceMapList;
        }

//        if (!TextUtils.isEmpty(searchBean.beforeTime)) {
//            totalRequstBean.beforeTime = searchBean.beforeTime;
//        } else {
//            totalRequstBean.beforeTime = searchSingleton.beforeTime;
//        }

        if (!TextUtils.isEmpty(searchBean.addressDistrict)) {
            totalRequstBean.addressDistrict = searchBean.addressDistrict;
        } else {
            totalRequstBean.addressDistrict = "";
        }


        if (!TextUtils.isEmpty(searchBean.subway)) {
            totalRequstBean.subway = searchBean.subway;
        } else {
            totalRequstBean.subway = "";
        }


        if (searchBean.propertyType != null) {
            totalRequstBean.propertyType = searchBean.propertyType;
        }
//        else {
//            totalRequstBean.propertyType = "";
//        }


        if (searchBean.bugetMapList != null) {
            totalRequstBean.bugetMapList = searchBean.bugetMapList;
        }
//        else {
//            totalRequstBean.minAvgPrige = "";
//        }

//        if (!TextUtils.isEmpty(searchBean.maxAvgPrige)) {
//            totalRequstBean.maxAvgPrige = searchBean.maxAvgPrige;
//        }else {
//            totalRequstBean.maxAvgPrige = "";
//        }

        if (searchBean.areaMapList != null) {
            if (searchBean.areaMapList.size() != 0) {
                totalRequstBean.areaMapList = searchBean.areaMapList;
            }
        } else {
            if (searchSingleton.isIndexHome) {
                totalRequstBean.areaMapList = searchSingleton.areaMapList;
            }
        }

        if (searchBean.priceMapList != null) {
            if (searchBean.priceMapList.size() != 0) {
                totalRequstBean.priceMapList = searchBean.priceMapList;
            }
        } else {
            if (searchSingleton.isIndexHome) {
                totalRequstBean.priceMapList = searchSingleton.priceMapList;
            }
        }
//        else {
//            totalRequstBean.minHouseSize = "";
//        }

//        if (!TextUtils.isEmpty(searchBean.maxHouseSize)) {
//            totalRequstBean.maxHouseSize = searchBean.maxHouseSize;
//        }else
//            {
//            totalRequstBean.maxHouseSize = "";
//        }

        if (!TextUtils.isEmpty(searchBean.sort)) {
            totalRequstBean.sort = searchBean.sort;
        }
        if (searchBean.devRoomInfo != null) {
            totalRequstBean.devRoomInfo = searchBean.devRoomInfo;
        }
//        else {
//            totalRequstBean.devBedroomInfo = "";
//        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isClear = false;
        allBaseBeanList.clear();
        page = 1;
        SearchRequestBean searchBean = (SearchRequestBean) intent.getSerializableExtra(SearchRequestBean.SEARCH_BEAN);
        setRequestBeanParams(searchBean);
        showLoading();
        mPresenter.requestSearchData(totalRequstBean, type, isTransparent, page + "");

        if (searchSingleton.isIndexHome) {
            SearchValue();
        } else {
            NoSearchValue();
        }
    }

    /**
     * 从首页搜索进来
     */
    private void SearchValue() {

        if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
            if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                tvContent1.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse);
            } else {
                tvContent1.setText(searchSingleton.whereHouse);
            }

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.budget)) {

                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    }
                }
            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少" + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·预算多少" + "·" + searchSingleton.openTime);
                    }
                } else {

                    if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·预算多少·家的大小");
                    }
                }
            }
        }

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
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·家的大小");
                    }
                }
            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少" + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·预算多少" + "·" + searchSingleton.openTime);
                    }
                } else {

                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·预算多少·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·预算多少·家的大小");
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(searchSingleton.budget)) {
            tvContent2.setText(searchSingleton.budget);

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {

                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·家的大小");
                    }
                }

            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTime)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·" + searchSingleton.budget + "·家的大小");
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(searchSingleton.openTime)) {
            tvContent3.setText(searchSingleton.openTime);

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                if (!TextUtils.isEmpty(searchSingleton.budget)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "-" + searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·预算多少·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhere + "·预算多少·" + searchSingleton.openTime);
                    }
                }

            } else {
                if (!TextUtils.isEmpty(searchSingleton.budget)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·" + searchSingleton.budget + "·" + searchSingleton.openTime);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                        tvSingleTitle.setText(searchSingleton.whereHouse + "·预算多少·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·预算多少·" + searchSingleton.openTime);
                    }
                }
            }
        }
    }


    /**
     * 从品牌地产过来
     */
    private void NoSearchValue() {
        if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
            if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {
                tvContent1.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch);
            } else {
                tvContent1.setText(searchSingleton.whereHouseSearch);
            }

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.budgetSearch)) {

                if (!TextUtils.isEmpty(searchSingleton.openTimeSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    }
                } else {

                    if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·家的大小");
                    }
                }
            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTimeSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·预算多少" + "·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·预算多少" + "·" + searchSingleton.openTimeSearch);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·预算多少·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·预算多少·家的大小");
                    }
                }
            }
        }


        if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {
            if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                tvContent1.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch);
            } else {
                tvContent1.setText(searchSingleton.buyWhereSearch);
            }

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.budgetSearch)) {

                if (!TextUtils.isEmpty(searchSingleton.openTimeSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    }
                } else {

                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·" + searchSingleton.budgetSearch + "·家的大小");
                    }
                }
            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTimeSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·预算多少" + "·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·预算多少" + "·" + searchSingleton.openTimeSearch);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·预算多少·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·预算多少·家的大小");
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(searchSingleton.budgetSearch)) {
            tvContent2.setText(searchSingleton.budgetSearch);

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {

                if (!TextUtils.isEmpty(searchSingleton.openTimeSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·" + searchSingleton.budgetSearch + "·家的大小");
                    }
                }

            } else {
                if (!TextUtils.isEmpty(searchSingleton.openTimeSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·家的大小");
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·" + searchSingleton.budgetSearch + "·家的大小");
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(searchSingleton.openTimeSearch)) {
            tvContent3.setText(searchSingleton.openTimeSearch);

            //一个搜索框值得设置
            if (!TextUtils.isEmpty(searchSingleton.buyWhereSearch)) {
                if (!TextUtils.isEmpty(searchSingleton.budgetSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "-" + searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTime);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·预算多少·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.buyWhereSearch + "·预算多少·" + searchSingleton.openTimeSearch);
                    }
                }

            } else {
                if (!TextUtils.isEmpty(searchSingleton.budgetSearch)) {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·" + searchSingleton.budgetSearch + "·" + searchSingleton.openTimeSearch);
                    }
                } else {
                    if (!TextUtils.isEmpty(searchSingleton.whereHouseSearch)) {
                        tvSingleTitle.setText(searchSingleton.whereHouseSearch + "·预算多少·" + searchSingleton.openTimeSearch);
                    } else {
                        tvSingleTitle.setText(searchSingleton.city + "·预算多少·" + searchSingleton.openTimeSearch);
                    }
                }
            }
        }
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.requestSearchData(totalRequstBean, type, isTransparent, page + "");
    }

    @Override
    public void showTip(String msg) {

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
                } else {
                    tvOffset2 = 0;
                    tvOffset3 = 0;
                }
                tvContent1.setTranslationY(-verticalOffset);
                tvContent2.setTranslationY(-verticalOffset - tvOffset2);
                tvContent3.setTranslationY(-verticalOffset - tvOffset3);
            }
        });
    }


    @OnClick({R.id.iv_arrow, R.id.rl_search, R.id.tvContent1, R.id.tvContent2, R.id.tvContent3,
            R.id.tv_clear, R.id.iv_back_search, R.id.tv_filter_map, R.id.iv_filter_map, R.id.tv_filter, R.id.iv_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                isClear = true;
                allBaseBeanList.clear();
                SearchSingleton singleton = SearchSingleton.getIstance();
                if (singleton.isIndexHome) {
//                    singleton.buyWhere = "北京市";
//                    singleton.whereHouse = "";
                    singleton.budget = "";
                    singleton.openTime = "";
                    singleton.minBugetValue = 0;
                    singleton.maxBugetValue = 0;
                    searchSingleton.minBugetValueArea = 0;
                    searchSingleton.maxBugetValueArea = 2000;
                    if (singleton.chooseSet != null) {
                        singleton.chooseSet.clear();
                        singleton.chooseSet = null;
                    }

                    if (singleton.chooseSetArea != null) {
                        singleton.chooseSetArea.clear();
                        singleton.chooseSetArea = null;
                    }


                } else {
//                    singleton.buyWhereSearch = "北京市";
//                    singleton.whereHouseSearch = "";
                    singleton.budgetSearch = "";
                    singleton.openTimeSearch = "";
                    singleton.minBugetValueBrand = 0;
                    singleton.maxBugetValueBrand = 0;
                    searchSingleton.minBugetValueBrandArea = 0;
                    searchSingleton.maxBugetValueBrandArea = 2000;
                    if (singleton.chooseSetBrand != null) {
                        singleton.chooseSetBrand.clear();
                        singleton.chooseSetBrand = null;
                    }

                    if (singleton.chooseSetBrandArea != null) {
                        singleton.chooseSetBrandArea.clear();
                        singleton.chooseSetBrandArea = null;
                    }
                }

                singleton.minPrice = "";
                singleton.maxPrice = "";
                //// TODO: 2017/7/6/006
//                totalRequstBean.bugetMapList = singleton.bugetMapList;
                if (singleton.priceMapList != null) {
                    singleton.priceMapList.clear();
                }

                if (singleton.areaMapList != null) {
                    singleton.areaMapList.clear();
                }

                if (totalRequstBean.priceMapList != null) {
                    totalRequstBean.priceMapList.clear();
                }

                if (totalRequstBean.areaMapList != null) {
                    totalRequstBean.areaMapList.clear();
                }
                totalRequstBean.addressDistrict = singleton.addressDistrict;
                totalRequstBean.subway = singleton.subway;
//                totalRequstBean.propertyType = singleton.propertyType;
                //// TODO: 2017/7/6/006
//                totalRequstBean.areaMapList = singleton.areaMapList;
//                totalRequstBean.areaMapList = singleton.maxHouseSize;
                totalRequstBean.sort = singleton.sort;
                //// TODO: 2017/7/6/006
//                totalRequstBean.devRoomInfo = singleton.devRoomInfo;
                totalRequstBean.devName = "";
                totalRequstBean.cityId = singleton.cityId;
                totalRequstBean.city = singleton.city;
                tvContent1.setText(singleton.city);
//                searchSingleton.whereHouse = "";
//                if (dao.getLocateCache() != null) {
//                    totalRequstBean.cityId = dao.localDao.getCityId();
//                    searchSingleton.cityId = dao.localDao.getCityId();
//                    tvContent1.setText(dao.localDao.getCity());
//                    searchSingleton.city = dao.localDao.getCity();
//                    searchSingleton.buyWhere = dao.localDao.getCity();
//                    searchSingleton.whereHouse = "";
//                } else {
//                    totalRequstBean.cityId = "110100";
//                    searchSingleton.cityId = "110100";
//                    tvContent1.setText("北京市");
//                    searchSingleton.city = "北京市";
//                    searchSingleton.buyWhere = "北京市";
//                    searchSingleton.whereHouse = "";
//                }

                if (searchSingleton.isIndexHome) {
                    EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));
                }


                tvContent2.setText("预算多少");
                tvContent3.setText("家的大小");

                tvSingleTitle.setText(searchSingleton.city + "·预算多少·家的大小");
                EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));

                if (totalRequstBean != null) {
                    showLoading();
                    mPresenter.requestSearchData(totalRequstBean, type, isTransparent, "1");
                }
                break;
            case R.id.iv_arrow:
                mAppBarLayout.setExpanded(false);
                break;
            case R.id.rl_search:
                mAppBarLayout.setExpanded(true);
                break;
            case R.id.tvContent1:
                startActivity(new Intent(this, WhereBuyActivity.class));
                break;
            case R.id.tvContent2:
                startActivity(new Intent(this, BugetActivity.class));
                break;
            case R.id.tvContent3:
//                startActivity(new Intent(this, OpenTimeActivity.class));
                startActivity(new Intent(this, HomeAreaActivity.class).putExtra("isClear", isClear));
                break;
            case R.id.iv_back_search:
                finish();
                break;
            case R.id.iv_filter_map:
            case R.id.tv_filter_map:
                if (searchSingleton.isIndexHome) {
                    MapFindHouseActivity.startActivity(this, searchSingleton.city, searchSingleton.cityId);
                } else {
                    MapFindHouseActivity.startActivity(this, searchSingleton.citySearch, searchSingleton.cityIdSearch);
                }

                break;
            case R.id.iv_filter:
            case R.id.tv_filter:
                HouseFilterActivity.startActivity(this, false, searchSingleton.city, searchSingleton.cityId, searchBean.type);
                break;
        }
    }

    @Override
    public void showSuccessData(List<HousesDetailBaseBean> baseBeanList) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        cancelLoading();
        this.baseBeanList = baseBeanList;
        if (page == Constants.PAGE_START) {
            mAdapter.setNewData(baseBeanList);
        } else {
            mAdapter.addData(baseBeanList);
            mAdapter.loadMoreComplete();
        }
        allBaseBeanList.addAll(baseBeanList);

    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
        CustomerUtils.showTip(this, msg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (searchSingleton != null) {
            searchSingleton.budgetSearch = "";
            searchSingleton.buyWhereSearch = "";
            searchSingleton.openTimeSearch = "";
            searchSingleton.searchtype = "0";

            //页面结束 清除筛选的保存值
            searchSingleton.location = "";
            searchSingleton.subway = "";
            searchSingleton.beforeTime = "";
//            if (searchSingleton.houseAreaIdSet != null) {
//                searchSingleton.houseAreaIdSet.clear();
//            }
//            if (searchSingleton.houseAreaIdSetSearch != null) {
//                searchSingleton.houseAreaIdSetSearch.clear();
//            }
//            searchSingleton.maxAvgPrige = "";
            searchSingleton.addressDistrict = "";
            searchSingleton.subway = "";
            searchSingleton.propertyType = "";
            if (searchSingleton.budgetSet != null) {
                searchSingleton.budgetSet.clear();
            }

//            searchSingleton.maxHouseSize = "";
            searchSingleton.sort = "";
            if (searchSingleton.devRoomInfoSet != null) {
                searchSingleton.devRoomInfoSet.clear();
            }
            if (searchSingleton.devRoomInfoSetSearch != null) {
                searchSingleton.devRoomInfoSetSearch.clear();
            }

            //从首页搜索进来
            searchSingleton.houseTypeId = 0;
            searchSingleton.sortId = 0;
            searchSingleton.location = "";
            searchSingleton.subway = "";

            //从品牌地产进来
            searchSingleton.houseTypeIdSearch = 0;
            searchSingleton.sortIdSearch = 0;

        }
    }

    @Override
    public void onLoadMoreRequested() {
        homeRecycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (baseBeanList != null && baseBeanList.size() >= Constants.PAGE_SIZE) {
                    page++;
                    mPresenter.requestSearchData(searchBean, type, isTransparent, page + "");
                } else {
                    mAdapter.loadMoreEnd();
                    CustomerUtils.showTip(SearchActivity.this, "没有更多数据了");
                }
            }
        }, 500);
    }
}
