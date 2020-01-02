package com.easylife.house.customer.ui.homesearch.buget;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zgm on 2017/3/20.
 */

public class BugetPresenter extends BasePresenterImpl<BugetContract.View> implements BugetContract.Presenter {

    @Override
    public void chooseFlowChild(Map<Integer, Integer> chooseSet) {

        ArrayList<Map<String, String>> chooseList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : chooseSet.entrySet()) {
            switch (entry.getKey()) {
                case 0:
                    Map<String, String> choose = new HashMap<>();
                    choose.put("minPrice", "0");
                    choose.put("maxPrice", "50");
                    chooseList.add(choose);
                    break;
                case 1:
                    Map<String, String> choose1 = new HashMap<>();
                    choose1.put("minPrice", "50");
                    choose1.put("maxPrice", "100");
                    chooseList.add(choose1);
                    break;
                case 2:
                    Map<String, String> choose2 = new HashMap<>();
                    choose2.put("minPrice", "100");
                    choose2.put("maxPrice", "200");
                    chooseList.add(choose2);
                    break;
                case 3:
                    Map<String, String> choose3 = new HashMap<>();
                    choose3.put("minPrice", "100");
                    choose3.put("maxPrice", "200");
                    chooseList.add(choose3);
                    break;
                case 4:
                    Map<String, String> choose4 = new HashMap<>();
                    choose4.put("minPrice", "300");
                    choose4.put("maxPrice", "500");
                    chooseList.add(choose4);
                    break;
                case 5:
                    Map<String, String> choose5 = new HashMap<>();
                    choose5.put("minPrice", "500");
                    choose5.put("maxPrice", "800");
                    chooseList.add(choose5);
                    break;
                case 6:
                    Map<String, String> choose6 = new HashMap<>();
                    choose6.put("minPrice", "800");
                    choose6.put("maxPrice", "1000");
                    chooseList.add(choose6);
                    break;
                case 7:
                    Map<String, String> choose7 = new HashMap<>();
                    choose7.put("minPrice", "1000");
                    choose7.put("maxPrice", "");
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
