package com.easylife.house.customer.config;

import android.text.TextUtils;

import com.easylife.house.customer.bean.ItemSelect;

import java.util.ArrayList;

/**
 * Created by Mars on 2017/2/27 20:33.
 * 描述：客户信息选择item
 */

public class ItemSelectManager {
    public static final int TYPE_SELECT_INFO_SEX = 1;
    public static final int TYPE_SELECT_INFO_SOURCE = 15;
    public static final int TYPE_SELECT_INFO_HOUSE_TYPE = 4;
    public static final int TYPE_SELECT_INFO_AREA = 5;
    public static final int TYPE_SELECT_INFO_LEVEL = 6;
    public static final int TYPE_SELECT_INFO_BUDGET = 7;
    public static final int TYPE_SELECT_INFO_FAMILY_STRUCT = 8;
    public static final int TYPE_SELECT_INFO_FORMAT_TYPE = 9;
    public static final int TYPE_SELECT_INFO_OCCUPTATION = 14;
    public static final int TYPE_SELECT_INFO_PURCHASING_MO = 10;
    public static final int TYPE_SELECT_INFO_PAY_TYPE = 11;
    public static final int TYPE_SELECT_INFO_ATTENTION = 12;
    public static final int TYPE_SELECT_INFO_SENIORITY = 13;
    public static final int TYPE_SELECT_INFO_LOAN = 22;

    public static final int TYPE_SELECT_RECORD = 16;

    public static final int TYPE_RECORD_BUILD = 17;
    public static final int TYPE_RECORD_BUILD_UNIT = 18;
    public static final int TYPE_RECORD_BUILD_FLOOR = 19;
    public static final int TYPE_RECORD_BUILD_CELLNO = 20;

    public static final int TYPE_DATE = 21;
    public static final int HOUSE_TYPE_FLAG_TEXTSIZE = 11;


    public final static String TYPE_RECORD_STATUS_0 = "0";
    public final static String TYPE_RECORD_STATUS_1 = "0";
    public final static String TYPE_RECORD_STATUS_2 = "0";

    public final static String TEXT_RECORD_STATUS_0 = "提交";
    public final static String TEXT_RECORD_STATUS_1 = "通过";
    public final static String TEXT_RECORD_STATUS_2 = "拒绝";

    public final static int TYPE_RECORD_NORMAL = 0;
    public final static int TYPE_RECORD_REPORT = 1;
    public final static int TYPE_RECORD_LOOK = 2;
    public final static int TYPE_RECORD_BUY = 3;
    public final static int TYPE_RECORD_SIGN = 4;
    public static final String TEXT_TYPE_ISINTENTION_NO = "无意向";
    public static final String TEXT_TYPE_RECORD_NORMAL = "常规跟进";
    public static final String TEXT_TYPE_RECORD_REPORT = "报备";
    public static final String TEXT_TYPE_RECORD_LOOK = "带看";
    public static final String TEXT_TYPE_RECORD_BUY = "认购";
    public static final String TEXT_TYPE_RECORD_SIGN = "签约";

    public static final String Sort_Record_Positive = "按最近跟进时间正序";
    public static final String Sort_Record_Invert = "按最近跟进时间倒序";
    public static final String Sort_Buy_Positive = "按认购时间正序";
    public static final String Sort_But_Invert = "按认购时间倒序";
    public static final String Sort_Report_Positive = "按报备时间正序";
    public static final String Sort_Report_Invert = "按报备时间倒序";
    public static final int CODE_FOR_WRITE_PERMISSION = 11;
    public static final String update_download_apk_name = "goodLifeCustomer.apk";

    public static final String TEXT_TYPE_DARE_STATE = "2017-01-01";

    public static String getSelectItemsText(int type, String id) {
        ArrayList<ItemSelect> data = getSelectItems(type);
        if (TextUtils.isEmpty(id) || data == null || data.size() == 0)
            return null;
        String text = "";
        for (ItemSelect item : data) {
            if (id.equals(item.id)) {
                text = item.text;
                break;
            }
        }
        return text;
    }

