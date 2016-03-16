package com.android.argb.edhlc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class NewGameAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private List<String[]> data; // 0 type - 1 text - 2 checkbox selection

    private CheckBox checkBox;

    public NewGameAdapter(Context context, List<String[]> data) {
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
            if (vi == null) {
                vi = inflater.inflate(R.layout.row_new_game_player, null);
            }

            TextView textViewDeckDescriptionDeckListCard = (TextView) vi.findViewById(R.id.text1);
            textViewDeckDescriptionDeckListCard.setText(data.get(position)[1]);

        } else if (isDeck(position)) {
            if (vi == null) {
                vi = inflater.inflate(R.layout.row_new_game_deck, null);
            }

            TextView textViewDeckDescriptionDeckListCard = (TextView) vi.findViewById(R.id.textViewDeckNewGame);
            textViewDeckDescriptionDeckListCard.setText(data.get(position)[1]);

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