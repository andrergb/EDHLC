package com.android.argb.edhlc;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.argb.edhlc.objects.Record;

import java.util.Calendar;

/**
 * -Created by agbarros on 25/02/2016.
 */
public class Utils {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void justifyListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null)
            return;

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }


    public static void toggleCardExpansion(Context context, final CardView card, TextView title, ImageView selector, int minHeight, int maxHeight) {
        if (card.getHeight() == minHeight) {
            expand(context, card, title, selector, maxHeight);
        } else {
            collapse(context, card, title, selector, minHeight);
        }
    }

    public static void collapse(Context context, final CardView card, TextView title, ImageView selector, int minHeight) {
        title.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));

        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate_180_clockwise);
        selector.startAnimation(rotation);
        selector.setColorFilter(ContextCompat.getColor(context, R.color.secondary_text));

        ValueAnimator anim = ValueAnimator.ofInt(card.getMeasuredHeightAndState(), minHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                layoutParams.height = val;
                card.setLayoutParams(layoutParams);
            }
        });
        anim.start();
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        String date = Constants.MONTH[c.get(Calendar.MONTH)]
                + " " + String.valueOf(c.get(Calendar.DAY_OF_MONTH))
                + ", " + String.valueOf(c.get(Calendar.YEAR));
        return date;
    }

    public static Bitmap getSquareBitmap(Bitmap sourceBitmap) {
        if (sourceBitmap.getWidth() >= sourceBitmap.getHeight()) {
            return Bitmap.createBitmap(sourceBitmap,
                    sourceBitmap.getWidth() / 2 - sourceBitmap.getHeight() / 2,
                    0,
                    sourceBitmap.getHeight(),
                    sourceBitmap.getHeight()
            );
        } else {
            return Bitmap.createBitmap(sourceBitmap,
                    0,
                    sourceBitmap.getHeight() / 2 - sourceBitmap.getWidth() / 2,
                    sourceBitmap.getWidth(),
                    sourceBitmap.getWidth()
            );
        }
    }

    public static void createRecordListElement(Activity parent, Record currentRecord, String highlightedPlayerName) {
        TextView textDateRecordCard = (TextView) parent.findViewById(R.id.textDateRecordCard);
        LinearLayout linearFirstLineRecordCard = (LinearLayout) parent.findViewById(R.id.linearFirstLineRecordCard);
        TextView textFirstIndicatorRecordCard = (TextView) parent.findViewById(R.id.textFirstIndicatorRecordCard);
        TextView textFirstPlayerRecordCard = (TextView) parent.findViewById(R.id.textFirstPlayerRecordCard);
        TextView textFirstDeckRecordCard = (TextView) parent.findViewById(R.id.textFirstDeckRecordCard);

        View divider1RecordCard = parent.findViewById(R.id.divider1RecordCard);

        LinearLayout linearSecondLineRecordCard = (LinearLayout) parent.findViewById(R.id.linearSecondLineRecordCard);
        TextView textSecondIndicatorRecordCard = (TextView) parent.findViewById(R.id.textSecondIndicatorRecordCard);
        TextView textSecondPlayerRecordCard = (TextView) parent.findViewById(R.id.textSecondPlayerRecordCard);
        TextView textSecondDeckRecordCard = (TextView) parent.findViewById(R.id.textSecondDeckRecordCard);

        View divider2RecordCard = parent.findViewById(R.id.divider2RecordCard);

        LinearLayout linearThirdLineRecordCard = (LinearLayout) parent.findViewById(R.id.linearThirdLineRecordCard);
        TextView textThirdIndicatorRecordCard = (TextView) parent.findViewById(R.id.textThirdIndicatorRecordCard);
        TextView textThirdPlayerRecordCard = (TextView) parent.findViewById(R.id.textThirdPlayerRecordCard);
        TextView textThirdDeckRecordCard = (TextView) parent.findViewById(R.id.textThirdDeckRecordCard);

        View divider3RecordCard = parent.findViewById(R.id.divider3RecordCard);

        LinearLayout linearFourthLineRecordCard = (LinearLayout) parent.findViewById(R.id.linearFourthLineRecordCard);
        TextView textFourthIndicatorRecordCard = (TextView) parent.findViewById(R.id.textFourthIndicatorRecordCard);
        TextView textFourthPlayerRecordCard = (TextView) parent.findViewById(R.id.textFourthPlayerRecordCard);
        TextView textFourthDeckRecordCard = (TextView) parent.findViewById(R.id.textFourthDeckRecordCard);

        Typeface typefaceMedium = Typeface.create("sans-serif-medium", Typeface.NORMAL);
        Typeface typefaceNormal = Typeface.create("sans-serif", Typeface.NORMAL);
        int colorAccent = ContextCompat.getColor(parent, R.color.accent_color);
        int colorSecondary = ContextCompat.getColor(parent, R.color.secondary_text);

        textDateRecordCard.setText("Played on " + currentRecord.getDate());

        switch (currentRecord.getTotalPlayers()) {
            case 2:
                linearFirstLineRecordCard.setVisibility(View.VISIBLE);
                linearSecondLineRecordCard.setVisibility(View.VISIBLE);
                linearThirdLineRecordCard.setVisibility(View.GONE);
                linearFourthLineRecordCard.setVisibility(View.GONE);

                divider1RecordCard.setVisibility(View.VISIBLE);
                divider2RecordCard.setVisibility(View.GONE);
                divider3RecordCard.setVisibility(View.GONE);

                textFirstDeckRecordCard.setText(currentRecord.getFirstPlace().getDeckName());
                textFirstPlayerRecordCard.setText(currentRecord.getFirstPlace().getDeckOwnerName());

                textSecondDeckRecordCard.setText(currentRecord.getSecondPlace().getDeckName());
                textSecondPlayerRecordCard.setText(currentRecord.getSecondPlace().getDeckOwnerName());

                if (highlightedPlayerName != null) {
                    textFirstDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                }
                break;

            case 3:
                linearFirstLineRecordCard.setVisibility(View.VISIBLE);
                linearSecondLineRecordCard.setVisibility(View.VISIBLE);
                linearThirdLineRecordCard.setVisibility(View.VISIBLE);
                linearFourthLineRecordCard.setVisibility(View.GONE);

                divider1RecordCard.setVisibility(View.VISIBLE);
                divider2RecordCard.setVisibility(View.VISIBLE);
                divider3RecordCard.setVisibility(View.GONE);

                textFirstDeckRecordCard.setText(currentRecord.getFirstPlace().getDeckName());
                textFirstPlayerRecordCard.setText(currentRecord.getFirstPlace().getDeckOwnerName());

                textSecondDeckRecordCard.setText(currentRecord.getSecondPlace().getDeckName());
                textSecondPlayerRecordCard.setText(currentRecord.getSecondPlace().getDeckOwnerName());

                textThirdDeckRecordCard.setText(currentRecord.getThirdPlace().getDeckName());
                textThirdPlayerRecordCard.setText(currentRecord.getThirdPlace().getDeckOwnerName());

                if (highlightedPlayerName != null) {
                    textFirstDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);


                    textSecondDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textThirdDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textThirdIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                }
                break;

            case 4:
                linearFirstLineRecordCard.setVisibility(View.VISIBLE);
                linearSecondLineRecordCard.setVisibility(View.VISIBLE);
                linearThirdLineRecordCard.setVisibility(View.VISIBLE);
                linearFourthLineRecordCard.setVisibility(View.VISIBLE);

                divider1RecordCard.setVisibility(View.VISIBLE);
                divider2RecordCard.setVisibility(View.VISIBLE);
                divider3RecordCard.setVisibility(View.VISIBLE);

                textFirstDeckRecordCard.setText(currentRecord.getFirstPlace().getDeckName());
                textFirstPlayerRecordCard.setText(currentRecord.getFirstPlace().getDeckOwnerName());

                textSecondDeckRecordCard.setText(currentRecord.getSecondPlace().getDeckName());
                textSecondPlayerRecordCard.setText(currentRecord.getSecondPlace().getDeckOwnerName());

                textThirdDeckRecordCard.setText(currentRecord.getThirdPlace().getDeckName());
                textThirdPlayerRecordCard.setText(currentRecord.getThirdPlace().getDeckOwnerName());

                textFourthDeckRecordCard.setText(currentRecord.getFourthPlace().getDeckName());
                textFourthPlayerRecordCard.setText(currentRecord.getFourthPlace().getDeckOwnerName());

                if (highlightedPlayerName != null) {
                    textFirstDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textThirdDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textThirdIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textFourthDeckRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFourthPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFourthPlayerRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFourthPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFourthIndicatorRecordCard.setTextColor(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFourthPlace().getDeckOwnerName()) ? colorAccent : colorSecondary);
                    textFourthIndicatorRecordCard.setTypeface(highlightedPlayerName.equalsIgnoreCase(currentRecord.getFourthPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                }
                break;
        }
    }

    public static void expand(Context context, final CardView card, TextView title, ImageView selector, int maxHeight) {
        title.setTextColor(ContextCompat.getColor(context, R.color.secondary_color));

        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate_180_anticlockwise);
        selector.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.arrow_up));
        selector.setRotation(0);
        selector.startAnimation(rotation);
        selector.setColorFilter(ContextCompat.getColor(context, R.color.secondary_color));

        ValueAnimator anim = ValueAnimator.ofInt(card.getMeasuredHeightAndState(), maxHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                layoutParams.height = val;
                card.setLayoutParams(layoutParams);
                Utils.makeViewVisible(card);
            }
        });
        anim.start();
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static void makeViewVisible(View view) {
        int viewTop = view.getTop();
        int viewBottom = view.getBottom();

        for (; ; ) {
            ViewParent viewParent = view.getParent();
            if (viewParent == null || !(viewParent instanceof ViewGroup))
                break;

            ViewGroup viewGroupParent = (ViewGroup) viewParent;
            if (viewGroupParent instanceof NestedScrollView) {

                NestedScrollView nestedScrollView = (NestedScrollView) viewGroupParent;
                int height = nestedScrollView.getHeight();
                int screenTop = nestedScrollView.getScrollY();
                int screenBottom = screenTop + height;
                int fadingEdge = nestedScrollView.getVerticalFadingEdgeLength();

                // leave room for top fading edge as long as rect isn't at very top
                if (viewTop > 0)
                    screenTop += fadingEdge;

                // leave room for bottom fading edge as long as rect isn't at very bottom
                if (viewBottom < nestedScrollView.getChildAt(0).getHeight())
                    screenBottom -= fadingEdge;

                int scrollYDelta = 0;

                if (viewBottom > screenBottom && viewTop > screenTop) {
                    // need to move down to get it in view: move down just enough so
                    // that the entire rectangle is in view (or at least the first
                    // screen size chunk).

                    if (viewBottom - viewTop > height) // just enough to get screen size chunk on
                        scrollYDelta += (viewTop - screenTop);
                    else              // get entire rect at bottom of screen
                        scrollYDelta += (viewBottom - screenBottom);

                    // make sure we aren't scrolling beyond the end of our content
                    int bottom = nestedScrollView.getChildAt(0).getBottom();
                    int distanceToBottom = bottom - screenBottom;
                    scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

                } else if (viewTop < screenTop && viewBottom < screenBottom) {
                    // need to move up to get it in view: move up just enough so that
                    // entire rectangle is in view (or at least the first screen
                    // size chunk of it).

                    if (viewBottom - viewTop > height)    // screen size chunk
                        scrollYDelta -= (screenBottom - viewBottom);
                    else                  // entire rect at top
                        scrollYDelta -= (screenTop - viewTop);

                    // make sure we aren't scrolling any further than the top our content
                    scrollYDelta = Math.max(scrollYDelta, -nestedScrollView.getScrollY());
                }
                nestedScrollView.smoothScrollBy(0, scrollYDelta);
                break;
            }
            // Transform coordinates to parent:
            int dy = viewGroupParent.getTop() - viewGroupParent.getScrollY();
            viewTop += dy;
            viewBottom += dy;

            view = viewGroupParent;
        }
    }
}
