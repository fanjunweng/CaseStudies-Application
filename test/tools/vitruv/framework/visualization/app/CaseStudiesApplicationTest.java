/**
 * 
 */
package tools.vitruv.framework.visualization.app;

import org.junit.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

/**
 * This class contains the unit test case of the CaseStudiesApplication class.
 */
public class CaseStudiesApplicationTest {

	/**
	 * Test method for {@link tools.vitruv.framework.visualization.app.CaseStudiesApplication#start(javafx.stage.Stage)}.
	 */
	@Test
    public void testStartStage() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
							new CaseStudiesApplication().start(new Stage()); //Run a stage
						} catch (Exception e) {
							e.printStackTrace();
						} 
                    }
                });
            }
        });
        thread.start();// Initialize the thread
    }
}
