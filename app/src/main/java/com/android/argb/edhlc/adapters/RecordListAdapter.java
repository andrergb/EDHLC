package com.android.argb.edhlc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.objects.Record;

import java.util.List;

public class RecordListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private List<Record> data;
    private int total;
    private String highlightedValue;

    public RecordListAdapter(Context context, List<Record> data, int total, String highlightedValue) {
        this.context = context;
        this.data = data;
        this.total = total;
        this.highlightedValue = highlightedValue;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_element_record, null);

        Utils.createRecordListElement(vi, data.get(position), highlightedValue);

        return vi;
    }
}