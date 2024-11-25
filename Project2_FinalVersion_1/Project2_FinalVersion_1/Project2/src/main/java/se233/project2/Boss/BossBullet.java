package se233.project2.Boss;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
public class BossBullet {

    // Constants
    private static final double BULLET_WIDTH = 5.0;
    private static final double BULLET_HEIGHT = 35.0;
    private static final Color BULLET_COLOR = Color.RED;
    private static final double BULLET_SPEED = 3.5;

    // Fields
    private final Rectangle bullet;

    // Constructor
    public BossBullet() {
        bullet = new Rectangle(BULLET_WIDTH, BULLET_HEIGHT, BULLET_COLOR);
    }

    // Methods
    public void BossBulletMoveDown() {
        bullet.setY(bullet.getY() + BULLET_SPEED);
    }

    public Rectangle getSprite() {
        return bullet;
    }
}
