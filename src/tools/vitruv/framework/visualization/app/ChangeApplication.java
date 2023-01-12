package tools.vitruv.framework.visualization.app;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.BorderLayout;
import java.io.*;
import java.util.*;
import java.util.Date.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.plaf.basic.BasicTextPaneUI;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import tools.vitruv.change.correspondence.CorrespondencePackage;
import tools.vitruv.change.correspondence.Correspondences;
import tools.vitruv.change.correspondence.CorrespondenceFactory;
import tools.vitruv.dsls.demo.familiespersons.families2persons.FamiliesToPersonsChangePropagationSpecification;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

public class ChangeApplication extends Application {

	private VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// initialize data structures
		vsumVisualizationAPI = VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI();

		// Main layout
		BorderPane pane = new BorderPane();

		createTreeView(vsumVisualizationAPI.getT1(), pane, (node, bp) -> bp.setLeft(node));
		createTreeView(vsumVisualizationAPI.getT2(), pane, (node, bp) -> bp.setCenter(node));
		createTreeView(vsumVisualizationAPI.getT3(), pane, (node, bp) -> bp.setRight(node));
		
		Text text = new Text();
		StringJoiner builder = new StringJoiner(System.lineSeparator());
		getResourceForPackageView(vsumVisualizationAPI.getT1()).getAllContents().forEachRemaining(eObject -> 
			builder.add(eObject + " --> " + vsumVisualizationAPI.getCorrespondingEObjects(eObject)  + " --> " + 
			vsumVisualizationAPI.getCorrespondingEObjects(vsumVisualizationAPI.getCorrespondingEObjects(eObject).iterator().next())));
		text.setText(builder.toString());
		pane.setBottom(text);
//		TreeView<String> personsTreeView = convertDataToTreeView(getResourceForPackageView(vsumVisualizationAPI.getT2()));
//		pane.setCenter(personsTreeView);
//		personsTreeView.setMinWidth(400);

		stage.setTitle("Visualization");
		stage.setScene(new Scene(pane, 1800, 750));
		stage.show();
	}

	private void createTreeView(EPackage ePackage, BorderPane borderLayout, BiConsumer<Node, BorderPane> addToPane) {
		TreeView<String> treeView = convertDataToTreeView(getResourceForPackageView(ePackage));
		treeView.setMinWidth(600);
		addToPane.accept(treeView, borderLayout);
	}

	private Resource getResourceForPackageView(EPackage ePackage) {
		return vsumVisualizationAPI.getView(ePackage).getRootObjects().iterator().next().eResource();
	}

	// Convert the resource data into tree view format
	public TreeView<String> convertDataToTreeView(Resource resource) {
		// Get content of the resource
		EObject model = resource.getContents().get(0); // FamilyRegister in the first layer
		EClass eclass = model.eClass();

		// Create a root of the tree
		TreeItem<String> rootItem = new TreeItem<String>(eclass.getName());
		rootItem.setExpanded(true);

		// Create a tree view
		TreeView<String> tree = new TreeView<>(rootItem);
		StringBuilder name = new StringBuilder();

		// TW: Please try to keep your code short and expressive
		for (EAttribute att : eclass.getEAttributes()) {
			name.append(att.getName() + ": " + model.eGet(att) + " ");
		}

		TreeItem<String> attributeNode = new TreeItem<String>(name.toString());
		rootItem.getChildren().add(attributeNode);

		// Create all children nodes for each sub root of the tree
		EList<EObject> subRootList = model.eContents(); // families in the second layer

		for (EObject subRoot : subRootList) {
			createChildren(rootItem, subRoot);
		}

		return tree;
	}

	// Recursive create all children nodes
	public void createChildren(TreeItem<String> parentItem, EObject node) {
		EStructuralFeature nodeName = node.eContainingFeature(); // = families, mothers, fathers, daughters, sons
		EClass nodeClass = node.eClass();

		List<EAttribute> nodeList = nodeClass.getEAllAttributes();

		if (nodeList != null && nodeList.size() > 0) {

			StringBuilder treeName = new StringBuilder(
					nodeName.getName() + " of type " + node.eClass().getName() + " ");

			for (EAttribute att : nodeList) {
				String attName = att.getName(); // lastname or firstname
				Object attValue = node.eGet(att);// Mueller or Lilian, Lea, Lukas

//				if (attValue.getClass().getTypeName())){
//					System.out.println(att.getContainerClass().getTypeName()); // Person/ Member
//					System.out.println(nodeClass.get);
//				}
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
