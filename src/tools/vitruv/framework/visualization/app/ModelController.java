package tools.vitruv.framework.visualization.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * This class is a model controller, that implements an interface of the single resource visualization controller,
 * loads single resource into visual tree view, 
 * and highlights the corresponding EObjects of a clicked EObject in the this tree view.
 *
 */
public class ModelController implements SingleResourceVisualizationController{
	private Model model;
	private TreeView<EObject> treeView;
	private ObjectProperty<Boolean> clickedProperty = new SimpleObjectProperty<Boolean>();
	
	ModelController(Model model, TreeView<EObject> treeView){
		this.model = model;
		this.treeView = treeView;
	}

	private Model getModel() {
		return this.model;
	}
	
	TreeView<EObject> getTreeView() {
		return this.treeView;
	}
	
	@Override
	public ObjectProperty<Boolean> clickProperty() {
		return clickedProperty;
	}
	
	private void click() {
		clickedProperty.set(true);
	}
	
	@Override
	public void declick() {
		clickedProperty.set(false);;
	}
	
	/**
	 * This method converts the root object and its children of the model into the tree view,
	 * creates all tree items for the tree view as EObject types.
	 */
	@Override
	public void loadDataToTreeView() {
		TreeItem<EObject> rootItem = createTreeItem(null, getModel().getRootObject());
		if(!getModel().getContentObjects(getModel().getRootObject()).isEmpty()) {
			getModel().getContentObjects(getModel().getRootObject()).forEach(object -> createTreeItem(rootItem, object));
		}
	}
	
	/**
	 * This method creates recursively all tree item children for the EAttribute of the corresponding EObject parent tree item 
	 * @param parentItem A parent item for the tree view
	 * @param object A EObject from the parent tree item
	 */
	private TreeItem<EObject> createTreeItem(TreeItem<EObject> parentItem, EObject object) {
		TreeItem<EObject> treeItem = new TreeItem<EObject>(object);
		treeItem.setExpanded(true);
		if(parentItem==null) {
			getTreeView().setRoot(treeItem);
			getTreeView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			getTreeView().setCellFactory(tv -> {
				PackageTreeCell cell = new PackageTreeCell();
				setTreeCellMouseEventHandler(cell);	
				return cell;
			});
		}else {
			parentItem.getChildren().add(treeItem);
			if (getModel().getAttributes(object)!=null && !getModel().getAttributes(object).isEmpty()) {
				getModel().getContentObjects(object).forEach(attribute -> createTreeItem(treeItem, attribute));
			}
		}
		return treeItem;
	}
	
	private void setTreeCellMouseEventHandler(PackageTreeCell cell) {
		cell.setOnMouseClicked(mouseEvent -> {
			if(cell.getTreeItem()!=null) {
				click();
			}
		});	
	}
	
	@Override
	public List<EObject> getSelectedObjects() {
		List<EObject> list = new ArrayList<>();
		getTreeView().getSelectionModel().getSelectedItems().forEach(selected -> {
			list.add(selected.getValue());
		});
		return list;
	}
	
	@Override
	public void selectCorrespondingObjects(Set<EObject> correspondingObjectSet){
		selectCorrespondingTreeItems(getTreeView().getRoot(), correspondingObjectSet);
		if(!getTreeView().getRoot().getChildren().isEmpty()) {
			getTreeView().getRoot().getChildren().forEach(child -> {selectCorrespondingTreeItems(child, correspondingObjectSet);});
		}
	}
	
	private void selectCorrespondingTreeItems(TreeItem<EObject> targetItem, Set<EObject> correspondingObjectSet) {
		correspondingObjectSet.forEach(correspondingObject -> {
			if(targetItem.getValue().equals(correspondingObject)) {
				getTreeView().getSelectionModel().select(targetItem);
				if(!targetItem.isLeaf() && !targetItem.getChildren().isEmpty()) {
					targetItem.getChildren().forEach(targetChildItem -> {
						selectCorrespondingTreeItems(targetChildItem, correspondingObjectSet);});
				}
			}
		});
	}
	
	@Override
	public void clearSelections() {
		getTreeView().getSelectionModel().clearSelection();
	}
}
