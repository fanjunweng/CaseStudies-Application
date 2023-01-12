package tools.vitruv.framework.visualization.api;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import tools.vitruv.change.composite.description.PropagatedChange;
import tools.vitruv.change.composite.description.VitruviusChange;
import tools.vitruv.framework.views.View;
import tools.vitruv.framework.visualization.api.impl.VSUMFamiliesPersonsInsurancesAPI;

public interface VSUMVisualizationAPI<T1 extends EPackage, T2 extends EPackage, T3 extends EPackage> {
	Set<EObject> getCorrespondingEObjects(EObject sourceObject);
	View getView(EPackage t);
	T1 getT1();
	T2 getT2();
	T3 getT3();
	List<PropagatedChange> propagateChange(VitruviusChange change);
	static VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> getVSUMFamiliesPersonsInsurancesAPI() {
		return new VSUMFamiliesPersonsInsurancesAPI();
	}
}
