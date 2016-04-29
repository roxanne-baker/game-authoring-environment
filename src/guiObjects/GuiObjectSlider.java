package guiObjects;

import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import view.enums.DefaultStrings;

/**
 * 
 * @author calinelson
 *
 */

public class GuiObjectSlider extends GuiObject{
	private Slider slider;
	private Label textLabel;
	private Label numLabel;
	private ResourceBundle myPropertiesNames;


	public GuiObjectSlider(String name, String resourceBundle,String language, SimpleObjectProperty<?> property, Object object) {
		super(name, resourceBundle);
		this.myPropertiesNames= ResourceBundle.getBundle(language + DefaultStrings.PROPERTIES.getDefault());
		createSlider(name, property, object);
		textLabel = new Label(myPropertiesNames.getString(getObjectName()));
		numLabel = new Label(Double.toString(slider.getValue()));
		numLabel.textProperty().bind(Bindings.convert(slider.valueProperty()));
		bindProperty(property);
	}



	private void createSlider(String name, SimpleObjectProperty<?> property, Object object) {
		if(object.getClass().equals(Integer.class)){
			slider = new Slider(Integer.parseInt(getResourceBundle().getString(name+"Min")),Integer.parseInt(getResourceBundle().getString(name+ "Max")), (Integer) object);
			slider.setMajorTickUnit(Integer.parseInt(getResourceBundle().getString(name+"MajorIncrement")));
			slider.setMinorTickCount(Integer.parseInt(getResourceBundle().getString(name + "MinorIncrement")));
			slider.setSnapToTicks(true);
			slider.setValue((Integer) property.getValue());
			
		}else{
			slider = new Slider(Double.parseDouble(getResourceBundle().getString(name+"Min")),Double.parseDouble(getResourceBundle().getString(name+ "Max")), (Double) object); 
			slider.setValue((Double) property.getValue());
		}
	}



	@SuppressWarnings("rawtypes")
	private void bindProperty(SimpleObjectProperty property) {
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            @SuppressWarnings("unchecked")
			public void changed(ObservableValue<? extends Number> ov,Number old_val, Number new_val) {
            	property.setValue(slider.valueProperty().get());
            
            }
        });
	}



	@Override
	public Object getCurrentValue() {
		return slider.getValue();
	}



	@Override
	public Object getGuiNode() {
		VBox vbox = new VBox();
		vbox.getChildren().addAll(textLabel,slider,numLabel);
		return vbox;
	}


}