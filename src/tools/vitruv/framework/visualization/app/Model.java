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
	private TreeView<EObject> treeView;
	
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
	
	public TreeView<EObject> getTreeView(){
		return this.treeView;
	}
	
	public void setTreeView(TreeView<EObject> treeView){
		this.treeView = treeView;
	}

}
