package org.example.pacman.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import org.example.pacman.Interfaces.Collision;
import org.example.pacman.Managers.ObjectManager;
import org.example.pacman.Utilities.Vector2;

public class Ghost implements Collision{

    private Vector2 position;
    private final Context context;
    private final Bitmap ghostBitmap;

    private int speedX = 2;
    private int speedY = 2;

    public Ghost(Context cont,Vector2 position, Bitmap ghostBitmap) {
        this.context = cont;
        this.position = position;
        this.ghostBitmap = ghostBitmap;
        ObjectManager.setTag("Ghost");
    }


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }


    @Override
    public void OnCollisionApple(Coin coin, int distance) {
        //empty
    }

    @Override
    public void OnCollisionPlayer(PacMan pacMan, int distance) {

        float distanceToTarget = Vector2.distance(position, pacMan.getPosition());

        if(distanceToTarget <= distance && ObjectManager.getTag().equals("PacMan")){
            ObjectManager.getInstance().RemoveFromListPac(pacMan);
            ObjectManager.setCollidedWithEnemy(true);
        }
    }
}
