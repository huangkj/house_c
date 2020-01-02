package com.easylife.house.customer.ui.mine.calculator.taxes;

import android.text.TextUtils;

import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.DoubleFomat;


public class TaxesPresenter extends BasePresenterImpl<TaxesContract.View> implements TaxesContract.Presenter {

    @Override
    public void showResult(boolean isFirst, String areaStr, String priceUnitStr) {
        if (mView == null)
            return;
        if (TextUtils.isEmpty(areaStr)) {
            mView.showTip("请输入房屋面积");
            return;
        }
        if (TextUtils.isEmpty(priceUnitStr)) {
            mView.showTip("请输入房屋单价");
            return;
        }

        double area = Double.parseDouble(areaStr);
        double priceUnit = Double.parseDouble(priceUnitStr);

        double moneyAll = area * priceUnit; //   房款总额
        double moneyYinHua = moneyAll * 0.0005; //    印花税    ------------  固定比例  0.05%
        double moneyQuanShu = 80; //   权属登记费 --------------     固定金额 80


        double moneyQiShui = 0; //   契税   ------  不同面积下不同比例  1% ， 1.5% ， 3%
        // 税费计算公式（主要是计算契税）
        if (isFirst) {
            // 首套
            if (area < 90) {
                // 面积小于90
                moneyQiShui = moneyAll * 0.01;
            } else if (area >= 90 && area <= 144) {
                moneyQiShui = moneyAll * 0.015;
            } else {
                moneyQiShui = moneyAll * 0.03;
            }
        } else {
            // 非首套
            moneyQiShui = moneyAll * 0.03;
        }
        double moneyAllTaxes = moneyYinHua + moneyQiShui + moneyQuanShu; //  税金总额   ------------     印花税+契税+权属登记费

        if (moneyAll >= 10000) {
            mView.initChart((float) (moneyAllTaxes), (float) (moneyAll), DoubleFomat.format2(moneyAll / 10000) + "万元",
                    (int) moneyYinHua + "元", (int) moneyQiShui + "元", (int) moneyQuanShu + "元", (int) moneyAllTaxes + "元");
        } else {
            mView.initChart((float) (moneyAllTaxes), (float) (moneyAll), DoubleFomat.format2(moneyAll) + "元",
                    (int) moneyYinHua + "元", (int) moneyQiShui + "元", (int) moneyQuanShu + "元", (int) moneyAllTaxes + "元");
        }
    }
}
