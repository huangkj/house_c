package com.easylife.house.customer.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.easylife.house.customer.R;


/**
 * 弹出输入框
 */
public class EditDialogUtil {
    OnSaveListener saveListener;
    private AlertDialog dialog;
    private EditText editText;

    public EditDialogUtil(final Context context, String title, int maxLength, OnSaveListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_edittext, null);
        editText = (EditText) view.findViewById(R.id.editText);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        saveListener = listener;
        dialog = new AlertDialog.Builder(context).setTitle(title)
                .setView(view)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (saveListener != null) {
                            saveListener.save(editText.getText().toString(), editText);
                        }
                    }
                }).setNegativeButton("取消", null).create();
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                InputTools.HideKeyboard(editText);
            }
        });
    }

    public interface OnSaveListener {
        public void save(String content, EditText editText);
    }

    public EditText showDialog() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return editText;
    }

    /**
     * 显示对话框
     */
    public void showDialog(String content) {
        editText.setText(content);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
