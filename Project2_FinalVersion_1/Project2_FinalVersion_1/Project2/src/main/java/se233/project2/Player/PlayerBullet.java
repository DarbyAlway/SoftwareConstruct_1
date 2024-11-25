package se233.project2.Player;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerBullet {

    public static final double BULLET_WIDTH = 5.0;
    public static final double BULLET_HEIGHT = 15.0;
    public static final Color BULLET_COLOR = Color.GREEN;
    public static final double BULLET_SPEED = 5.0;  // Speed at which the bullet moves upward

    private final Rectangle bullet;
    private final PlayerShip playerShip;

    public PlayerBullet(double x, double y , PlayerShip playerShip) {
        bullet = new Rectangle(BULLET_WIDTH, BULLET_HEIGHT, BULLET_COLOR);
        bullet.setX(x);
        bullet.setY(y);
        this.playerShip = playerShip;
        playerShip.playBulletSound(1); // Call playBulletSound here
    }

    public void moveUp() {
        bullet.setY(bullet.getY() - BULLET_SPEED);
    }

    public Rectangle getSprite() {
        return bullet;
    }

    public double getY() {
        return bullet.getY();
    }

}

