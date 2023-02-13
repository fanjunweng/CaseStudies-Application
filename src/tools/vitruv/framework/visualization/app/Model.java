package tools.vitruv.framework.visualization.app;


import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import tools.vitruv.framework.views.View;

/**
 * This class model refers to a model in the MVC pattern, that contains the package view and the view resource.
 *
 */
public class Model {
	private Resource resource;
	private EPackage ePackage;
	private View view;
	private EObject rootObject;
	
	/**
	 * The construction function for the Model class
	 * @param ePackage An EPackage, which belongs to one of these three packages 
	 * @param view A view of the package
	 */
	public Model(EPackage ePackage, View view) { 
		this.ePackage = ePackage;
		this.view = view;
		this.resource = this.view.getRootObjects().iterator().next().eResource();
		this.rootObject = this.resource.getContents().get(0);
	}
	
	/**
	 * This method gets the ePackage object
	 * @return A representation of the model object EPackage
	 */
	public EPackage getEPackage() {
		return this.ePackage;
	}
	
	public View getView() {
		return this.view;
	}
	
	/**
	 * This method gets the resource object
	 * @return A persistent document containing the package view
	 */
	public Resource getResource() {
		return this.resource;
	}

	public EObject getRootObject() {
		return this.rootObject;
	}
	
	public EList<EObject> getChildren(EObject object) {
		return object.eContents();
	}
	
	public EList<EAttribute> getAttributes(EObject object) {
		return object.eClass().getEAllAttributes();
	}
}
