package events;

import api.IEventSystem;
import api.ILevel;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import datamanagement.XMLReader;
import datamanagement.XMLWriter;
import javafx.scene.input.KeyEvent;
import utility.Pair;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/***
 * Created by ajonnav 04/12/16
 *
 * @author Anirudh Jonnavithula, Carolyn Yao For non-key events, we want to
 *         write a string that denotes which entity and which property to watch. 
 *         For a key event, we ask the inputSystem to listen, and write the 
 *         key character to file to write to data. 
 *         We also write to file the Action that corresponds to the property change or 
 *         key event. When we read the file, aka play the game, we read the strings from data files
 *         and create Triggers, which add listeners to said properties or keys. The
 *         Triggers are mapped to Actions in the EventSystem map. 
 */
public class EventSystem implements Observer, IEventSystem {
    private final InputSystem inputSystem = new InputSystem();
    private transient ILevel universe;
    private ListMultimap<Trigger, Action> actionMap = ArrayListMultimap.create();

    public EventSystem(ILevel universe) {
        setUniverse(universe);
    }

    @Override
    public void registerEvent(Trigger trigger, Action action) {
        actionMap.put(trigger, action);
        if (!actionMap.containsKey(trigger)) {
            trigger.addObserver(this);
            trigger.addHandler(universe, inputSystem);
        }
    }

    @Override
    public void updateInputs() {
        this.inputSystem.processInputs();
    }

    @Override
    public void takeInput(KeyEvent k) {
        this.inputSystem.takeInput(k);
    }

    @Override
    public void update(Observable o, Object arg) {
        List<Action> actions = actionMap.get((Trigger) o);
        actions.stream().forEach(e -> e.activate(universe));
    }

    private void unbindEvents() {
        stopObservingTriggers(actionMap);
        clearListeners();
    }

    private void bindEvents() {
        watchTriggers(actionMap);
        addHandlers();
    }

    @Override
    public void setUniverse(ILevel universe) {
        this.unbindEvents();
        this.universe = universe;
        this.bindEvents();
    }

    @Override
    public File saveEventsToFile(String filepath) {
        this.unbindEvents();
        new XMLWriter<Pair<Trigger, List<Action>>>().writeToFile(filepath, convertMapToList(actionMap));
        this.bindEvents();
        return new File(filepath);
    }

    @Override
    public void readEventsFromFilePath(String filepath) {
        List<Pair<Trigger, List<Action>>> eventList = new XMLReader<Pair<Trigger, List<Action>>>()
                .readFromFile(filepath);
        actionMap = convertListToMap(eventList);
        this.bindEvents();
    }

    @Override
    public void readEventsFromFile(File file) {
        List<Pair<Trigger, List<Action>>> eventList = new XMLReader<Pair<Trigger, List<Action>>>()
                .readFromFile(file.getPath());
        actionMap = convertListToMap(eventList);
        this.bindEvents();
    }

    @Override
    public String returnEventsAsString() {
        return new XMLWriter<Pair<Trigger, List<Action>>>().writeToString(convertMapToList(actionMap));
    }

    private ListMultimap<Trigger, Action> convertListToMap(List<Pair<Trigger, List<Action>>> eventList) {
        ListMultimap<Trigger, Action> returnMap = ArrayListMultimap.create();
        for (Pair<Trigger, List<Action>> event : eventList) {
            Trigger trigger = event._1();
            List<Action> actionList = event._2();
            actionList.stream().forEach(action -> returnMap.put(trigger, action));
        }
        return returnMap;
    }

    private List<Pair<Trigger, List<Action>>> convertMapToList(ListMultimap<Trigger, Action> map) {
        return map.keySet().stream().map(trigger -> new Pair<>(trigger, map.get(trigger))).collect(Collectors.toList());
    }

    private void stopObservingTriggers(ListMultimap<Trigger, Action> map) {
        for (Trigger trigger : map.keySet()) {
            trigger.deleteObserver(this);
        }
    }

    private void watchTriggers(ListMultimap<Trigger, Action> map) {
        for (Trigger trigger : map.keySet()) {
            trigger.addObserver(this);
        }
    }

    private void addHandlers() {
        actionMap.keySet().stream().forEach(trigger -> trigger.addHandler(universe, inputSystem));
    }

    private void clearListeners() {
        actionMap.keySet().stream().forEach(trigger -> trigger.clearListener(universe, inputSystem));
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        this.unbindEvents();
        out.defaultWriteObject();
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        watchTriggers(actionMap);
    }

}
