package tools.vitruv.framework.visualization.api.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import edu.kit.ipd.sdq.metamodels.families.FamiliesFactory;
import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.families.Family;
import edu.kit.ipd.sdq.metamodels.families.FamilyRegister;
import edu.kit.ipd.sdq.metamodels.families.Member;
import edu.kit.ipd.sdq.metamodels.families.impl.FamiliesPackageImpl;
import edu.kit.ipd.sdq.metamodels.insurance.InsuranceDatabase;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonRegister;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import edu.kit.ipd.sdq.metamodels.persons.impl.PersonsPackageImpl;
import tools.vitruv.change.composite.description.PropagatedChange;
import tools.vitruv.change.composite.description.VitruviusChange;
import tools.vitruv.change.correspondence.Correspondence;
import tools.vitruv.change.correspondence.view.EditableCorrespondenceModelView;
import tools.vitruv.change.interaction.UserInteractionFactory;
import tools.vitruv.change.propagation.ProjectMarker;
import tools.vitruv.dsls.demo.familiespersons.families2persons.FamiliesToPersonsChangePropagationSpecification;
import tools.vitruv.dsls.demo.familiespersons.persons2families.PersonsToFamiliesChangePropagationSpecification;
import tools.vitruv.dsls.demo.insurancepersons.persons2insurance.PersonsToInsuranceChangePropagationSpecification;
import tools.vitruv.dsls.reactions.runtime.correspondence.CorrespondencePackage;
import tools.vitruv.dsls.reactions.runtime.correspondence.impl.CorrespondencePackageImpl;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.views.View;
import tools.vitruv.framework.views.ViewTypeFactory;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;
import tools.vitruv.framework.vsum.VirtualModelBuilder;
import tools.vitruv.framework.vsum.internal.InternalVirtualModel;

