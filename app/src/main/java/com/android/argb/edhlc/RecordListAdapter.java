package com.android.argb.edhlc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.objects.Record;

import java.util.List;

public class RecordListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private List<Record> data;
    private int total;


    public RecordListAdapter(Context context, List<Record> data, int total) {
        this.context = context;
        this.data = data;
        this.total = total;
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
        LinearLayout linearThirdLineRecordCard = (LinearLayout) vi.findViewById(R.id.linearThirdLineRecordCard);
        LinearLayout linearFourthLineRecordCard = (LinearLayout) vi.findViewById(R.id.linearFourthLineRecordCard);
        View divider2RecordCard = vi.findViewById(R.id.divider2RecordCard);
        View divider3RecordCard = vi.findViewById(R.id.divider3RecordCard);

        textDateRecordCard.setText("Played on " + data.get(position).getDate());

        textFirstDeckRecordCard.setText(data.get(position).getFirstPlace().getDeckName());
        textFirstPlayerRecordCard.setText(data.get(position).getFirstPlace().getDeckOwnerName());

        textSecondDeckRecordCard.setText(data.get(position).getSecondPlace().getDeckName());
        textSecondPlayerRecordCard.setText(data.get(position).getSecondPlace().getDeckOwnerName());

        switch (total) {
            case 2:
                linearThirdLineRecordCard.setVisibility(View.GONE);
                linearFourthLineRecordCard.setVisibility(View.GONE);
                divider2RecordCard.setVisibility(View.GONE);
                divider3RecordCard.setVisibility(View.GONE);
                break;
            case 3:
                textThirdDeckRecordCard.setText(data.get(position).getThirdPlace().getDeckName());
                textThirdPlayerRecordCard.setText(data.get(position).getThirdPlace().getDeckOwnerName());

                linearFourthLineRecordCard.setVisibility(View.GONE);
                divider3RecordCard.setVisibility(View.GONE);
                break;
            case 4:
                textThirdDeckRecordCard.setText(data.get(position).getThirdPlace().getDeckName());
                textThirdPlayerRecordCard.setText(data.get(position).getThirdPlace().getDeckOwnerName());
                textFourthDeckRecordCard.setText(data.get(position).getFourthPlace().getDeckName());
                textFourthPlayerRecordCard.setText(data.get(position).getFourthPlace().getDeckOwnerName());
                break;
        }
        return vi;
    }
}