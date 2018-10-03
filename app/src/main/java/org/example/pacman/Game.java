package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;
import org.example.pacman.Managers.ObjectManager;
import org.example.pacman.Objects.Coin;
import org.example.pacman.Objects.Ghost;
import org.example.pacman.Objects.PacMan;
import org.example.pacman.Utilities.Vector2;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * Game logic
 */
class Game {
    //context is a reference to the activity
    private final Context context;
    private final int coinsToPickUp = 4;
    private int pacManAmount = 1;
    private int points; //X points do we have

    private int timeToElapse; //counter as to how long pacMan have left
    private int elapsedTimeStart;

    //bitmaps of the pacMan and their directions
    private final Bitmap pacBitmap;
    private final Bitmap pacBitmapLeft;
    private Bitmap pacBitmapUp;
    private Bitmap pacBitmapDown;

    //bitmap of coin
    private Bitmap coinBitmap;

    //bitmap of ghost enemy
    private final Bitmap ghostBitmap;

    public Bitmap getGhostBitmapRight() {
        return ghostBitmapRight;
    }

    private final Bitmap ghostBitmapRight;

    private ArrayList<Vector2> randomPosApple;
    private ArrayList<Vector2> randomPacManPos;
    ArrayList<Vector2> enemyPos;
    //textview reference to points
    private final TextView pointsView;
    //a reference to the gameview
    private GameView gameView;

    //Game time counter
    private final TextView counter;
    private int screenEndX;
    private int screenEndY;
    private final int rightSideScreenValue = 150;
    private final int leftSideScreenValue = 10;


    private Ghost ghost;

    public Game(Context context, TextView view, TextView gameCounter)
    {
        this.context = context;
        this.pointsView = view;
        counter = gameCounter;

        pacBitmapLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_left);
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        pacBitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_up);
        pacBitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_down);

        ghostBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost);
        ghostBitmapRight =  BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost_right);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gold_coin);

        //scale bitmap
        Bitmap originalBitmap = coinBitmap;
        coinBitmap = Bitmap.createScaledBitmap(
                originalBitmap, 80, 80, false);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }


    public void startGame()
    {
        timeToElapse = 20;
        elapsedTimeStart = timeToElapse;
        pointsView.setText(context.getResources().getString(R.string.points, " ", points));
        counter.setText(context.getResources().getString(R.string.countDown, " ", timeToElapse));
        Vector2.dir = Vector2.bitmapDirection.Right;
        ((MainActivity) context).setMoveDirection(MainActivity.moveDirection.Zero);
        ghost = new Ghost(context, new Vector2(400,400), ghostBitmap);
        addRandomPositions();
        AddRandomPacmanLocation();
        spawnCoinsRandom();
        gameView.invalidate(); //redraw screen
    }

    /**
     * This method is called in main activity, and is used to update the timer textView
     */
    public void updateCountTimer(){
        counter.setText(context.getResources().getString(R.string.countDown, " ", timeToElapse));
    }

    private void winGame(){

        if(ObjectManager.getCoins().size() == 0){
                String win = "you won!";
                int duration = Toast.LENGTH_SHORT;
                Toast t =  Toast.makeText(context.getApplicationContext(),win,duration);
                t.show();
             ((MainActivity) context).setMoveDirection(MainActivity.moveDirection.Zero);
               restartGame();
        }
    }

    public void restartGame(){
        ObjectManager.getCoins().clear();
        ObjectManager.getPacMan().clear();
        ObjectManager.setPacManSpeed(4);
        startGame();
    }


    public void setSize(int h, int w)
    {
        this.screenEndY = h;
        this.screenEndX = w;
    }


    public void chasePlayer(){
            //get distance between two points
        for (int i = 0; i < ObjectManager.getPacMan().size(); i++) {
            float dx = ObjectManager.getPacMan().get(0).getPosition().x - ghost.getPosition().x;
            float dy = ObjectManager.getPacMan().get(0).getPosition().y - ghost.getPosition().y;
            double distance = Math.sqrt((dx*dx)+(dy*dy));

            Vector2.dir = Vector2.bitmapDirection.Right;
            //the movement based on distance between the two points and the ghost's speed.
            ghost.getPosition().x = ghost.getPosition().x + (float)(dx / distance * ghost.getSpeedX());
            ghost.getPosition().y = ghost.getPosition().y + (float)(dy / distance * ghost.getSpeedY());
        }

    }

    /**
     * Swipe/Touch movement
     */
    public void moveUpSwipe(){

        for (int i = 0; i < ObjectManager.getPacMan().size(); i++) {
            ((MainActivity) context).setMoveDirection(MainActivity.moveDirection.UP);
            if(ObjectManager.getPacMan().get(0).getPosition().y > leftSideScreenValue) {
                Vector2.dir = Vector2.bitmapDirection.Up;
                ObjectManager.getPacMan().get(0).getPosition().y = ObjectManager.getPacMan().get(0).getPosition().y - ObjectManager.getPacManSpeed();
                doCollisionCheckApples();
                gameView.invalidate();
            }
        }

    }

    public void moveDownSwipe(){
        for (int i = 0; i < ObjectManager.getPacMan().size(); i++) {
            ((MainActivity) context).setMoveDirection(MainActivity.moveDirection.Down);
            if (ObjectManager.getPacMan().get(0).getPosition().y < screenEndY - rightSideScreenValue) {
                Vector2.dir = Vector2.bitmapDirection.Down;
                ObjectManager.getPacMan().get(0).getPosition().y = ObjectManager.getPacMan().get(0).getPosition().y + ObjectManager.getPacManSpeed();
                doCollisionCheckApples();
                gameView.invalidate();
            }
        }
    }

    public void moveLeftSwipe(){

        for (int i = 0; i < ObjectManager.getPacMan().size(); i++) {
            ((MainActivity) context).setMoveDirection(MainActivity.moveDirection.Left);
            if (ObjectManager.getPacMan().get(0).getPosition().x > leftSideScreenValue) {
                //bitmap facing direction
                Vector2.dir = Vector2.bitmapDirection.Left;
                ObjectManager.getPacMan().get(0).getPosition().x = ObjectManager.getPacMan().get(0).getPosition().x - ObjectManager.getPacManSpeed();
                doCollisionCheckApples();
                gameView.invalidate();
            }
        }
    }

    public void moveRightSwipe(){
        for (int i = 0; i < ObjectManager.getPacMan().size(); i++) {
            ((MainActivity) context).setMoveDirection(MainActivity.moveDirection.Right);
            if (ObjectManager.getPacMan().get(0).getPosition().x < screenEndX - rightSideScreenValue) {
                Vector2.dir = Vector2.bitmapDirection.Right;
                ObjectManager.getPacMan().get(0).getPosition().x = ObjectManager.getPacMan().get(0).getPosition().x + ObjectManager.getPacManSpeed();
                doCollisionCheckApples();
                gameView.invalidate();
            }
        }

    }

    public void SwipeMove(){
        gameView.setOnTouchListener(new Touch(context.getApplicationContext()) {
            public void onSwipeTop() {
                moveUpSwipe();
            }
            public void onSwipeRight() {

                moveRightSwipe();
            }
            public void onSwipeLeft() {
                moveLeftSwipe();
            }
            public void onSwipeBottom() {
              moveDownSwipe();
            }
        });
    }


