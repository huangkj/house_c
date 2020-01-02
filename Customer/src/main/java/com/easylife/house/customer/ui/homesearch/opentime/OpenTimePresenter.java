package com.easylife.house.customer.ui.homesearch.opentime;

import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.CustomerUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zgm on 2017/3/20.
 */

public class OpenTimePresenter extends BasePresenterImpl<OpenTimeContract.View> implements OpenTimeContract.Presenter {
    @Override
    public void chooseOpenTime(String choosePosition) {
        String date = "";
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = sdf.format(dt);

        switch (choosePosition) {
            case "0":

                break;
            case "1":
                try {
                    date = CustomerUtils.subMonth(dateNow, 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "2":
                try {
                    date = CustomerUtils.subMonth(dateNow, 3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                try {
                    date = CustomerUtils.subMonth(dateNow, 6);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }

        if (mView != null) mView.showChooseTime(date);
    }
}
