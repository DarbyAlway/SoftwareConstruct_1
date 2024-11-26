package se233.project2.Player;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Bomb {
    private ImageView sprite;
    private double x, y;

    // Sprite sheet parameters
    private int FRAMES = 4; // adjust depending on your sprite sheet
    private static double BOMB_FRAME_WIDTH = 80; // adjust for your sprite's frame width
    private static double BOMB_FRAME_HEIGHT = 80; // adjust for your sprite's frame height
    private int currentFrame = 0;

    private int FRAME_DELAY = 30;  // Number of update cycles to wait before switching to the next frame. Adjust as needed.
    private int delayCounter = 0;


    public Bomb(double x, double y) {
        this.x = x;
        this.y = y;

        URL resource = getClass().getResource("/se233/project2/spritesheet_SpecialAttack .png");
        if (resource == null) {
            throw new RuntimeException("Cannot find resource: /se233/project2/spritesheet_SpecialAttack.png");
        }

        Image bombImage = new Image(resource.toExternalForm());
        this.sprite = new ImageView(bombImage);

        // Set the viewport to show only the first frame initially
        this.sprite.setViewport(new Rectangle2D(0, 0, BOMB_FRAME_WIDTH, BOMB_FRAME_HEIGHT));

        this.sprite.setX(x);
        this.sprite.setY(y);
    }
    public double getBombX() {
        return x ;
        // return the x-coordinate
    }

    public double getBombY() {
        return y ;
        // return the y-coordinate
    }


    public ImageView getSprite() {
        return sprite;
    }

    public void moveDown() {
        y -= 1;  // adjust for desired bomb speed
        sprite.setY(y);

        delayCounter++;
        if (delayCounter >= FRAME_DELAY) {
            currentFrame++;
            if (currentFrame >= FRAMES) {
                currentFrame = 0; // loop back to the first frame
            }
            delayCounter = 0; // Reset the delay counter
        }

        // Change the viewport to show the next frame
        sprite.setViewport(new Rectangle2D(currentFrame * BOMB_FRAME_WIDTH, 0, BOMB_FRAME_WIDTH, BOMB_FRAME_WIDTH));
    }

}
