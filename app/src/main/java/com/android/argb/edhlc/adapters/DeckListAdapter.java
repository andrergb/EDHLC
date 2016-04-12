package com.android.argb.edhlc.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.RoundedAvatarDrawable;
import com.android.argb.edhlc.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeckListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<String[]> data; // 0 imagePath - 1 title - 2 subTitle - 3 identity - 4 selection
    private boolean isInEditMode = false;

    private CheckBox checkBox;

    public DeckListAdapter(Context context, List<String[]> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void checkBoxClearAllSelections() {
        for (int i = 0; i < data.size(); i++) {
            data.set(i, new String[]{data.get(i)[0], data.get(i)[1], data.get(i)[2], data.get(i)[3], "false"});
        }
    }

    public boolean checkBoxGetSelection(int position) {
        return getDataChecked().get(position).equalsIgnoreCase("true");
    }

    public void checkBoxSetSelection(int position, String mode) {
        data.set(position, new String[]{data.get(position)[0], data.get(position)[1], data.get(position)[2], data.get(position)[3], mode});
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public List<String> getDataChecked() {
        ArrayList<String> dataChecked = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
            dataChecked.add(data.get(i)[4]);

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
            if (data.get(i)[4].equalsIgnoreCase("true"))
                total++;
        return total;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_deck_list, null);

        //Line
        LinearLayout deckListLine = (LinearLayout) vi.findViewById(R.id.deckListLine);
        deckListLine.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        if (getDataChecked(position))
            deckListLine.setBackgroundColor(ContextCompat.getColor(context, R.color.gray300));

        //Avatar
        ImageView imageViewAvatarDeckListCard = (ImageView) vi.findViewById(R.id.imageViewAvatarDeckListCard);
        File croppedImageFile = new File(context.getFilesDir(), data.get(position)[0]);
        Bitmap bitmap;
        if (croppedImageFile.isFile())
            bitmap = BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath());
        else
            bitmap = BitmapFactory.decodeResource(vi.getResources(), R.drawable.avatar_holder);
        RoundedAvatarDrawable roundedImage = new RoundedAvatarDrawable(Utils.getSquareBitmap(bitmap));
        roundedImage.setAntiAlias(true);
        imageViewAvatarDeckListCard.setImageDrawable(roundedImage);

        //Avatar checkbox
        checkBox = (CheckBox) vi.findViewById(R.id.checkboxAvatarDeckListCard);
        checkBox.setVisibility(isInEditMode() ? View.VISIBLE : View.GONE);
        checkBox.setChecked(checkBoxGetSelection(position));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxSetSelection(position, isChecked ? "true" : "false");
            }
        });

        //Deck name
        TextView textViewDeckNameDeckListCard = (TextView) vi.findViewById(R.id.textViewDeckNameDeckListCard);
        textViewDeckNameDeckListCard.setText(data.get(position)[1]);

        //Deck description
        TextView textViewDeckDescriptionDeckListCard = (TextView) vi.findViewById(R.id.textViewDeckDescriptionDeckListCard);
        textViewDeckDescriptionDeckListCard.setText(data.get(position)[2]);

        //Deck identity
        List<ImageView> listIdentityHolder = new ArrayList<>();
        ImageView imageViewMana1 = (ImageView) vi.findViewById(R.id.imageViewMana1);
        ImageView imageViewMana2 = (ImageView) vi.findViewById(R.id.imageViewMana2);
        ImageView imageViewMana3 = (ImageView) vi.findViewById(R.id.imageViewMana3);
        ImageView imageViewMana4 = (ImageView) vi.findViewById(R.id.imageViewMana4);
        ImageView imageViewMana5 = (ImageView) vi.findViewById(R.id.imageViewMana5);
        ImageView imageViewMana6 = (ImageView) vi.findViewById(R.id.imageViewMana6);

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
        final String footerIdentity = data.get(position)[3];
        if (footerIdentity.charAt(0) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(vi.getContext(), R.drawable.mana_white));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(1) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(vi.getContext(), R.drawable.mana_blue));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(2) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(vi.getContext(), R.drawable.mana_black));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(3) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(vi.getContext(), R.drawable.mana_red));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(4) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(vi.getContext(), R.drawable.mana_green));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (footerIdentity.charAt(5) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(vi.getContext(), R.drawable.mana_colorless));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
        }

        return vi;
    }

    public boolean isInEditMode() {
        return isInEditMode;
    }

    public void setIsInEditMode(boolean isInEditMode) {
        this.isInEditMode = isInEditMode;
    }
}