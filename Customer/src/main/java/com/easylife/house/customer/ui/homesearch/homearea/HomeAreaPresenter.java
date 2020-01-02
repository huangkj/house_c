package com.easylife.house.customer.ui.homesearch.homearea;

import android.widget.CheckBox;

import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.ui.homesearch.buget.BugetContract;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgm on 2017/3/20.
 */

public class HomeAreaPresenter extends BasePresenterImpl<HomeAreaContract.View> implements HomeAreaContract.Presenter {

    @Override
    public void chooseFlowChild(Map<Integer, Integer> chooseSet) {

        ArrayList<Map<String, String>> chooseList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : chooseSet.entrySet()) {
            switch (entry.getKey()) {
                case 0:
                    Map<String, String> choose = new HashMap<>();
                    choose.put("minHouseSize", "0");
                    choose.put("maxHouseSize", "50");
                    chooseList.add(choose);
                    break;
                case 1:
                    Map<String, String> choose1 = new HashMap<>();
                    choose1.put("minHouseSize", "50");
                    choose1.put("maxHouseSize", "70");
                    chooseList.add(choose1);
                    break;
                case 2:
                    Map<String, String> choose2 = new HashMap<>();
                    choose2.put("minHouseSize", "70");
                    choose2.put("maxHouseSize", "90");
                    chooseList.add(choose2);
                    break;
                case 3:
                    Map<String, String> choose3 = new HashMap<>();
                    choose3.put("minHouseSize", "90");
                    choose3.put("maxHouseSize", "110");
                    chooseList.add(choose3);
                    break;
                case 4:
                    Map<String, String> choose4 = new HashMap<>();
                    choose4.put("minHouseSize", "110");
                    choose4.put("maxHouseSize", "150");
                    chooseList.add(choose4);
                    break;
                case 5:
                    Map<String, String> choose5 = new HashMap<>();
                    choose5.put("minHouseSize", "150");
                    choose5.put("maxHouseSize", "200");
                    chooseList.add(choose5);
                    break;
                case 6:
                    Map<String, String> choose6 = new HashMap<>();
                    choose6.put("minHouseSize", "200");
                    choose6.put("maxHouseSize", "300");
                    chooseList.add(choose6);
                    break;
                case 7:
                    Map<String, String> choose7 = new HashMap<>();
                    choose7.put("minHouseSize", "300");
                    choose7.put("maxHouseSize", "");
                    chooseList.add(choose7);
                    break;
            }
        }

        if (mView != null)
            mView.showChooseTime(chooseList);

    }

//    @Override
//    public void lisenearETisEmpty(HashSet<Integer> chooseSet, final FlowViewGroup mFlowViewTime) {
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    if (!editText.getText().toString().equals("") && editText.getText().toString() != null) {
//                        ((BugetActivity) mView.getContext()).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                clearRadioBtn(mFlowViewTime);
//                            }
//                        });
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(timerTask, 0, 500);
//    }

    public void clearRadioBtn(FlowViewGroup mFlowViewTime) {
        if (mView != null)
            for (int i = 0; i < mFlowViewTime.getChildCount(); i++) {
                CheckBox radiobtn = (CheckBox) mFlowViewTime.getChildAt(i);
                radiobtn.setChecked(false);
                radiobtn.setTextColor(mView.getContext().getResources().getColor(R.color.gradient_end));
            }
    }
}
