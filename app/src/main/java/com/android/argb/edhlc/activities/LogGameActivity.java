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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;

public class LogGameActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;

    private LinearLayout firstLineParent;
    private LinearLayout firstLine;
    private CardView firstCard;
    private ImageView avatarFirstLogGame;
    private TextView playerFirstNameLogGame;
    private TextView playerFirstDeckLogGame;

    private LinearLayout secondLineParent;
    private LinearLayout secondLine;
    private CardView secondCard;
    private ImageView avatarSecondLogGame;
    private TextView playerSecondNameLogGame;
    private TextView playerSecondDeckLogGame;

    private LinearLayout thirdLineParent;
    private LinearLayout thirdLine;
    private CardView thirdCard;
    private ImageView avatarThirdLogGame;
    private TextView playerThirdNameLogGame;
    private TextView playerThirdDeckLogGame;

    private LinearLayout fourthLineParent;
    private LinearLayout fourthLine;
    private CardView fourthCard;
    private ImageView avatarFourthLogGame;
    private TextView playerFourthNameLogGame;
    private TextView playerFourthDeckLogGame;


    private ScrollView scrollView;
    private int totalPlayers;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivityNew.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_game);

        createStatusBar();
        createToolbar();
        createLayout();
        updateLayout();
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
        Intent intent = getIntent();
        totalPlayers = intent.getIntExtra("LOG_GAME_TOTAL_PLAYERS", 0);

        firstLine = (LinearLayout) findViewById(R.id.firstLine);
        firstLine.setOnDragListener(new MyDragListener());
        firstCard = (CardView) findViewById(R.id.firstCard);
        firstCard.setOnTouchListener(new MyTouchListener());
        avatarFirstLogGame = (ImageView) findViewById(R.id.avatarFirstLogGame);
        playerFirstNameLogGame = (TextView) findViewById(R.id.playerFirstNameLogGame);
        playerFirstDeckLogGame = (TextView) findViewById(R.id.playerFirstDeckLogGame);

        secondLine = (LinearLayout) findViewById(R.id.secondLine);
        secondLine.setOnDragListener(new MyDragListener());
        secondCard = (CardView) findViewById(R.id.secondCard);
        secondCard.setOnTouchListener(new MyTouchListener());
        avatarSecondLogGame = (ImageView) findViewById(R.id.avatarSecondLogGame);
        playerSecondNameLogGame = (TextView) findViewById(R.id.playerSecondNameLogGame);
        playerSecondDeckLogGame = (TextView) findViewById(R.id.playerSecondDeckLogGame);

        thirdLineParent = (LinearLayout) findViewById(R.id.thirdLineParent);
        if (totalPlayers >= 3) {
            thirdLine = (LinearLayout) findViewById(R.id.thirdLine);
            thirdLine.setOnDragListener(new MyDragListener());
            thirdCard = (CardView) findViewById(R.id.thirdCard);
            thirdCard.setOnTouchListener(new MyTouchListener());
            avatarThirdLogGame = (ImageView) findViewById(R.id.avatarThirdLogGame);
            playerThirdNameLogGame = (TextView) findViewById(R.id.playerThirdNameLogGame);
            playerThirdDeckLogGame = (TextView) findViewById(R.id.playerThirdDeckLogGame);
        } else {
            thirdLineParent.setVisibility(View.GONE);
        }

        fourthLineParent = (LinearLayout) findViewById(R.id.fourthLineParent);
        if (totalPlayers >= 4) {
            fourthLine = (LinearLayout) findViewById(R.id.fourthLine);
            fourthLine.setOnDragListener(new MyDragListener());
            fourthCard = (CardView) findViewById(R.id.fourthCard);
            fourthCard.setOnTouchListener(new MyTouchListener());
            avatarFourthLogGame = (ImageView) findViewById(R.id.avatarFourthLogGame);
            playerFourthNameLogGame = (TextView) findViewById(R.id.playerFourthNameLogGame);
            playerFourthDeckLogGame = (TextView) findViewById(R.id.playerFourthDeckLogGame);
        } else {
            fourthLineParent.setVisibility(View.GONE);
        }

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

    private void updateLayout() {
        Intent intent = getIntent();

        String p1Name = intent.getStringExtra("LOG_GAME_PLAYER_1");
        String p1Deck = intent.getStringExtra("LOG_GAME_DECK_1");
        playerFirstNameLogGame.setText(p1Name);
        playerFirstDeckLogGame.setText(p1Deck);
        avatarFirstLogGame.setImageDrawable(Utils.getRoundedImage(this, p1Name, p1Deck));

        String p2Name = intent.getStringExtra("LOG_GAME_PLAYER_2");
        String p2Deck = intent.getStringExtra("LOG_GAME_DECK_2");
        playerSecondNameLogGame.setText(p2Name);
        playerSecondDeckLogGame.setText(p2Deck);
        avatarSecondLogGame.setImageDrawable(Utils.getRoundedImage(this, p2Name, p2Deck));

        if (totalPlayers >= 3) {
            String p3Name = intent.getStringExtra("LOG_GAME_PLAYER_3");
            String p3Deck = intent.getStringExtra("LOG_GAME_DECK_3");
            playerThirdNameLogGame.setText(p3Name);
            playerThirdDeckLogGame.setText(p3Deck);
            avatarThirdLogGame.setImageDrawable(Utils.getRoundedImage(this, p3Name, p3Deck));
        }

        if (totalPlayers >= 4) {
            String p4Name = intent.getStringExtra("LOG_GAME_PLAYER_4");
            String p4Deck = intent.getStringExtra("LOG_GAME_DECK_4");
            playerFourthNameLogGame.setText(p4Name);
            playerFourthDeckLogGame.setText(p4Deck);
            avatarFourthLogGame.setImageDrawable(Utils.getRoundedImage(this, p4Name, p4Deck));
        }
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
