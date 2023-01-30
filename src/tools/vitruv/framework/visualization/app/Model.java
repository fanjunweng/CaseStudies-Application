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
	 * 
	 * @param vsumVisualizationAPI
	 * @param ePackage
	 */
	public Model(VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage>  vsumVisualizationAPI,
				 EPackage ePackage) {
		this.vsumVisualizationAPI = vsumVisualizationAPI;
		this.ePackage = ePackage;
		this.resource = vsumVisualizationAPI.getView(this.ePackage).getRootObjects().iterator().next().eResource();
	}
	
	/**
	 * 
	 * @return
	 */
	public VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> getVSUMVisualizationAPI(){
		return this.vsumVisualizationAPI;
	}
	
	/**
	 * 
	 * @param vsumVisualizationAPI
	 */
	public void setVSUMVisualizationAPI(VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI){
		this.vsumVisualizationAPI = vsumVisualizationAPI;
	}
	
	/**
	 * 
	 * @return
	 */
	public Resource getResourceForPackageView() {
		return this.resource;
	}
	
	/**
	 * 
	 * @param ePackage
	 */
	public void setResourceForPackageView(EPackage ePackage) {
		this.ePackage = ePackage;
		this.resource = vsumVisualizationAPI.getView(this.ePackage).getRootObjects().iterator().next().eResource();
	}
	
	/**
	 * 
	 * @return
	 */
	public EPackage getEPackage() {
		return this.ePackage;
	}
	
	/**
	 * 
	 * @return
	 */
	public TreeView<EObject> getTreeView(){
		return this.treeView;
	}
	
	/**
	 * 
	 * @param treeView
	 */
	public void setTreeView(TreeView<EObject> treeView){
		this.treeView = treeView;
	}

}
