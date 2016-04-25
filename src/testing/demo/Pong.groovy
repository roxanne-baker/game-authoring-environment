package testing.demo

import api.*
import events.Action
import javafx.scene.image.ImageView
import javafx.scene.input.KeyEvent
import model.component.character.Health
import model.component.character.Score
import model.component.movement.Position
import model.component.movement.Velocity
import model.component.physics.Collision
import model.component.physics.Gravity
import model.component.physics.Mass
import model.component.physics.RestitutionCoefficient
import model.component.visual.ImagePath
import model.entity.Entity
import model.physics.PhysicsEngine

/**
 * 
 * @author Tom
 *
 */
public class Pong implements IGameScript {
	private ISystemManager game;
	private ILevel universe;
	private IPhysicsEngine physics = new PhysicsEngine();
    private IEventSystem events;

	public void init(GroovyShell shell, ISystemManager game) {
		this.game = game;
		this.universe = game.getEntitySystem();
        this.events = universe.getEventSystem();

		// TODO: figure out why these don't work
		//		this.engine.put("game", this.model);
		//		this.engine.put("universe", this.model.getEntitySystem());
		//		this.engine.put("demo", new GroovyDemoTest());
		shell.setVariable("game", this.game);
		shell.setVariable("universe", universe);
		shell.setVariable("demo", new GroovyDemoTest());

	}

    private void initKeyInputs() {
        events.registerEvent(new KeyEvent("W"), new Action("testing/demo/"));
    }

	public void update(double dt) {
		this.physics.update(universe, dt);
	}

	private class GroovyDemoTest {
		IEntity getRhondu() {
			IEntity character = new Entity("Rhondu")
			character.addComponent(new Health((double) 100))
			character.addComponent(new Score((double) 100))
			Position pos = new Position(50.0, -150.0)
			character.addComponent(pos)
			ImagePath path = new ImagePath();
			ImageView img = path.getImageView();
			img.setScaleX(0.05);
			img.setScaleY(0.05);
			character.addComponents(path, new Velocity(20.0, 0.0), new Gravity(),
					new Collision("ball"), new RestitutionCoefficient(1.0), new Mass(5));
			return character;
		}

		IEntity getPlatform() {
			IEntity platform = new Entity("Platform");
			ImagePath path = new ImagePath();
			ImageView img = path.getImageView();
			platform.addComponents(path, new Position(100, 300),
					new Collision("platform"), new RestitutionCoefficient(1.2), new Mass(100));
			return platform;
		}

        IEntity getLeftWall() {
            IEntity platform = new Entity("RightWall");
            ImagePath path = new ImagePath();
            ImageView img = path.getImageView();

            platform.addComponents(path, new Position(-78, 7),
                    new Collision("platform"), new RestitutionCoefficient(1.2), new Mass(100));
            return platform;
        }

        IEntity getRightWall() {
            IEntity platform = new Entity("RightWall");
            ImagePath path = new ImagePath();
            ImageView img = path.getImageView();

            platform.addComponents(path, new Position(686, 7),
                    new Collision("platform"), new RestitutionCoefficient(1.2), new Mass(100));
            return platform;
        }

        @Deprecated
		void run(ISystemManager game) {
			ILevel universe = game.getEntitySystem();
			universe.addEntities(this.getRhondu(), this.getPlatform());
		}
	}

}
