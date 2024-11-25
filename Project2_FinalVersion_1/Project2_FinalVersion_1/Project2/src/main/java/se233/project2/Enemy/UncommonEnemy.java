package se233.project2.Enemy;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import static se233.project2.Enemy.CommonEnemy.rand;

public class UncommonEnemy extends Enemy {
    // Sprite sheet properties
    private static final String UNCOMMON_ENEMY_SPRITE_SHEET_PATH = "/se233/project2/spritesheet_UncommonEnemy.png";
    private final int totalFrames = 4;  // Adjust according to your sprite sheet
    private int currentFrame = 0;
    private static final double UNCOMMON_ENEMY_SPRITE_WIDTH = 40;  // Adjust these values according to your sprite sheet's frame size
    private static final double UNCOMMON_ENEMY_SPRITE_HEIGHT = 40;

    private int direction = rand.nextInt(2) == 0 ? -1 : 1;  // -1 for left, 1 for right
    private double moveDistance = 0.2  + rand.nextDouble() * 1.8;  // Random distance between 0.2 and 2
    private double UNCOMMON_ENEMY_VERTICAL_SPEED = 40;
    private double sinMultiplier = 0.01;
    private double sinValue = rand.nextDouble() * 2 * Math.PI;

    private int frameChangeCounter = 0;
    private int FRAME_CHANGE_THRESHOLD = 75;


    public void goToNextFrame() {
        frameChangeCounter++;
        if (frameChangeCounter >= FRAME_CHANGE_THRESHOLD) {
            currentFrame = (currentFrame + 1) % totalFrames;
            double xOffset = currentFrame * UNCOMMON_ENEMY_SPRITE_WIDTH;
            sprite.setViewport(new Rectangle2D(xOffset, 0, UNCOMMON_ENEMY_SPRITE_WIDTH, UNCOMMON_ENEMY_SPRITE_HEIGHT));
            frameChangeCounter = 0;  // Reset the counter
        }
    }
    public void move() {
        sinValue += sinMultiplier;
        double sinEffect = Math.sin(sinValue);
        double modifiedMoveDistance = moveDistance * (1 + sinEffect);

        double newX = getUncommonEnemyX() + direction * modifiedMoveDistance;

        if (newX < 0 || newX > gameWidth - UNCOMMON_ENEMY_SPRITE_WIDTH) {
            direction = -direction;
            newX = getUncommonEnemyY() + direction * modifiedMoveDistance;
            double newY = getSprite().getY() + UNCOMMON_ENEMY_VERTICAL_SPEED;
            getSprite().setY(newY);
        }

        getSprite().setX(newX);
        goToNextFrame();  // Move to the next frame for animation
    }



    public double getUncommonEnemyX() {
        return getSprite().getX();
    }

    public double getUncommonEnemyY() {
        return getSprite().getY();
    }


    public UncommonEnemy(double x, double y, double gameWidth) {
        super(loadSpriteSheet(), x, y, gameWidth);
        sprite.setViewport(new Rectangle2D(0, 0, UNCOMMON_ENEMY_SPRITE_WIDTH, UNCOMMON_ENEMY_SPRITE_HEIGHT));
    }
    private static Image loadSpriteSheet() {
        try {
            return new Image(UncommonEnemy.class.getResource(UNCOMMON_ENEMY_SPRITE_SHEET_PATH).toString());
        } catch (NullPointerException e) {
            System.err.println("Error loading the UncommonEnemy image.");
            System.exit(1);
            return null;
        }
    }


    // Override or add methods specific to UncommonEnemy.
    // For instance, maybe this enemy has a different shooting mechanism or movement pattern.
    @Override
    public EnemyBullet shoot() {
        EnemyBullet enemyBullet = new EnemyBullet(getUncommonEnemyX() + sprite.getFitWidth() / 2 - 2.5, getUncommonEnemyY()  + sprite.getFitHeight());
        return enemyBullet;
    }

    @Override
    public boolean decideToShoot() {
        return rand.nextInt(1000) < 5;  // this gives a 0.5% chance to shoot every time it's called, adjust as needed
    }

}
