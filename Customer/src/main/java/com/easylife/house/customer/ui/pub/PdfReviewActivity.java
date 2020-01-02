package com.easylife.house.customer.ui.pub;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.view.pdfviewpager.library.RemotePDFViewPager;
import com.easylife.house.customer.view.pdfviewpager.library.adapter.PDFPagerAdapter;
import com.easylife.house.customer.view.pdfviewpager.library.remote.DownloadFile;
import com.easylife.house.customer.view.pdfviewpager.library.util.FileUtil;

import butterknife.Bind;

public class PdfReviewActivity extends BaseActivity
        implements DownloadFile.Listener {

    public static void startActivity(Context context, String name, String url) {
        context.startActivity(new Intent(context, PdfReviewActivity.class)
                .putExtra("name", name)
                .putExtra("url", url)
        );
    }

    private RemotePDFViewPager remotePDFViewPager;

    @Bind(R.id.pdf_root)
    LinearLayout pdf_root;

    private String pdfUrl;
    private String pdfName;
    private PDFPagerAdapter adapter;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_pdf, null);
    }

    @Override
    protected void initView() {
        pdfName = getIntent().getStringExtra("pdfName");
        pdfUrl = getIntent().getStringExtra("url");

        if (!TextUtils.isEmpty(pdfName))
            tvTitle.setText(pdfName);

        showLoading();
        setDownloadButtonListener();
    }

    protected void setDownloadButtonListener() {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(ctx, pdfUrl, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.close();
        }
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }

    private void updateLayout() {
        if (pdf_root != null) {
            pdf_root.removeAllViewsInLayout();
            pdf_root.addView(remotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cancleLoading();
        }
    }

    @Override
    public void onFailure(Exception e) {
//        cancelProgressDialog();
        ToastUtils.showShort("加载失败");
        finish();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

}
