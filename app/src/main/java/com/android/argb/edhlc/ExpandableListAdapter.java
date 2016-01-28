package com.android.argb.edhlc;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.argb.edhlc.activities.PlayerListActivity;

import java.util.HashMap;
import java.util.List;

//http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String[]> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;

    public ExpandableListAdapter(Context context, List<String[]> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)[0]).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.expandable_list_footer, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)[0]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //Header
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String[] textContent = (String[]) getGroup(groupPosition);
        final String headerText = textContent[0];
        final String headerSubTex = textContent[1];

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_header, null);
        }

        TextView textHeader = (TextView) convertView.findViewById(R.id.textHeader);
        textHeader.setText(headerText);

        TextView textHeaderSub = (TextView) convertView.findViewById(R.id.textHeaderSub);
        textHeaderSub.setText(headerSubTex);

        ImageButton buttonMore = (ImageButton) convertView.findViewById(R.id.buttonMore);
        buttonMore.setFocusable(false);
        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof PlayerListActivity) {
                    ((PlayerListActivity) mContext).onClickButtonMoreHeader(headerText);
                }
            }
        });

        Button iconAvatar = (Button) convertView.findViewById(R.id.iconAvatar);
        iconAvatar.setFocusable(false);

        CardView mCardView = (CardView) convertView.findViewById(R.id.cardView);

        switch (groupPosition % 6) {
            case 0:
                iconAvatar.setBackground(convertView.getResources().getDrawable(R.drawable.mana_black));
                mCardView.setBackgroundColor(isExpanded ? convertView.getResources().getColor(R.color.edh_mana_black) : convertView.getResources().getColor(android.R.color.background_light));
                break;
            case 1:
                iconAvatar.setBackground(convertView.getResources().getDrawable(R.drawable.mana_blue));
                mCardView.setBackgroundColor(isExpanded ? convertView.getResources().getColor(R.color.edh_mana_blue) : convertView.getResources().getColor(android.R.color.background_light));
                break;
            case 2:
                iconAvatar.setBackground(convertView.getResources().getDrawable(R.drawable.mana_colorless));
                mCardView.setBackgroundColor(isExpanded ? convertView.getResources().getColor(R.color.edh_mana_colorless) : convertView.getResources().getColor(android.R.color.background_light));
                break;
            case 3:
                iconAvatar.setBackground(convertView.getResources().getDrawable(R.drawable.mana_green));
                mCardView.setBackgroundColor(isExpanded ? convertView.getResources().getColor(R.color.edh_mana_green) : convertView.getResources().getColor(android.R.color.background_light));
                break;
            case 4:
                iconAvatar.setBackground(convertView.getResources().getDrawable(R.drawable.mana_red));
                mCardView.setBackgroundColor(isExpanded ? convertView.getResources().getColor(R.color.edh_mana_red) : convertView.getResources().getColor(android.R.color.background_light));
                break;
            case 5:
                iconAvatar.setBackground(convertView.getResources().getDrawable(R.drawable.mana_white));
                mCardView.setBackgroundColor(isExpanded ? convertView.getResources().getColor(R.color.edh_mana_white) : convertView.getResources().getColor(android.R.color.background_light));
                break;
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}