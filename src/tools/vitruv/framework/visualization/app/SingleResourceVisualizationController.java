package tools.vitruv.framework.visualization.app;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import javafx.beans.property.ObjectProperty;

public interface SingleResourceVisualizationController {
//	public VSUMFamiliesPersonsInsurancesAPI getVSUMFamiliesPersonsInsurancesAPI();
	
	Model getModel();
	void loadDataToTreeView();
	List<EObject> getSelectedObject();
	void selectCorrespondingObjects(Set<EObject> correspondingObjectSet);
	ObjectProperty<Boolean> clickedProperty();
	void declick();
	void clearSelections();
}