//endregion
private void doCollisionCheckApples()
    {
        for (int i = 0; i <ObjectManager.getCoins().size(); i++) {
            ObjectManager.getPacMan().get(0).OnCollisionApple(ObjectManager.getCoins().get(i), 100);
            if(ObjectManager.isPickedUp()){
                ((MainActivity)context).getSoundManager().playSound(1);
                winGame();
                points--;
                ObjectManager.setPickedUp(false);
            }
        }
        pointsView.setText(context.getResources().getString(R.string.points, " ", points));
    }

    public void doCollisionCheckGhosts(){
        for (int i = 0; i < ObjectManager.getPacMan().size(); i++) {
            ghost.OnCollisionPlayer(ObjectManager.getPacMan().get(0),100);
            if(ObjectManager.isCollidedWithEnemy()){
                ObjectManager.setCollidedWithEnemy(false);
                restartGame();
            }
        }

    }

    /**
     * Method containing all the different points
     * - to randomize when spawning coins and pacMan.
     */
    private void addRandomPositions(){

        randomPacManPos = new ArrayList<>();

        randomPacManPos.add(new Vector2(500,1000));
        randomPacManPos.add(new Vector2(500,800));
        randomPacManPos.add(new Vector2(500,500));
        randomPacManPos.add(new Vector2(500,300));
        randomPacManPos.add(new Vector2(200,1000));
        randomPacManPos.add(new Vector2(200,800));
        randomPacManPos.add(new Vector2(200,500));
        randomPacManPos.add(new Vector2(200,300));
        randomPacManPos.add(new Vector2(300,1000));
        randomPacManPos.add(new Vector2(300,800));
        randomPacManPos.add(new Vector2(300,500));
        randomPacManPos.add(new Vector2(300,300));
        randomPacManPos.add(new Vector2(900,1000));
        randomPacManPos.add(new Vector2(900,800));
        randomPacManPos.add(new Vector2(900,500));
        randomPacManPos.add(new Vector2(900,300));
        randomPacManPos.add(new Vector2(900,100));

        randomPosApple = new ArrayList<>();

        randomPosApple.add(new Vector2(500,1280));
        randomPosApple.add(new Vector2(500,1000));
        randomPosApple.add(new Vector2(500,800));
        randomPosApple.add(new Vector2(500,500));
        randomPosApple.add(new Vector2(500,300));
        randomPosApple.add(new Vector2(500,100));
        randomPosApple.add(new Vector2(500,20));

        randomPosApple.add(new Vector2(200,1280));
        randomPosApple.add(new Vector2(200,1000));
        randomPosApple.add(new Vector2(200,800));
        randomPosApple.add(new Vector2(200,500));
        randomPosApple.add(new Vector2(200,300));
        randomPosApple.add(new Vector2(200,100));
        randomPosApple.add(new Vector2(200,20));

        randomPosApple.add(new Vector2(300,1280));
        randomPosApple.add(new Vector2(300,1000));
        randomPosApple.add(new Vector2(300,800));
        randomPosApple.add(new Vector2(300,500));
        randomPosApple.add(new Vector2(300,300));
        randomPosApple.add(new Vector2(300,100));
        randomPosApple.add(new Vector2(300,20));

        randomPosApple.add(new Vector2(700,1280));
        randomPosApple.add(new Vector2(700,1000));
        randomPosApple.add(new Vector2(700,800));
        randomPosApple.add(new Vector2(700,500));
        randomPosApple.add(new Vector2(700,300));
        randomPosApple.add(new Vector2(700,100));
        randomPosApple.add(new Vector2(700,20));

        randomPosApple.add(new Vector2(900,1280));
        randomPosApple.add(new Vector2(900,1000));
        randomPosApple.add(new Vector2(900,800));
        randomPosApple.add(new Vector2(900,500));
        randomPosApple.add(new Vector2(900,300));
        randomPosApple.add(new Vector2(900,100));
        randomPosApple.add(new Vector2(900,20));
    }

    /**
     * spawns coins within bounds, coins can however, spawn on the same position :/
     */
    //TODO coin bug sometimes idk why
    private void spawnCoinsRandom(){
        for (int i = 0; i < coinsToPickUp; i++) {
            Random r = new Random();
            int s = r.nextInt(randomPosApple.size());
            ObjectManager.getInstance().AddToListCoin(new Coin(context, randomPosApple.get(s)));
            points = coinsToPickUp + 1;
        }
    }


    private void AddRandomPacmanLocation(){
        for (int i = 0; i < pacManAmount; i++) {
            Random r = new Random();
            int s = r.nextInt(randomPacManPos.size());
            ObjectManager.getInstance().AddToListPac(new PacMan(context, randomPacManPos.get(s), pacBitmap));
        }
    }



    public int getPoints()
    {
        return points;
    }

    public Bitmap getAppleCoinBitmap() {return coinBitmap; }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getPacBitmapLeft()
    {
        return pacBitmapLeft;
    }

    public Bitmap getGhostBitmap() {
        return ghostBitmap;
    }

    public float getGhostPosX(){
        return ghost.getPosition().x;
    }

    public float getGhostPosY(){
        return ghost.getPosition().y;
    }

    public int getScreenEndY() {
        return screenEndY;
    }

    public void setScreenEndY(int screenEndY) {
        this.screenEndY = screenEndY;
    }

    public int getScreenEndX() {
        return screenEndX;
    }

    public void setScreenEndX(int screenEndX) {
        this.screenEndX = screenEndX;
    }

    public Bitmap getPacBitmapUp() {
        return pacBitmapUp;
    }

    public void setPacBitmapUp(Bitmap pacBitmapUp) {
        this.pacBitmapUp = pacBitmapUp;
    }

    public Bitmap getPacBitmapDown() {
        return pacBitmapDown;
    }

    public void setPacBitmapDown(Bitmap pacBitmapDown) {
        this.pacBitmapDown = pacBitmapDown;
    }


    public int getTimeToElapse() {
        return timeToElapse;
    }

    public void setTimeToElapse(int timeToElapse) {
        this.timeToElapse = timeToElapse;
    }
}