    public static ArrayList<ItemSelect> getSelectItems(int type) {
        ArrayList<ItemSelect> list = new ArrayList<>();
        switch (type) {
            case TYPE_SELECT_INFO_SEX:
                list.add(new ItemSelect("男", "0"));
                list.add(new ItemSelect("女", "1"));
                break;
            case TYPE_SELECT_INFO_SOURCE:
                list.add(new ItemSelect("APP", "0"));
                list.add(new ItemSelect("录入", "1"));
                list.add(new ItemSelect("扫码录入", "2"));
                break;
            case TYPE_SELECT_INFO_HOUSE_TYPE:
                list.add(new ItemSelect("一居", "0"));
                list.add(new ItemSelect("两居", "1"));
                list.add(new ItemSelect("三居", "2"));
                list.add(new ItemSelect("四居", "3"));
                list.add(new ItemSelect("五居", "4"));
                list.add(new ItemSelect("五居以上", "5"));
                break;
            case TYPE_SELECT_INFO_AREA:
                list.add(new ItemSelect("50㎡以内", "0"));
                list.add(new ItemSelect("50-70㎡", "1"));
                list.add(new ItemSelect("70-90㎡", "2"));
                list.add(new ItemSelect("90-110㎡", "3"));
                list.add(new ItemSelect("110-130㎡", "4"));
                list.add(new ItemSelect("130-150㎡", "5"));
                list.add(new ItemSelect("150-200㎡", "6"));
                list.add(new ItemSelect("200-300㎡", "7"));
                list.add(new ItemSelect("300㎡以上", "8"));
                break;
            case TYPE_SELECT_INFO_LEVEL:
                list.add(new ItemSelect("弱", "0"));
                list.add(new ItemSelect("中", "1"));
                list.add(new ItemSelect("强", "2"));
                break;
            case TYPE_SELECT_INFO_BUDGET:
                list.add(new ItemSelect("100万以下", "0"));
                list.add(new ItemSelect("100-150万", "1"));
                list.add(new ItemSelect("150-200万", "2"));
                list.add(new ItemSelect("200-300万", "3"));
                list.add(new ItemSelect("300-500万", "4"));
                list.add(new ItemSelect("500-1000万", "5"));
                list.add(new ItemSelect("1000万以上", "6"));
                break;
            case TYPE_SELECT_INFO_FAMILY_STRUCT:
                list.add(new ItemSelect("未婚", "5"));
                list.add(new ItemSelect("已婚无小孩", "1"));
                list.add(new ItemSelect("已婚有小孩", "2"));
                list.add(new ItemSelect("已婚有小孩和老人", "3"));
                list.add(new ItemSelect("其他", "4"));
                break;
            case TYPE_SELECT_INFO_FORMAT_TYPE:
                list.add(new ItemSelect("普通住宅", "0"));
                list.add(new ItemSelect("商住两用", "1"));
                list.add(new ItemSelect("联排别墅", "2"));
                list.add(new ItemSelect("独栋别墅", "3"));
                list.add(new ItemSelect("其他", "4"));
                break;
            case TYPE_SELECT_INFO_OCCUPTATION:
                list.add(new ItemSelect("一般雇员", "6"));
                list.add(new ItemSelect("企业主管", "1"));
                list.add(new ItemSelect("企业经理", "2"));
                list.add(new ItemSelect("机关事业单位", "3"));
                list.add(new ItemSelect("自由职业者", "4"));
                list.add(new ItemSelect("其他", "5"));
                break;
            case TYPE_SELECT_INFO_PURCHASING_MO:
                list.add(new ItemSelect("自住", "0"));
                list.add(new ItemSelect("结婚用房", "1"));
                list.add(new ItemSelect("改善居住", "2"));
                list.add(new ItemSelect("养老", "3"));
                list.add(new ItemSelect("为子女购房", "4"));
                list.add(new ItemSelect("为父母购房", "5"));
                list.add(new ItemSelect("投资", "6"));
                list.add(new ItemSelect("投资兼自住", "7"));
                list.add(new ItemSelect("上班方便", "8"));
                list.add(new ItemSelect("其他", "9"));
                break;
            case TYPE_SELECT_INFO_PAY_TYPE:
                list.add(new ItemSelect("一次性付款", "0"));
                list.add(new ItemSelect("分期付款", "1"));
                list.add(new ItemSelect("商业贷款", "2"));
                list.add(new ItemSelect("公积金贷款", "3"));
                list.add(new ItemSelect("组合型贷款", "4"));
                list.add(new ItemSelect("其他", "5"));
                break;
            case TYPE_SELECT_INFO_ATTENTION:
                list.add(new ItemSelect("项目地段", "0"));
                list.add(new ItemSelect("周边配套", "1"));
                list.add(new ItemSelect("环境", "2"));
                list.add(new ItemSelect("交通", "3"));
                list.add(new ItemSelect("未来潜力", "4"));
                list.add(new ItemSelect("开发商", "5"));
                list.add(new ItemSelect("建筑风格", "6"));
                list.add(new ItemSelect("物业管理", "7"));
                list.add(new ItemSelect("工程质量", "8"));
                list.add(new ItemSelect("交房时间", "9"));
                list.add(new ItemSelect("户型设计", "10"));
                list.add(new ItemSelect("销售价格", "11"));
                list.add(new ItemSelect("其他", "12"));
                break;
            case TYPE_SELECT_INFO_SENIORITY:
                list.add(new ItemSelect("有", "0"));
                list.add(new ItemSelect("无", "1"));
                break;
            case TYPE_SELECT_INFO_LOAN:
                list.add(new ItemSelect("是", "0"));
                list.add(new ItemSelect("否", "1"));
                break;
            case TYPE_SELECT_RECORD:
                list.add(new ItemSelect(TEXT_TYPE_RECORD_NORMAL));
                list.add(new ItemSelect(TEXT_TYPE_RECORD_REPORT));
                list.add(new ItemSelect(TEXT_TYPE_RECORD_LOOK));
                list.add(new ItemSelect(TEXT_TYPE_RECORD_BUY));
                list.add(new ItemSelect(TEXT_TYPE_RECORD_SIGN));
                break;
            case TYPE_DATE:
//                Date start = Date2StringUtil.getDateYMD(TEXT_TYPE_DARE_STATE);
//                Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
//                Calendar newCalendar = (Calendar) mCalendar.clone();
//                newCalendar.set(Calendar.YEAR, 2017);
//                newCalendar.set(Calendar.DAY_OF_YEAR, 01);
//                DateFormat formatDays = new SimpleDateFormat("yyyy年MM月");
                break;
        }
        return list;
    }
}
