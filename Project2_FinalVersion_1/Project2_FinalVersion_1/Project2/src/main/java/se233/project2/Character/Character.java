package se233.project2.Character;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public abstract class Character {

    // Fields (member variables)
    protected ImageView sprite;
    protected double positionX, positionY;
    protected double speed = 2.65;
    protected double gameWidth;

    // Constructor
    public Character(Image image, double positionX, double positionY, double gameWidth) {
        this.sprite = new ImageView(image);
        this.positionX = positionX;
        this.positionY = positionY;
        this.gameWidth = gameWidth;
        this.sprite.setX(positionX);
        this.sprite.setY(positionY);
    }

    public Character() {

    }

    // Getter for sprite
    public ImageView getSprite() {
        return sprite;
    }

    // Movement methods
    public void moveLeft() {
        positionX -= speed;
        ensureCharacterIsWithinLeftBoundary();
        sprite.setX(positionX);
    }

    public void moveRight() {
        positionX += speed;
        ensureCharacterIsWithinRightBoundary();
        sprite.setX(positionX);
    }

    // Boundary check methods
    protected void ensureCharacterIsWithinLeftBoundary() {
        if (positionX < 0) positionX = 0;
    }

    protected void ensureCharacterIsWithinRightBoundary() {
        if (positionX > gameWidth - sprite.getFitWidth()) positionX = gameWidth - sprite.getFitWidth();
    }

}