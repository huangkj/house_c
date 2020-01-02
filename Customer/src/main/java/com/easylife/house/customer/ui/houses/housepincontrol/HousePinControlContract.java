package com.easylife.house.customer.ui.houses.housepincontrol;

import com.easylife.house.customer.bean.HouseStatistics;
import com.easylife.house.customer.bean.ModelUnit;
import com.easylife.house.customer.bean.ResultBuild;
import com.easylife.house.customer.bean.ResultHousePinControl;
import com.easylife.house.customer.bean.ResultStructure;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class HousePinControlContract {
    interface View extends BaseView {
        void loadFail(NetBaseStatus code);

        void initListData(ResultHousePinControl data);

        void initListResultStructure(List<ResultStructure> data);

        void initListResultBuild(List<ResultBuild> data);

        void initListHouseStatistics(HouseStatistics data);
    }

    interface Presenter extends BasePresenter<View> {
        void getHouseData(String devId, String buildId, String structure);

        void getBuildList(String devId);

        void getStructureList(String devId, String buildId);

        void getHouseStatistics(String devId);

    }
}
