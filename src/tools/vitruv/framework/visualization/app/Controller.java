package tools.vitruv.framework.visualization.app;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

public class Controller implements Initializable{
	@FXML private BorderPane mainPane;
	@FXML private TreeView<String> leftTree;
	@FXML private TreeView<String> centerTree;
	@FXML private TreeView<String> rightTree;
	@FXML private TextArea textArea;
	
	private VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// initialize data structures
		vsumVisualizationAPI = VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI();
		
		//create the three tree view in the left, center and right side of the main border pane
		createTreeView(vsumVisualizationAPI.getT1(), leftTree);
		createTreeView(vsumVisualizationAPI.getT2(), centerTree);
		createTreeView(vsumVisualizationAPI.getT3(), rightTree);
		
		//create the text area in the bottom side of the main border pane
		createTextArea();
	
//		TreeView<String> personsTreeView = convertDataToTreeView(getResourceForPackageView(vsumVisualizationAPI.getT2()));
//		pane.setCenter(personsTreeView);
//		personsTreeView.setMinWidth(400);
		
	} 
	
	private void createTreeView(EPackage ePackage, TreeView<String> tree) {
	convertDataToTreeView(getResourceForPackageView(ePackage), tree);
}

	private void createTextArea() {
		StringJoiner builder = new StringJoiner(System.lineSeparator());
		getResourceForPackageView(vsumVisualizationAPI.getT1()).getAllContents().forEachRemaining(eObject -> 
			builder.add(eObject + " --> " + vsumVisualizationAPI.getCorrespondingEObjects(eObject)  + " --> " + 
			vsumVisualizationAPI.getCorrespondingEObjects(vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next())));
		textArea.setText(builder.toString());
	}
	
	private Resource getResourceForPackageView(EPackage ePackage) {
		return vsumVisualizationAPI.getView(ePackage).getRootObjects().iterator().next().eResource();
	}

	// Convert the resource data into tree view format
	public void convertDataToTreeView(Resource resource, TreeView<String> tree) {
		// Get content of the resource
		EObject model = resource.getContents().get(0); // FamilyRegister in the first layer
	
		// Create a root for the tree
		TreeItem<String> rootItem = new TreeItem<String>(model.eClass().getName());
		rootItem.setExpanded(true);
		tree.setRoot(rootItem);
		
		StringBuilder name = new StringBuilder();
	
		// TW: Create the name of the attribute of the id
		for (EAttribute att : model.eClass().getEAttributes()) {
			name.append(att.getName() + ": " + model.eContainingFeature() + " "); // = what is the id number??
		}
	
		
		TreeItem<String> attributeNode = new TreeItem<String>(name.toString());
		rootItem.getChildren().add(attributeNode);
	
	
		// Create all children nodes for each sub root of the tree
		EList<EObject> subRootList = model.eContents(); // families in the second layer
	
		for (EObject subRoot : subRootList) {
			createChildren(rootItem, subRoot);
		}
	
//		return tree;
	}

	// Recursive create all children nodes
	public void createChildren(TreeItem<String> parentItem, EObject node) {
		EStructuralFeature nodeName = node.eContainingFeature(); // = families, mothers, fathers, daughters, sons
		List<EAttribute> nodeList = node.eClass().getEAllAttributes();
	
		if (nodeList != null && nodeList.size() > 0) {
			
			StringBuilder treeName = new StringBuilder(
					nodeName.getName() + " of type " + node.eClass().getName() + " ");
	
			for (EAttribute att : nodeList) {
				String attName = att.getName(); // lastname or firstname
				Object attValue = node.eGet(att);// Mueller or Lilian, Lea, Lukas
	
				// Create Tree view item for the sub root
				treeName.append(attName + ": " + attValue + "   ");
			}
	
			TreeItem<String> memberNode = new TreeItem<String>(treeName.toString());
			parentItem.getChildren().add(memberNode);
			memberNode.setExpanded(true);
	
			// Create Tree view item for the children of the sub root
			for (EAttribute att : nodeList) {
				for (EObject e : node.eContents()) {
					createChildren(memberNode, e);
				}
			}
		}
}
	
}