public class VSUMFamiliesPersonsInsurancesAPI implements
		VSUMVisualizationAPI<edu.kit.ipd.sdq.metamodels.families.FamiliesPackage, edu.kit.ipd.sdq.metamodels.persons.PersonsPackage, edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage> {
	private static URI ROOT = URI.createFileURI(new File("").getAbsolutePath() + "/resources/");
	private static URI FAMILY_URI = URI
			.createFileURI(new File("").getAbsolutePath() + "/resources/model/" + "register.families");
	private FamilyRegister familyRegister;
	private PersonRegister personRegister;
	private InsuranceDatabase insuranceRegister;
	private InternalVirtualModel vsum;
	private BiMap<EObject, EObject> familyMap;
	private BiMap<EObject, EObject> personMap;
	private BiMap<EObject, EObject> insuranceMap;

	public VSUMFamiliesPersonsInsurancesAPI() {
		registerFactories();
		new File(ROOT.path()).mkdir();
		try {
			ProjectMarker.markAsProjectRootFolder(Path.of(ROOT.path()));
		} catch (IOException e) {
		}
		buildInternalVirtualModel();
		initiliazeRegisters();
	}

	private void buildInternalVirtualModel() {
		this.vsum = new VirtualModelBuilder()
				.withChangePropagationSpecification(new FamiliesToPersonsChangePropagationSpecification())
				.withChangePropagationSpecification(new PersonsToFamiliesChangePropagationSpecification())
				.withChangePropagationSpecification(new PersonsToInsuranceChangePropagationSpecification())
				.withStorageFolder(new File(new File("").getAbsolutePath() + "/resources/"))
				.withUserInteractor(UserInteractionFactory.instance.createUserInteractor(
						UserInteractionFactory.instance.createPredefinedInteractionResultProvider(null)))
				.buildAndInitialize();
	}

	private void initiliazeRegisters() {
		if (vsum.getViewSourceModels().size() > 0) {
			extractRegisters();
		} else {
			generateRegisters();
		}
		familyMap = HashBiMap.create();
		personMap = HashBiMap.create();
		insuranceMap = HashBiMap.create();

	}

	private void extractRegisters() {
		vsum.getViewSourceModels().forEach(it -> {
			if (it.getContents().get(0) instanceof FamilyRegister) {
				this.familyRegister = (FamilyRegister) it.getContents().get(0);
			} else if (it.getContents().get(0) instanceof PersonRegister) {
				this.personRegister = (PersonRegister) it.getContents().get(0);
			} else if (it.getContents().get(0) instanceof InsuranceDatabase) {
				this.insuranceRegister = (InsuranceDatabase) it.getContents().get(0);
			}
		});
	}

	private void generateRegisters() {
		familyRegister = (FamilyRegister) FamiliesFactory.eINSTANCE
				.create(FamiliesPackage.eINSTANCE.getFamilyRegister());
		familyRegister.setId("Family Register");
		var selector = vsum.createSelector(ViewTypeFactory.createIdentityMappingViewType("all"));
		CommittableView view = selector.createView().withChangeDerivingTrait();
		view.registerRoot(familyRegister, FAMILY_URI);
		Family family1 = FamiliesFactory.eINSTANCE.createFamily();
		family1.setLastName("Mueller");
		Member father1 = FamiliesFactory.eINSTANCE.createMember();
		father1.setFirstName("Alex");
		father1.setFamilyFather(family1);
		Member mother1 = FamiliesFactory.eINSTANCE.createMember();
		mother1.setFirstName("Jenny");
		mother1.setFamilyMother(family1);
		Member son1 = FamiliesFactory.eINSTANCE.createMember();
		son1.setFirstName("Florian");
		son1.setFamilySon(family1);
		familyRegister.getFamilies().add(family1);
		
		
		Family family2 = FamiliesFactory.eINSTANCE.createFamily();
		family2.setLastName("Schmidt");
		Member father2 = FamiliesFactory.eINSTANCE.createMember();
		father2.setFirstName("Lukas");
		father2.setFamilyFather(family2);
		Member mother2 = FamiliesFactory.eINSTANCE.createMember();
		mother2.setFirstName("Lilian");
		mother2.setFamilyMother(family2);
		Member dauther2 = FamiliesFactory.eINSTANCE.createMember();
		dauther2.setFirstName("Lea");
		dauther2.setFamilyDaughter(family2);
		
		familyRegister.getFamilies().add(family2);
		
		
		
		// the consistency preservation is active and generates a person register
		// automatically
		view.commitChanges();
		extractRegisters();
	}

	private void registerFactories() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		EPackage.Registry.INSTANCE.put(CorrespondencePackage.eNS_URI, CorrespondencePackageImpl.init());
		EPackage.Registry.INSTANCE.put(FamiliesPackage.eNS_URI, FamiliesPackageImpl.init());
		EPackage.Registry.INSTANCE.put(PersonsPackage.eNS_URI, PersonsPackageImpl.init());
	}

	private View getT1View() {
		return getView(familyRegister.eResource(), familyMap, "family");
	}

	private View getT2View() {
		return getView(personRegister.eResource(), personMap, "person");
	}

	private View getT3View() {
		return getView(insuranceRegister.eResource(), insuranceMap, "insurance");
	}

	private View getView(Resource resource, Map<EObject, EObject> register, String description) {
		register.clear();
		var selector = vsum.createSelector(ViewTypeFactory.createIdentityMappingViewType(description));
		selector.getSelectableElements().stream().filter(element -> element.eResource().equals(resource))
				.forEach(it -> selector.setSelected(it, true));
		View view = selector.createView();
		Iterator<EObject> viewIterator = view.getRootObjects().iterator().next().eResource().getAllContents();
		Iterator<EObject> sourceIterator = resource.getAllContents();
		while (viewIterator.hasNext()) {
			register.put(viewIterator.next(), sourceIterator.next());
		}
		return view;
	}

	@Override
	public List<PropagatedChange> propagateChange(VitruviusChange change) {
		return vsum.propagateChange(change);
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.lineSeparator());
		getT1View().getRootObjects().forEach(it -> {
			joiner.add(buildCorrespondenceVisualization(it));
			it.eAllContents().forEachRemaining(eObject -> {
				joiner.add(buildCorrespondenceVisualization(eObject));
			});
		});
		return joiner.toString();
	}

	private String buildCorrespondenceVisualization(EObject eObject) {
		StringJoiner inner = new StringJoiner("<--------->");
		inner.add(eObject.toString());
		if (getCorrespondingEObjects(eObject).size() > 0) {
			getCorrespondingEObjects(eObject).forEach(it -> {
				inner.add(it.toString());
				getCorrespondingEObjects(it).forEach(its -> {
					inner.add(its.toString());
				});
			});
		}
		return inner.toString();
	}

	@Override
	public View getView(EPackage t) {
		if (t.equals(getT1())) {
			return getT1View();
		} else if (t.equals(getT2())) {
			return getT2View();
		} else if (t.equals(getT3())) {
			return getT3View();
		} else {
			throw new IllegalArgumentException("Unexpected value: " + t);
		}
	}

	@Override
	public FamiliesPackage getT1() {
		return FamiliesPackage.eINSTANCE;
	}

	@Override
	public PersonsPackage getT2() {
		return PersonsPackage.eINSTANCE;
	}

	@Override
	public InsurancePackage getT3() {
		return InsurancePackage.eINSTANCE;
	}

	@Override
	public Set<EObject> getCorrespondingEObjects(EObject sourceObject) {
		var sourceObj = vsum.getCorrespondenceModel().getCorrespondingEObjects(familyMap.getOrDefault(sourceObject,
				personMap.getOrDefault(sourceObject, insuranceMap.get(sourceObject))));
		if (sourceObj.iterator().hasNext()) {
			var sourceO = sourceObj.iterator().next();
			return Set.of(familyMap.inverse().getOrDefault(sourceO,
					personMap.inverse().getOrDefault(sourceO, insuranceMap.inverse().getOrDefault(sourceO, null))));
		}
		return Set.of();
	}

}
