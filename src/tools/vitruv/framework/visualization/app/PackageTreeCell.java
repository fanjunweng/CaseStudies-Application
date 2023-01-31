package tools.vitruv.framework.visualization.app;

import org.eclipse.emf.ecore.EObject;

import javafx.scene.control.TreeCell;

/**
 * This class refers to the custom tree cell from the tree view.
 *
 */
public class PackageTreeCell extends TreeCell<EObject>{

    @Override
    public void updateItem(EObject item, boolean empty) {
        super.updateItem(item, empty);
        
        
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
        	StringBuilder text  = new StringBuilder();
        	
        	//If this cell is not as the root tree item and have some containing feature
        	if(getItem().eContainingFeature() != null) {
        		//A feature and the type of the feature
        		text.append(getItem().eContainingFeature().getName() 
        				+ " of type " + getItem().eClass().getName() + " ");
        		
        		//An attribute of the feature and the attribute value
        		getItem().eClass().getEAllAttributes().forEach(attribute -> {
        			if(getItem().eGet(attribute) != null) {
        				text.append(attribute.getName() +": "+ getItem().eGet(attribute) + "   ");
        			}else {
        				text.append(attribute.getName() +": "+ "   ");
        			}
        		});
        		
        		//Show the number of the children
        		if(!getTreeItem().isLeaf()) {
        			text.append("     ("+ item.eContents().size()+")");
        		}
        		setText(text.toString());
        		
        	}else{
        		text.append(getItem().eClass().getName());
        		getItem().eClass().getEAllAttributes().forEach(e -> {
        			text.append("  "+ e.getName() +": "+ getItem().eGet(e));
        		});
        		text.append("     ("+ item.eContents().size()+")");
        		setText(text.toString());
        	}
        
            setGraphic(getTreeItem().getGraphic());
         
        }

    }
}
