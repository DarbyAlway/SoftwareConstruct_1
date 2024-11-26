package se233.project2.Player;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import se233.project2.Character.Character;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class PlayerShip extends Character {

    // Constant for the image path
    private final String bulletSoundPath = "/se233/project2/S1_[cut_2sec].mp3";

    private static final String PLAYER_SHIP_SPRITE_SHEET_PATH = "/se233/project2/spritesheet_playership.png";
    private int currentFrame = 0;
    private int totalFrames = 4; // Total frames in the sprite sheet.
    protected static double PLAYER_SHIP_SPRITE_WIDTH = 35;
    protected static double PLAYER_SHIP_SPRITE_HEIGHT = 35;

    private int score = 0;


    public PlayerShip(double x, double y, double gameWidth) {
        super(loadSpriteSheet(), x, y, gameWidth);
        this.sprite.setViewport(new Rectangle2D(0, 0, PLAYER_SHIP_SPRITE_WIDTH, PLAYER_SHIP_SPRITE_HEIGHT));
    }

    public PlayerShip() {
        super();
    }

    public void goToNextFrame() {
        currentFrame = (currentFrame + 1) % totalFrames;
        double xOffset = currentFrame * PLAYER_SHIP_SPRITE_WIDTH;
        sprite.setViewport(new Rectangle2D(xOffset, 0, PLAYER_SHIP_SPRITE_WIDTH, PLAYER_SHIP_SPRITE_HEIGHT));
    }


    private static Image loadSpriteSheet() {
        try {
            return new Image(PlayerShip.class.getResource(PLAYER_SHIP_SPRITE_SHEET_PATH).toString());
        } catch (NullPointerException e) {
            System.err.println("Error loading the PlayerShip image.");
            System.exit(1);
            return null; // Won't reach here, but the return is needed to satisfy the compiler.
        }
    }

    public double getX() {
        return this.getSprite().getX();
    }

    public double getY() {
        return this.getSprite().getY();
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        ensureSpriteIsWithinLeftBoundary();
    }

    @Override
    public void moveRight() {
        super.moveRight();
        ensureSpriteIsWithinRightBoundary();
    }

    private void ensureSpriteIsWithinLeftBoundary() {
        if (getX() < 0) {
            sprite.setX(0);
        }
    }

    private void ensureSpriteIsWithinRightBoundary() {
        if (getX() > gameWidth - sprite.getFitWidth()) {
            sprite.setX(gameWidth - sprite.getFitWidth());
        }
    }


    void playBulletSound(int bulletCount) {
        try {
            Media sound = new Media(getClass().getResource(bulletSoundPath).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(bulletCount); // เล่นเสียงวนซ้ำตามจำนวนกระสุน
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing the bullet sound.");
            e.printStackTrace();
        }
    }

    public Bomb dropBomb() {
        double shipWidth = sprite.getBoundsInLocal().getWidth();
        double shipHeight = sprite.getBoundsInLocal().getHeight();
        return new Bomb(positionX + (shipWidth / 2), positionY + shipHeight);  // Drops bomb from the center-bottom of the ship
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int i) {
        score += i;
    }

}

