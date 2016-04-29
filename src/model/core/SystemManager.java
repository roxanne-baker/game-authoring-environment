package model.core;

import api.IEntity;
import api.IEventSystem;
import api.ILevel;
import api.ISystemManager;
import datamanagement.XMLReader;
import groovy.lang.GroovyShell;
import javafx.scene.Node;
import model.entity.Entity;
import model.entity.Level;
//import testing.demo.GroovyDemoTest;

/**
 * Created by rhondusmithwick on 3/31/16.
 *
 * @author Rhondu Smithwick
 */
public class SystemManager implements ISystemManager {

	private GroovyShell shell = new GroovyShell(); // CANNOT BE SCRIPT ENGINE
	private ILevel universe = new Level();
	private ILevel sharedUniverse = new Level();
	private boolean isRunning = true;
	private Node root;

	public SystemManager(Node root) {
		this(root, new Level());
	}

	public SystemManager(Node root, ILevel level) {
		this.root = root;
		this.universe = level;
		initLevel();
	}

	@Deprecated
	public SystemManager() {
	}

	@Deprecated
	public SystemManager(ILevel level) {
		this.universe = level;
		initLevel();
	}

	private void initLevel() {
		universe.init(shell, this);
		root.setOnKeyPressed(e -> universe.getEventSystem().takeInput(e)); // TODO: take in all inputs
		shell.setVariable("game", this);
		shell.setVariable("universe", universe);
		//shell.setVariable("demo", new GroovyDemoTest()); // TODO: remove
	}

	@Override
	public void pauseLoop() {
		System.out.println("paused");
		this.isRunning = false;
	}

	@Override
	public void step(double dt) {
		System.out.println("running: " + this.isRunning);
		if (this.isRunning) {
			universe.update(dt);
		}
	}

	@Override
	public ILevel getEntitySystem() {
		return this.universe;
	}

	@Deprecated
	@Override
	public IEventSystem getEventSystem() {
		System.out.println("Events deprecated in SystemManager.");
		System.exit(1);
		return null;
	}

	@Override
	public void play() {
		this.isRunning = true;
	}

	// private void readObject(ObjectInputStream in) throws IOException,
	// ClassNotFoundException {
	// in.defaultReadObject();
	// this.eventSystem = new EventSystem(universe);
	// this.physics = new PhysicsEngine();
	// }

	@Override
	public ILevel getSharedEntitySystem() {
		return this.sharedUniverse;
	}

	@Override
	public void saveLevel(String filename) {
		this.universe.serialize(filename);
	}

	@Override
	public void saveSharedLevel(String filename) {
		this.sharedUniverse.serialize(filename);
	}

	@Override
	public void loadLevel(String filename) {
		this.universe = new XMLReader<ILevel>().readSingleFromFile(filename);
		initLevel();
	}

	@Override
	public void loadSharedLevel(String filename) {
		this.sharedUniverse = new XMLReader<ILevel>().readSingleFromFile(filename);
	}

	private IEntity[] idsToEntityArray(ILevel system, String... ids) {
		IEntity[] entities = new Entity[ids.length];
		for (int i = 0; i < entities.length; i++) {
			entities[i] = system.getEntity(ids[i]);
		}
		return entities;
	}

	@Override
	public void moveEntitiesToMainSystem(IEntity... entities) {
		for (IEntity e : entities) {
			this.sharedUniverse.removeEntity(e.getID());
			this.universe.addEntity(e);
		}
	}

	@Override
	public void moveEntitiesToMainSystem(String... ids) {
		this.moveEntitiesToMainSystem(this.idsToEntityArray(this.sharedUniverse, ids));
	}

	@Override
	public void moveEntitiesToSharedSystem(IEntity... entities) {
		for (IEntity e : entities) {
			this.universe.removeEntity(e.getID());
			this.sharedUniverse.addEntity(e);
		}
	}

	@Override
	public void moveEntitiesToSharedSystem(String... ids) {
		this.moveEntitiesToSharedSystem(this.idsToEntityArray(this.universe, ids));
	}

	@Override
	public GroovyShell getShell() {
		return this.shell;
	}

}
