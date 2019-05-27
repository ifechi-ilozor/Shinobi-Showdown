package Indy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**Standard setup*/

public class App extends Application {
	
	@Override
    public void start(Stage stage) {
    	PaneOrganizer organizer = new PaneOrganizer();
    	Scene scene = new Scene(organizer.getRoot(), Constants.OPEN_WIDTH, Constants.OPEN_HEIGHT);
    	stage.setScene(scene);
    	stage.setTitle("Shinobi Showdown");
    	stage.show();
    }

    public static void main(String[] argv) {
    	launch(argv);
    }
}
    
   