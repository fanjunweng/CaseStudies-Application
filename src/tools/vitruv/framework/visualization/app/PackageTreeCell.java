package tools.vitruv.framework.visualization.app;

import org.eclipse.emf.ecore.EObject;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PackageTreeCell extends TreeCell<EObject>{
	
//	private TextField textField;

    @Override
    public void updateItem(EObject item, boolean empty) {
        super.updateItem(item, empty);
        
//        System.out.println("111111");
        
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
        	StringBuilder text  = new StringBuilder();
        	
        	if(getItem().eContainingFeature() !=null) {
        		text.append(getItem().eContainingFeature().getName() 
        				+ " of type " + getItem().eClass().getName() + " ");
        		
        		getItem().eClass().getEAllAttributes().forEach(e -> {
        			text.append(e.getName() +": "+ getItem().eGet(e) + "   ");
        		});
        		setText(text.toString());
        		
        	}else{
        		text.append(getItem().eClass().getName());
        		getItem().eClass().getEAllAttributes().forEach(e -> {
        			text.append("  "+ e.getName() +": "+ getItem().eGet(e));
        		});
        		setText(text.toString());
        	}
        
        
            setGraphic(getTreeItem().getGraphic());
        }
    }
}
