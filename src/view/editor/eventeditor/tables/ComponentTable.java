package view.editor.eventeditor.tables;

import java.util.ResourceBundle;

import api.IComponent;
import api.ISerializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entity.Entity;

/**
 * Component Table. Lists certain Components from a given entity.
 * @author Alankmc
 *
 */
public class ComponentTable extends Table
{
	public ComponentTable(PropertyTableManager manager, String language) throws NoSuchMethodException, SecurityException
	{
		// Passes the manager's pickComponent method.
		super(manager, ResourceBundle.getBundle(language).getString("pickComponent"), 
				manager.getClass().getMethod("componentWasClicked", IComponent.class),
				IComponent.class);	
   	}
	
	@Override
	public void fillEntries(Object dataHolder) 
	{
		for (IComponent component: ((Entity)dataHolder).getAllComponents())
		{
			String[] splitClassName = component.getClass().toString().split("\\.");
			getEntries().add(new Entry(component, splitClassName[splitClassName.length - 1]));
		}
	}
}