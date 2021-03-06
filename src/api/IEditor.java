package api;

import javafx.scene.control.ScrollPane;

public interface IEditor { //This will be an abstract class that all editors will extend.

    ScrollPane getPane (); // This is a method all subclasses should implement.

    void populateLayout (); // This is a method all subclasses should implement

}
