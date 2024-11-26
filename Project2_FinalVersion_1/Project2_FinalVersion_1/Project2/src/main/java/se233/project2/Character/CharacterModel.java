package se233.project2.Character;

public class CharacterModel {
    private double positionX;
    private double positionY;
    private final double speed;
    private final double gameWidth;

    public CharacterModel(double positionX, double positionY, double speed, double gameWidth) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speed = speed;
        this.gameWidth = gameWidth;
    }

    // Movement methods
    public void moveLeft() {
        positionX -= speed;
        if (positionX < 0) {
            positionX = 0;  // Ensure it doesn't go out of bounds
        }
    }

    public void moveRight() {
        positionX += speed;
        if (positionX > gameWidth) {
            positionX = gameWidth;  // Ensure it doesn't go out of bounds
        }
    }

    // Getters and Setters
    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }
}
