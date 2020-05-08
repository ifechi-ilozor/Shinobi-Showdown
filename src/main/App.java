package Indy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The App Class sets up the interface in which the program runs.
 */
public class App extends Application {
	
	@Override
    public void start(Stage stage) {
			// Standard setup
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
