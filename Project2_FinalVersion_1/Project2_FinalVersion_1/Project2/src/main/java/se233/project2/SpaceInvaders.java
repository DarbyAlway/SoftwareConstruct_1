package se233.project2;

import javafx.animation.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import se233.project2.Boss.BossBullet;
import se233.project2.Boss.BossEnemy;
import se233.project2.Enemy.CommonEnemy;
import se233.project2.Enemy.EnemyBullet;
import se233.project2.Enemy.UncommonEnemy;
import se233.project2.Player.Bomb;
import se233.project2.Player.PlayerBullet;
import se233.project2.Player.PlayerShip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;



public class SpaceInvaders extends Application {

    // 1. JavaFX UI Elements
    private javafx.scene.text.Text warningText;
    private javafx.scene.text.Text scoreText;
    private List<ImageView> heartLives = new ArrayList<>();
    private Image heartImage = new Image(getClass().getResourceAsStream("/se233/project2/Life_Crystal.png"));
    private Stage primaryStage;
    private Rectangle healthBar;
    private Rectangle healthBarBackground;

    // 2. Game Entities and their lists
    private List<EnemyBullet> enemyBullets = new ArrayList<>();
    private List<CommonEnemy> commonEnemies = new ArrayList<>();
    private List<PlayerBullet> playerBullets = new ArrayList<>();
    private List<UncommonEnemy> uncommonEnemies = new ArrayList<>();
    private List<BossEnemy> bossEnemies = new ArrayList<>();
    private List<BossBullet> bossBullets = new ArrayList<>();
    private PlayerShip playerShip;
    private BossEnemy boss;
    List<Bomb> bombs = new ArrayList<>();

    // 3. Player related attributes
    private int playerLives = 3; // Initialize with 3 lives
    private int score = 0;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    // 4. Game configuration and settings
    private final double GAMEWIDTH = 800;
    private final double GAMEHEIGHT = 600;

    // 5. Animation and Timing
    private int frameCounter = 0;
    private int updateEveryNFrames = 75; // Adjust this number to control animation speed.
    int FRAMECHANGECOUNTER = 0;
    int FRAMECHANGETHRESHOLD = 50; // Adjust this value to control animation speed
    int bossFrameCounter = 0;
    final int bossUpdateEveryNFrames = 5;  // Adjust this value to change the boss animation speed
    private boolean bossActive = false;  // Flag to track if boss is active

    // 6. Logs and Debugging

    private static final Logger LOGGER = LogManager.getLogger(SpaceInvaders.class);



    // @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            // Configure the primary stage
            configureStage();

