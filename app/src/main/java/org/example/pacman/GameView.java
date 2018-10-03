package org.example.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import org.example.pacman.Managers.ObjectManager;
import org.example.pacman.Utilities.Vector2;

public class GameView extends View {

    private Game game;
    private Paint paint;

    public void setGame(Game game) {
        this.game = game;
    }

    /* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
    public GameView(Context context) {
        super(context);
       paint = new Paint();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    //In the onDraw we put all our code that should be
    //drawn whenever we update the screen.
    @Override
    protected void onDraw(Canvas canvas) {
        //Here we get the height and weight
        game.setScreenEndY(canvas.getHeight());
        game.setScreenEndX(canvas.getWidth());

        //update the size for the canvas to the game.
        game.setSize(game.getScreenEndY(), game.getScreenEndX());
        Log.d("screen stuff", game.getScreenEndX() + ", " + game.getScreenEndY());
        //Making a new paint object
        canvas.drawColor(getResources().getColor(R.color.Black)); //clear entire canvas to white color

        for (int i = 0; i < ObjectManager.getCoins().size(); i++) {
            if (ObjectManager.isPickedUp()) {
                break;

            } else {
                canvas.drawBitmap(game.getAppleCoinBitmap(), ObjectManager.getCoins().get(i).getCoinPosition().x, ObjectManager.getCoins().get(i).getCoinPosition().y, paint);
            }

        }

        if (Vector2.dir == Vector2.bitmapDirection.Right) {
            canvas.drawBitmap(game.getGhostBitmapRight(), game.getGhostPosX(), game.getGhostPosY(), paint);
        } else {
            canvas.drawBitmap(game.getGhostBitmap(), game.getGhostPosX(), game.getGhostPosY(), paint);
        }

        for (int i = 0; i < ObjectManager.getPacMan().size(); i++) {
            if (ObjectManager.isCollidedWithEnemy()) {
                break;
            } else {
                switch (Vector2.dir) {
                    case Left:
                        canvas.drawBitmap(game.getPacBitmapLeft(), ObjectManager.getPacMan().get(0).getPosition().x, ObjectManager.getPacMan().get(0).getPosition().y, paint);
                        break;
                    case Right:
                        canvas.drawBitmap(game.getPacBitmap(), ObjectManager.getPacMan().get(0).getPosition().x, ObjectManager.getPacMan().get(0).getPosition().y, paint);
                        break;
                    case Up:
                        canvas.drawBitmap(game.getPacBitmapUp(), ObjectManager.getPacMan().get(0).getPosition().x, ObjectManager.getPacMan().get(0).getPosition().y, paint);
                        break;
                    case Down:
                        canvas.drawBitmap(game.getPacBitmapDown(), ObjectManager.getPacMan().get(0).getPosition().x, ObjectManager.getPacMan().get(0).getPosition().y, paint);
                        break;
                }
            }
        }
        super.onDraw(canvas);

    }

}

