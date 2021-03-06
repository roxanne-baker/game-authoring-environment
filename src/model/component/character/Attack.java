package model.component.character;

import api.IComponent;
import javafx.beans.property.SimpleObjectProperty;
import utility.SingleProperty;

import java.util.List;


/**
 * The class for Attack. Contains a single double property.
 *
 * @author Roxanne Baker, Rhondu Smithwick
 */
@SuppressWarnings("serial")
public class Attack implements IComponent {

    private final SingleProperty<Double> singleProperty = new SingleProperty<>("Attack", 0.0);

    /**
     * Empty constructor. Has attack at 0.
     */
    public Attack () {
    }

    /**
     * Construct with an initial value.
     *
     * @param attack the initial value
     */
    public Attack (double attack) {
        setAttack(attack);
    }

    /**
     * Get the attack as a property.
     *
     * @return the attack property
     */
    public SimpleObjectProperty<Double> attackProperty () {
        return singleProperty.property1();
    }

    public double getAttack () {
        return attackProperty().get();
    }

    public void setAttack (double attack) {
        attackProperty().set(attack);
    }

    @Override
    public String toString () {
        return String.format("Attack: %s", getAttack());
    }

    @Override
    public List<SimpleObjectProperty<?>> getProperties () {
        return singleProperty.getProperties();
    }

    @Override
    public void update () {
        setAttack(getAttack());
    }
}