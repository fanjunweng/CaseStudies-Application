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
	@FXML private TreeView<String> leftTree;
	@FXML private TreeView<String> centerTree;
	@FXML private TreeView<String> rightTree;
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
		createAllVisualCorrespondences();
		
	
//		TreeView<String> personsTreeView = convertDataToTreeView(getResourceForPackageView(vsumVisualizationAPI.getT2()));
//		pane.setCenter(personsTreeView);
//		personsTreeView.setMinWidth(400);
	} 
	
	private void createTreeView(Model model, TreeView<String> tree) {
		convertDataToTreeView(model, tree);
}

	private void createTextArea() {
		StringJoiner builder = new StringJoiner(System.lineSeparator());
		model1.getResourceForPackageView().getAllContents().forEachRemaining(eObject -> 
			builder.add(eObject + " --> " + vsumVisualizationAPI.getCorrespondingEObjects(eObject)  + " --> " + 
			vsumVisualizationAPI.getCorrespondingEObjects(vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next())));
		textArea.setText(builder.toString());
	}
	
	private void createAllVisualCorrespondences() {
//		model1.getResourceForPackageView().getAllContents().forEachRemaining(eObject -> {
//		createCorrespondenceLine(eObject, vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next(), vsumVisualizationAPI.getT1());
//	1 = eObject;
//	2 = vsumVisualizationAPI.getCorrespondingEObjects(eObject);
//	3 = vsumVisualizationAPI.getCorrespondingEObjects(vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next());
//	});
		Line line = createCorrespondenceLine(new Line(), leftTree, centerTree);
		Line line2 = createCorrespondenceLine(new Line(), centerTree, rightTree);
		

        mainPane.getChildren().add(line);
        mainPane.getChildren().add(line2);
	}

	private Line createCorrespondenceLine(Line line, TreeView<String> sourceTree, TreeView<String> targetTree) {
		ObjectProperty<Point2D> start = new SimpleObjectProperty<>(this, "start");
		ObjectProperty<Point2D> end = new SimpleObjectProperty<>(this, "end");
		
		line.getStyleClass().add("assignment");
		line.setManaged(false);
		line.setStroke(Color.BLUE);
		line.startXProperty().bind(Bindings.createDoubleBinding(() -> {
			if (start.get()==null) {
				return 0.0 ;
			} else {
				return start.get().getX();
			}
		}, start));
		
		line.startYProperty().bind(Bindings.createDoubleBinding(() -> {
			if (start.get()==null) {
				return 0.0 ;
			} else {
				return start.get().getY();
			}
		}, start));
		
		line.endXProperty().bind(Bindings.createDoubleBinding(() -> {
			if (end.get()==null) {
				return 0.0 ;
			} else {
				return end.get().getX();
			}
		}, end));

		line.endYProperty().bind(Bindings.createDoubleBinding(() -> {
			if (end.get()==null) {
				return 0.0 ;
			} else {
				return end.get().getY();
			}
		}, end));
		
		line.visibleProperty().bind(
				Bindings.isNotNull(start)
				.and(Bindings.isNotNull(end)));
		
		sourceTree.setCellFactory((TreeView<String> p) -> {
			TextFieldTreeCell cell = new TextFieldTreeCell();

			cell.itemProperty().addListener((obs, oldValue, newValue) -> {
				if(newValue != null) {
					bindLocation(start, cell, leftTree);
				}
			});
			return cell;
		});
		
		targetTree.setCellFactory((TreeView<String> p) -> {
			TextFieldTreeCell cell = new TextFieldTreeCell();
			System.out.println(cell.itemProperty().getValue());
			
			cell.itemProperty().addListener((obs, oldValue, newValue) -> {
				if(newValue!=null) {
					bindLocation(end, cell, centerTree);
				}
			});
			return cell;
		});
        
        return line;
	}
	
	private void bindLocation(ObjectProperty<Point2D> locationToBind, TreeCell<?> cell, TreeView<?> tree) {
		Node treeParent = tree.getParent();
		ObjectBinding<Point2D> locationBinding = new ObjectBinding<Point2D>() {
			
			{
				cell.localToSceneTransformProperty().addListener((obs, oldTransform, newTransform) -> this.invalidate());
				tree.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> this.invalidate());
			}

			@Override
			protected Point2D computeValue() {
				Bounds cellBoundsInLocal = cell.getBoundsInLocal();
				Point2D cellCenter = new Point2D(
						cellBoundsInLocal.getMinX() + cellBoundsInLocal.getWidth() / 2,
						cellBoundsInLocal.getMinY() + cellBoundsInLocal.getHeight() / 2);
				Point2D cellCenterInScene = cell.localToScene(cellCenter);
				Point2D cellCenterInTreeParent = treeParent.sceneToLocal(cellCenterInScene);
				return cellCenterInTreeParent ;			}
			
		};
		locationToBind.bind(locationBinding);
	}
	
	private List<Node> getNode(EPackage sourcePackage, EObject sourceObject) {
		final List<Node> nodeList = null;

		if(sourcePackage.equals(vsumVisualizationAPI.getT1())){
			
			for(Node n: leftTree.getChildrenUnmodifiable()) {
				System.out.println(n.toString() +"--"+ sourceObject.toString());
				if(n.toString().equals(sourceObject.toString())) {
					nodeList.add(n);
				}		
			}
		}else if(sourcePackage.equals(vsumVisualizationAPI.getT2())){
			for(Node n: centerTree.getChildrenUnmodifiable()) {
				System.out.println(n.toString() +"--"+ sourceObject.toString());
				if(n.toString().equals(sourceObject.toString())) {
					nodeList.add(n);
				}		
			}
		}
		return nodeList;
	}
	
