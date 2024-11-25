package se233.project2.Enemy;

import javafx.scene.image.Image;
import se233.project2.Character.Character;

public abstract class Enemy extends Character {

    public Enemy(Image image, double x, double y, double gameWidth) {
        super(image, x, y, gameWidth);
    }

    public abstract EnemyBullet shoot();

    public abstract boolean decideToShoot();
}
