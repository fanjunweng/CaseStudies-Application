package tools.vitruv.framework.visualization.app;


import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.EObject;

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import javafx.scene.control.TreeView;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

/**
 * This class model refers to a model in the MVC pattern.
 *
 */
public class Model {
	private VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage>  vsumVisualizationAPI;
	private Resource resource;
	private EPackage ePackage;
	private TreeView<EObject> treeView;
	
	/**
	 * The construction function for the Model class
	 * @param vsumVisualizationAPI  An API for the model visualization of three packages 
	 * @param ePackage An EPackage, which belongs to one of these three packages 
	 */
	public Model(VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage>  vsumVisualizationAPI,
				 EPackage ePackage) {
		this.vsumVisualizationAPI = vsumVisualizationAPI;
		this.ePackage = ePackage;
		this.resource = vsumVisualizationAPI.getView(this.ePackage).getRootObjects().iterator().next().eResource();
	}
	
	/**
	 * This method gets the vsumVisualizationAPI object
	 * @return An API for the model visualization of three packages
	 */
	public VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> getVSUMVisualizationAPI(){
		return this.vsumVisualizationAPI;
	}
	
	/**
	 * This method sets the vsumVisualizationAPI object
	 * @param vsumVisualizationAPI An API for the model visualization of three packages
	 */
	public void setVSUMVisualizationAPI(VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI){
		this.vsumVisualizationAPI = vsumVisualizationAPI;
	}
	
	/**
	 * This method gets the resource object
	 * @return A persistent document containing the package view
	 */
	public Resource getResourceForPackageView() {
		return this.resource;
	}
	
	/**
	 * This method sets the resource object according to the EPackage
	 * @param ePackage A representation of the model object EPackage
	 */
	public void setResourceForPackageView(EPackage ePackage) {
		this.ePackage = ePackage;
		this.resource = vsumVisualizationAPI.getView(this.ePackage).getRootObjects().iterator().next().eResource();
	}
	
	/**
	 * This method gets the ePackage object
	 * @return A representation of the model object EPackage
	 */
	public EPackage getEPackage() {
		return this.ePackage;
	}
	
	/**
	 * This method gets the treeView object
	 * @return A tree view control
	 */
	public TreeView<EObject> getTreeView(){
		return this.treeView;
	}
	
	/**
	 * This method sets the treeView object
	 * @param treeView A view of hierarchical structures
	 */
	public void setTreeView(TreeView<EObject> treeView){
		this.treeView = treeView;
	}
}
