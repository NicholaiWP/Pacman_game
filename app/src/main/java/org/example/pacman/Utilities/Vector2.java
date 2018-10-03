package org.example.pacman.Utilities;

import android.graphics.Bitmap;

//Vector class used for placement
public class Vector2 {

    public enum bitmapDirection {
        Left,
        Right,
        Down,
        Up,
        None
    }

    //Float values used for horizontal and vertical positioning in game
    public float x;
    public float y;


    public static bitmapDirection dir;

    /**
     * Empty constructor
     */
    public Vector2()
    {

    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    private Vector2 add(float x, float y)
    {
        this.x += x;
        this.y += y;

        return this;
    }

    public Vector2 add(Vector2 vec) {
        return add(vec.x, vec.y);
    }


    public Vector2 sub(float x, float y)
    {
        this.x -= x;
        this.y -= y;

        return this;
    }

    public Vector2 sub(Vector2 vec)
    {
        this.x -= vec.x;
        this.y -= vec.y;

        return this;
    }

    public Vector2 mul(float multiply)
    {
        this.x *= multiply;
        this.y *= multiply;

        return this;
    }

    public Vector2 div(float div)
    {
        this.x /= div;
        this.y /= div;

        return this;
    }

    public static float distance(Vector2 a, Vector2 b) {
        return (float) Math.sqrt(sqrDistance(a, b));
    }

    private static float sqrDistance(Vector2 a, Vector2 b) {
        return (float) (Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static bitmapDirection getDir() {
        return dir;
    }

    public static void setDir(bitmapDirection dir) {
        Vector2.dir = dir;
    }
}
