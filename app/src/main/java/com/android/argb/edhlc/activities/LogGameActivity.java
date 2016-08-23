package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
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

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;

import java.util.ArrayList;
import java.util.List;

/* Created by ARGB */
public class LogGameActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private View statusBarBackground;

    private LinearLayout firstLine;
    private ImageView avatarFirstLogGame;
    private TextView playerFirstNameLogGame;
    private TextView playerFirstDeckLogGame;

    private LinearLayout secondLine;
    private ImageView avatarSecondLogGame;
    private TextView playerSecondNameLogGame;
    private TextView playerSecondDeckLogGame;

    private LinearLayout thirdLine;
    private ImageView avatarThirdLogGame;
    private TextView playerThirdNameLogGame;
    private TextView playerThirdDeckLogGame;

    private LinearLayout fourthLine;
    private ImageView avatarFourthLogGame;
    private TextView playerFourthNameLogGame;
    private TextView playerFourthDeckLogGame;


    private ScrollView scrollView;
    private int totalPlayers;
    private boolean isDragging;

    @Override
    public void onBackPressed() {
        Intent intentHome = new Intent(this, MainActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
        this.finish();

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.actions_log_game:
                showConfirmDialog();
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
        CardView firstCard = (CardView) findViewById(R.id.firstCard);
        if (firstCard != null) {
            firstCard.setOnTouchListener(new MyTouchListener());
        }
        avatarFirstLogGame = (ImageView) findViewById(R.id.avatarFirstLogGame);
        playerFirstNameLogGame = (TextView) findViewById(R.id.playerFirstNameLogGame);
        playerFirstDeckLogGame = (TextView) findViewById(R.id.playerFirstDeckLogGame);

        secondLine = (LinearLayout) findViewById(R.id.secondLine);
        secondLine.setOnDragListener(new MyDragListener());
        CardView secondCard = (CardView) findViewById(R.id.secondCard);
        if (secondCard != null) {
            secondCard.setOnTouchListener(new MyTouchListener());
        }
        avatarSecondLogGame = (ImageView) findViewById(R.id.avatarSecondLogGame);
        playerSecondNameLogGame = (TextView) findViewById(R.id.playerSecondNameLogGame);
        playerSecondDeckLogGame = (TextView) findViewById(R.id.playerSecondDeckLogGame);

        LinearLayout thirdLineParent = (LinearLayout) findViewById(R.id.thirdLineParent);
        if (totalPlayers >= 3) {
            thirdLine = (LinearLayout) findViewById(R.id.thirdLine);
            thirdLine.setOnDragListener(new MyDragListener());
            CardView thirdCard = (CardView) findViewById(R.id.thirdCard);
            if (thirdCard != null) {
                thirdCard.setOnTouchListener(new MyTouchListener());
            }
            avatarThirdLogGame = (ImageView) findViewById(R.id.avatarThirdLogGame);
            playerThirdNameLogGame = (TextView) findViewById(R.id.playerThirdNameLogGame);
            playerThirdDeckLogGame = (TextView) findViewById(R.id.playerThirdDeckLogGame);
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
            avatarFourthLogGame = (ImageView) findViewById(R.id.avatarFourthLogGame);
            playerFourthNameLogGame = (TextView) findViewById(R.id.playerFourthNameLogGame);
            playerFourthDeckLogGame = (TextView) findViewById(R.id.playerFourthDeckLogGame);
        } else {
            if (fourthLineParent != null) {
                fourthLineParent.setVisibility(View.GONE);
            }
        }

        scrollView = (ScrollView) findViewById(R.id.scrollLogGame);
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
        mActionBar.setTitle("Log Game");
    }

    private void showConfirmDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Log game");
        alertDialogBuilder.setMessage(getString(R.string.question_log_log));
        alertDialogBuilder.setPositiveButton("LOG",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RecordsDataAccessObject recordDB = new RecordsDataAccessObject(LogGameActivity.this);
                        recordDB.open();

                        String firstName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) firstLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                        String firstDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) firstLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();

                        String secondName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) secondLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                        String secondDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) secondLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();

                        List<Deck> records = new ArrayList<>();
                        records.add(new Deck(firstName, firstDeck));
                        records.add(new Deck(secondName, secondDeck));
                        if (totalPlayers >= 3) {
                            String thirdName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) thirdLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                            String thirdDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) thirdLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
                            records.add(new Deck(thirdName, thirdDeck));
                        }
                        if (totalPlayers >= 4) {
                            String fourthName = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) fourthLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
                            String fourthDeck = ((TextView) ((LinearLayout) ((LinearLayout) ((CardView) fourthLine.getChildAt(0)).getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
                            records.add(new Deck(fourthName, fourthDeck));
                        }

                        recordDB.addRecord(new Record(records, Utils.getCurrentDate()));
                        recordDB.close();

                        if (getIntent().getBooleanExtra("LOG_GAME_GO_TO_NEW_GAME", false)) {
                            Intent intent = new Intent(LogGameActivity.this, NewGameActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("NEW_GAME_IS_VALID", true);
                            intent.putExtra("NEW_GAME_TOTAL_PLAYER", totalPlayers);

                            Intent intent2 = getIntent();
                            intent.putExtra("NEW_GAME_PLAYER_1", intent2.getStringExtra("LOG_GAME_PLAYER_1"));
                            intent.putExtra("NEW_GAME_DECK_1", intent2.getStringExtra("LOG_GAME_DECK_1"));
                            intent.putExtra("NEW_GAME_PLAYER_2", intent2.getStringExtra("LOG_GAME_PLAYER_2"));
                            intent.putExtra("NEW_GAME_DECK_2", intent2.getStringExtra("LOG_GAME_DECK_2"));
                            if (totalPlayers >= 3) {
                                intent.putExtra("NEW_GAME_PLAYER_3", intent2.getStringExtra("LOG_GAME_PLAYER_3"));
                                intent.putExtra("NEW_GAME_DECK_3", intent2.getStringExtra("LOG_GAME_DECK_3"));
                            }
                            if (totalPlayers >= 4) {
                                intent.putExtra("NEW_GAME_PLAYER_4", intent2.getStringExtra("LOG_GAME_PLAYER_4"));
                                intent.putExtra("NEW_GAME_DECK_4", intent2.getStringExtra("LOG_GAME_DECK_4"));
                            }
                            startActivity(intent);
                            LogGameActivity.this.finish();
                        } else {
                            Intent intent = new Intent(LogGameActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            LogGameActivity.this.finish();
                        }
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateLayout() {
        Intent intent = getIntent();

        DecksDataAccessObject decksDb = new DecksDataAccessObject(this);
        decksDb.open();

        String p1Name = intent.getStringExtra("LOG_GAME_PLAYER_1");
        String p1Deck = intent.getStringExtra("LOG_GAME_DECK_1");
        playerFirstNameLogGame.setText(p1Name);
        playerFirstDeckLogGame.setText(p1Deck);
        avatarFirstLogGame.setImageDrawable(Utils.getRoundedImage(this, p1Name, p1Deck));
        avatarFirstLogGame.setColorFilter(decksDb.getDeck(p1Name, p1Deck).getDeckShieldColor()[0], PorterDuff.Mode.DST_OVER);

        String p2Name = intent.getStringExtra("LOG_GAME_PLAYER_2");
        String p2Deck = intent.getStringExtra("LOG_GAME_DECK_2");
        playerSecondNameLogGame.setText(p2Name);
        playerSecondDeckLogGame.setText(p2Deck);
        avatarSecondLogGame.setImageDrawable(Utils.getRoundedImage(this, p2Name, p2Deck));
        avatarSecondLogGame.setColorFilter(decksDb.getDeck(p2Name, p2Deck).getDeckShieldColor()[0], PorterDuff.Mode.DST_OVER);

        if (totalPlayers >= 3) {
            String p3Name = intent.getStringExtra("LOG_GAME_PLAYER_3");
            String p3Deck = intent.getStringExtra("LOG_GAME_DECK_3");
            playerThirdNameLogGame.setText(p3Name);
            playerThirdDeckLogGame.setText(p3Deck);
            avatarThirdLogGame.setImageDrawable(Utils.getRoundedImage(this, p3Name, p3Deck));
            avatarThirdLogGame.setColorFilter(decksDb.getDeck(p3Name, p3Deck).getDeckShieldColor()[0], PorterDuff.Mode.DST_OVER);
        }

        if (totalPlayers >= 4) {
            String p4Name = intent.getStringExtra("LOG_GAME_PLAYER_4");
            String p4Deck = intent.getStringExtra("LOG_GAME_DECK_4");
            playerFourthNameLogGame.setText(p4Name);
            playerFourthDeckLogGame.setText(p4Deck);
            avatarFourthLogGame.setImageDrawable(Utils.getRoundedImage(this, p4Name, p4Deck));
            avatarFourthLogGame.setColorFilter(decksDb.getDeck(p4Name, p4Deck).getDeckShieldColor()[0], PorterDuff.Mode.DST_OVER);
        }

        decksDb.close();

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
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                isDragging = view.startDrag(data, shadowBuilder, view, 0);
                if (isDragging)
                    view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }
}
