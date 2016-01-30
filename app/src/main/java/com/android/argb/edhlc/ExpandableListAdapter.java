package com.android.argb.edhlc;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.activities.PlayerListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String[]> mListDataHeader;
    private HashMap<String, List<String[]>> mListDataChild;

    public ExpandableListAdapter(Context context, List<String[]> listDataHeader, HashMap<String, List<String[]>> listChildData) {
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
        final String[] textContent = (String[]) getChild(groupPosition, childPosition);
        final String footerText = textContent[0];
        final String footerIdentity = textContent[1];

        if (convertView == null) {
            LayoutInflater mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.expandable_list_footer, null);
        }

        //Child background
        LinearLayout main = (LinearLayout) convertView.findViewById(R.id.linearMainFooter);
        main.setBackgroundColor(convertView.getResources().getColor(R.color.gray300));

        //Child text: deck name
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(footerText);
        txtListChild.setFocusable(false);

        //Last child margin
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Resources r = mContext.getResources();
        int dp2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        int dp14 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
        int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        if (isLastChild)
            params.setMargins(dp14, dp2, dp14, dp16);
        else
            params.setMargins(dp14, dp2, dp14, dp2);
        CardView cardView = (CardView) convertView.findViewById(R.id.cardView);
        cardView.setLayoutParams(params);

        //Deck identity
        List<ImageView> listIdentityHolder = new ArrayList<>();
        ImageView imageViewMana1 = (ImageView) convertView.findViewById(R.id.imageViewMana1);
        ImageView imageViewMana2 = (ImageView) convertView.findViewById(R.id.imageViewMana2);
        ImageView imageViewMana3 = (ImageView) convertView.findViewById(R.id.imageViewMana3);
        ImageView imageViewMana4 = (ImageView) convertView.findViewById(R.id.imageViewMana4);
        ImageView imageViewMana5 = (ImageView) convertView.findViewById(R.id.imageViewMana5);
        ImageView imageViewMana6 = (ImageView) convertView.findViewById(R.id.imageViewMana6);

        imageViewMana1.setVisibility(View.GONE);
        imageViewMana2.setVisibility(View.GONE);
        imageViewMana3.setVisibility(View.GONE);
        imageViewMana4.setVisibility(View.GONE);
        imageViewMana5.setVisibility(View.GONE);
        imageViewMana6.setVisibility(View.GONE);

        listIdentityHolder.add(imageViewMana6);
        listIdentityHolder.add(imageViewMana5);
        listIdentityHolder.add(imageViewMana4);
        listIdentityHolder.add(imageViewMana3);
        listIdentityHolder.add(imageViewMana2);
        listIdentityHolder.add(imageViewMana1);

        int index = 0;
        if (footerIdentity.charAt(0) == '1') {
            listIdentityHolder.get(index).setBackground(convertView.getResources().getDrawable(R.drawable.mana_white));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(1) == '1') {
            listIdentityHolder.get(index).setBackground(convertView.getResources().getDrawable(R.drawable.mana_blue));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(2) == '1') {
            listIdentityHolder.get(index).setBackground(convertView.getResources().getDrawable(R.drawable.mana_black));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(3) == '1') {
            listIdentityHolder.get(index).setBackground(convertView.getResources().getDrawable(R.drawable.mana_red));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(4) == '1') {
            listIdentityHolder.get(index).setBackground(convertView.getResources().getDrawable(R.drawable.mana_green));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(5) == '1') {
            listIdentityHolder.get(index).setBackground(convertView.getResources().getDrawable(R.drawable.mana_colorless));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
        }

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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String[] textContent = (String[]) getGroup(groupPosition);
        final String headerText = textContent[0];
        final String headerSubTex = textContent[1];

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_header, null);
        }

        //Background
        LinearLayout main = (LinearLayout) convertView.findViewById(R.id.linearMainHeader);
        main.setBackgroundColor(isExpanded ? convertView.getResources().getColor(R.color.gray300) : convertView.getResources().getColor(android.R.color.transparent));

        //Expandable indicator
        ImageView expandableIndicator = (ImageView) convertView.findViewById(R.id.iconIndicator);
        expandableIndicator.setFocusable(false);
        expandableIndicator.setImageDrawable(isExpanded ? convertView.getResources().getDrawable(R.drawable.arrow_down) : convertView.getResources().getDrawable(R.drawable.arrow_right));
        expandableIndicator.setColorFilter(isExpanded ? convertView.getResources().getColor(android.R.color.black) : convertView.getResources().getColor(R.color.gray600));

        //Group 1st line: player name
        TextView textHeader = (TextView) convertView.findViewById(R.id.textHeader);
        textHeader.setText(headerText);
        textHeader.setTextColor(isExpanded ? convertView.getResources().getColor(android.R.color.black) : convertView.getResources().getColor(R.color.gray600));

        //Group 2nd line: player info
        TextView textHeaderSub = (TextView) convertView.findViewById(R.id.textHeaderSub);
        textHeaderSub.setText(headerSubTex);

        //Button more - linear to expand clickable area
        LinearLayout buttonMore1 = (LinearLayout) convertView.findViewById(R.id.buttonMore1);
        buttonMore1.setFocusable(false);
        buttonMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof PlayerListActivity) {
                    ((PlayerListActivity) mContext).onClickButtonMoreHeader(headerText, groupPosition);
                }
            }
        });

        //Button more - image
        ImageView buttonMore2 = (ImageView) convertView.findViewById(R.id.buttonMore2);
        buttonMore2.setFocusable(false);
        buttonMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof PlayerListActivity) {
                    ((PlayerListActivity) mContext).onClickButtonMoreHeader(headerText, groupPosition);
                }
            }
        });

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