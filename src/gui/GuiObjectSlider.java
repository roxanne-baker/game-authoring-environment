package gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class GuiObjectSlider extends GuiObject{
	private Slider slider;
	private Label textLabel;
	private Label numLabel;


	public GuiObjectSlider(String name, String resourceBundle,EventHandler<ActionEvent> event, Property<?> property, Object object) {
		super(name, resourceBundle);
		slider = new Slider(Integer.parseInt(getResourceBundle().getString(name+"Min")),Integer.parseInt(getResourceBundle().getString(name+ "Max")), (double) object); 
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(Integer.parseInt(getResourceBundle().getString(name+"Increment")));
		slider.setValue((double) property.getValue()); 
		textLabel = new Label(getResourceBundle().getString(getObjectName()+"Label"));
		numLabel = new Label(Double.toString(slider.getValue()));
		numLabel.textProperty().bind(Bindings.convert(slider.valueProperty()));
		bindProperty(property);
	}



	private void bindProperty(Property property) {
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
	public Control getControl() {
		return slider;
	}



	@Override
	public Object getGuiNode() {
		VBox vbox = new VBox();
		vbox.getChildren().addAll(textLabel,slider,numLabel);
		return vbox;
	}


}
