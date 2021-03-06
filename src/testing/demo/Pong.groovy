package testing.demo

import api.*
import events.Action
import events.KeyTrigger
import model.component.character.Score
import model.component.character.UserControl
import model.component.movement.Position
import model.component.physics.Collision

/**
 *
 * @author Tom
 *
 */
public class Pong implements IGameScript {
    public static final String PATH = "src/testing/demo/";
    private final String movePaddleScript = PATH + "MovePaddle.groovy";
    private final int winningScore = 3;

    private ISystemManager game;
    private ILevel universe;
    private IPhysicsEngine physics;
    private IEventSystem events;

    @Override
    public void init(GroovyShell shell, ISystemManager game) {
        this.game = game;
        this.universe = game.getLevel();
        this.physics = game.getLevel().getPhysicsEngine();
        this.events = universe.getEventSystem();

        // TODO: figure out why these don't work
        //		this.engine.put("game", this.model);
        //		this.engine.put("universe", this.model.getEntitySystem());
        //		this.engine.put("demo", new GroovyDemoTest());
        shell.setVariable("demo", new GroovyDemoTest());

        initKeyInputs();
        initSprites();
    }

    private void initSprites() {
        // Ball
        IEntity ball = SpriteLoader.createBall("Ball", new Position(190.0, 190.0));
        //Paddles
        IEntity leftPaddle = SpriteLoader.createPaddle("LeftPaddle", new Position(100, 160));
        leftPaddle.addComponent(new UserControl());
        IEntity rightPaddle = SpriteLoader.createPaddle("RightPaddle", new Position(500, 160));
        rightPaddle.addComponent(new UserControl());
        // Walls
        IEntity leftWall = SpriteLoader.createPlatform("LeftWall", new Position(-500, 7));
        IEntity rightWall = SpriteLoader.createPlatform("RightWall", new Position(800, 7));
        IEntity ceiling = SpriteLoader.createPlatform("Ceiling", new Position(160, -500));
        IEntity floor = SpriteLoader.createPlatform("Floor", new Position(160, 500));

        universe.addEntities(ball, leftPaddle, rightPaddle, leftWall, rightWall, ceiling, floor);
    }

    private void initKeyInputs() {
        Map<String, Object> wKey = new HashMap<>();
        wKey.put("key", "W");
        wKey.put("player", "LeftPaddle");
        Map<String, Object> sKey = new HashMap<>();
        sKey.put("key", "S");
        sKey.put("player", "LeftPaddle");
        Map<String, Object> dKey = new HashMap<>();
        dKey.put("key", "D");
        dKey.put("player", "LeftPaddle");
        events.registerEvent(new KeyTrigger("W"), new Action(movePaddleScript, wKey));
        events.registerEvent(new KeyTrigger("S"), new Action(movePaddleScript, sKey));
        events.registerEvent(new KeyTrigger("D"), new Action(movePaddleScript, dKey));

        Map<String, Object> iKey = new HashMap<>();
        iKey.put("key", "I");
        iKey.put("player", "RightPaddle");
        Map<String, Object> kKey = new HashMap<>();
        kKey.put("key", "K");
        kKey.put("player", "RightPaddle");
        Map<String, Object> lKey = new HashMap<>();
        lKey.put("key", "L");
        lKey.put("player", "RightPaddle");
        events.registerEvent(new KeyTrigger("I"), new Action(movePaddleScript, iKey));
        events.registerEvent(new KeyTrigger("K"), new Action(movePaddleScript, kKey));
        events.registerEvent(new KeyTrigger("L"), new Action(movePaddleScript, lKey));
    }

    @Override
    public void update(double dt) {
        physics.update(universe, dt);
//        events.updateInputs(dt);
        updateGameLogic();
    }

    private void updateGameLogic() {
        // Check for game over
        for (IEntity e : universe.getEntitiesWithComponents(Score.class)) {
            Score score = e.getComponent(Score.class);
            if (score.getScore() >= winningScore) {
                System.out.println(e.getName() + " has won.");
            }
        }

        // Update scores
        IEntity ball = universe.getEntitiesWithName("Ball").get(0);
        String ballID = ball.getID();

        IEntity leftWall = universe.getEntitiesWithName("LeftWall").get(0);
        String leftColStr = leftWall.getComponent(Collision.class).getCollidingIDs();
//        print(leftColStr);
        if (leftColStr.contains(ballID)) {
            IEntity rightPaddle = universe.getEntitiesWithName("RightPaddle").get(0);
            Score s = rightPaddle.getComponent(Score.class);
            s.increment();
            System.out.println("Right: " + s.getScore());
        }

        IEntity rightWall = universe.getEntitiesWithName("RightWall").get(0);
        String rightColStr = rightWall.getComponent(Collision.class).getCollidingIDs();
//        print(rightColStr);
        if (rightColStr.contains(ballID)) {
            IEntity leftPaddle = universe.getEntitiesWithName("LeftPaddle").get(0);
            Score s = leftPaddle.getComponent(Score.class);
            s.increment();
            System.out.println("Left: " + s.getScore());
        }
    }

}
