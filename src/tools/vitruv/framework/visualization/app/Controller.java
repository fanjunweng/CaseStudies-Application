package tools.vitruv.framework.visualization.app;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

/**
 * 
 * This class refers to a controller in the MVC pattern that acts on both model (Model.java) and view (view.fxml).
 *
 */
public class Controller implements Initializable{
	//There are all Javafx view elements whose layout is defined in the view.fxml file.
	@FXML private BorderPane mainPane;
	@FXML private TreeView<EObject> leftTree;// A tree view for the FamiliesPackage on the left
	@FXML private TreeView<EObject> centerTree;// A tree view for the PersonsPackage in the middle 
	@FXML private TreeView<EObject> rightTree;// A tree view for the InsurancePackage on the right 
	
	private VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI; //A API for the visualization of three packages 
	private Model model1;// A model for the FamiliesPackage view
	private Model model2;// A model for the PersonsPackage view
	private Model model3;// A model for the InsurancePackage view

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Instantiate a visualization API of the internal virtual model for the tree packages FamiliesPackage, PersonsPackage and InsurancePackage
		vsumVisualizationAPI = VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI();
		//Create models for the three package views from visualization API of the internal virtual model
		model1 = new Model(vsumVisualizationAPI, vsumVisualizationAPI.getT1());
		model2 = new Model(vsumVisualizationAPI, vsumVisualizationAPI.getT2());
		model3 = new Model(vsumVisualizationAPI, vsumVisualizationAPI.getT3());
		//Create the three tree view in the left, center and right side of the main pane
		createTreeView();
		printCorrespondingEObjects();
	}
	
	/**
	 * This method sets up a tree view for each model and transforms the package data into a tree view for display.
	 */
	private void createTreeView() {
		model1.setTreeView(leftTree);
		model2.setTreeView(centerTree);
		model3.setTreeView(rightTree);
		convertDataToTreeView(model1);
		convertDataToTreeView(model2);
		convertDataToTreeView(model3);
}

	/**
	 * This method creates content in the text area that shows all corresponding Eobjects and the direction of their correspondence.
	 */
	private void printCorrespondingEObjects() {
		StringJoiner builder = new StringJoiner(System.lineSeparator());
		model1.getResourceForPackageView().getAllContents().forEachRemaining(eObject -> 
			builder.add(eObject + " --> " + vsumVisualizationAPI.getCorrespondingEObjects(eObject)  + " --> " + 
			vsumVisualizationAPI.getCorrespondingEObjects(vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next()) + "\n"));
		System.out.println(builder.toString());
	}


	/**
	 * This method converts the resource of the corresponding package from the model into the tree view,
	 * creates all tree items for the tree view as EObject types,
	 * 
	 * @param model A model of a package view
	 */
	public void convertDataToTreeView(Model model) {
		//Get the resource from the model
		Resource modelResource = model.getResourceForPackageView();
		//Get the first Eobject from the resource content of the model
		EObject rootObject = modelResource.getContents().get(0);
		// Create a root for the tree view
		TreeItem<EObject> rootItem = new TreeItem<EObject>(rootObject); 
		rootItem.setExpanded(true);
		model.getTreeView().setRoot(rootItem);
		//Set up tree items of the tree view for multiple selection
		model.getTreeView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		//Set the tree cell factory for the tree using the class PackageTreeCell
		defineTreeCell(model);
		// Create tree items for all objects of the root object, whose parent is the root tree item.
		rootObject.eContents().forEach(object -> createChildren(rootItem, object));
	}
	
	/**
	 * This method sets the tree view cells as objects of custom tree view cells (see custom class: PackageTreeCell.class),
	 * and.......
	 * @param model A model for the package view
	 */
	private void defineTreeCell(Model model) {
		//Set the tree cell factory for the tree using the class PackageTreeCell
		model.getTreeView().setCellFactory(tv -> {
			//Create the custom tree cell
			PackageTreeCell cell = new PackageTreeCell();
			//If a tree item cell is clicked, all tree items with the corresponding EObject from other tree views will be automatically selected
			cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
			    	//Clear all selection highlights of the corresponding tree items
		    		clearAllSelections();
		    		//If a tree cell of the left tree (with FamilyPackeg view) is clicked
			    	if(model.getTreeView().equals(model1.getTreeView())) {
			    		//Find the Eobject in the cell of the left tree that corresponds to the item in the middle tree
			    		selectCorrespondingTreeItem(cell, leftTree, centerTree); //Correspondence direction: FamiliesPackage -> PersonsPackage
			    		//Find the tree item of the center tree that corresponds to the item in the right tree
			    		selectCorrespondingTreeItem(centerTree, rightTree); //Correspondence direction: PersonsPackage -> InsurancePackage

			    	} else if(model.getTreeView().equals(model2.getTreeView())) {
			    		//Find the tree item of the center tree that corresponds to the item in the leftTree tree
			    		selectCorrespondingTreeItem(cell, centerTree, leftTree);  //Correspondence direction: PersonsPackage -> FamiliesPackage
			    		//Find the tree item of the center tree that corresponds to the item in the right tree
			    		selectCorrespondingTreeItem(cell, centerTree, rightTree); //Correspondence direction: PersonsPackage -> InsurancePackage
			    
			    	} else if(model.getTreeView().equals(model3.getTreeView())) {
			    		//Find the tree item of the right tree that corresponds to the item in the center tree 
			    		selectCorrespondingTreeItem(cell, rightTree, centerTree); //Correspondence direction: InsurancePackage -> PersonsPackage
			    		selectCorrespondingTreeItem(cell, rightTree, leftTree); //?
			    	}
		    	}
			});
			return cell;
		});
	}

	
	/**
	 * This method creates recursively all tree item children for the EAttribute of the corresponding EObject parent tree item 
	 * @param parentItem A parent item for the tree view
	 * @param object A EObject from the parent tree item
	 */
	private void createChildren(TreeItem<EObject> parentItem, EObject object) {
		TreeItem<EObject> childItem = new TreeItem<EObject>(object);
		parentItem.getChildren().add(childItem);
		childItem.setExpanded(true);
		
		// Calling its own functions for all existing Eattributes of the Eobject
		if (object.eClass().getEAllAttributes()!=null && object.eClass().getEAllAttributes().size()>0) {
			object.eContents().forEach(attribute -> createChildren(childItem, attribute));
		}
	}
	
	/**
	 * This method finds all tree items with the corresponding Eobject from other target trees, 
	 * if a tree item with the source EObject from the source tree cell is clicked.
	 * @param cell PackageTreeCell A custom tree cell of the tree view
	 * @param sourceTree A tree view in which a tree item cell is clicked
	 * @param targetTree A tree view in which the tree items with the corresponding EObejct are searched.
	 */
	public void selectCorrespondingTreeItem(PackageTreeCell cell, TreeView<EObject> sourceTree, TreeView<EObject> targetTree) {
		sourceTree.getSelectionModel().select(cell.getTreeItem());
		if(cell.getTreeItem()!=null && targetTree.getChildrenUnmodifiable().size()>0) {
			//select matched root
			if(targetTree.getRoot()!=null) {
				selectMatchedTreeItems(targetTree, targetTree.getRoot(), vsumVisualizationAPI.getCorrespondingEObjects(cell.getTreeItem().getValue()));
				//select matched children of the root
				if(targetTree.getRoot().getChildren().size() > 0) {
					targetTree.getRoot().getChildren().forEach(targetItem -> {
						selectMatchedTreeItems(targetTree, targetItem, vsumVisualizationAPI.getCorrespondingEObjects(cell.getTreeItem().getValue()));
					});
				}
			}
		}
	}
	
	/**
	 * This method finds and selects all tree items with the corresponding Eobject from other target trees, 
	 * if a tree item with the source EObject from the source tree cell is selected.
	 * @param sourceTree A tree view in which some tree item cells are selected
	 * @param targetTree A tree view in which the tree items with the corresponding EObejct are searched.
	 */
	public void selectCorrespondingTreeItem(TreeView<EObject> sourceTree, TreeView<EObject> targetTree) {
		sourceTree.getSelectionModel().getSelectedItems().forEach(selected -> {
			if(targetTree.getRoot()!=null) {
				selectMatchedTreeItems(targetTree, targetTree.getRoot(), vsumVisualizationAPI.getCorrespondingEObjects(selected.getValue()));
				//select matched children of the root
				if(targetTree.getRoot().getChildren().size() > 0) {
					targetTree.getRoot().getChildren().forEach(targetItem -> {
						selectMatchedTreeItems(targetTree, targetItem, vsumVisualizationAPI.getCorrespondingEObjects(selected.getValue()));
					});
				}
			}
		});
	}
		
	/**
	 * This method clears the selection highlights of all corresponding objects that already exist to support the current new round of highlights.
	 */
	private void clearAllSelections() {
		leftTree.getSelectionModel().clearSelection();
		centerTree.getSelectionModel().clearSelection();
		rightTree.getSelectionModel().clearSelection();
	}
	
	/**
	 * This method recursively finds and selects the matched tree item from the target tree, which is the corresponding object from the source tree item.
	 * @param targetTree A target tree view in which the tree items of the corresponding objects will be found.
	 * @param targetItem A target tree item from the target tree view
	 * @param correspondingObjectSet A set of the corresponding EObjects
	 */
	public void selectMatchedTreeItems(TreeView<EObject> targetTree, TreeItem<EObject> targetItem, Set<EObject> correspondingObjectSet) {
		correspondingObjectSet.forEach(correspondingObject -> {
			if(correspondingObject.eClass().equals(targetItem.getValue().eClass())) {
				Boolean isMatched = true;
				for(EAttribute targetAttribute : targetItem.getValue().eClass().getEAllAttributes()){
					for(EAttribute correspondingAttribute: correspondingObject.eClass().getEAllAttributes()) {
						if(correspondingAttribute.getName().equals(targetAttribute.getName())){
							if(correspondingObject.eGet(correspondingAttribute) == null 
									&& targetItem.getValue().eGet(targetAttribute) == null){
								isMatched &= true;
							}else if(correspondingObject.eGet(correspondingAttribute) != null 
									&& targetItem.getValue().eGet(targetAttribute) != null){
								if(correspondingAttribute.getName().equals(targetAttribute.getName())
										&& correspondingObject.eGet(correspondingAttribute).equals(targetItem.getValue().eGet(targetAttribute))) {
									isMatched &= true;
								}else {
									isMatched &= false;
								}
							}
						}
					}
				}
				
				//Whether the tree item matches the corresponding EObject
				if(isMatched) {
					targetTree.getSelectionModel().select(targetItem);
					if(!targetItem.isLeaf()) {
						targetItem.getChildren().forEach(targetChildItem -> {selectMatchedTreeItems(targetTree, targetChildItem, correspondingObjectSet);});
					}
				}
			}
		});
	}
}	