//	private void buildVisualCorrespondence(Set<EObject> correspondingEObjects, EPackage targetPackage) {
//		if (correspondingEObjects.size() > 0) {
//			correspondingEObjects.forEach(it -> {
//				vsumVisualizationAPI.getView(targetPackage).getRootObjects().forEach(its -> {
//					System.out.println("it: " + it());
//				});
//			});
//		}
//	}
	
//	private Resource getResourceForPackageView(EPackage ePackage) {
//		return vsumVisualizationAPI.getView(ePackage).getRootObjects().iterator().next().eResource();
//	}

	// Convert the resource data into tree view format
	public void convertDataToTreeView(Model model, TreeView<String> tree) {
		// Get content of the resource
		Resource modelResource = model.getResourceForPackageView();
		EObject resource = modelResource.getContents().get(0); // FamilyRegister in the first layer
		int nodeIndex = -1; //parent node is root
		// Create a root for the tree
		TreeItem<String> rootItem = new TreeItem<String>(resource.eClass().getName()); 
		rootItem.setExpanded(true);
		tree.setRoot(rootItem);
		model.addEObject(resource.eClass(), nodeIndex);
		
		StringBuilder name = new StringBuilder();

//		// TW: Create the name of the attribute of the id
		for (EAttribute att : resource.eClass().getEAttributes()) {
			name.append(att.getName() + ": " + resource.eContainingFeature() + " "); // = what is the id number??
			model.addEObject(att, nodeIndex++);
		}
	
		TreeItem<String> attributeNode = new TreeItem<String>(name.toString()); 
		rootItem.getChildren().add(attributeNode);
	
	
		// Create all children nodes for each sub root of the tree
		EList<EObject> subRootList = resource.eContents(); // families in the second layer
	
		for (EObject subRoot : subRootList) {
			createChildren(rootItem, subRoot, model, nodeIndex);
		}
	}

	// Recursive create all children nodes
	public void createChildren(TreeItem<String> parentItem, EObject node, Model model, int nodeIndex) {
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
				model.addEObject(att, nodeIndex++);
			}
			
			TreeItem<String> memberNode = new TreeItem<String>(treeName.toString());
			parentItem.getChildren().add(memberNode);
			memberNode.setExpanded(true);
	
			// Create Tree view item for the children of the sub root
			for (EAttribute att : nodeList) {
				for (EObject e : node.eContents()) {
					createChildren(memberNode, e, model, nodeIndex++);
				}
			}
		}
	}	
}


