package tools.vitruv.framework.visualization.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.util.Date.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import tools.vitruv.change.correspondence.CorrespondencePackage;
import tools.vitruv.change.correspondence.Correspondences;
import tools.vitruv.change.correspondence.CorrespondenceFactory;
import tools.vitruv.dsls.demo.familiespersons.families2persons.FamiliesToPersonsChangePropagationSpecification;

public class ChangeApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		//Main layout
        BorderPane pane = new BorderPane();
        
        //Create a tree 
		String filePath = "resources/FamilyRegister.families";
//		String filePath = "resources/PersonRegister.persons" + ".xml";
		Resource resource = loadData(filePath);
		TreeView<String> tree = convertDataToTreeView(resource);
		tree.setMinWidth(600);
		pane.setLeft(tree);
		
		//Create the corresponding tree
		Resource correspondenceResource = getCorrespondence(filePath, resource);
		if (correspondenceResource != null) {
			TreeView<String> correspondenceTree = convertDataToTreeView(correspondenceResource);
			pane.setRight(correspondenceTree);
			correspondenceTree.setMinWidth(600);
		}
        
		stage.setTitle("Visualization");
		stage.setScene(new Scene(pane, 1200, 750));
		stage.show();
	}
	
	
	
	public Resource loadData(String fileName) throws FileNotFoundException, IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		HashMap<String, Object> options = setUpLoading(resourceSet, fileName);
		
	    //Create and load resource with the uri
	    URI uri = URI.createFileURI(new File(fileName).getAbsolutePath());
		Resource resource = resourceSet.createResource(uri);
		resource.load(options);
		resourceSet.getResources().add(resource);

		return resource;
	}

	//Convert the resource data into tree view format
	public TreeView<String> convertDataToTreeView(Resource resource) {
		//Get content of the resource
		EObject model = resource.getContents().get(0); //FamilyRegister in the first layer
		EClass eclass = model.eClass();
		System.out.println(eclass.getName());
		
		//Create a root of the tree
		TreeItem<String> rootItem = new TreeItem<String>(eclass.getName());
		rootItem.setExpanded(true);
		
		//Create a tree view 
		TreeView<String> tree = new TreeView<> (rootItem);
		StringBuilder name = new StringBuilder();
		
		//Create a tree item for attribute
		for (Iterator<EAttribute> iterator = eclass.getEAllAttributes().iterator(); iterator.hasNext();) {
			  EAttribute att = (EAttribute) iterator.next(); //id:111
			  Object value = model.eGet(att); 
			  name.append(att.getName() + ":  " +value + "   ");
		}
		
		TreeItem<String> attributeNode = new TreeItem<String>(name.toString());
		rootItem.getChildren().add(attributeNode);
		
		
		//Create all children nodes for each sub root of the tree
		EList<EObject> subRootList = model.eContents(); //families in the second layer
		
		for(EObject subRoot : subRootList) {
			createChildren(rootItem, subRoot);
		}

        return tree; 
	}
	
	//Set up the loading
	public HashMap<String, Object> setUpLoading(ResourceSet resourceSet, String fileName) {
		//Initial the model according to the package
		if (fileName.endsWith(PersonsPackage.eNAME)) {
			PersonsPackage.eINSTANCE.eClass();
			resourceSet.getPackageRegistry().put(PersonsPackage.eINSTANCE.getNsURI(), PersonsPackage.eINSTANCE);
			
		}else if (fileName.endsWith(FamiliesPackage.eNAME )){
			FamiliesPackage.eINSTANCE.eClass();
			resourceSet.getPackageRegistry().put(FamiliesPackage.eINSTANCE.getNsURI(), FamiliesPackage.eINSTANCE);		
		}
		

		//Register the extension of the XML file
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
		options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
		options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
			
		//Register the default resource factory -- only needed for stand-alone application	
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	    
	    return options;
	}
	
	
	//Recursive create all children nodes
	public void createChildren(TreeItem<String> parentItem, EObject node) {
		EStructuralFeature nodeName = node.eContainingFeature(); //= families, mothers, fathers, daughters, sons
		EClass nodeClass = node.eClass();
		
		List<EAttribute> nodeList = nodeClass.getEAllAttributes();
		
		if(nodeList != null && nodeList.size() > 0) {
			
			StringBuilder treeName = new StringBuilder(nodeName.getName()+ "    " + node.eClass().getName());
			
			for(EAttribute att : nodeList) {
				String attName = att.getName(); //lastname or firstname
				Object attValue = node.eGet(att);//Mueller or Lilian, Lea, Lukas
				
//				if (attValue.getClass().getTypeName())){
//					System.out.println(att.getContainerClass().getTypeName()); // Person/ Member
//					System.out.println(nodeClass.get);
//				}
				//Create Tree view item for the sub root
				treeName.append(attName + ": " + attValue + "   ");
			}
			
			TreeItem<String> memberNode = new TreeItem<String>(treeName.toString());
			parentItem.getChildren().add(memberNode);
			memberNode.setExpanded(true);
			
			//Create Tree view item for the children of the sub root
			for(EAttribute att : nodeList) {
		        for(EObject e: node.eContents()){   	
		        	createChildren(memberNode, e);
		        }
			}
		}
	}

	//Get the corresponding resource of the imported model resource
	public Resource getCorrespondence(String filepath, Resource resource) throws FileNotFoundException, IOException {
//		Unknown ESuperType
//		EPackage superpac = pac.getESuperPackage();
//		if(superpac !=null) {
//			System.out.println(superpac.getName());
//		}
//		Package pac = resource.getClass();

		
		Resource correspondenceResource = null;
		
		if(filepath.endsWith(PersonsPackage.eINSTANCE.getName())) {
			String className = FamiliesPackage.eINSTANCE.getFamilyRegister().getName();
			FamiliesPackage.eINSTANCE.eClass();
			String path = "resources/" + className + "." + FamiliesPackage.eINSTANCE.getName() ;
			correspondenceResource = loadData(path);
		
		}else if(filepath.endsWith(FamiliesPackage.eINSTANCE.getName())) {
			String className = PersonsPackage.eINSTANCE.getPersonRegister().getName();
			PersonsPackage.eINSTANCE.eClass();
		
			String path = "resources/" + className + "." + PersonsPackage.eINSTANCE.getName();
			correspondenceResource = loadData(path);
			
			CorrespondenceFactory.eINSTANCE.create(PersonsPackage.eINSTANCE.getPersonRegister());
			Correspondences e = CorrespondenceFactory.eINSTANCE.createCorrespondences();

			CorrespondencePackage.eINSTANCE.setEFactoryInstance(CorrespondenceFactory.eINSTANCE);
			System.out.println(CorrespondenceFactory.eINSTANCE.getCorrespondencePackage().getNsPrefix());
		}
		
		CorrespondencePackage p = CorrespondenceFactory.eINSTANCE.getCorrespondencePackage();

		return correspondenceResource;
	}
}
