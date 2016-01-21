/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.argb.edhlc.listviewdragginganimation;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.R;

import java.util.HashMap;
import java.util.List;

public class StableArrayAdapter extends ArrayAdapter<String> {

    final int INVALID_ID = -1;

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    public StableArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        LinearLayout linearLayoutRowTwoItemListBorder = (LinearLayout) view.findViewById(R.id.LinearLayoutRowTwoItemListBorder);
        LinearLayout linearLayoutRowTwoItemListBorderEmpty = (LinearLayout) view.findViewById(R.id.LinearLayoutRowTwoItemListBorderEmpty);

        if (!getItem(position).equalsIgnoreCase(parent.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parent.getResources().getString(R.string.default_deck_1))
                && !getItem(position).equalsIgnoreCase(parent.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parent.getResources().getString(R.string.default_deck_2))
                && !getItem(position).equalsIgnoreCase(parent.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parent.getResources().getString(R.string.default_deck_3))
                && !getItem(position).equalsIgnoreCase(parent.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parent.getResources().getString(R.string.default_deck_4))) {

            linearLayoutRowTwoItemListBorder.setVisibility(View.VISIBLE);
            linearLayoutRowTwoItemListBorderEmpty.setVisibility(View.GONE);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            TextView text2 = (TextView) view.findViewById(R.id.text2);

            text1.setText(getItem(position).split(System.getProperty("line.separator"))[0]);
            text2.setText(getItem(position).split(System.getProperty("line.separator"))[1]);
        } else {
            linearLayoutRowTwoItemListBorder.setVisibility(View.GONE);
            linearLayoutRowTwoItemListBorderEmpty.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

}
