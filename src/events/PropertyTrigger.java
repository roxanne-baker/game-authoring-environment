package events;

import api.IComponent;
import api.IEntity;
import api.ILevel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utility.SingleProperty;
import voogasalad.util.reflection.Reflection;

/***
 * @author Anirudh Jonnavithula, Carolyn Yao Implements a ChangeListener that
 *         listens to change in a SimpleObjectProperty.
 */

public class PropertyTrigger extends Trigger {
    private final String entityID;
    private Class<? extends IComponent> componentClass;
    private SimpleObjectProperty<Double> property;
    private String propertyName;

    public PropertyTrigger(String entityID, Class<? extends IComponent> componentClass, SimpleObjectProperty<Double> property) {
        this.entityID = entityID;
        this.componentClass = componentClass;
        this.property = property;
    }

    public PropertyTrigger(String entityID, Class<? extends IComponent> componentClass, String propertyName) {
        this.entityID = entityID;
        this.componentClass = componentClass;
        this.propertyName = propertyName;
    }
    
    @Override
    public void changed(ObservableValue arg0, Object arg1, Object arg2) {
        setChanged();
        notifyObservers();
    }

    @Override
    @Deprecated
    public void clearListener(ILevel universe, InputSystem inputSystem) {
        property.removeListener(this);
    }
    
    @Override
    public void clearListener(ILevel universe) { property.removeListener(this); }

    @Override
    @Deprecated
    public void addHandler(ILevel universe, InputSystem inputSystem) {
        property.addListener(this);
    }

    @Override
    public void addHandler(ILevel universe) {
        property.addListener(this);
    }

    private SimpleObjectProperty<?> getProperty(ILevel level) {
        IEntity entity = level.getEntitySystem().getEntity(entityID);
        IComponent component = entity.getComponent(componentClass);
        return component.getProperty(propertyName);
    }

    @Override
    public String toString() {
        return String.format("%s; %s; %s; %s", getClass().getSimpleName(), entityID, componentClass.getSimpleName(),
                property.toString());
    }

}
