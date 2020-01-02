package com.easylife.house.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.PYBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 有字母排序的ListView
 */
public class PYListAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<PYBean> list;
    private boolean showBtn = true;
    private ItemClickListener<PYBean> listener;

    public PYListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public PYListAdapter(Context context, ItemClickListener<PYBean> listener) {
        super();
        this.context = context;
        this.listener = listener;
        this.list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 重新给集合填充数据
     *
     * @param mList
     */
    public synchronized void setData(List<? extends PYBean> mList) {
        this.list.clear();
        notifyDataSetChanged();
        if (mList != null) {
            this.list.addAll(mList);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取集合
     *
     * @return
     */
    public List<PYBean> getList() {
        return this.list;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_user_contact, null);
            viewHolder.layout_letter = convertView
                    .findViewById(R.id.layout_letter);
            viewHolder.layout_body = convertView.findViewById(R.id.layout_body);
            viewHolder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tvTitle);
            viewHolder.tvLetter = (TextView) convertView
                    .findViewById(R.id.tvLetter);
            viewHolder.tvAdd = (ImageView) convertView.findViewById(R.id.btnAdd);
//            viewHolder.ivHeader = (ImageView) convertView
//                    .findViewById(R.id.ivHeader);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PYBean entity = list.get(position);

        if (showBtn) {
            viewHolder.tvAdd.setVisibility(View.VISIBLE);

            if (entity.getSelected()) {
                viewHolder.tvAdd.setVisibility(View.VISIBLE);
                viewHolder.tvAdd.setEnabled(false);
            } else {
                viewHolder.tvAdd.setVisibility(View.GONE);
                viewHolder.tvAdd.setEnabled(true);
            }
        } else {
            viewHolder.tvAdd.setVisibility(View.GONE);
        }

        initInfo(viewHolder, entity, position);

        String name = entity.getBeanName();
        if (!TextUtils.isEmpty(name)) {
            if (name.contains("\r"))
                name = name.replace("\r", "");
            if (name.contains("\n"))
                name = name.replace("\n", "");
        }
        viewHolder.tvTitle.setText(name);

        viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.itemClick(0, 0, entity);
            }
        });

        return convertView;
    }

    public void initInfo(ViewHolder viewHolder, PYBean entity, int position) {
        viewHolder.layout_body.setVisibility(View.VISIBLE);

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (section != -1) {
            if (position == getPositionForSection(section)) {
                viewHolder.layout_letter.setVisibility(View.VISIBLE);
                String chr = entity.getPys();
                if (chr != null && chr.length() > 1)
                    chr = chr.substring(0, 1);
                viewHolder.tvLetter.setText(chr);
            } else {
                viewHolder.layout_letter.setVisibility(View.GONE);
            }
        }
//        if (entity instanceof FriendBean) {
//            //我的好友显示头像
//            FriendBean bean = (FriendBean) entity;
//            viewHolder.ivHeader.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(bean.icon, viewHolder.ivHeader, CacheManager.getUserHeaderDisplay());
//        } else {
//            viewHolder.ivHeader.setVisibility(View.GONE);
//        }
    }

    class ViewHolder {
        View layout_letter;
        View layout_body;

        TextView tvLetter;
        TextView tvTitle;
        ImageView tvAdd;
        /**
         * 头像
         */
//        ImageView ivHeader;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getPys();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        try {
            return list.get(position).getPys().charAt(0);
        } catch (Exception e) {
            return -1;
        }
    }
}
