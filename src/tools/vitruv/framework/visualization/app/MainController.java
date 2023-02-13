package tools.vitruv.framework.visualization.app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
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
			
		//Listener 1
		controllerList.get(0).clickedProperty().addListener((o, oldVal, newVal) -> {
			if(newVal==true && !controllerList.get(0).getSelectedObject().isEmpty()) {
				System.out.println("0 old: "+ oldVal); 
				System.out.println("new: "+ newVal + "\n");
				controllerList.get(1).clearSelections();
				controllerList.get(2).clearSelections();
				propagateToNextController(controllerList.get(0), controllerList.get(1));
				propagateToNextController(controllerList.get(1), controllerList.get(2));
				controllerList.get(0).declick();
				controllerList.get(1).declick();
				controllerList.get(2).declick();
			}
		});
		
		//Listener 2
	   controllerList.get(1).clickedProperty().addListener((o, oldVal, newVal) -> {
			if(newVal==true && !controllerList.get(1).getSelectedObject().isEmpty()) {
				System.out.println("1 old: "+ oldVal); 
				System.out.println("new: "+ newVal + "\n");
			
				controllerList.get(0).clearSelections();
				controllerList.get(2).clearSelections();
				propagateToNextController(controllerList.get(1), controllerList.get(0));
				propagateToNextController(controllerList.get(1), controllerList.get(2));
				controllerList.get(0).declick();
				controllerList.get(1).declick();
				controllerList.get(2).declick();

			}
		});
	   
	   //Listener 3
	  	controllerList.get(2).clickedProperty().addListener((o, oldVal, newVal) -> {
			if(newVal==true && !controllerList.get(2).getSelectedObject().isEmpty()) {
				System.out.println("2 old: "+ oldVal); 
				System.out.println("new: "+ newVal + "\n");
				controllerList.get(1).clearSelections();
				controllerList.get(0).clearSelections();
				propagateToNextController(controllerList.get(2), controllerList.get(1));
				propagateToNextController(controllerList.get(2), controllerList.get(0));
				controllerList.get(0).declick();
				controllerList.get(1).declick();
				controllerList.get(2).declick();
			}
		});
	}
	

	
	private VSUMVisualizationAPI<T1, T2, T3> getVsumVisualizationAPI() {
		return vsumVisualizationAPI;
	}
	
	private void propagateToNextController(ModelController currentController, ModelController nextController) {
		if(currentController.getSelectedObject()!=null) {
			currentController.getSelectedObject().forEach(o -> {
				nextController.selectCorrespondingObjects(getVsumVisualizationAPI().getCorrespondingEObjects(o));
			});
		}
	}
}	