            // Display the game menu
            showMenu();
        } catch (Exception e) {
            logError(e.getMessage());
            handleInitializationError(); // Handle any errors that occur during initialization
        }
    }

    private void configureStage() {
        // Allow user to resize the window
        primaryStage.setResizable(false);
    }

    private void logError(String message) {
        // For simplicity, just print the error to the console
        System.err.println("Error: " + message);

        // In a real-world scenario, you'd likely want to log this to a file or error monitoring system.
    }

    private void handleInitializationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Initialization Error");
        alert.setHeaderText("An error occurred during game initialization");
        alert.setContentText("Would you like to retry or exit the game?");

        ButtonType retryButton = new ButtonType("Retry");
        ButtonType exitButton = new ButtonType("Exit");

        alert.getButtonTypes().setAll(retryButton, exitButton);

        ButtonType result = alert.showAndWait().orElse(exitButton);  // Default to exit if no selection

        if (result == retryButton) {
            // Retry the initialization
            start(primaryStage);
        } else {
            // Exit the game
            System.exit(0);
        }
    }


    private void showMenu() {
        try {
            Pane menuRoot = new Pane();
            Scene menuScene = new Scene(menuRoot, GAMEWIDTH, GAMEHEIGHT);

            // Load your background image
            Image backgroundImage = new Image(getClass().getResource("/se233/project2/GAME_BG.jpg").toString());

            ImageView backgroundView = new ImageView(backgroundImage);

            // Optional: set the size of the ImageView if you want to scale/stretch it
            backgroundView.setFitWidth(GAMEWIDTH);
            backgroundView.setFitHeight(GAMEHEIGHT);

            // Add the background ImageView to the gameRoot pane first
            menuRoot.getChildren().add(backgroundView);

            Button startButton = new Button("Start Game");
            startButton.setLayoutX(GAMEWIDTH / 2 - 50); // roughly center
            startButton.setLayoutY(GAMEHEIGHT / 2 - 15); // roughly center
            startButton.setOnAction(event -> startGame());

            menuRoot.getChildren().add(startButton);

            primaryStage.setScene(menuScene);
            primaryStage.setTitle("Space Invaders Menu");
            primaryStage.show();

        } catch (NullPointerException e) {
            logError("Unable to load the background image or other resources.");
            handleInitializationError();
        } catch (Exception e) {
            logError("An error occurred while setting up the menu: " + e.getMessage());
            handleInitializationError();
        }
    }



    private void displayGameOver() {
        try {
            Pane gameOverRoot = new Pane();
            Scene gameOverScene = new Scene(gameOverRoot, GAMEWIDTH, GAMEHEIGHT);

            // Load your background image
            Image backgroundImage = new Image(getClass().getResource("/se233/project2/Gameover.jpg").toString());

            // Create an ImageView for your background image
            ImageView backgroundView = new ImageView(backgroundImage);

            // set the size of the ImageView to scale/stretch it
            backgroundView.setFitWidth(GAMEWIDTH);
            backgroundView.setFitHeight(GAMEHEIGHT);

            // Add the background ImageView to the gameRoot pane first
            gameOverRoot.getChildren().add(backgroundView);

            javafx.scene.text.Text gameOverText = new javafx.scene.text.Text("GAME OVER");
            gameOverText.setFont(new Font(50));
            gameOverText.setX(GAMEWIDTH / 2 - gameOverText.getLayoutBounds().getWidth() / 2);
            gameOverText.setY(GAMEHEIGHT / 2 - gameOverText.getLayoutBounds().getHeight() / 2);

            gameOverRoot.getChildren().add(gameOverText);

            // Create a fade transition for the gameOverText
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), gameOverText);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.3);  // you can adjust this value as desired
            fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();

            primaryStage.setScene(gameOverScene);
            primaryStage.show();

        } catch (NullPointerException e) {
            logError("Unable to load the game over image or other resources.");
            handleInitializationError();
        } catch (Exception e) {
            logError("An error occurred while displaying game over screen: " + e.getMessage());
            handleInitializationError();
        }
    }


    private void endGameScene() {
        try {
            Pane endGameRoot = new Pane();
            Scene endGameScene = new Scene(endGameRoot, GAMEWIDTH, GAMEHEIGHT);

            // Assuming you have a different jpg for the end game scene.
            Image backgroundImage = new Image(getClass().getResource("/se233/project2/EndgameBG.jpg").toString());

            ImageView backgroundView = new ImageView(backgroundImage);

            // Optional: set the size of the ImageView if you want to scale/stretch it
            backgroundView.setFitWidth(GAMEWIDTH);
            backgroundView.setFitHeight(GAMEHEIGHT);

            // Add the background ImageView to the endGameRoot pane first
            endGameRoot.getChildren().add(backgroundView);

            // You can adjust the text message as you wish
            javafx.scene.text.Text endGameText = new javafx.scene.text.Text("CONGRATULATIONS, YOU WON!");
            endGameText.setFont(new Font(50));
            endGameText.setFill(Color.WHITE);
            endGameText.setX(GAMEWIDTH / 2 - endGameText.getLayoutBounds().getWidth() / 2);
            endGameText.setY(GAMEHEIGHT / 2 - endGameText.getLayoutBounds().getHeight() / 2);

            endGameRoot.getChildren().add(endGameText);

            // Create a fade transition for the endGameText
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), endGameText);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.3);  // you can adjust this value as desired
            fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();

            primaryStage.setScene(endGameScene);
            primaryStage.show();

        } catch (NullPointerException e) {
            logError("Unable to load the end game image or other resources.");
            handleInitializationError();
        } catch (Exception e) {
            logError("An error occurred while displaying the end game screen: " + e.getMessage());
            handleInitializationError();
        }
    }



    private void showWarningText1(String message) {
        try {
            warningText.setText(message);
            warningText.setOpacity(1.0); // Ensure it's fully visible to start with

            // Create a FadeTransition to fade out the warning text
            FadeTransition fade = new FadeTransition(Duration.seconds(2), warningText);
            fade.setFromValue(1.0); // Start at full opacity
            fade.setToValue(0.0);  // End at zero opacity (fully transparent)

            // Schedule the warning to disappear after the fade completes
            fade.setOnFinished(e -> warningText.setText(""));

            fade.play();

        } catch (NullPointerException e) {
            logError("Warning text not initialized or message is null: " + e.getMessage());
        } catch (Exception e) {
            logError("An error occurred while showing warning text: " + e.getMessage());
        }
    }


    private void showWarningText2(String message) {
        try {
            warningText.setText(message);
            warningText.setOpacity(1.0); // Ensure it's fully visible to start with

            // Create a TranslateTransition to move the warning text
            TranslateTransition translate = new TranslateTransition(Duration.seconds(0.1), warningText); // Adjust the duration as per your preference
            translate.setByY(-50); // Translate it 50 pixels upwards
            translate.setCycleCount(1); // Set the number of cycles to 1 (1 play)

            // Schedule the warning to disappear after the transition completes
            translate.setOnFinished(e -> {
                warningText.setText("");
                warningText.setTranslateY(0); // Reset the translation to 0
            });

            translate.play();

        } catch (NullPointerException e) {
            logError("Warning text not initialized or message is null: " + e.getMessage());
        } catch (Exception e) {
            logError("An error occurred while showing and translating warning text: " + e.getMessage());
        }
    }


    private void initializePlayerLivesDisplay(Pane gameRoot) {
        try {
            if (gameRoot == null || heartImage == null) {
                throw new NullPointerException("Game root or heart image is not initialized.");
            }
            for (int i = 0; i < playerLives; i++) {
                ImageView heart = new ImageView(heartImage);
                heart.setX(10 + (i * (heartImage.getWidth() + 5)));  // 5 is the spacing between hearts
                heart.setY(10);
                heartLives.add(heart);
                gameRoot.getChildren().add(heart);
            }
        } catch (NullPointerException e) {
            logError(e.getMessage());
        } catch (Exception e) {
            logError("An error occurred while initializing player lives display: " + e.getMessage());
        }
    }

    private void updatePlayerLivesDisplay() {
        try {
            if (playerLives < heartLives.size()) {
                ImageView lostHeart = heartLives.remove(heartLives.size() - 1);
                if (lostHeart != null) {
                    lostHeart.setImage(null);  // Or you can use a broken heart image if you have one
                } else {
                    throw new NullPointerException("Lost heart is null.");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            logError("Heart lives list might be empty: " + e.getMessage());
        } catch (NullPointerException e) {
            logError(e.getMessage());
        } catch (Exception e) {
            logError("An error occurred while updating player lives display: " + e.getMessage());
        }
    }


    private void initializeBossHealthBar(Pane gameRoot) {
        try {
            if (gameRoot == null) {
                throw new NullPointerException("Game root is not initialized.");
            }

            healthBarBackground = new Rectangle(280, 10, 200, 20);
            healthBarBackground.setFill(Color.WHITE);

            healthBar = new Rectangle(280, 10, 200, 20);
            healthBar.setFill(Color.RED);

            gameRoot.getChildren().addAll(healthBarBackground, healthBar);
        } catch (NullPointerException e) {
            logError(e.getMessage());
        } catch (Exception e) {
            logError("An error occurred while initializing boss health bar: " + e.getMessage());
        }
    }

    private void updateBossHealthBar() {
        try {
            if (boss == null) {
                throw new NullPointerException("Boss is not initialized.");
            }

            double healthPercentage = (double) boss.getHealth() / 100.0;
            if (healthPercentage < 0 || healthPercentage > 1) {
                throw new IllegalArgumentException("Boss health percentage is out of bounds.");
            }

            healthBar.setWidth(260 * healthPercentage);
        } catch (NullPointerException e) {
            logError(e.getMessage());
        } catch (IllegalArgumentException e) {
            logError(e.getMessage());
        } catch (Exception e) {
            logError("An error occurred while updating boss health bar: " + e.getMessage());
        }
    }



    private void startGame() {
        Pane gameRoot = new Pane();
        Scene gameScene = new Scene(gameRoot, GAMEWIDTH, GAMEHEIGHT);

        // Load your background image
        Image backgroundImage = new Image(getClass().getResource("/se233/project2/GAME_BG.jpg").toString());

        // Create an ImageView for your background image
        ImageView backgroundView = new ImageView(backgroundImage);

        // Optional: set the size of the ImageView if you want to scale/stretch it
        backgroundView.setFitWidth(GAMEWIDTH);
        backgroundView.setFitHeight(GAMEHEIGHT);

        // Add the background ImageView to the gameRoot pane first
        gameRoot.getChildren().add(backgroundView);

        playerShip = new PlayerShip(GAMEWIDTH / 2 - 30, GAMEHEIGHT - 40, GAMEHEIGHT);
        // position player ship at the bottom center
        gameRoot.getChildren().add(playerShip.getSprite());
        initializePlayerLivesDisplay(gameRoot);  // Initialize the heart lives display

        // Initialization and setup for the warningText
        warningText = new javafx.scene.text.Text("");
        warningText.setFill(Color.RED);
        warningText.setFont(new Font(35));
        warningText.setTextAlignment(TextAlignment.CENTER);

        // Adjust X and Y positions after setting its content (so that we can compute its width and height)
        warningText.textProperty().addListener((observable, oldValue, newValue) -> {
            warningText.setX(GAMEWIDTH / 2 - warningText.getLayoutBounds().getWidth() / 2);
            warningText.setY(GAMEHEIGHT / 2 - warningText.getLayoutBounds().getHeight() / 2);
        });

        gameRoot.getChildren().add(warningText);

        //Score text
        scoreText = new javafx.scene.text.Text(710, 30, "Score: 0");
        scoreText.setFill(Color.YELLOW);
        scoreText.setFont(new Font(20));
        gameRoot.getChildren().add(scoreText);

        // Detect when keys are pressed
        gameScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                case A:
                    moveLeft = true;
                    break;
                case RIGHT:
                case D:
                    moveRight = true;
                    break;
                case SPACE:
                    PlayerBullet playerBullet = new PlayerBullet(playerShip.getX() + playerShip.getSprite().getFitWidth() / 2 - 2.5, playerShip.getY(), playerShip);
                    playerBullets.add(playerBullet);
                    gameRoot.getChildren().add(playerBullet.getSprite());
                    break;
                case B:
                    Bomb bomb = playerShip.dropBomb();
                    gameRoot.getChildren().add(bomb.getSprite());
                    bombs.add(bomb);
                    break;
            }
        });


        // Detect when keys are released
        gameScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                case A:
                    moveLeft = false;
                    break;
                case RIGHT:
                case D:
                    moveRight = false;
                    break;
            }
        });

        // Add Common enemy ships
        for (int i = 0; i < 10; i++) { // example, spawn 10 enemies
            CommonEnemy enemy = new CommonEnemy(randomXPosition(), randomYPosition(), GAMEWIDTH);

            commonEnemies.add(enemy);
            gameRoot.getChildren().add(enemy.getSprite());
        }
        // Add Uncommon enemy ships
        for (int i = 0; i < 5; i++) { // example, spawn 5 uncommon enemies
            UncommonEnemy uncommonEnemy = new UncommonEnemy(randomXPosition(), randomYPosition(), GAMEWIDTH);

            uncommonEnemies.add(uncommonEnemy);
            gameRoot.getChildren().add(uncommonEnemy.getSprite());
        }

        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Space Invaders Game");


        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 1. Handle player actions
                handlePlayerMovement();

                // 2. Handle bullet actions
                handlePlayerBulletActions();

                // 3. Handle enemy actions
                handleCommonEnemyActions();
                handleUncommonEnemyActions();

                // 4. Handle boss actions, if active
                handleBossActions();

                // 5. Handle bullet and bomb movements
                bossBullets.forEach(BossBullet::BossBulletMoveDown);
                bombs.forEach(Bomb::moveDown);

                // 6. Handle collisions
                handleCollisions();

                // 7. Cleanup operations
                cleanupOutOfBoundsBullets();
                cleanupOutOfBoundsEnemyBullets();
                cleanupOutOfBoundsBossBullets();
            }

            private void handleBossActions() {
                if (bossActive) {
                    // Boss movement and animation
                    boss.move();
                    LOGGER.info("Boss moved.");

                    bossFrameCounter++;
                    if (bossFrameCounter % bossUpdateEveryNFrames == 0) {
                        boss.updateAnimationFrame();
                        LOGGER.debug("Boss animation frame updated."); // FINEST in java.util.logging is roughly equivalent to DEBUG in Log4j2
                    }

                    // Boss shooting logic
                    handleBossShooting();
                } else {
                    LOGGER.info("Boss is not active."); // FINE in java.util.logging is roughly equivalent to INFO in Log4j2
                }
            }


            private void handleBossShooting() {
                if (boss.decideToShoot()) {
                    BossBullet bullet = boss.shoot();
                    gameRoot.getChildren().add(bullet.getSprite());
                    bossBullets.add(bullet);
                    LOGGER.info("Boss decided to shoot.");
                } else {
                    LOGGER.debug("Boss decided not to shoot."); // FINE in java.util.logging is roughly equivalent to DEBUG in Log4j2
                }
            }

            private void handlePlayerMovement() {
                frameCounter++;

                // Handle player's movement
                if (moveLeft) {
                    playerShip.moveLeft();
                    LOGGER.info("Player ship moved left");
                }
                if (moveRight) {
                    playerShip.moveRight();
                    LOGGER.info("Player ship moved right");
                }

                // Update player animation frame
                updatePlayerAnimationFrame();
            }


            private void updatePlayerAnimationFrame() {
                if (frameCounter % updateEveryNFrames == 0) {
                    playerShip.goToNextFrame();
                    LOGGER.debug("Player ship animation frame updated");
                }
            }

            private void handlePlayerBulletActions() {
                LOGGER.info("Handling player bullet actions");
                moveAllPlayerBulletsUp();
            }

            private void moveAllPlayerBulletsUp() {
                playerBullets.forEach(PlayerBullet::moveUp);
                LOGGER.debug("Moved all player bullets up");
            }




            private void handleCommonEnemyActions() {
                try {
                    List<EnemyBullet> bulletsToShoot = new ArrayList<>();

                    commonEnemies.forEach(enemy -> {
                        enemy.move();
                        LOGGER.debug("Common enemy moved");

                        // Animation logic
                        FRAMECHANGECOUNTER++;
                        if (FRAMECHANGECOUNTER >= FRAMECHANGETHRESHOLD) {
                            enemy.goToNextFrame();
                            LOGGER.trace("Common enemy animation frame updated");
                            FRAMECHANGECOUNTER = 0;
                        }

                        if (enemy.decideToShoot()) {
                            EnemyBullet bullet = enemy.shoot();
                            bulletsToShoot.add(bullet);
                            gameRoot.getChildren().add(bullet.getSprite());
                            LOGGER.info("Common enemy decided to shoot");
                        }

                        if (checkPlayerEnemyCollision(playerShip, enemy)) {
                            this.stop();
                            displayGameOver();
                            LOGGER.error("Player collided with common enemy. Game over.");
                        }
                    });

                    enemyBullets.addAll(bulletsToShoot);
                    enemyBullets.forEach(EnemyBullet::moveDown);

                } catch (Exception e) {
                    LOGGER.error("Error in handleCommonEnemyActions: {}", e.getMessage(), e);
                }
            }



            private void handleUncommonEnemyActions() {
                try {
                    List<EnemyBullet> bulletsToShoot = new ArrayList<>();

                    uncommonEnemies.forEach(enemy -> {
                        enemy.move();

                        // Animation logic
                        FRAMECHANGECOUNTER++;
                        if (FRAMECHANGECOUNTER >= FRAMECHANGETHRESHOLD) {
                            enemy.goToNextFrame();
                            FRAMECHANGECOUNTER = 0;
                        }

                        if (enemy.decideToShoot()) {
                            EnemyBullet bullet = enemy.shoot();
                            bulletsToShoot.add(bullet);
                            gameRoot.getChildren().add(bullet.getSprite());
                        }

                        if (checkPlayerEnemyCollision(playerShip, enemy)) {
                            this.stop();
                            displayGameOver();
                        }
                    });

                    enemyBullets.addAll(bulletsToShoot);
                    enemyBullets.forEach(EnemyBullet::moveDown);

                } catch (Exception e) {
                    logError("An error occurred while handling uncommon enemy actions: " + e.getMessage());
                }
            }



            private void handleCollisions() {
                List<PlayerBullet> bulletsToRemove = new ArrayList<>();
                List<CommonEnemy> commonEnemiesToRemove = new ArrayList<>();
                List<UncommonEnemy> uncommonEnemiesToRemove = new ArrayList<>();
                List<EnemyBullet> enemyBulletsToRemove = new ArrayList<>();
                List<BossBullet> bossBulletsToRemove = new ArrayList<>();
                List<Bomb> bombsToRemove = new ArrayList<>();

                for (PlayerBullet playerBullet : playerBullets) {
                    for (CommonEnemy enemy : commonEnemies) {
                        if (checkBulletEnemyCollision(playerBullet, enemy)) {
                            bulletsToRemove.add(playerBullet);
                            commonEnemiesToRemove.add(enemy);
                            gameRoot.getChildren().removeAll(playerBullet.getSprite(), enemy.getSprite());
                            createExplosion(playerBullet.getSprite().getX(), playerBullet.getSprite().getY());

                            score += 10;  // Award 10 points for hitting a CommonEnemy
                            LOGGER.info("Player hit a CommonEnemy. Score increased by 10.");
                        }
                    }

                    for (UncommonEnemy enemy : uncommonEnemies) {
                        if (checkBulletEnemyCollision(playerBullet, enemy)) {
                            bulletsToRemove.add(playerBullet);
                            uncommonEnemiesToRemove.add(enemy);
                            gameRoot.getChildren().removeAll(playerBullet.getSprite(), enemy.getSprite());
                            createExplosion(playerBullet.getSprite().getX(), playerBullet.getSprite().getY());

                            score += 15;  // Award 15 points for hitting an UncommonEnemy
                            LOGGER.info("Player hit an UncommonEnemy. Score increased by 15.");
                        }
                    }

                    // After the loops that handle collisions with CommonEnemy and UncommonEnemy, add:
                    if (bossActive && checkPlayerBulletBossCollision(playerBullet, boss)) {
                        bulletsToRemove.add(playerBullet);
                        gameRoot.getChildren().remove(playerBullet.getSprite());
                        boss.reduceHealth(5); // Let's say each bullet reduces health by 5 units. Adjust as necessary.
                        LOGGER.info("Player bullet hit the boss. Boss health reduced by 5.");

                        updateBossHealthBar(); // Assuming you will have a visual representation of the boss's health.
                        if (boss.getHealth() <= 0) {
                            gameRoot.getChildren().remove(boss.getSprite());  // Remove the boss from the scene.
                            LOGGER.error("Boss defeated. Transitioning to End Game scene.");

                            this.stop();  // Stop the game loop.
                            endGameScene();  // Transition to the End Game scene.
                            return;  // Exit out of the method to prevent any further processing.
                        }
                    }

                }

                // Collision check for Bombs with BossEnemy
                for (Bomb bomb : bombs) {
                    if (bossActive && checkBombBossCollision(bomb, boss)) {
                        bombsToRemove.add(bomb);
                        gameRoot.getChildren().remove(bomb.getSprite());
                        boss.reduceHealth(10); // Adjust the damage value as necessary. Here, a bomb reduces health by 10 units.
                        LOGGER.info("Bomb hit the boss. Boss health reduced by 10.");

                        createExplosion(bomb.getSprite().getX(), bomb.getSprite().getY());
                        updateBossHealthBar(); // Update the visual representation of the boss's health.

                        if (boss.getHealth() <= 0) {
                            gameRoot.getChildren().remove(boss.getSprite());  // Remove the boss from the scene.
                            LOGGER.error("Boss defeated by a bomb. Transitioning to End Game scene.");

                            this.stop();  // Stop the game loop.
                            endGameScene();  // Transition to the End Game scene.
                            return;  // Exit out of the method to prevent any further processing.
                        }
                    }
                }

                // Inside handleCollisions method where player is hit by enemy bullet:
                for (EnemyBullet enemyBullet : enemyBullets) {
                    if (checkBulletPlayerCollision(enemyBullet, playerShip)) {
                        enemyBulletsToRemove.add(enemyBullet);
                        gameRoot.getChildren().remove(enemyBullet.getSprite());
                        LOGGER.warn("Player hit by enemy bullet. Lives left: " + (playerLives-1));

                        // Decrement player's life count and update the displayed lives.
                        playerLives--;
                        updatePlayerLivesDisplay();

                        // Display the warning
                        showWarningText1("You Losing a Life!");

                        // Schedule the warning to disappear after a short duration (e.g., 2 seconds)
                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(e -> warningText.setText(""));
                        pause.play();

                        // If player's lives are depleted, end the game
                        if (playerLives <= 0) {
                            this.stop();  // Stop the game loop
                            LOGGER.error("Player lives depleted. Game over.");
                            displayGameOver();  // Display game over message or transition to a game over screen
                            return;
                        }
                    }
                }



                // Inside handleCollisions method where player is hit by boss bullet:
                for (BossBullet bossBullet : bossBullets) {
                    if (checkBossBulletPlayerCollision(bossBullet, playerShip)) {
                        gameRoot.getChildren().remove(bossBullet.getSprite());
                        bossBulletsToRemove.add(bossBullet);
                        LOGGER.warn("Player hit by boss bullet. Lives left: " + (playerLives-1));

                        // Decrement player's life count and update the displayed lives.
                        playerLives--;
                        updatePlayerLivesDisplay();

                        // Display the "You're losing life!" message
                        Text loseLifeText = new Text(500 / 2, 600 / 2, "You're losing life!");
                        loseLifeText.setFont(new Font(30));
                        loseLifeText.setFill(Color.RED);
                        gameRoot.getChildren().add(loseLifeText);

                        // After a short duration (e.g., 2 seconds), remove the "You're losing life!" message
                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(e -> gameRoot.getChildren().remove(loseLifeText));
                        pause.play();

                        // If player's lives are depleted, end the game
                        if (playerLives <= 0) {
                            this.stop();  // Stop the game loop
                            LOGGER.error("Player lives depleted after hit by boss bullet. Game over.");
                            displayGameOver();  // Display game over message or transition to a game over screen
                            return;
                        }
                    }
                }

                for (Bomb bomb : bombs) {
                    for (CommonEnemy enemy : commonEnemies) {
                        if (bomb.getSprite().getBoundsInParent().intersects(enemy.getSprite().getBoundsInParent())) {
                            bombsToRemove.add(bomb);
                            commonEnemiesToRemove.add(enemy);
                            gameRoot.getChildren().removeAll(bomb.getSprite(), enemy.getSprite());
                            createExplosion(bomb.getSprite().getX(), bomb.getSprite().getY());

                            score += 50;  // Award points for hitting an enemy with a bomb.
                            LOGGER.info("Bomb hit a CommonEnemy. Score increased by 50. Current Score: {}", score);
                        }
                    }

                    for (UncommonEnemy enemy : uncommonEnemies) {
                        if (bomb.getSprite().getBoundsInParent().intersects(enemy.getSprite().getBoundsInParent())) {
                            bombsToRemove.add(bomb);
                            uncommonEnemiesToRemove.add(enemy);
                            gameRoot.getChildren().removeAll(bomb.getSprite(), enemy.getSprite());
                            createExplosion(bomb.getSprite().getX(), bomb.getSprite().getY());

                            score += 100;  // Award more points for hitting an uncommon enemy with a bomb.
                            LOGGER.info("Bomb hit an UncommonEnemy. Score increased by 100. Current Score: {}", score);
                        }
                    }

                    // Add more enemy types as required
                }



                scoreText.setText("Score: " + score);

                playerBullets.removeAll(bulletsToRemove);
                commonEnemies.removeAll(commonEnemiesToRemove);
                uncommonEnemies.removeAll(uncommonEnemiesToRemove);
                enemyBullets.removeAll(enemyBulletsToRemove);
                bossBullets.removeAll(bossBulletsToRemove);
                bombs.removeAll(bombsToRemove);

                // After removing the enemies, check if all enemies are eliminated
                if (commonEnemies.isEmpty() && uncommonEnemies.isEmpty()) {
                    showWarningText2("All enemies eliminated!");
                    LOGGER.info("All common and uncommon enemies have been eliminated.");
                }

                // Check if all enemies are defeated and the boss isn't active yet
                if (commonEnemies.isEmpty() && uncommonEnemies.isEmpty() && !bossActive) {
                    introduceBoss();
                    LOGGER.info("All enemies defeated. Introducing the boss...");
                }

            }

            private void introduceBoss() {
                boss = new BossEnemy(GAMEWIDTH / 2 - 50, 10, GAMEWIDTH);  // Spawning boss at top-center
                gameRoot.getChildren().add(boss.getSprite());
                bossActive = true;
                LOGGER.info("Boss introduced at top-center of the game field.");

                initializeBossHealthBar(gameRoot);
                LOGGER.info("Boss health bar initialized.");
            }





            private void createExplosion(double x, double y) {
                // Assuming each frame in the sprite sheet is 64x64 pixels (change as per your sprite sheet)
                final double FRAME_WIDTH = 40;
                final double FRAME_HEIGHT = 40;

                // Assuming there are 10 frames in the explosion animation (change as per your sprite sheet)
                final int TOTAL_FRAMES = 4;

                // Load the entire sprite sheet using a relative path
                URL resourceUrl = getClass().getResource("/se233/project2/spritesheet_Explosion.png"); // Change the path
                if (resourceUrl == null) {
                    System.err.println("Could not find explosion sprite sheet!");
                    LOGGER.error("Failed to load explosion sprite sheet from resources.");
                    return;
                }
                Image explosionSpriteSheet = new Image(resourceUrl.toString());
                ImageView explosion = new ImageView(explosionSpriteSheet);

                // Set the initial viewport to show the first frame
                explosion.setViewport(new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT));

                // Set the position of the ImageView using the passed in x and y coordinates
                explosion.setX(x - FRAME_WIDTH / 2);  // Centered on the passed in x coordinate
                explosion.setY(y - FRAME_HEIGHT / 2); // Centered on the passed in y coordinate

                gameRoot.getChildren().add(explosion);
                LOGGER.info("Explosion effect added at coordinates x={}, y={}.", x, y);

                // Create a Timeline to animate the explosion
                Timeline explosionAnimation = new Timeline();

                for (int i = 0; i < TOTAL_FRAMES; i++) {
                    final int frameIndex = i;
                    explosionAnimation.getKeyFrames().add(new KeyFrame(
                            Duration.seconds(0.05 * (i + 1)), // assuming each frame lasts 0.05 seconds
                            event -> explosion.setViewport(new Rectangle2D(FRAME_WIDTH * frameIndex, 0, FRAME_WIDTH, FRAME_HEIGHT))
                    ));
                }

                explosionAnimation.setOnFinished(e -> {
                    gameRoot.getChildren().remove(explosion);
                    LOGGER.info("Explosion animation completed and removed from the game scene.");
                });

                explosionAnimation.play();
                LOGGER.info("Explosion animation started.");
            }


            private void cleanupOutOfBoundsBullets() {
                playerBullets.removeIf(playerBullet -> {
                    if (playerBullet.getSprite().getY() < 0) {
                        gameRoot.getChildren().remove(playerBullet.getSprite());
                        LOGGER.info("Removed out-of-bounds player bullet at Y={0}.", playerBullet.getSprite().getY());
                        return true;
                    }
                    return false;
                });
            }

            private void cleanupOutOfBoundsEnemyBullets() {
                bossBullets.removeIf(bossBullet -> {
                    if (bossBullet.getSprite().getY() > GAMEHEIGHT) {
                        gameRoot.getChildren().remove(bossBullet.getSprite());
                        LOGGER.info("Removed out-of-bounds enemy bullet at Y={0}.", bossBullet.getSprite().getY());
                        return true;
                    }
                    return false;
                });
            }

            private void cleanupOutOfBoundsBossBullets() {
                bossBullets.removeIf(bossBullet -> {
                    if (bossBullet.getSprite().getY() > GAMEHEIGHT) {
                        gameRoot.getChildren().remove(bossBullet.getSprite());
                        LOGGER.info("Removed out-of-bounds boss bullet at Y={0}.", bossBullet.getSprite().getY());
                        return true;
                    }
                    return false;
                });
            }

        }.start();
    }




    // Utility functions for generating random positions
    private double randomXPosition() {
        return Math.random() * (GAMEWIDTH - 60);  // adjust as needed
    }

    private double randomYPosition() {
        return Math.random() * (GAMEWIDTH / 2);  // only on the top half of the screen
    }

    // Collision detection functions for CommonEnemy
    private boolean checkBulletEnemyCollision(PlayerBullet playerBullet, CommonEnemy enemy) {
        return playerBullet.getSprite().getBoundsInParent().intersects(enemy.getSprite().getBoundsInParent());
    }

    private boolean checkPlayerEnemyCollision(PlayerShip player, CommonEnemy enemy) {
        return player.getSprite().getBoundsInParent().intersects(enemy.getSprite().getBoundsInParent());
    }

    // Collision detection functions for UncommonEnemy
    private boolean checkBulletEnemyCollision(PlayerBullet playerBullet, UncommonEnemy enemy) {
        return playerBullet.getSprite().getBoundsInParent().intersects(enemy.getSprite().getBoundsInParent());
    }

    private boolean checkPlayerEnemyCollision(PlayerShip player, UncommonEnemy enemy) {
        return player.getSprite().getBoundsInParent().intersects(enemy.getSprite().getBoundsInParent());
    }

    // Collision detection functions for PlayerShip
    private boolean checkBulletPlayerCollision(EnemyBullet enemyBullet, PlayerShip playerShip) {
        return enemyBullet.getSprite().getBoundsInParent().intersects(playerShip.getSprite().getBoundsInParent());
    }

    private boolean checkBossBulletPlayerCollision(BossBullet bossBullet, PlayerShip playerShip) {
        return bossBullet.getSprite().getBoundsInParent().intersects(playerShip.getSprite().getBoundsInParent());
    }

    // Collision detection functions for BossEnemy
    private boolean checkPlayerBulletBossCollision(PlayerBullet playerBullet, BossEnemy boss) {
        return playerBullet.getSprite().getBoundsInParent().intersects(boss.getSprite().getBoundsInParent());
    }

    private boolean checkBombBossCollision(Bomb bomb, BossEnemy boss) {
        return bomb.getSprite().getBoundsInParent().intersects(boss.getSprite().getBoundsInParent());
    }





    public static void main(String[] args) {
        launch(args);
    }
}
