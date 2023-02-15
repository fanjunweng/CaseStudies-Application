package tools.vitruv.framework.visualization.app;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EObject;

/**
 * This interface is a controller for individual resource visualization, 
 * that implements the functionality of set single resource data in xmi format into a visual tree view control,
 * and and highlighting the corresponding object of a clicked object.
 */
public interface SingleResourceVisualizationController {
	/**
	 * Convert single resource data in XMI format into a visual tree view
	 */
	void setResource();
	
	/**
	 *Set highlighting operations for selected EObjects and their corresponding EObjects in each tree view
	 * @param consumer An operation representation that accepts a single input argument 
	 */
	void setSelectedObjectsChangedConsumer(Consumer<SingleResourceVisualizationController> consumer);

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
