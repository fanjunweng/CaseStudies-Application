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
 * This class is a test class for the model controller class, 
 * and contains the unit test cases for all methods.
 */
public class ModelControllerTest{

	/**
	 * Test method for {@link tools.vitruv.framework.visualization.app.ModelController#setResource()}.
	 */
	@Test
	public void testLoadDataToTreeView() {
		Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        	Model model = new Model(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1(), VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getView(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1()));
                        	ModelController controller = new ModelController(model, new TreeView<EObject>());
                        	controller.setResource();
                        	assertEquals(model.getResource().getContents().size(), controller.getTreeView().getChildrenUnmodifiable().size());
                        	assertEquals(model.getRootObject(), controller.getTreeView().getRoot().getValue());
                        	
                        	model.getResource().getContents().forEach(x -> {
                        		controller.getTreeView().getChildrenUnmodifiable().forEach(y -> {
                        			assertEquals(x, ((PackageTreeCell) y).getTreeItem().getValue());
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

	/**
	 * Test method for {@link tools.vitruv.framework.visualization.app.ModelController#getSelectedObjects()}.
	 */
	@Test
	public void testGetSelectedObjects() {
		Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                          	Model model = new Model(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1(), VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getView(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1()));
                        	ModelController controller = new ModelController(model, new TreeView<EObject>());
                        	controller.getTreeView().getSelectionModel().select(controller.getTreeView().getRoot());
                        	controller.getTreeView().getSelectionModel().select(controller.getTreeView().getRoot().getChildren().get(0));
                        	assertEquals(controller.getTreeView().getSelectionModel().getSelectedItems().size(), controller.getSelectedObjects());
                        	controller.getTreeView().getSelectionModel().getSelectedItems().forEach(x -> {
                        		controller.getSelectedObjects().forEach(y -> {
                        			assertEquals(x,y);
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

	/**
	 * Test method for {@link tools.vitruv.framework.visualization.app.ModelController#selectCorrespondingObjects(java.util.Set)}.
	 */
//	@Test
//	public void testSelectCorrespondingObjects() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link tools.vitruv.framework.visualization.app.ModelController#clearSelections()}.
	 */
	@Test
	public void testClearSelections() {
		Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        	ModelController controller = new ModelController(new Model(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1(), VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getView(VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI().getT1())), new TreeView<EObject>());
                        	controller.getTreeView().getSelectionModel().select(controller.getTreeView().getRoot());
                        	controller.getTreeView().getSelectionModel().select(controller.getTreeView().getRoot().getChildren().get(0));
                        	assertEquals(2, controller.getTreeView().getSelectionModel().getSelectedItems().size());
                        	
                        	controller.clearSelections();
                        	assertEquals(0, controller.getTreeView().getSelectionModel().getSelectedItems().size());
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
