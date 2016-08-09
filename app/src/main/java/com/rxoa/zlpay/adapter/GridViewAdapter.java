package com.rxoa.zlpay.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rxoa.zlpay.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
	private List<HashMap<String, Object>> list;
    private Map<String,Object> tempGridViewItem;
    private LayoutInflater layoutInflater;

    public GridViewAdapter(Context context,
    	List<HashMap<String, Object>> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 数据总数
     */
    @Override
    public int getCount() {

        return list.size();
    }

    /**
     * 获取当前数据
     */
    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (layoutInflater != null) {

            view = layoutInflater
                    .inflate(R.layout.home_app_item, null);
            ImageView imageView = (ImageView) view
                    .findViewById(R.id.ItemImage);
            TextView textView = (TextView) view.findViewById(R.id.ItemText);
            //获取自定义的类实例
            tempGridViewItem = list.get(position);
            imageView.setImageResource((Integer)tempGridViewItem.get("itemImage"));
            textView.setText(tempGridViewItem.get("itemText").toString());
        }
        return view;
    }
}
