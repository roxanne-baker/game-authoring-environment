package testing.animation;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import model.component.visual.AnimatedSprite;
import model.entity.Entity;
import view.editor.eventeditor.AnimationChooser;

public class AnimationEditorTesting extends Application {
    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        Entity entity = new Entity();
        entity.addComponent(new AnimatedSprite());
//		AnimationEditor animatorEditor = new AnimationEditor(entity);
//		animatorEditor.populateLayout();
        String animationName = new AnimationChooser(entity).initChooser();
        System.out.println(animationName);
        Group g = new Group();
//		Scene scene = new Scene(g);
//		g.getChildren().add(animatorEditor.getPane());
//		stage.setScene(scene);
//		stage.show();

    }
}
