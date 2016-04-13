package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
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

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.objects.ActivePlayer;

/* Created by ARGB */
public class NewGame2Activity extends AppCompatActivity {

    private ActionBar mActionBar;
    private View statusBarBackground;

    private String p1Name;
    private String p1Deck;
    private LinearLayout firstLine;
    private ImageView avatarFirstNewGame2;
    private TextView playerFirstNameNewGame2;
    private TextView playerFirstDeckNewGame2;

    private String p2Name;
    private String p2Deck;
    private LinearLayout secondLine;
    private ImageView avatarSecondNewGame2;
    private TextView playerSecondNameNewGame2;
    private TextView playerSecondDeckNewGame2;

    private String p3Name;
    private String p3Deck;
    private LinearLayout thirdLine;
    private ImageView avatarThirdNewGame2;
    private TextView playerThirdNameNewGame2;
    private TextView playerThirdDeckNewGame2;

    private String p4Name;
    private String p4Deck;
    private LinearLayout fourthLine;
    private ImageView avatarFourthNewGame2;
    private TextView playerFourthNameNewGame2;
    private TextView playerFourthDeckNewGame2;

    private ScrollView scrollView;
    private int totalPlayers;
    private boolean isDragging;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NewGameActivity.class);
        intent.putExtra("NEW_GAME_IS_VALID", true);

        intent.putExtra("NEW_GAME_TOTAL_PLAYER", totalPlayers);

        intent.putExtra("NEW_GAME_PLAYER_1", p1Name);
        intent.putExtra("NEW_GAME_DECK_1", p1Deck);

        intent.putExtra("NEW_GAME_PLAYER_2", p2Name);
        intent.putExtra("NEW_GAME_DECK_2", p2Deck);

        if (totalPlayers >= 3) {
            intent.putExtra("NEW_GAME_PLAYER_3", p3Name);
            intent.putExtra("NEW_GAME_DECK_3", p3Deck);
        }
        if (totalPlayers >= 4) {
            intent.putExtra("NEW_GAME_PLAYER_4", p4Name);
            intent.putExtra("NEW_GAME_DECK_4", p4Deck);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        this.finish();

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_game2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NewGameActivity.class);
                intent.putExtra("NEW_GAME_IS_VALID", true);

                intent.putExtra("NEW_GAME_TOTAL_PLAYER", totalPlayers);

                intent.putExtra("NEW_GAME_PLAYER_1", p1Name);
                intent.putExtra("NEW_GAME_DECK_1", p1Deck);

                intent.putExtra("NEW_GAME_PLAYER_2", p2Name);
                intent.putExtra("NEW_GAME_DECK_2", p2Deck);

                if (totalPlayers >= 3) {
                    intent.putExtra("NEW_GAME_PLAYER_3", p3Name);
                    intent.putExtra("NEW_GAME_DECK_3", p3Deck);
                }
                if (totalPlayers >= 4) {
                    intent.putExtra("NEW_GAME_PLAYER_4", p4Name);
                    intent.putExtra("NEW_GAME_DECK_4", p4Deck);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                this.finish();

                return true;

            case R.id.actions_start_game:
                DecksDataAccessObject decksDb = new DecksDataAccessObject(this);
                decksDb.open();

                String firstName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) firstLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                String firstDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) firstLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
                Utils.savePlayerInSharedPreferences(this, new ActivePlayer(decksDb.getDeck(firstName, firstDeck), true, 40, 0, 0, 0, 0, 0));

                String secondName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) secondLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                String secondDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) secondLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
                Utils.savePlayerInSharedPreferences(this, new ActivePlayer(decksDb.getDeck(secondName, secondDeck), true, 40, 0, 0, 0, 0, 1));

                if (totalPlayers >= 3) {
                    String thirdName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) thirdLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                    String thirdDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) thirdLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
                    Utils.savePlayerInSharedPreferences(this, new ActivePlayer(decksDb.getDeck(thirdName, thirdDeck), true, 40, 0, 0, 0, 0, 2));
                }

                if (totalPlayers >= 4) {
                    String fourthName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) fourthLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                    String fourthDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) fourthLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
                    Utils.savePlayerInSharedPreferences(this, new ActivePlayer(decksDb.getDeck(fourthName, fourthDeck), true, 40, 0, 0, 0, 0, 3));
                }

                getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit().putInt(Constants.CURRENT_GAME_TOTAL_PLAYERS, totalPlayers).apply();
                getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit().putInt(Constants.CURRENT_VIEW_TAB, 0).apply();

                Utils.resetHistoryLife(this);

                decksDb.close();

                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                this.finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game2);

        createStatusBar();
        createToolbar();
        createLayout();
        createPositionIndicators();
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
        totalPlayers = intent.getIntExtra("NEW_GAME2_TOTAL_PLAYERS", 0);

        firstLine = (LinearLayout) findViewById(R.id.firstLine);
        firstLine.setOnDragListener(new MyDragListener());
        CardView firstCard = (CardView) findViewById(R.id.firstCard);
        if (firstCard != null) {
            firstCard.setOnTouchListener(new MyTouchListener());
        }
        avatarFirstNewGame2 = (ImageView) findViewById(R.id.avatarFirstNewGame2);
        playerFirstNameNewGame2 = (TextView) findViewById(R.id.playerFirstNameNewGame2);
        playerFirstDeckNewGame2 = (TextView) findViewById(R.id.playerFirstDeckNewGame2);

        secondLine = (LinearLayout) findViewById(R.id.secondLine);
        secondLine.setOnDragListener(new MyDragListener());
        CardView secondCard = (CardView) findViewById(R.id.secondCard);
        if (secondCard != null) {
            secondCard.setOnTouchListener(new MyTouchListener());
        }
        avatarSecondNewGame2 = (ImageView) findViewById(R.id.avatarSecondNewGame2);
        playerSecondNameNewGame2 = (TextView) findViewById(R.id.playerSecondNameNewGame2);
        playerSecondDeckNewGame2 = (TextView) findViewById(R.id.playerSecondDeckNewGame2);

        LinearLayout thirdLineParent = (LinearLayout) findViewById(R.id.thirdLineParent);
        if (totalPlayers >= 3) {
            thirdLine = (LinearLayout) findViewById(R.id.thirdLine);
            thirdLine.setOnDragListener(new MyDragListener());
            CardView thirdCard = (CardView) findViewById(R.id.thirdCard);
            if (thirdCard != null) {
                thirdCard.setOnTouchListener(new MyTouchListener());
            }
            avatarThirdNewGame2 = (ImageView) findViewById(R.id.avatarThirdNewGame2);
            playerThirdNameNewGame2 = (TextView) findViewById(R.id.playerThirdNameNewGame2);
            playerThirdDeckNewGame2 = (TextView) findViewById(R.id.playerThirdDeckNewGame2);
        } else {
            if (thirdLineParent != null) {
                thirdLineParent.setVisibility(View.GONE);
            }
        }

        LinearLayout fourthLineParent = (LinearLayout) findViewById(R.id.fourthLineParent);
        if (totalPlayers >= 4) {
            fourthLine = (LinearLayout) findViewById(R.id.fourthLine);
            fourthLine.setOnDragListener(new MyDragListener());
            CardView fourthCard = (CardView) findViewById(R.id.fourthCard);
            if (fourthCard != null) {
                fourthCard.setOnTouchListener(new MyTouchListener());
            }
            avatarFourthNewGame2 = (ImageView) findViewById(R.id.avatarFourthNewGame2);
            playerFourthNameNewGame2 = (TextView) findViewById(R.id.playerFourthNameNewGame2);
            playerFourthDeckNewGame2 = (TextView) findViewById(R.id.playerFourthDeckNewGame2);
        } else {
            if (fourthLineParent != null) {
                fourthLineParent.setVisibility(View.GONE);
            }
        }

        scrollView = (ScrollView) findViewById(R.id.scrollNewGame2);
    }

    private void createPositionIndicators() {
        View indicator111 = findViewById(R.id.indicator111);
        View indicator112 = findViewById(R.id.indicator112);
        View indicator113 = findViewById(R.id.indicator113);
        View indicator121 = findViewById(R.id.indicator121);
        View indicator122 = findViewById(R.id.indicator122);
        View indicator123 = findViewById(R.id.indicator123);

        View indicator211 = findViewById(R.id.indicator211);
        View indicator212 = findViewById(R.id.indicator212);
        View indicator213 = findViewById(R.id.indicator213);
        View indicator221 = findViewById(R.id.indicator221);
        View indicator222 = findViewById(R.id.indicator222);
        View indicator223 = findViewById(R.id.indicator223);


        View indicator311 = findViewById(R.id.indicator311);
        View indicator312 = findViewById(R.id.indicator312);
        View indicator313 = findViewById(R.id.indicator313);
        View indicator321 = findViewById(R.id.indicator321);
        View indicator322 = findViewById(R.id.indicator322);
        View indicator323 = findViewById(R.id.indicator323);


        View indicator411 = findViewById(R.id.indicator411);
        View indicator412 = findViewById(R.id.indicator412);
        View indicator413 = findViewById(R.id.indicator413);
        View indicator421 = findViewById(R.id.indicator421);
        //View indicator422 = findViewById(R.id.indicator422);
        View indicator423 = findViewById(R.id.indicator423);

        assert indicator111 != null;
        assert indicator112 != null;
        assert indicator113 != null;
        assert indicator121 != null;
        assert indicator122 != null;
        assert indicator123 != null;

        assert indicator211 != null;
        assert indicator212 != null;
        assert indicator213 != null;
        assert indicator221 != null;
        assert indicator222 != null;
        assert indicator223 != null;

        assert indicator311 != null;
        assert indicator312 != null;
        assert indicator313 != null;
        assert indicator321 != null;
        assert indicator322 != null;
        assert indicator323 != null;

        assert indicator411 != null;
        assert indicator412 != null;
        assert indicator413 != null;
        assert indicator421 != null;
        assert indicator423 != null;

        if (totalPlayers == 2) {
            indicator112.setVisibility(View.VISIBLE);
            indicator112.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            indicator122.setVisibility(View.VISIBLE);
            indicator122.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);

            indicator212.setVisibility(View.VISIBLE);
            indicator212.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator222.setVisibility(View.VISIBLE);
            indicator222.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);

        } else if (totalPlayers == 3) {
            indicator111.setVisibility(View.VISIBLE);
            indicator111.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            indicator113.setVisibility(View.VISIBLE);
            indicator113.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator122.setVisibility(View.VISIBLE);
            indicator122.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);

            indicator211.setVisibility(View.VISIBLE);
            indicator211.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator213.setVisibility(View.VISIBLE);
            indicator213.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            indicator222.setVisibility(View.VISIBLE);
            indicator222.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);

            indicator311.setVisibility(View.VISIBLE);
            indicator311.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator313.setVisibility(View.VISIBLE);
            indicator313.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator322.setVisibility(View.VISIBLE);
            indicator322.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);

        } else if (totalPlayers == 4) {
            indicator111.setVisibility(View.VISIBLE);
            indicator111.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            indicator113.setVisibility(View.VISIBLE);
            indicator113.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator121.setVisibility(View.VISIBLE);
            indicator121.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator123.setVisibility(View.VISIBLE);
            indicator123.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);

            indicator211.setVisibility(View.VISIBLE);
            indicator211.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator213.setVisibility(View.VISIBLE);
            indicator213.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            indicator221.setVisibility(View.VISIBLE);
            indicator221.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator223.setVisibility(View.VISIBLE);
            indicator223.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);

            indicator311.setVisibility(View.VISIBLE);
            indicator311.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator313.setVisibility(View.VISIBLE);
            indicator313.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator321.setVisibility(View.VISIBLE);
            indicator321.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            indicator323.setVisibility(View.VISIBLE);
            indicator323.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);

            indicator411.setVisibility(View.VISIBLE);
            indicator411.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator413.setVisibility(View.VISIBLE);
            indicator413.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator421.setVisibility(View.VISIBLE);
            indicator421.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray300), PorterDuff.Mode.SRC_IN);
            indicator423.setVisibility(View.VISIBLE);
            indicator423.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
        }
    }

    private void createStatusBar() {
        statusBarBackground = findViewById(R.id.statusBarBackground);
        if (statusBarBackground != null) {
            ViewGroup.LayoutParams params = statusBarBackground.getLayoutParams();
            params.height = Utils.getStatusBarHeight(this);
            statusBarBackground.setLayoutParams(params);
            statusBarBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color));
        }

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
        mActionBar.setTitle("New Game");
    }

    private void updateLayout() {
        Intent intent = getIntent();

        p1Name = intent.getStringExtra("NEW_GAME2_PLAYER_1");
        p1Deck = intent.getStringExtra("NEW_GAME2_DECK_1");
        playerFirstNameNewGame2.setText(p1Name);
        playerFirstDeckNewGame2.setText(p1Deck);
        avatarFirstNewGame2.setImageDrawable(Utils.getRoundedImage(this, p1Name, p1Deck));

        p2Name = intent.getStringExtra("NEW_GAME2_PLAYER_2");
        p2Deck = intent.getStringExtra("NEW_GAME2_DECK_2");
        playerSecondNameNewGame2.setText(p2Name);
        playerSecondDeckNewGame2.setText(p2Deck);
        avatarSecondNewGame2.setImageDrawable(Utils.getRoundedImage(this, p2Name, p2Deck));

        if (totalPlayers >= 3) {
            p3Name = intent.getStringExtra("NEW_GAME2_PLAYER_3");
            p3Deck = intent.getStringExtra("NEW_GAME2_DECK_3");
            playerThirdNameNewGame2.setText(p3Name);
            playerThirdDeckNewGame2.setText(p3Deck);
            avatarThirdNewGame2.setImageDrawable(Utils.getRoundedImage(this, p3Name, p3Deck));
        }

        if (totalPlayers >= 4) {
            p4Name = intent.getStringExtra("NEW_GAME2_PLAYER_4");
            p4Deck = intent.getStringExtra("NEW_GAME2_DECK_4");
            playerFourthNameNewGame2.setText(p4Name);
            playerFourthDeckNewGame2.setText(p4Deck);
            avatarFourthNewGame2.setImageDrawable(Utils.getRoundedImage(this, p4Name, p4Deck));
        }

        isDragging = false;
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

                    if (draggedView != null) {
                        ViewGroup owner = (ViewGroup) draggedView.getParent();
                        LinearLayout container = (LinearLayout) viewTarget;

                        owner.removeView(draggedView);
                        container.addView(draggedView);

                        container.removeView(currentView);
                        owner.addView(currentView);

                        draggedView.setVisibility(View.VISIBLE);
                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    isDragging = false;
                    viewTarget.setBackgroundResource(R.drawable.shape);
                    View draggedView2 = (View) event.getLocalState();
                    if (draggedView2 != null)
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
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !isDragging) {
                isDragging = true;
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
