package org.example.pacman.Managers;

import org.example.pacman.Objects.Coin;
import org.example.pacman.Objects.Ghost;
import org.example.pacman.Objects.PacMan;

import java.util.ArrayList;

public class ObjectManager {
    private static final ObjectManager ourInstance = new ObjectManager();

    public static ObjectManager getInstance() {
        return ourInstance;
    }

    private static final ArrayList<Coin> coins = new ArrayList<>();
    
    public static ArrayList<PacMan> getPacMan() {
        return pac;
    }

    private static final ArrayList<PacMan> pac = new ArrayList<>();

    public static boolean isPickedUp() {
        return pickedUp;
    }

    public static void setPickedUp(boolean pickedUp) {
        ObjectManager.pickedUp = pickedUp;
    }

    private static boolean pickedUp;

    public static int getPacManSpeed() {
        return pacManSpeed;
    }

    public static void setPacManSpeed(int pacManSpeed) {
        ObjectManager.pacManSpeed = pacManSpeed;
    }

    private static int pacManSpeed = 4;

    private static boolean collidedWithEnemy;


    private ObjectManager() {
    }

    public void RemoveFromListCoin(Coin coin){
        coins.remove(coin);
    }

    public void AddToListCoin(Coin coin){
        coins.add(coin);
    }

    public void RemoveFromListPac(PacMan pacMan){
        ObjectManager.pac.remove(pacMan);
    }

    public void AddToListPac(PacMan pacMan){
        ObjectManager.pac.add(pacMan);
    }

    public static boolean isCollidedWithEnemy() {
        return collidedWithEnemy;
    }

    public static void setCollidedWithEnemy(boolean collidedWithEnemy) {
        ObjectManager.collidedWithEnemy = collidedWithEnemy;
    }

    public static ArrayList<Coin> getCoins() {
        return coins;
    }

}
