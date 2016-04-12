package com.android.argb.edhlc.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<String[]> data; // 0 title - 1 subTitle - 2 selection
    private boolean isInEditMode = false;

    private CheckBox checkBox;

    public PlayerListAdapter(Context context, List<String[]> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void checkBoxClearAllSelections() {
        for (int i = 0; i < data.size(); i++) {
            data.set(i, new String[]{data.get(i)[0], data.get(i)[1], "false"});
        }
    }

    public boolean checkBoxGetSelection(int position) {
        return getDataChecked().get(position).equalsIgnoreCase("true");
    }

    public void checkBoxSetSelection(int position, String mode) {
        data.set(position, new String[]{data.get(position)[0], data.get(position)[1], mode});
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public List<String> getDataChecked() {
        ArrayList<String> dataChecked = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
            dataChecked.add(data.get(i)[2]);

        return dataChecked;
    }

    public boolean getDataChecked(int pos) {
        return getDataChecked().get(pos).equalsIgnoreCase("true");
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getTotalDataChecked() {
        int total = 0;
        for (int i = 0; i < data.size(); i++)
            if (data.get(i)[2].equalsIgnoreCase("true"))
                total++;
        return total;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_player_list, null);

        //Line
        LinearLayout deckListLine = (LinearLayout) vi.findViewById(R.id.playerListLine);
        deckListLine.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        if (getDataChecked(position))
            deckListLine.setBackgroundColor(ContextCompat.getColor(context, R.color.gray300));

        //Checkbox
        checkBox = (CheckBox) vi.findViewById(R.id.checkboxPlayerList);
        checkBox.setVisibility(isInEditMode() ? View.VISIBLE : View.GONE);
        checkBox.setChecked(checkBoxGetSelection(position));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxSetSelection(position, isChecked ? "true" : "false");
            }
        });

        //Player name
        TextView textViewDeckNameDeckListCard = (TextView) vi.findViewById(R.id.textViewPlayerNamePlayerList);
        textViewDeckNameDeckListCard.setText(data.get(position)[0]);

        //Deck description
        TextView textViewDeckDescriptionDeckListCard = (TextView) vi.findViewById(R.id.textViewPlayerDescriptionPlayerList);
        textViewDeckDescriptionDeckListCard.setText(data.get(position)[1]);

        return vi;
    }

    public boolean isInEditMode() {
        return isInEditMode;
    }

    public void setIsInEditMode(boolean isInEditMode) {
        this.isInEditMode = isInEditMode;
    }
}