package events;

import api.IComponent;
import api.IEntity;
import api.IInputSystem;
import api.ILevel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

/***
 * Implements a changeListener that listens to change in a SimpleObjectProperty.
 * <p> The check on the action IE Health == 0 would then occur in a Groovy Script</p>
 *
 * @author Anirudh Jonnavithula, Carolyn Yao
 */

public class PropertyTrigger extends Trigger {
    private final String entityID;
    private final Class<? extends IComponent> componentClass;
    private final String propertyName;
    private SimpleObjectProperty<Double> property;

    public PropertyTrigger (String entityID, Class<? extends IComponent> componentClass, SimpleObjectProperty<Double> property) {
        this.entityID = entityID;
        this.componentClass = componentClass;
        this.property = property;
        this.propertyName = property.getName();
    }

    public PropertyTrigger (String entityID, Class<? extends IComponent> componentClass, String propertyName) {
        this.entityID = entityID;
        this.componentClass = componentClass;
        this.propertyName = propertyName;
    }

    @Override
    public void changed (ObservableValue arg0, Object arg1, Object arg2) {
        setChanged();
        notifyObservers();
    }

    @Override
    @Deprecated
    public void clearListener (ILevel universe, IInputSystem inputSystem) {
        property.removeListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clearListener (ILevel universe) {
        getProperty(universe).removeListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public void addHandler (ILevel universe, IInputSystem inputSystem) {
        getProperty(universe).addListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addHandler (ILevel universe) {
        getProperty(universe).addListener(this);
    }

    private SimpleObjectProperty<?> getProperty (ILevel level) {
        IEntity entity = level.getEntitySystem().getEntity(entityID);
        IComponent component = entity.getComponent(componentClass);
        return component.getProperty(propertyName);
    }

    @Override
    public String toString () {
        return String.format("%s; %s; %s; %s", getClass().getSimpleName(), entityID, componentClass.getSimpleName(),
                propertyName);
    }

}
