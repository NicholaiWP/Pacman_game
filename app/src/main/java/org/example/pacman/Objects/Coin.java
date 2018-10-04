package org.example.pacman.Objects;

import android.content.Context;

import org.example.pacman.Managers.ObjectManager;
import org.example.pacman.Utilities.Vector2;


/**
 * This class should contain information about a single Coin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class Coin {

    private final Context context;

    public Vector2 getCoinPosition() {
        return CoinPosition;
    }

    public int getCoinSize(){return ObjectManager.getCoins().size(); }

    public void setCoinPosition(Vector2 coinPosition) {
        this.CoinPosition = coinPosition;
    }

    private Vector2 CoinPosition;

    public Coin(Context context, Vector2 pos) {
        CoinPosition = pos;
        this.context = context;

    }



}
