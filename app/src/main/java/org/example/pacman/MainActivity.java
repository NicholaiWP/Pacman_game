package org.example.pacman;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.example.pacman.Managers.ObjectManager;
import org.example.pacman.Managers.SoundManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public enum moveDirection{
        Right,
        Left,
        Down,
        UP,
        Zero
    }

    //reference to the main view
    private GameView gameView;

    //reference to the game class.
    private Game game;

    //sound manager reference
    private SoundManager soundManager;

    private TextView countDownTextView;

    //gameTimer
    private Timer gameTimer;
    //Count down timer
    private Timer countDownTimer;

    //is the game running?
    private boolean running = false;

    private boolean counterRunning = false;

    //pacMan swiping direction enum field
    private moveDirection moveDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);

        countDownTextView = findViewById(R.id.counter);

        game = new Game(this,textView, countDownTextView);
        game.setGameView(gameView);
        soundManager = new SoundManager(this);
        gameView.setGame(game);
        game.startGame();
        game.SwipeMove();

        soundManager.playMusic();
        soundManager.setMusicEnabled(true);

        gameTimer = new Timer();
        running = true; //should the game be running?
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 25);

        counterRunning = true;
        countDownTimer = new Timer();
        countDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               counterMethod();
            }
        }, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.action_newGame:
                game.restartGame();
                return true;
            case R.id.action_pause:
                running = false;
                counterRunning = false;
                ObjectManager.setPacManSpeed(0);
                soundManager.stopMusic();
                break;
            case R.id.action_resume:
                running = true;
                counterRunning = true;
                ObjectManager.setPacManSpeed(6);
                soundManager.playMusic();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //just to make sure if the app is killed, then the timers stops.
        gameTimer.cancel();
        countDownTimer.cancel();
        soundManager.stopMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameTimer.cancel();
        countDownTimer.cancel();
        running = false;
        soundManager.stopMusic();

    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.playMusic();
        running = true;
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 25);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(i);
    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
    }

    private void counterMethod(){
        this.runOnUiThread(Counter_Tick);
    }

    private final Runnable Counter_Tick = new Runnable() {
        @Override
        public void run() {

            if(counterRunning){
                game.setTimeToElapse(game.getTimeToElapse() - 1);
                game.updateCountTimer();
                if (game.getTimeToElapse() <= 0) {

                    Toast t = Toast.makeText(getApplicationContext(), R.string.loose, Toast.LENGTH_SHORT);
                    t.show();
                    game.restartGame();
                }
            }
            }
    };

    private final Runnable Timer_Tick = new Runnable() {
        public void run() {
            if (running)
            {
                game.chasePlayer();
                game.doCollisionCheckGhosts();
                switch (moveDirection) {
                    case Right:
                        game.moveRightSwipe();
                        break;
                    case Left:
                        game.moveLeftSwipe();
                        break;
                    case Down:
                        game.moveDownSwipe();
                        break;
                    case UP:
                        game.moveUpSwipe();
                        break;
                    case Zero:
                        break;
                }
            }

        }
    };

    public void setMoveDirection(MainActivity.moveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

}
