package tools.vitruv.framework.visualization.app;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

/**
 * 
 * This class refers to a controller in the MVC pattern that acts on both model (Model.java) and view (view.fxml).
 *
 */
public class MainController<T1 extends EPackage, T2 extends EPackage, T3 extends EPackage> implements Initializable{
	@FXML private BorderPane mainPane; // A main layout control
	@FXML private TreeView<EObject> leftTree;// A tree view for the FamiliesPackage on the left
	@FXML private TreeView<EObject> centerTree;// A tree view for the PersonsPackage in the middle 
	@FXML private TreeView<EObject> rightTree;// A tree view for the InsurancePackage on the right 
	
	private Model model1;// A model for the FamiliesPackage view
	private Model model2;// A model for the PersonsPackage view
	private Model model3;// A model for the InsurancePackage view
	private ArrayList<ModelController> controllerList = new ArrayList<>();
	private VSUMVisualizationAPI<T1, T2, T3> vsumVisualizationAPI;


	MainController(VSUMVisualizationAPI<T1, T2, T3> vsumVisualizationAPI, Model M1, Model M2, Model M3){
		this.vsumVisualizationAPI = vsumVisualizationAPI;
		this.model1 = M1;
		this.model2 = M2;
		this.model3 = M3;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		controllerList.add(new ModelController(model1, leftTree));
		controllerList.add(new ModelController(model2, centerTree));
		controllerList.add(new ModelController(model3, rightTree));
		controllerList.forEach(modelController -> {
			modelController.loadDataToTreeView();
		});
		selectCorrepondingObjects();
	}
	
	public void selectCorrepondingObjects() {
		controllerList.forEach(currentController -> {
			currentController.clickedProperty().addListener((o, oldVal, newVal) -> {
				if(newVal==true) {
					controllerList.forEach(remainingController -> {
						if(remainingController!=currentController) {
							remainingController.clearSelections();
						}
					});
					controllerList.forEach(remainingController -> {
						propagateToNextController(currentController.getSelectedObject(), remainingController);
					});
					
					controllerList.forEach(remainingController1 -> {
						controllerList.forEach(remainingController2 -> {
							if(remainingController1!=currentController && remainingController2!=currentController) {
								propagateToNextController(remainingController1.getSelectedObject(), remainingController2);
							}
						});	
					});

					currentController.declick();
				}
			});
		});
	}

	
	private VSUMVisualizationAPI<T1, T2, T3> getVsumVisualizationAPI() {
		return vsumVisualizationAPI;
	}
	
	private void propagateToNextController(List<EObject> selectedObjects, ModelController nextController) {
		if(selectedObjects!=null) {
			selectedObjects.forEach(o -> {
				nextController.selectCorrespondingObjects(getVsumVisualizationAPI().getCorrespondingEObjects(o));
			});
		}
	}
}	