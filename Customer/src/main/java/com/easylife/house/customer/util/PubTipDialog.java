package com.easylife.house.customer.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemSelectAdapter;
import com.easylife.house.customer.bean.IItemSelect;
import com.easylife.house.customer.bean.ItemSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mars
 */
public class PubTipDialog {

    private Context context;
    private Dialog dialog;
    private InsideListener listener;

    public PubTipDialog(Context context, InsideListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public PubTipDialog(Context context) {
        this.context = context;
    }

    /**
     * @param title     提示的标题
     * @param content   提示的第一行内容
     * @param content1  提示的第二行内容
     * @param strCancel 取消按钮的文本内容
     * @param strOk     确定按钮的文本内容
     */
    @SuppressLint("InflateParams")
    public void showdialog(String title, String content, String content1,
                           String strCancel, String strOk) {
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pub_toast_tip, null);
            Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
            Button btnOk = (Button) view.findViewById(R.id.btnOk);
            TextView tvTitle = (TextView) view.findViewById(R.id.tvHouseName);
            TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
            TextView tvContent1 = (TextView) view.findViewById(R.id.tvContent1);

            tvTitle.setText(title);
            tvContent.setText(content);
            tvContent1.setText(content1);
            btnCancel.setText(strCancel);
            btnOk.setText(strOk);
            // dialog = new AlertDialog.Builder(context).create();
            // dialog.show();
            // dialog.dismiss();
            // dialog.setContentView(view);
            dialog = AlertDialogUtil.getAlertDialog(context, view);
            btnCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.note(false);
                    dialog.dismiss();
                }
            });
            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.note(true);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    public void showPayTip(int layoutId) {
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layoutId, null);
            dialog = AlertDialogUtil.getAlertDialog(context, view);

            View btnOk = view.findViewById(R.id.btnOk);
            if (btnOk != null)
                btnOk.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.note(true);
                    }
                });
            View btnCancel = view.findViewById(R.id.btnCancel);
            if (btnCancel != null)
                btnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.note(false);
                    }
                });
        }
        dialog.show();
    }

    @SuppressLint("InflateParams")
    public void showDialogUpdatePassWord() {
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_tip_update_password, null);
            Button btnOk = (Button) view.findViewById(R.id.btnOk);

            dialog = AlertDialogUtil.getAlertDialog(context, view);
            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.note(true);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    @SuppressLint("InflateParams")
    public void showdialog(String title, String content, String content1,
                           String strCancel, String strOk, final InsideListener mlistener) {
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pub_toast_tip, null);
            Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
            Button btnOk = (Button) view.findViewById(R.id.btnOk);
            TextView tvTitle = (TextView) view.findViewById(R.id.tvHouseName);
            TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
            TextView tvContent1 = (TextView) view.findViewById(R.id.tvContent1);

            tvTitle.setText(title);
            tvContent.setText(content);
            tvContent1.setText(content1);
            btnCancel.setText(strCancel);
            btnOk.setText(strOk);
            // dialog = new AlertDialog.Builder(context).create();
            // dialog.show();
            // dialog.dismiss();
            // dialog.setContentView(view);
            dialog = AlertDialogUtil.getAlertDialog(context, view);
            btnCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mlistener.note(false);
                    dialog.dismiss();
                }
            });
            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mlistener.note(true);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private ItemSelectAdapter selectAdapter;
    private RecyclerView recyclerView;

    @SuppressLint("InflateParams")
    public void showDialogList(String title, String defaultText, List<? extends IItemSelect> list, final ItemSelectListener mlistener) {
        if (list == null) {
            CustomerUtils.showTip(context, "显示数据错误");
            return;
        }
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.pop_top_select, null);

            ((TextView) contentView.findViewById(R.id.title)).setText(title);

            recyclerView = (RecyclerView) contentView.findViewById(R.id.listview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setVerticalScrollBarEnabled(false);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);

            selectAdapter = new ItemSelectAdapter(R.layout.pub_item_select, null);
            recyclerView.setAdapter(selectAdapter);

            recyclerView.addOnItemTouchListener(new OnItemClickListener() {

                @Override
                public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    dialog.dismiss();
                    mlistener.click(i, (ItemSelect) baseQuickAdapter.getItem(i));
                }
            });

            dialog = AlertDialogUtil.getAlertDialog(context, contentView);
        }
        int defaultScrollPosition = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().equals(defaultText)) {
                defaultScrollPosition = i;
            }
        }
        defaultScrollPosition = defaultScrollPosition - 3;
        if (defaultScrollPosition < 0) {
            defaultScrollPosition = 0;
        }
        List<ItemSelect> data = new ArrayList<>();
        for (IItemSelect a : list) {
            data.add(new ItemSelect(a.getText(), a.getId()));
        }
        selectAdapter.setNewData(data);
        recyclerView.scrollToPosition(defaultScrollPosition);
        selectAdapter.setDefaultItem(defaultText);
        dialog.show();
    }

    public interface ItemSelectListener {
        void click(int i, ItemSelect date);
    }

    public interface InsideListener {
        void note(boolean isOK);
    }

}
