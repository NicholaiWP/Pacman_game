package org.example.pacman.Interfaces;

import org.example.pacman.Objects.Coin;
import org.example.pacman.Objects.Ghost;
import org.example.pacman.Objects.PacMan;

public interface Collision {
    void OnCollisionApple(Coin coin, int distance);
    void OnCollisionPlayer(PacMan pacMan, int distance);
}
