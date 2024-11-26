package se233.project2.Character;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CharacterView {
    private final ImageView sprite;

    public CharacterView(Image image, double initialX, double initialY) {
        this.sprite = new ImageView(image);
        this.sprite.setX(initialX);
        this.sprite.setY(initialY);
    }

    // Update the sprite's position based on the model's data
    public void updatePosition(double x, double y) {
        sprite.setX(x);
        sprite.setY(y);
    }

    // Getter for the sprite
    public ImageView getSprite() {
        return sprite;
    }
}
