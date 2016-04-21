package api;

import java.util.List;

import datamanagement.XMLReader;

/**
 * Created by rhondusmithwick on 3/31/16.
 *
 * @author Rhondu Smithwick
 */
public interface ISystemManager {

	/**
	 * This will play the game loop.
	 */
	void play();

	/**
	 * This will pause the game loop.
	 */
	void pauseLoop();

	/**
	 * This will step the game's loop.
	 */
	void step(double dt);

	/**
	 * Get the main entity system
	 *
	 * @return IEntitySystem-type entity system
	 */
	IEntitySystem getEntitySystem();

	/**
	 * Get the shared entity system
	 *
	 * @return IEntitySystem-type entity system
	 */
	IEntitySystem getSharedEntitySystem();

	void saveEntitySystem(String filename);

	void saveSharedEntitySystem(String filename);

	void loadEntitySystem(String filename);

	void loadSharedEntitySystem(String filename);

	void moveEntitiesToMainSystem(IEntity... entities);

	void moveEntitiesToMainSystem(String... ids);

	void moveEntitiesToSharedSystem(IEntity... entities);

	void moveEntitiesToSharedSystem(String... ids);

	@Deprecated
	/**
	 * Get the current event system
	 *
	 * @return IEventSystem-type event system
	 */
	IEventSystem getEventSystem();

	@Deprecated
	/**
	 * Reads objects from file with specified fileName. Must be of type T.
	 *
	 * @param fileName
	 *            the file
	 * @param <T>
	 *            the type of all the objects.
	 * @return list of objects of type T
	 */
	static <T extends ISerializable> List<T> evaluate(String fileName) {
		return new XMLReader<T>().readFromFile(fileName);
	}

	@Deprecated
	/**
	 * Reads objects from specified string. Must be of type T.
	 *
	 * @param stringToReadFrom
	 *            the string
	 * @param <T>
	 *            the type of all the objects.
	 * @return list of objects of type T
	 */
	static <T extends ISerializable> List<T> evaluateString(String stringToReadFrom) {
		return new XMLReader<T>().readFromString(stringToReadFrom);
	}

	void setEntitySystem(IEntitySystem system);

	IEntitySystem getUniverse();

	void saveSystem(String filename);

	List<String> getDetails();

	void setDetails(List<String> list);

}
