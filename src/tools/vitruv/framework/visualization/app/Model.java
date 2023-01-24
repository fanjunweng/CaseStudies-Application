package tools.vitruv.framework.visualization.app;


import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

public class Model {
	private VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage>  vsumVisualizationAPI;
	private Resource resource;
	private EPackage ePackage;
	
	public Model(VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage>  vsumVisualizationAPI,
				 EPackage ePackage) {
		this.vsumVisualizationAPI = vsumVisualizationAPI;
		this.ePackage = ePackage;
		this.resource = vsumVisualizationAPI.getView(this.ePackage).getRootObjects().iterator().next().eResource();
	}
	
	public VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> getVSUMVisualizationAPI(){
		return this.vsumVisualizationAPI;
	}
	
	public void setVSUMVisualizationAPI(VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI){
		this.vsumVisualizationAPI = vsumVisualizationAPI;
	}
	
	public Resource getResourceForPackageView() {
		return this.resource;
	}
	
	public void setResourceForPackageView(EPackage ePackage) {
		this.ePackage = ePackage;
		this.resource = vsumVisualizationAPI.getView(this.ePackage).getRootObjects().iterator().next().eResource();
	}
	
	public EPackage getEPackage() {
		return this.ePackage;
	}

}
//	public void iterateResource(Resource resource, TreeView<String> tree) {
//		// Get content of the resource
//		EObject model = resource.getContents().get(0); // FamilyRegister in the first layer
//		
//		
//		// Create a root for the tree
//		TreeItem<String> rootItem = new TreeItem<String>(model.eClass().getName());
//		rootItem.setExpanded(true);
//		tree.setRoot(rootItem);
//		
//		StringBuilder name = new StringBuilder();
//	
//		// TW: Create the name of the attribute of the id
//		for (EAttribute att : model.eClass().getEAttributes()) {
//			name.append(att.getName() + ": " + model.eContainingFeature() + " "); // = what is the id number??
//		}
//	
//		
//		TreeItem<String> attributeNode = new TreeItem<String>(name.toString());
//		rootItem.getChildren().add(attributeNode);
//	
//	
//		// Create all children nodes for each sub root of the tree
//		EList<EObject> subRootList = model.eContents(); // families in the second layer
//	
//		for (EObject subRoot : subRootList) {
//			createChildren(rootItem, subRoot);
//		}
//	
////		return tree;
//	}
//
//	// Recursive create all children nodes
//	public void createChildren(TreeItem<String> parentItem, EObject node) {
//		EStructuralFeature nodeName = node.eContainingFeature(); // = families, mothers, fathers, daughters, sons
//		List<EAttribute> nodeList = node.eClass().getEAllAttributes();
//	
//		if (nodeList != null && nodeList.size() > 0) {
//			
//			StringBuilder treeName = new StringBuilder(
//					nodeName.getName() + " of type " + node.eClass().getName() + " ");
//	
//			for (EAttribute att : nodeList) {
//				String attName = att.getName(); // lastname or firstname
//				Object attValue = node.eGet(att);// Mueller or Lilian, Lea, Lukas
//	
//				// Create Tree view item for the sub root
//				treeName.append(attName + ": " + attValue + "   ");
//			}
//	
//			TreeItem<String> memberNode = new TreeItem<String>(treeName.toString());
//			parentItem.getChildren().add(memberNode);
//			memberNode.setExpanded(true);
//	
//			// Create Tree view item for the children of the sub root
//			for (EAttribute att : nodeList) {
//				for (EObject e : node.eContents()) {
//					createChildren(memberNode, e);
//				}
//			}
//		}
//	}	
//}

//}
