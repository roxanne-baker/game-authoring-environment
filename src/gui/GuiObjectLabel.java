package gui;


import api.ISerializable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Label;


public class GuiObjectLabel extends GuiObject{
	private Label myLabel;
	
	public GuiObjectLabel(String name, String resourceBundle,EventHandler<ActionEvent> event, Property<?> property) {
		super(name, resourceBundle);
		myLabel = new Label(getResourceBundle().getString(getObjectName()+ "LABEL"));
		bindProperty(property);

	}


	private void bindProperty(Property property) {
		myLabel.textProperty().bind(property);
		
	}


	@Override
	public Object getCurrentValue() {
		return myLabel.getText();
	}

	@Override
	public Control getControl() {
		return myLabel;
	}

	@Override
	public Object getGuiNode() {
		return myLabel;
	}



}