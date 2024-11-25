package se233.project2.Character;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public abstract class Character {

    // Fields (member variables)
    protected ImageView sprite;
    protected double x, y;
    protected static double SPEED = 2.65;
    protected double gameWidth;

    // Constructor
    public Character(Image image, double x, double y, double gameWidth) {
        this.sprite = new ImageView(image);
        this.x = x;
        this.y = y;
        this.gameWidth = gameWidth;
        this.sprite.setX(x);
        this.sprite.setY(y);
    }

    public Character() {

    }

    // Getter for sprite
    public ImageView getSprite() {
        return sprite;
    }

    // Movement methods
    public void moveLeft() {
        x -= SPEED;
        ensureCharacterIsWithinLeftBoundary();
        sprite.setX(x);
    }

    public void moveRight() {
        x += SPEED;
        ensureCharacterIsWithinRightBoundary();
        sprite.setX(x);
    }

    // Boundary check methods
    protected void ensureCharacterIsWithinLeftBoundary() {
        if (x < 0) x = 0;
    }

    protected void ensureCharacterIsWithinRightBoundary() {
        if (x > gameWidth - sprite.getFitWidth()) x = gameWidth - sprite.getFitWidth();
    }

}

