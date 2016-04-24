package testing.games;

/**
 * Created by cyao42 on 4/22/2016.
 */

import api.IEntity;
import api.ILevel;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import datamanagement.XMLReader;
import events.Action;
import events.EventSystem;
import api.IEventSystem;
import events.KeyTrigger;
import events.PropertyTrigger;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.component.character.Health;
import model.component.character.Score;
import model.component.movement.Position;
import model.component.movement.Velocity;
import model.component.physics.Collision;
import model.component.physics.Gravity;
import model.component.visual.ImagePath;
import model.entity.Entity;
import model.entity.Level;
import model.physics.PhysicsEngine;

import java.io.File;
import java.io.IOException;


import api.IEntity;
import api.ILevel;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import datamanagement.XMLReader;
import events.Action;
import events.EventSystem;
import events.KeyTrigger;
import events.PropertyTrigger;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.component.character.Health;
import model.component.character.Score;
import model.component.movement.Position;
import model.component.movement.Velocity;
import model.component.physics.Gravity;
import model.component.visual.ImagePath;
import model.entity.Entity;
import model.entity.Level;
import model.physics.PhysicsEngine;

import java.io.File;
import java.io.IOException;

public class CollisionTestGame {

    public static final String TITLE = "Collision test game";
    public static final int KEY_INPUT_SPEED = 5;
    private static Group root;
    private final ILevel universe = new Level();
    private IEventSystem eventSystem = universe.getEventSystem();
    private final PhysicsEngine physics = new PhysicsEngine();
    //private IEntity character;
    //private IEntity platform;
    private final String IMAGE_PATH_BLASTOISE = "resources/images/blastoise.png";
    private final String IMAGE_PATH_CHARIZARD = "resources/images/charizard.png";
    private final String healthScriptPath = "resources/groovyScripts/CollisionGameCollideHealth.groovy";
    private final String transformScriptPath = "resources/groovyScripts/CollisionGameHealthChangeImage.groovy";
    private final String moveRightScriptPath = "resources/groovyScripts/keyInputMoveRight.groovy";
    private final String moveLeftScriptPath = "resources/groovyScripts/keyInputMoveLeft.groovy";

    private final String moveRightScriptPath2 = "resources/groovyScripts/keyInputMoveRight2.groovy";
    private final String moveLeftScriptPath2 = "resources/groovyScripts/keyInputMoveLeft2.groovy";
    private final String jumpScriptPath = "resources/groovyScripts/keyInputJump.groovy";
    private Scene myScene;

    /**
     * Returns name of the game.
     */
    public String getTitle() {
        return TITLE;
    }

    /**
     * Create the game's scene
     */
    public Scene init(int width, int height) {
        // Create a scene graph to organize the scene
        root = new Group();
        // Create a place to see the shapes
        myScene = new Scene(root, width, height, Color.WHITE);
        myScene.setOnKeyPressed(e -> eventSystem.takeInput(e));
        initEngine();
        return myScene;
    }

    public void initEngine() {
        addCharacter("Anolyn", "blastoise.xml", IMAGE_PATH_BLASTOISE, 50.0, 200.0);
        addCharacter("Cani", "charizard.xml", IMAGE_PATH_CHARIZARD, 200.0, 200.0);
        propertyEventSetup("Anolyn", healthScriptPath, Position.class, "Fall");
        propertyEventSetup("Cani", transformScriptPath, Collision.class, "Collision ID");
        keyEventSetup("D", moveRightScriptPath);
        keyEventSetup("A", moveLeftScriptPath);
        keyEventSetup("W", jumpScriptPath);
        keyEventSetup("J", moveRightScriptPath2);
        keyEventSetup("L", moveLeftScriptPath2);
    }

    private void addCharacter(String name, String XMLName, String imagePath, Double posX, Double posY) {
        //int var = 0;
        //if (var == 0) {
            IEntity character = new Entity(name);
            character.forceAddComponent(new Health((double) 100), true);
            character.addComponent(new Collision("")); // instantiated by string instead of collection
            Position pos = new Position(posX, posY);
            character.forceAddComponent(pos, true);
            character.forceAddComponent(new ImagePath(imagePath), true);
            character.forceAddComponent(new Velocity(0, 0), true);
            universe.addEntity(character);
            character.addComponent(new ImagePath(imagePath));
            //character.addComponent(new Gravity(5000));
            character.serialize(XMLName);
        //}
//        else {
//            character = new XMLReader<IEntity>().readSingleFromFile("character.xml");
//            universe.addEntity(character);
//            eventSystem.readEventFromFile("eventtest.xml");
//        }
        drawCharacter(character);
    }

    public void propertyEventSetup(String charName, String scriptName, Class component, String propertyName) {
        eventSystem.registerEvent(
                new PropertyTrigger(universe.getEntitiesWithName(charName).get(0).getID(), component, propertyName),
                new Action(scriptName));
        //eventSystem.saveEventsToFile("eventtest.xml");
        //EventFileWriter w = new EventFileWriter();
        //w.addEvent(KeyTrigger.class.toString().split(" ")[1], "A", moveLeftScriptPath);
        //w.addEvent(KeyTrigger.class.toString().split(" ")[1], "D", moveRightScriptPath);
        //w.writeEventsToFile("eventTest2.xml");
    }

    public void keyEventSetup(String key, String scriptName) {
        eventSystem.registerEvent(new KeyTrigger(key), new Action(scriptName));
    }

    public void step(double dt) {
        physics.update(universe, dt);
        // inputSystem.processInputs();
        eventSystem.updateInputs();
        // moveEntity(character, 1);
    }

    public void drawCharacter(IEntity character) {
        ImagePath imgPath = character.getComponent(ImagePath.class);
        ImageView charSprite = imgPath.getImageView();
        charSprite.setFitHeight(100);
        charSprite.setPreserveRatio(true);
        root.getChildren().add(charSprite);
    }

    private Action getAction(String scriptPath) {
        String script = null;
        try {
            script = Files.toString(new File(scriptPath), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Action(script);
    }
}
