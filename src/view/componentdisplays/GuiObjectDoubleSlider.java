package view.componentdisplays;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Slider;

/**
 * Class to set double property with slider
 *
 * @author calinelson
 */
public class GuiObjectDoubleSlider extends GuiObjectSlider {

    /**
     * constructor for double slider
     *
     * @param name           name of property
     * @param resourceBundle resourcebundle
     * @param language       display language
     * @param property       property to bind
     * @param object         object
     */
    public GuiObjectDoubleSlider (String name, String resourceBundle, String language, SimpleObjectProperty<?> property) {
        super(name, resourceBundle, language, property);
    }

    @Override
    protected Slider createSlider (String name, SimpleObjectProperty<?> property) {
        Slider slider = new Slider(Double.parseDouble(getResourceBundle().getString(name + "Min")), Double.parseDouble(getResourceBundle().getString(name + "Max")), (Double) property.getValue());
        slider.setValue((Double) property.getValue());
        return slider;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected void bindProperty (SimpleObjectProperty property, Slider slider) {
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            property.setValue(slider.valueProperty().get());

        });
    }

}
