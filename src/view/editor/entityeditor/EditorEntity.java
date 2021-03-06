package view.editor.entityeditor;

import api.IComponent;
import api.IEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.component.visual.AnimatedSprite;
import view.componentdisplays.GuiObject;
import view.componentdisplays.GuiObjectFactory;
import view.editor.Editor;
import view.enums.DefaultStrings;
import view.utilities.ButtonFactory;
import view.utilities.TextFieldFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Melissa Zhang
 * @author Cali Nelson
 * @author Ben Zhang
 */

public class EditorEntity extends Editor {
    private final GuiObjectFactory guiFactory = new GuiObjectFactory();
    private final IEntity myEntity;
    private final String myLanguage;
    private final ResourceBundle myResources;
    private final ResourceBundle myComponentNames;
    private final ScrollPane scrollPane;
    private ObservableList<IEntity> entityList = FXCollections.observableArrayList();
    private TextField name;
    private List<String> myComponents;
    private VBox container;


    public EditorEntity (String language, IEntity toEdit) {
        scrollPane = new ScrollPane();
        myLanguage = language;
        myResources = ResourceBundle.getBundle(language);
        myComponentNames = ResourceBundle.getBundle(language + DefaultStrings.COMPONENTS.getDefault());
        myEntity = toEdit;
    }

    public EditorEntity (String language, IEntity toEdit, ObservableList<IEntity> addToList) {
        this(language, toEdit);
        entityList = addToList;
    }

    private void getComponents () {
        ResourceBundle myLocs = ResourceBundle.getBundle(DefaultStrings.COMPONENT_LOC.getDefault());
        Enumeration<String> iter = myLocs.getKeys();
        while (iter.hasMoreElements()) {
            myComponents.add(myComponentNames.getString(iter.nextElement()));
        }
    }

    @Override
    public ScrollPane getPane () {
        return scrollPane;
    }

    @Override
    public void populateLayout () {
        container = new VBox();
        scrollPane.setContent(container);
        container.getStyleClass().add("vbox");
        myComponents = new ArrayList<>();
        addName();
        addComponentsToPane();
        addButtons();
    }

    private void addButtons () {
        Button saveButton = ButtonFactory.makeButton(myResources.getString("saveEntity"), e -> save());
        Button addButton = ButtonFactory.makeButton(myResources.getString("addComponent"), e -> addComponent());
        Button removeButton = ButtonFactory.makeButton(myResources.getString("removeComponent"), e -> removeComponent());
        container.getChildren().addAll(addButton, removeButton, saveButton);
    }

    private void addName () {
        name = TextFieldFactory.makeTextArea(myResources.getString("enterName"));
        name.setText(myEntity.getName());
        container.getChildren().add(name);
    }

    private void addComponentsToPane () {
        Collection<IComponent> componentList = myEntity.getAllComponents();
        componentList.stream().forEach(this::addObject);
    }

    private void addObject (IComponent component) {
        if (component.getClass().equals(AnimatedSprite.class)) {
            container.getChildren().add((Node) guiFactory.createNewGuiObject("AnimatedSprite", DefaultStrings.GUI_RESOURCES.getDefault(), myLanguage, myEntity).getGuiNode());
        }
        component.getProperties().stream().forEach(this::addVisualObject);
    }

    private void addVisualObject (SimpleObjectProperty<?> property) {
        GuiObject object = guiFactory.createNewGuiObject(property.getName(), DefaultStrings.GUI_RESOURCES.getDefault(), myLanguage, property);
        if (object != null && !(property.getName().equals("ImagePath") && myEntity.hasComponent(AnimatedSprite.class))) {
            container.getChildren().add((Node) object.getGuiNode());
        }
    }

    private void removeComponent () {
        (new ComponentRemover(myLanguage, myEntity)).modifyComponentList();
        updateEditor();
    }

    private void addComponent () {
        (new ComponentAdder(myLanguage, myEntity)).modifyComponentList();
        updateEditor();
    }

    @Override
    public void updateEditor () {
        getComponents();
        populateLayout();
    }

    private void save () {
        myEntity.updateComponents();
        myEntity.setName(name.getText());
        myEntity.getAllComponents().stream().forEach(this::removeBindings);
        entityList.remove(myEntity);
        entityList.add(myEntity);
        container = new VBox();
        container.getChildren().add(super.saveMessage(myResources.getString("saveMessage")));
        scrollPane.setContent(container);
    }

    private void removeBindings (IComponent e) {
        e.removeBindings();

    }
}
