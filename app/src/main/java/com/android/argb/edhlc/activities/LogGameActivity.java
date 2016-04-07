package com.android.argb.edhlc.activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;

public class LogGameActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;
    private CardView firstCard;
    private CardView secondCard;
    private CardView thirdCard;
    private CardView fourthCard;
    private LinearLayout firstLine;
    private LinearLayout secondLine;
    private LinearLayout thirdLine;
    private LinearLayout fourthLine;
    private int mScrollDistance;
    private ScrollView scrollView;

    @Override
    public void onBackPressed() {
        Intent intentHome = new Intent(this, MainActivityNew.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
        this.finish();

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenu = menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_game);

        createStatusBar();
        createToolbar();
        createLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createLayout() {
        firstCard = (CardView) findViewById(R.id.firstCard);
        secondCard = (CardView) findViewById(R.id.secondCard);
        thirdCard = (CardView) findViewById(R.id.thirdCard);
        fourthCard = (CardView) findViewById(R.id.fourthCard);

        firstLine = (LinearLayout) findViewById(R.id.firstLine);
        secondLine = (LinearLayout) findViewById(R.id.secondLine);
        thirdLine = (LinearLayout) findViewById(R.id.thirdLine);
        fourthLine = (LinearLayout) findViewById(R.id.fourthLine);

        firstCard.setOnTouchListener(new MyTouchListener());
        secondCard.setOnTouchListener(new MyTouchListener());
        thirdCard.setOnTouchListener(new MyTouchListener());
        fourthCard.setOnTouchListener(new MyTouchListener());

        firstLine.setOnDragListener(new MyDragListener());
        secondLine.setOnDragListener(new MyDragListener());
        thirdLine.setOnDragListener(new MyDragListener());
        fourthLine.setOnDragListener(new MyDragListener());

        scrollView = (ScrollView) findViewById(R.id.scrollLogGame);
    }

    private void createStatusBar() {
        statusBarBackground = findViewById(R.id.statusBarBackground);
        ViewGroup.LayoutParams params = statusBarBackground.getLayoutParams();
        params.height = Utils.getStatusBarHeight(this);
        statusBarBackground.setLayoutParams(params);
        statusBarBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black_transparent));
        }
    }

    private void createToolbar() {
        assert getSupportActionBar() != null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)));
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setTitle("Log Game");
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View viewTarget, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    viewTarget.setBackgroundResource(R.drawable.shape_droptarget);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    viewTarget.setBackgroundResource(R.drawable.shape);
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    Display display = getWindowManager().getDefaultDisplay();
                    Point displaySize = new Point();
                    display.getSize(displaySize);
                    int displayMaxY = displaySize.y;

                    Rect rect = new Rect();
                    viewTarget.getGlobalVisibleRect(rect);
                    Point touchPosition = new Point(rect.left + Math.round(event.getX()), rect.top + Math.round(event.getY()));

                    int scrollTopThreshold = displayMaxY / 5 + mActionBar.getHeight() + statusBarBackground.getHeight();
                    int scrollBottomThreshold = displayMaxY - (displayMaxY / 5);

                    if (touchPosition.y < scrollTopThreshold)
                        scrollView.smoothScrollBy(0, -15);
                    if (touchPosition.y > scrollBottomThreshold)
                        scrollView.smoothScrollBy(0, 15);
                    break;

                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    View currentView = ((LinearLayout) viewTarget).getChildAt(0);

                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    LinearLayout container = (LinearLayout) viewTarget;

                    owner.removeView(draggedView);
                    container.addView(draggedView);

                    container.removeView(currentView);
                    owner.addView(currentView);

                    draggedView.setVisibility(View.VISIBLE);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    viewTarget.setBackgroundResource(R.drawable.shape);
                    View draggedView2 = (View) event.getLocalState();
                    draggedView2.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
            return true;
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }
}
