package se233.project2.Character;

public class CharacterController {
    private final CharacterModel model;
    private final CharacterView view;

    public CharacterController(CharacterModel model, CharacterView view) {
        this.model = model;
        this.view = view;
    }

    // Movement methods
    public void moveLeft() {
        model.moveLeft();
        updateView();
    }

    public void moveRight() {
        model.moveRight();
        updateView();
    }

    // Update the view's position based on the model's data
    private void updateView() {
        view.updatePosition(model.getPositionX(), model.getPositionY());
    }
}
