package tools.vitruv.framework.visualization.app;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringJoiner;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

public class Controller implements Initializable{
	@FXML private BorderPane mainPane;
	@FXML private TreeView<EObject> leftTree;
	@FXML private TreeView<EObject> centerTree;
	@FXML private TreeView<EObject> rightTree;
	@FXML private TextArea textArea;
//	@FXML private Line line;
	
	private VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI;
	private Model model1;
	private Model model2;
	private Model model3;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// initialize data structures
		vsumVisualizationAPI = VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI();
		model1 = new Model(vsumVisualizationAPI, vsumVisualizationAPI.getT1());
		model2 = new Model(vsumVisualizationAPI, vsumVisualizationAPI.getT2());
		model3 = new Model(vsumVisualizationAPI, vsumVisualizationAPI.getT3());
		//create the three tree view in the left, center and right side of the main pane
		createTreeView(model1, leftTree);
		createTreeView(model2, centerTree);
		createTreeView(model3, rightTree);
		
		//create the text area in the bottom side of the main pane
		createTextArea();
	
//		TreeView<String> personsTreeView = convertDataToTreeView(getResourceForPackageView(vsumVisualizationAPI.getT2()));
//		pane.setCenter(personsTreeView);
//		personsTreeView.setMinWidth(400);
	} 
	
	private void createTreeView(Model model, TreeView<EObject> tree) {
		convertDataToTreeView(model, tree);
}

	private void createTextArea() {
		StringJoiner builder = new StringJoiner(System.lineSeparator());
		model1.getResourceForPackageView().getAllContents().forEachRemaining(eObject -> 
			builder.add(eObject + " --> " + vsumVisualizationAPI.getCorrespondingEObjects(eObject)  + " --> " + 
			vsumVisualizationAPI.getCorrespondingEObjects(vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next())));
		textArea.setText(builder.toString());
	}
	
	

	// Convert the resource data into tree view format
	public void convertDataToTreeView(Model model, TreeView<EObject> tree) {
		// Get content of the resource
		Resource modelResource = model.getResourceForPackageView();
		EObject resource = modelResource.getContents().get(0); // FamilyRegister in the first layer
		// Create a root for the tree
		TreeItem<EObject> rootItem = new TreeItem<EObject>(resource); 
		rootItem.setExpanded(true);
		tree.setRoot(rootItem);
		
		//Set the tree cell factory for the tree using the class PackageTreeCell
		tree.setCellFactory(tv -> {
			PackageTreeCell cell = new PackageTreeCell();
			cell.setOnMouseClicked(e -> {
//			        tree.getSelectionModel().select(cell.getTreeItem());
				//The color of the corresponding cell will be changed to blue violet.
				cell.setStyle("-fx-background-color:#8A2BE2;");
			    });
		    return cell ;
		});
	
	
		// Create all children nodes for each sub root of the tree
		for (EObject subRoot : resource.eContents()) {
			createChildren(rootItem, subRoot);
		}
	}

	// Recursive create all children nodes
	public void createChildren(TreeItem<EObject> parentItem, EObject node) {
		TreeItem<EObject> memberNode = new TreeItem<EObject>(node);
		parentItem.getChildren().add(memberNode);
		memberNode.setExpanded(true);
		
		List<EAttribute> nodeList = node.eClass().getEAllAttributes();
		if (nodeList != null && nodeList.size() > 0) {
			// Create Tree view item for the children of the sub root
			for (EObject e : node.eContents()) {
				createChildren(memberNode, e);
			}
		}
	}
}	



