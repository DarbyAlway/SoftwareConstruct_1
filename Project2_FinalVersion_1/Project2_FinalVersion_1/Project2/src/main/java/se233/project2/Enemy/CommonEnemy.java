package se233.project2.Enemy;

import javafx.scene.image.Image;
import java.util.Random;
import javafx.geometry.Rectangle2D;

public class CommonEnemy extends Enemy  {
    static final Random rand = new Random();
    private int direction = rand.nextInt(2) == 0 ? -1 : 1;  // -1 for left, 1 for right
    private double moveDistance = 0.2  + rand.nextDouble() * 1.8;  // Random distance between 1 and 6
    private final double gameWidth;
    private static double VERTICAL_SPEED = 40;

    // Sprite sheet properties
    private final int totalFrames = 4;  // For example, adjust according to your sprite sheet
    private int currentFrame = 0;
    private static final double COMMON_ENEMY_SPRITE_WIDTH = 35;  // Adjust these values according to your sprite sheet's frame size
    private static final double COMMON_ENEMY_SPRITE_HEIGHT = 35;

    private  int frameChangeCounter = 0;
    private int frameChangeThreshold = 75;


    public CommonEnemy(double x, double y, double gameWidth) {
        super(new Image(CommonEnemy.class.getResource("/se233/project2/spritesheet_CommonEnemy.png").toString()), x, y, gameWidth);
        this.gameWidth = gameWidth;

        // Set the viewport to the first frame
        sprite.setViewport(new Rectangle2D(0, 0, COMMON_ENEMY_SPRITE_WIDTH, COMMON_ENEMY_SPRITE_HEIGHT));
    }





    public void move() {
        double newX = getX() + direction * moveDistance;

        // Check boundaries and change direction if reached the edges
        if (newX < 0 || newX > gameWidth - getSprite().getBoundsInLocal().getWidth()) {
            direction = -direction;
            newX = getX() + direction * moveDistance;
            double newY = getSprite().getY() + VERTICAL_SPEED;
            getSprite().setY(newY);
        }

        getSprite().setX(newX);

        // Animate to the next frame
        goToNextFrame();
    }

    // Add a method to shoot
    public EnemyBullet shoot() {
        EnemyBullet enemyBullet = new EnemyBullet(getX() + sprite.getFitWidth() / 2 - 2.5, getY() + sprite.getFitHeight());
        return enemyBullet;
    }

    // Add a method to decide if the enemy should shoot
    public boolean decideToShoot() {
        return rand.nextInt(1000) < 5;
    }

    // Go to the next frame
    public void goToNextFrame() {
        frameChangeCounter++;
        if (frameChangeCounter >= frameChangeThreshold) {
            currentFrame = (currentFrame + 1) % totalFrames;
            double xOffset = currentFrame * COMMON_ENEMY_SPRITE_WIDTH;
            sprite.setViewport(new Rectangle2D(xOffset, 0, COMMON_ENEMY_SPRITE_WIDTH, COMMON_ENEMY_SPRITE_HEIGHT));
            frameChangeCounter = 0;  // Reset the counter
        }
    }
    public double getX() {
        return getSprite().getX();
    }

    public double getY() {
        return getSprite().getY();
    }

}

