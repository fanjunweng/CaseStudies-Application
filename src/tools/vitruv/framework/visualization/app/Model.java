package tools.vitruv.framework.visualization.app;


import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import tools.vitruv.framework.views.View;

/**
 * This class model refers to a model in the MVC pattern, that have the package, view and view resource, that contains 
 * root EObject, content EObjects of the root, feature EClasses and attributes EAttributs of the feature.
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
	 * @param view A package view
	 */
	public Model(EPackage ePackage, View view) { 
		this.ePackage = ePackage;
		this.view = view;
		this.resource = this.view.getRootObjects().iterator().next().eResource();
		this.rootObject = this.resource.getContents().get(0);
	}
	
	public EPackage getEPackage() {
		return this.ePackage;
	}
	
	public View getView() {
		return this.view;
	}
	
	public Resource getResource() {
		return this.resource;
	}

	public EObject getRootObject() {
		return this.rootObject;
	}
	
	public EList<EObject> getContentObjects(EObject object) {
		return object.eContents();
	}
	
	public EClass getFeature(EObject object) {
		return object.eClass();
	}
	
	public EList<EAttribute> getAttributes(EObject object) {
		return getFeature(object).getEAllAttributes();
	}
}
