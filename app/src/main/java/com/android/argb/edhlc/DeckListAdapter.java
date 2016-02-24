package com.android.argb.edhlc;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class DeckListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<String[]> data;

    public DeckListAdapter(Context context, List<String[]> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_deck_list, null);

        ImageView imageViewAvatarDeckListCard = (ImageView) vi.findViewById(R.id.imageViewAvatarDeckListCard);
        File croppedImageFile = new File(context.getFilesDir(), data.get(position)[0]);
        if (croppedImageFile.isFile())
            imageViewAvatarDeckListCard.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));

        TextView textViewDeckNameDeckListCard = (TextView) vi.findViewById(R.id.textViewDeckNameDeckListCard);
        textViewDeckNameDeckListCard.setText(data.get(position)[1]);

        TextView textViewDeckDescriptionDeckListCard = (TextView) vi.findViewById(R.id.textViewDeckDescriptionDeckListCard);
        textViewDeckDescriptionDeckListCard.setText(data.get(position)[2]);

        return vi;
    }
}