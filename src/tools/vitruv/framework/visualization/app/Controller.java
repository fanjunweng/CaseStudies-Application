package tools.vitruv.framework.visualization.app;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import tools.vitruv.change.composite.description.PropagatedChange;
import tools.vitruv.change.composite.description.VitruviusChange;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

public class Controller implements Initializable{
	@FXML private BorderPane mainPane;
	@FXML private TreeView<EObject> leftTree;
	@FXML private TreeView<EObject> centerTree;
	@FXML private TreeView<EObject> rightTree;
	@FXML private TextArea textArea;
	
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
		createTreeView();
		//create the text area in the bottom side of the main pane
		createTextArea();
//		TransactionalChangeImpl change = new TransactionalChangeImpl();
//		List<PropagatedChange> list = propagateChange(new TransactionalChangeImpl().resolveAndApply(model1.getResourceForPackageView().getResourceSet()));
	} 
	
	private void createTreeView() {
		model1.setTreeView(leftTree);
		model2.setTreeView(centerTree);
		model3.setTreeView(rightTree);
		convertDataToTreeView(model1);
		convertDataToTreeView(model2);
		convertDataToTreeView(model3);
}

	private void createTextArea() {
		StringJoiner builder = new StringJoiner(System.lineSeparator());
		model1.getResourceForPackageView().getAllContents().forEachRemaining(eObject -> 
			builder.add(eObject + " --> " + vsumVisualizationAPI.getCorrespondingEObjects(eObject)  + " --> " + 
			vsumVisualizationAPI.getCorrespondingEObjects(vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next())));
		textArea.setText(builder.toString());
	}

	// Convert the resource data into tree view format
	public void convertDataToTreeView(Model model) {
		// Get content of the resource
		Resource modelResource = model.getResourceForPackageView();
		EObject resource = modelResource.getContents().get(0); // FamilyRegister in the first layer
		// Create a root for the tree
		TreeItem<EObject> rootItem = new TreeItem<EObject>(resource); 
		rootItem.setExpanded(true);
		model.getTreeView().setRoot(rootItem);
		
		//Set the tree cell factory for the tree using the class PackageTreeCell
		model.getTreeView().setCellFactory(tv -> {
			PackageTreeCell cell = new PackageTreeCell();
			//If a tree item is clicked, all corresponding tree items from other tree views will be automatically selected
			cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
		    		clearAllSelections();
			    	if(model.getTreeView().equals(model1.getTreeView())) {
			    		setCorrespondingTreeNodes(cell, leftTree, centerTree);
			    		setCorrespondingTreeNodes(centerTree, rightTree);
			 
			    	} else if(model.getTreeView().equals(model2.getTreeView())) {
			    		setCorrespondingTreeNodes(cell, centerTree, leftTree);
			    		setCorrespondingTreeNodes(cell, centerTree, rightTree);
			    
			    	} else if(model.getTreeView().equals(model3.getTreeView())) {
			    		setCorrespondingTreeNodes(cell, rightTree, centerTree);
			    	}
		    	}
			    
			});
			return cell;
		});
	
		// Create all children nodes for each sub root of the tree
//		System.out.println(resource.eContents().size());
		for (EObject subRoot : resource.eContents()) {
			createChildren(rootItem, subRoot);
		}
	}

	// Recursive create all children nodes for a new tree view
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
	
	//Find all corresponding tree nodes from other target trees, if a tree node from the source tree cell is clicked.
	public void setCorrespondingTreeNodes(PackageTreeCell cell, TreeView<EObject> sourceTree, TreeView<EObject> targetTree) {
		sourceTree.getSelectionModel().select(cell.getTreeItem());
		if(cell.getTreeItem() !=null) {
			vsumVisualizationAPI.getCorrespondingEObjects(cell.getTreeItem().getValue()).forEach(correspondingObjects -> {
				correspondingObjects.eClass().getEAllAttributes().forEach(attribute -> {
					
					targetTree.getRoot().getChildren().forEach(targetNode -> {
						findTargetLeaf(targetNode, correspondingObjects, attribute, targetTree);
					});
				});
			});
		}
	}
	
	//Find all corresponding tree nodes from other target trees, if a tree node from the source tree is selected.
	public void setCorrespondingTreeNodes(TreeView<EObject> sourceTree, TreeView<EObject> targetTree) {
		sourceTree.getSelectionModel().getSelectedItems().forEach(selected -> {
			vsumVisualizationAPI.getCorrespondingEObjects(selected.getValue()).forEach(correspondingObjects -> {
				correspondingObjects.eClass().getEAllAttributes().forEach(attribute -> {
					
					targetTree.getRoot().getChildren().forEach(targetNode -> {
						findTargetLeaf(targetNode, correspondingObjects, attribute, targetTree);
					});
				});
			});
		});
	}
		
	//Clear all the corresponding selection in order to show new corresponding tree items.
	public void clearAllSelections() {
		leftTree.getSelectionModel().clearSelection();
		centerTree.getSelectionModel().clearSelection();
		rightTree.getSelectionModel().clearSelection();
	}
	
	//The corresponding leaf tree item will be automatically selected.
	public void findTargetLeaf(TreeItem<EObject> targetNode, EObject correspondingObjects, EAttribute attribute, TreeView<EObject> targetTree) {
		if(targetNode.isLeaf()) {
			targetNode.getValue().eClass().getEAllAttributes().forEach(attribute2 -> {
	
				if(correspondingObjects.eGet(attribute) != null){
					System.out.println("corresponding: "+correspondingObjects.toString());
//					System.out.println("\n correspondingObjects: "+ correspondingObjects.eGet(attribute).toString());
					
//					if(targetNode.getValue().eGet(attribute2) !=null) {
//						System.out.println(" ///"+ targetNode.getValue().eGet(attribute2).toString());
//					}
							
//					System.out.println("\n attribute: "+attribute.getName()+", "+ attribute2.getName());
					
					if(attribute.getName().equals(attribute2.getName())
							&& targetNode.getValue().eGet(attribute2)
							.equals(correspondingObjects.eGet(attribute))) {
							
						targetTree.getSelectionModel().select(targetNode);
					}
				}
			});
		}else {
			targetNode.getChildren().forEach(n -> {
				findTargetLeaf(n, correspondingObjects, attribute, targetTree);
			});
		}
	}
}	
	



