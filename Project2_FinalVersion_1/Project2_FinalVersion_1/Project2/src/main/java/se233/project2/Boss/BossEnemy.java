package se233.project2.Boss;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import se233.project2.Character.Character;


public class BossEnemy{
    // Constants
    private static final Random rand = new Random();
    private static final int totalFrames = 4;
    private static final double BOSS_SPRITE_WIDTH = 200;
    private static final double BOSS_SPRITE_HEIGHT = 200;
    private static final int frameChangeThreshold = 77;

    // Fields
    private ImageView sprite;
    private double x, y;
    private double gameWidth;
    private double SPEED = 1;
    private double direction = 1;
    private double verticalSpeed = 0;

    private int currentFrame = 0;
    private Random randomX;
    private int directionChangeCounter = 0;
    private int frameChangeCounter = 0;

    private int HEALTH = 100;

    // Constructor
    public BossEnemy(double x, double y, double gameWidth) {
        this.randomX = new Random();
        this.x = x;
        this.y = y;
        this.gameWidth = gameWidth;

        URL resourceUrl = getClass().getResource("/se233/project2/spritesheet_BossEnemy.png");
        if (resourceUrl != null) {
            String filePath = resourceUrl.toExternalForm();
            Image bossImage = new Image(filePath);
            sprite = new ImageView(bossImage);
            sprite.setX(x);
            sprite.setY(y);
            sprite.setViewport(new Rectangle2D(0, 0, BOSS_SPRITE_WIDTH, BOSS_SPRITE_HEIGHT));
        } else {
            System.err.println("Resource not found: boss_spritesheet.png");
        }
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void move() {
        directionChangeCounter++;

        // Increase this value for faster direction change frequency.
        if (directionChangeCounter % 100 == 0) { // Change direction randomly every 100 moves
            direction = randomX.nextBoolean() ? 1 : -1;
        }

        // Increase the range for greater speed variations.
        SPEED = 1 + 2 * randomX.nextDouble(); // Randomly adjust speed between 1 and 3

        // Increase this value for greater horizontal distance during each move.
        double moveDistance = 1.5;  // Adjust as desired. This value determines horizontal speed.

        double newX = x + direction * moveDistance * SPEED; // Use speed multiplier here for effect.

        // Check boundaries and change direction if reached the edges
        if (newX < 0 || newX > gameWidth - sprite.getBoundsInLocal().getWidth()) {
            direction = -direction;
            newX = x + direction * moveDistance * SPEED; // Recalculate the newX after changing the direction
        }

        x = newX;
        sprite.setX(x);

        double newY = sprite.getY() + verticalSpeed;
        sprite.setY(newY);

        updateAnimationFrame();  // Animate sprite sheet on every move
    }


    // Add a method to shoot
    public BossBullet shoot() {
        // Get boss dimensions
        double bossWidth = sprite.getFitWidth();
        double bossHeight = sprite.getFitHeight();

        // Create a new boss bullet
        BossBullet bossBullet = new BossBullet();

        // Get bullet dimensions
        double bulletWidth = 5;  // Replace with the bullet's actual width
        double bulletHeight = 10; // Replace with the bullet's actual height


        // Calculate the bullet's starting position
        double bulletStartPositionX = sprite.getX() + (bossWidth / 2) - (bulletWidth / 2);
        double bulletStartPositionY = sprite.getY() + (bossHeight / 2) - (bulletHeight / 2);

        // Adjust BossBullet's position
        bossBullet.getSprite().setTranslateX(bulletStartPositionX);
        bossBullet.getSprite().setTranslateY(bulletStartPositionY);

        return bossBullet;
    }


    // Add a method to decide if the boss should shoot
    public boolean decideToShoot() {
        return rand.nextInt(1000) < 30;  // this gives a 1% chance to shoot every time it's called, adjust as needed
    }


    // Go to the next frame
    public void updateAnimationFrame() {
        frameChangeCounter++;
        if (frameChangeCounter >= frameChangeThreshold) {
            currentFrame = (currentFrame + 1) % totalFrames;
            double xOffset = currentFrame * BOSS_SPRITE_WIDTH;
            sprite.setViewport(new Rectangle2D(xOffset, 0, BOSS_SPRITE_WIDTH, BOSS_SPRITE_HEIGHT));
            frameChangeCounter = 0;  // Reset the counter
        }
    }

    public void reduceHealth(int amount) {
        HEALTH -= amount;
        if (HEALTH <= 0) {
            HEALTH = 0;
            // Handle the boss's death here. Perhaps a method like `handleDeath()`.
        }
    }


    public int getHealth() {
        return this.HEALTH;
    }

}
