/**
 * 
 */
package tools.vitruv.framework.visualization.app;

import static org.junit.Assert.*;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TreeView;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

/**
 *
 */
public class MainControllerTest {
	/**
	 * Test method for {@link tools.vitruv.framework.visualization.app.MainController#selectCorrepondingObjects(tools.vitruv.framework.visualization.app.ModelController)}.
	 */
	@Test
	public void testSelectCorrepondingObjects() {
		Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        	Model model1 = new Model(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1(), VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getView(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1()));
                        	ModelController controller1 = new ModelController(model1, new TreeView<EObject>());
                        	Model model2 = new Model(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT2(), VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getView(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT2()));
                        	ModelController controller2 = new ModelController(model2, new TreeView<EObject>());
                        	controller1.getTreeView().getSelectionModel().select(controller1.getTreeView().getRoot());
                        	controller1.getSelectedObjects().get(0);
                        	controller2.selectCorrespondingObjects(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getCorrespondingEObjects(controller1.getSelectedObjects().get(0)));
                        	VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getCorrespondingEObjects(controller1.getSelectedObjects().get(0)).forEach(selected1 -> {
                        		controller2.getSelectedObjects().forEach(selected2 -> {
                        	    	assertEquals(selected1, selected2);
                        		});
                        	});
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
