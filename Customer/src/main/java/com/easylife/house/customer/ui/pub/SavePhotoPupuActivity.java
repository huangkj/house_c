package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.easylife.house.customer.R;

/**
 * 弹出菜单显示
 * @Desc 
 * @author heiyue
 * @Email heiyue623@126.com
 * @Date 2015-6-7
 */
public class SavePhotoPupuActivity extends Activity {
	public static boolean isSave;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub_item_popu_save_photo);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		isSave = false;
		findViewById(R.id.pop_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isSave = true;
				setResult(RESULT_OK);
				finish();
			}
		});
		// 取消
		findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	/**
	 * 打开
	 * @param context
	 * @param requestCode
	 */
	public static void open(Activity context,int requestCode){
		Intent intent = new Intent(context, SavePhotoPupuActivity.class);
		context.startActivityForResult(intent, requestCode);
	}
}
