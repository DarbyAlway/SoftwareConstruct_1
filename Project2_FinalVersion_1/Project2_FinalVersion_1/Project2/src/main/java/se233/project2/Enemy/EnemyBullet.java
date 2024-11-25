package se233.project2.Enemy;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyBullet {

    private static final double BULLET_WIDTH = 5.0;
    private static final double BULLET_HEIGHT = 15.0;
    private static final Color BULLET_COLOR = Color.RED;  // Different color to differentiate enemy bullets
    private static final double BULLET_SPEED = 0.75;  // Speed at which the bullet moves downward

    private final Rectangle bullet;

    public EnemyBullet(double x, double y) {
        bullet = new Rectangle(BULLET_WIDTH, BULLET_HEIGHT, BULLET_COLOR);
        bullet.setX(x);
        bullet.setY(y);
    }

    public void moveDown() {
        bullet.setY(bullet.getY() + BULLET_SPEED);
    }

    public Rectangle getSprite() {
        return bullet;
    }
}
