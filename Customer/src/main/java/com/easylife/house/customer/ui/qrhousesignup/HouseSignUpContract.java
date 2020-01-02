package com.easylife.house.customer.ui.qrhousesignup;

import com.easylife.house.customer.bean.CommitSignInfoBean;
import com.easylife.house.customer.bean.HouseSignBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

/**
 * Created by zgm on 2017/4/21.
 */

public class HouseSignUpContract {

    interface View extends BaseView {
        void getVerifyCodeSucc();
        void showFail(String msg);

        void showSign(HouseSignBean houseSignBean);

        void showCommitSuc(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void getVerifyCode(String phone);

        void requestSignInfo(String userCode,String devId,String token,String brokerUserCode);

        void commitSignInfo(CommitSignInfoBean commitSignInfoBean);
    }
}
