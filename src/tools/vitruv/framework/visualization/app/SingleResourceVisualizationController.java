package tools.vitruv.framework.visualization.app;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import javafx.beans.property.ObjectProperty;

/**
 * This interface is a controller for individual resource visualization, 
 * that implements the functionality of loading single resource data in xmi format into a visual tree view control,
 * and and highlighting the corresponding object of a clicked object.
 */
public interface SingleResourceVisualizationController {
	/**
	 * Load single resource data in xmi format into a visual tree view control
	 */
	void loadDataToTreeView();
	/**
	 * Return the click boolean property of the tree view of the single resource in order to add a listener to it
	 * @return click boolean property of this tree view
	 */
	ObjectProperty<Boolean> clickProperty();
	/**
	 * Set the click boolean property of this tree view to false 
	 * in oder to change the observable value of the property listener
	 */
	void declick();
	/**
	 * Return a list of the selected EObjects in order to find their corresponding EObjects
	 * @return list of the selected EObjects
	 */
	List<EObject> getSelectedObjects();
	
	/**
	 * Select all corresponding EObjects from the other tree views in order to highlight the correspondence.
	 * @param correspondingObjectSet A set of corresponding EObjects of the selected EObjects
	 */
	void selectCorrespondingObjects(Set<EObject> correspondingObjectSet);
	
	/**
	 * Clear all selections in this tree view 
	 * in order to start a new round of selection and highlight the corresponding EObjects.
	 */
	void clearSelections();
}
