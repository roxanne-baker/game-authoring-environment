package model.component.movement;

import api.IComponent;
import javafx.beans.property.SimpleObjectProperty;
import utility.TwoProperty;

import java.util.List;


/**
 * Created by rhondusmithwick on 4/1/16.
 *
 * @author Rhondu Smithwick
 */
@SuppressWarnings("serial")
public class Position implements IComponent {

    /**
     * The two property, which holds a property for x and a property for y.
     */
    private final TwoProperty<Double, Double> twoProperty = new TwoProperty<>("XPosition", 0.0, "YPosition", 0.0);

    /**
     * Empty constructor. Starts at 0.0.
     */
    public Position () {
    }

    /**
     * Constructor with initial values.
     *
     * @param x initial x value
     * @param y initial y value
     */
    public Position (double x, double y) {
        setXY(x, y);
    }

    /**
     * Get the x property.
     *
     * @return the x property
     */
    public SimpleObjectProperty<Double> xProperty () {
        return twoProperty.property1();
    }

    public double getX () {
        return xProperty().get();
    }

    public void setX (double x) {
        xProperty().set(x);
    }

    /**
     * Get the y property.
     *
     * @return the y property
     */
    public SimpleObjectProperty<Double> yProperty () {
        return twoProperty.property2();
    }

    public double getY () {
        return yProperty().get();
    }

    public void setY (double y) {
        yProperty().set(y);
    }


    public void setXY (double x, double y) {
        setX(x);
        setY(y);
    }

    public void add (double dx, double dy) {
        this.setXY(getX() + dx, getY() + dy);
    }

    @Override
    public String toString () {
        return String.format("Position: [X: %s, Y: %s]", getX(), getY());
    }

    @Override
    public List<SimpleObjectProperty<?>> getProperties () {
        return twoProperty.getProperties();
    }

    @Override
    public void update () {
        setX(getX());
        setY(getY());
    }

}
