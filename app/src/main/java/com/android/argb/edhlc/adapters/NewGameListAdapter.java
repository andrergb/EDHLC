package com.android.argb.edhlc.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.argb.edhlc.R;

import java.util.List;

public class NewGameListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private List<String[]> data; // 0 type - 1 text - 2 checkbox selection

    private CheckBox checkBox;

    public NewGameListAdapter(Context context, List<String[]> data) {
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

        if (isPlayer(position)) {
            vi = inflater.inflate(R.layout.row_new_game_player, null);

            TextView textViewPlayerNewGame = (TextView) vi.findViewById(R.id.textViewPlayerNewGame);
            textViewPlayerNewGame.setText(data.get(position)[1]);
            textViewPlayerNewGame.setTypeface(null, isSelected(position) ? Typeface.BOLD : Typeface.NORMAL);


        } else if (isDeck(position)) {
            vi = inflater.inflate(R.layout.row_new_game_deck, null);

            TextView textViewDeckNewGame = (TextView) vi.findViewById(R.id.textViewDeckNewGame);
            textViewDeckNewGame.setText(data.get(position)[1]);
            textViewDeckNewGame.setTypeface(null, isSelected(position) ? Typeface.BOLD : Typeface.NORMAL);

            CheckBox checkBox = (CheckBox) vi.findViewById(R.id.checkboxDeckNewGame);
            checkBox.setChecked(data.get(position)[2].equalsIgnoreCase("TRUE"));
        }

        return vi;
    }

    public boolean isDeck(int position) {
        return data.get(position)[0].equalsIgnoreCase("DECK");
    }

    public boolean isPlayer(int position) {
        return data.get(position)[0].equalsIgnoreCase("PLAYER");
    }

    public boolean isSelected(int position) {
        return data.get(position)[2].equalsIgnoreCase("TRUE");
    }
}