package tools.vitruv.framework.visualization.app;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.families.impl.FamiliesPackageImpl;


public class ChangeApplication extends Application {


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			//Register the package
			FamiliesPackage familiesPackage = FamiliesPackageImpl.init();
//			   familiesPackage.eINSTANCE.eClass();

			//Load Resource from XML file
			HashMap<String, Object> options = new HashMap<String, Object>();
			options.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
			options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
			options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
			
			
			ResourceSet resourceSet = new ResourceSetImpl();
			
			//Register the default resource factory -- only needed for stand-alone application	
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new XMIResourceFactoryImpl());
		    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		    
		    //Create and load resource with the uri
		    URI uri = URI.createFileURI(new File("resources/FamilyRegister.families" + ".xml").getAbsolutePath());
			Resource resource = resourceSet.createResource(uri);
			resource.load(options);
			
			//Get content of the resource
			int i = 0;   
			  
            for (TreeIterator ti = resource.getAllContents(); ti.hasNext();) {   
                System.out.println(i);   
                EObject object = (EObject) ti.next();

    			System.out.println(object);
//                if(object.eContainingFeature() != null) {
                	//object.eContainingFeature().getName() == families, mothers, fathers, daughters, sons
                	//object.eClass().getInstanceClassName() // .getName() == Family, Member
//                	System.out.println(object.eContainer());   
              
//                }
          
                i++;   
            } 
//			EObject root = resource.getContents().get(4);
//			System.out.println(root.eContainingFeature());
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		stage.setTitle("Visualization");
		stage.show();
		
	}
	
//    public void getNodes(Element node, TreeItem<String> rootItem){
//        List<Attribute> att = node.attributes();
//
//        if(att.size() > 0){
//            for (int j = 0; j < att.size(); j++) {
//                Attribute a = att.get(j);
//                if(!a.getName().equals("version")) {
//                   TreeItem<String> item = new TreeItem<> (a.getParent().getName() + " " + a.getValue());
//                    rootItem.getChildren().add(item);
//                }
//            }
//        }
//        List<Element> nodes = node.elements();
//        for(Element e: nodes){
//            this.getNodes(e, rootItem);
//        }
//    }


}
