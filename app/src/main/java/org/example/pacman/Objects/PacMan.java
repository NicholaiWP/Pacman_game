package org.example.pacman.Objects;
import android.content.Context;
import android.graphics.Bitmap;
import org.example.pacman.Interfaces.Collision;
import org.example.pacman.Managers.ObjectManager;
import org.example.pacman.Utilities.Vector2;

public class PacMan implements Collision {

    private Vector2 position;
    private Context context;
    private Bitmap myPacBitmap;

    public PacMan(Context context, Vector2 position, Bitmap pacMan) {
        this.context = context;
        this.position = position;
        myPacBitmap = pacMan;
    }

    @Override
    public void OnCollisionApple(Coin coin, int distance) {

        float distanceToTarget = Vector2.distance(position, coin.getCoinPosition());

        if(distanceToTarget <= distance){

                ObjectManager.getInstance().RemoveFromListCoin(coin);
                ObjectManager.setPickedUp(true);
        }

    }

    @Override
    public void OnCollisionPlayer(PacMan pacMan, int distance) {
        //Empty
    }



    public Bitmap getMyPacBitmap() {
        return myPacBitmap;
    }

    public void setMyPacBitmap(Bitmap myPacBitmap) {
        this.myPacBitmap = myPacBitmap;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}
