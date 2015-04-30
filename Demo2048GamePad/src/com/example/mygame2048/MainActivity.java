package com.example.mygame2048;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.vszone.gamepad.GamePadManager;
import cn.vszone.gamepad.android.GamePadActiveActivity;
import cn.vszone.gamepad.android.MyGamePadActivity;
import cn.vszone.gamepad.vo.GamePad;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout        root              = null;
    private TextView            tvScore           = null;
    private TextView            tvBestScore       = null;
    private static MainActivity mainActivity      = null;
    private AnimLayer           animlayer         = null;
    private Button              btnRestartGame    = null;
    private GameView            gameView          = null;

    private int                 score             = 0;

    public static final String  SP_KEY_BEST_SCORE = "bestScore";

    public MainActivity() {
        mainActivity = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GamePadManager.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (LinearLayout) findViewById(R.id.container);
        root.setBackgroundColor(0xfffaf8ef);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);

        gameView = (GameView) findViewById(R.id.gameView);

        animlayer = (AnimLayer) findViewById(R.id.animlayer);

        btnRestartGame = (Button) findViewById(R.id.btnNewGame);
        btnRestartGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                gameView.startGame();
                GamePadManager.getInstance().setMode(GamePadManager.EVENT_MODE_GAME);
            }
        });
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public AnimLayer getAnimLayer() {
        return animlayer;
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();

        int maxScore = Math.max(score, getBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }

    public void saveBestScore(int s) {
        Editor e = getPreferences(MODE_PRIVATE).edit();
        e.putInt(SP_KEY_BEST_SCORE, s);
        e.commit();
    }

    public int getBestScore() {
        return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
    }

    public void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }

    boolean isSecondBack = false;

    @Override
    public void onBackPressed() {
        if (!isSecondBack) {
            isSecondBack = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    isSecondBack = false;
                }
            }, 1000);
        } else {
            super.onBackPressed();
        }
    }

    protected void swipeRight() {
        gameView.swipeRight();

    }

    protected void swipeLeft() {
        gameView.swipeLeft();

    }

    protected void swipeDown() {
        gameView.swipeDown();

    }

    protected void swipeUp() {
        gameView.swipeUp();

    }
    /* 
     * @see com.example.mygame2048.BaseActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        //在游戏模式下 当窗口可操作时 设备SDK模式为游戏模式,并注销事件监听
        GamePadManager.getInstance().setMode(GamePadManager.EVENT_MODE_GAME);
    }
    /*
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mygamepad) {
            startActivity(new Intent(getApplication(), MyGamePadActivity.class));
//            startActivity(new Intent(getApplication(), GamePadActiveActivity.class));
        }

    }
}
