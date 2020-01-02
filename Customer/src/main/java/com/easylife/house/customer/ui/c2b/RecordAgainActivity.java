package com.easylife.house.customer.ui.c2b;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Record;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/10/19 17:19.
 * 描述：再报备
 */

public class RecordAgainActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Bind(R.id.btnMan)
    RadioButton btnMan;
    @Bind(R.id.btnWoMan)
    RadioButton btnWoMan;
    @Bind(R.id.tvPhone)
    TextView tvPhone;
    @Bind(R.id.tvCity)
    TextView tvCity;
    @Bind(R.id.tvDev)
    TextView tvDev;
    @Bind(R.id.tvArrivedDate)
    TextView tvArrivedDate;

    public static void startActivity(Activity activity, Record record, int requestCode) {
        Intent intent = new Intent(activity, RecordAgainActivity.class);
        intent.putExtra("record", record);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_record_again, null);
    }

    private Record record;
    private Calendar now;
    private int year, month, day;
    private String chooseTime;

    @Override
    protected void initView() {
        record = (Record) getIntent().getSerializableExtra("record");

        initRecord();
    }

    private void initRecord() {
        if (record == null)
            return;
        tvCustomerName.setText(record.name);
        tvPhone.setText(record.phone);
        tvDev.setText(record.devName);
        tvCity.setText(record.city);
    }

    @Override
    protected void setActionBarDetail() {

    }


    @OnClick({R.id.tvArrivedDate, R.id.btnCommit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvArrivedDate:
                now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.setAccentColor(getResources().getColor(R.color.gradient_start));
                dpd.show(getFragmentManager(), "DatePickerDialog");
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                break;
            case R.id.btnCommit:
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear + 1;
        day = dayOfMonth;

        TimePickerDialog tpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), true);
        tpd.setAccentColor(getResources().getColor(R.color.gradient_start));
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String minuteStr = "";
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = minute + "";
        }
        chooseTime = year + "-" + month + "-" + day + " " + hourOfDay + ":" + minuteStr;
        tvArrivedDate.setText(chooseTime);
    }
}
