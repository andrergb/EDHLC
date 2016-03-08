package com.android.argb.edhlc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Record2ListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<String[]> data; //TODO 4x 0 title - 1 subTitle


    public Record2ListAdapter(Context context, List<String[]> data) {
        this.context = context;
        this.data = data;
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

        TextView textDateRecordCard = (TextView) vi.findViewById(R.id.textDateRecordCard);

        TextView textFirstDeckRecordCard = (TextView) vi.findViewById(R.id.textFirstDeckRecordCard);
        TextView textFirstPlayerRecordCard = (TextView) vi.findViewById(R.id.textFirstPlayerRecordCard);

        TextView textSecondDeckRecordCard = (TextView) vi.findViewById(R.id.textSecondDeckRecordCard);
        TextView textSecondPlayerRecordCard = (TextView) vi.findViewById(R.id.textSecondPlayerRecordCard);

        TextView textThirdDeckRecordCard = (TextView) vi.findViewById(R.id.textThirdDeckRecordCard);
        TextView textThirdPlayerRecordCard = (TextView) vi.findViewById(R.id.textThirdPlayerRecordCard);

        TextView textFourthDeckRecordCard = (TextView) vi.findViewById(R.id.textFourthDeckRecordCard);
        TextView textFourthPlayerRecordCard = (TextView) vi.findViewById(R.id.textFourthPlayerRecordCard);

        //TODO estrutura de dados
        textDateRecordCard.setText("Played on " + data.get(position)[0]);

        textFirstDeckRecordCard.setText(data.get(position)[0]);
        textFirstPlayerRecordCard.setText(data.get(position)[1]);

        textSecondDeckRecordCard.setText(data.get(position)[0]);
        textSecondPlayerRecordCard.setText(data.get(position)[1]);

        textThirdDeckRecordCard.setText(data.get(position)[0]);
        textThirdPlayerRecordCard.setText(data.get(position)[1]);

        textFourthDeckRecordCard.setText(data.get(position)[0]);
        textFourthPlayerRecordCard.setText(data.get(position)[1]);

        return vi;
    }
}