package tools.vitruv.framework.visualization.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.event.ChangeListener;

import org.eclipse.emf.ecore.EObject;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class ModelController implements SingleResourceVisualizationController{
	private Model model;
	private TreeView<EObject> tree;
	private static final ObjectProperty<Boolean> clickedProperty = new SimpleObjectProperty<Boolean>();
	
	ModelController(Model model, TreeView<EObject> tree){
		this.model = model;
		this.tree = tree;
	}

	@Override
	public Model getModel() {
		return this.model;
	}
	
	@Override
	public ObjectProperty<Boolean> clickedProperty() {
		return clickedProperty;
	}
	
	public TreeView<EObject> getTreeView() {
		return this.tree;
	}

	public void declick() {
		clickedProperty.set(false);;
	}
	
	private void click() {
		clickedProperty.set(true);
	}
	
	/**
	 * This method converts the resource of the corresponding package from the model into the tree view,
	 * creates all tree items for the tree view as EObject types,
	 * 
	 * @param model A model of a package model view
	 */
	public void loadDataToTreeView() {
		TreeItem<EObject> rootItem = new TreeItem<EObject>(this.model.getRootObject()); 
		rootItem.setExpanded(true);
		getTreeView().setRoot(rootItem);
		getTreeView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getModel().getChildren(getModel().getRootObject()).forEach(object -> createChildren(rootItem, object));

		
		getTreeView().setCellFactory(tv -> {
			PackageTreeCell cell = new PackageTreeCell();
			cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
			    public void handle(MouseEvent event) {
					Node node = event.getPickResult().getIntersectedNode();
//					PackageTreeCell cel = recursiveFindCellByItem(cell.getTreeItem(), node);
					if(cell.getTreeItem()!=null) {
						System.out.println("click: "+cell.getTreeItem().getValue().toString());
						System.out.println("have: "+cell.getTreeView().getSelectionModel().getSelectedItem().getValue().toString());
						click();
						System.out.println(clickedProperty());
						cell.getTreeView().getSelectionModel().clearSelection();
						cell.getTreeView().getSelectionModel().select(cell.getTreeItem());
						System.out.println(cell.getTreeItem().getValue()+" IN");
					}
				}
			});
			
			return cell;
		});
//		setMouseClickEvent();
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
		if (object.eClass().getEAllAttributes()!=null && !object.eClass().getEAllAttributes().isEmpty()) {
			object.eContents().forEach(attribute -> createChildren(childItem, attribute));
		}
	}
	
	public List<EObject> getSelectedObject() {
		List<EObject> list = new ArrayList<>();
		getTreeView().getSelectionModel().getSelectedItems().forEach(p -> {
			list.add(p.getValue());
		});
		return list;
	}
	
	public void selectCorrespondingObjects(Set<EObject> correspondingObjectSet){
		clearSelections();

		selectMatchedTreeItems(getTreeView().getRoot(), correspondingObjectSet);
		
		if(!getTreeView().getRoot().getChildren().isEmpty()) {
			getTreeView().getRoot().getChildren().forEach(child -> {
				selectMatchedTreeItems(child, correspondingObjectSet);
			});
		}
	}
	
	private void selectMatchedTreeItems(TreeItem<EObject> targetItem, Set<EObject> correspondingObjectSet) {
		correspondingObjectSet.forEach(correspondingObject -> {
				//Whether the tree item matches the corresponding EObject
			if(targetItem.getValue().equals(correspondingObject)) {
				System.out.println("targetItem "+ targetItem.getValue().toString());
				getTreeView().getSelectionModel().select(targetItem);
				
				if(!targetItem.isLeaf() && !targetItem.getChildren().isEmpty()) {
					targetItem.getChildren().forEach(targetChildItem -> {
						selectMatchedTreeItems(targetChildItem, correspondingObjectSet);});
				}
			}
		});
	}
	
	public void clearSelections() {
		getTreeView().getSelectionModel().clearSelection();
	}
}
