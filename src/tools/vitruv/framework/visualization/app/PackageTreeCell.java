package tools.vitruv.framework.visualization.app;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;

/**
 * This class implements the tree cell of the tree view and customizes the UI controls in the tree cell.
 *
 */
public class PackageTreeCell extends TreeCell<EObject>{
	private int spacing = 4;

    @Override
    public void updateItem(EObject item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
        	//A horizonal container to place all UI controls
        	HBox hbox = new HBox(spacing);
      
        	if(getItem().eContainingFeature()!=null) {
        		hbox.getChildren().addAll(createFeatureLabel(), new Label( " of type "));
        	}
        	hbox.getChildren().addAll(createClassLabel());
   
    		if(getItem().eClass().getEAllAttributes().isEmpty()) {
    			hbox.getChildren().add(createSeparator());
    		}	
    		
    		getItem().eClass().getEAllAttributes().forEach(attribute -> {
    			hbox.getChildren().add(createAttributeLabel(attribute));
    			
    			if(getItem().eGet(attribute)!= null) {
    				hbox.getChildren().addAll(createAttributeValueLabel(attribute));
    			}
    			hbox.getChildren().add(createSeparator());
    		});
    		
    		hbox.getChildren().addAll(createStatusLabel(ChangeType.CREATED.toString())
    				,createStatusLabel(ChangeType.DELETED.toString())
    	    		,createStatusLabel(ChangeType.UPDATED.toString()));
    		
    		if(!getTreeItem().isLeaf()) {
    			hbox.getChildren().add(createChildNumberLabel());
    		}
            setGraphic(hbox);
        }
    }
    
    private Node createClassLabel() {
    	Node classLabel= new Label(getItem().eClass().getName());
    	classLabel.getStyleClass().add("classStyle");
		return classLabel;
    }
    
    private Node createAttributeLabel(EAttribute attribute) {
    	Node attributeLabel = new Label(attribute.getName() +":");
		attributeLabel.getStyleClass().add("attributeStyle");
		return attributeLabel;
    }
    
    private Node createAttributeValueLabel(EAttribute attribute) {
		Node valueLabel = new Label(getItem().eGet(attribute).toString());
		valueLabel.getStyleClass().add("valueStyle");
		return valueLabel;
    }
    
    private Node createFeatureLabel() {
    	Node featureLabel = new Label(getItem().eContainingFeature().getName());	
    	featureLabel.getStyleClass().add("featureStyle");
		return featureLabel;
    }
 
    private Node createStatusLabel(String status) {
    	Node statusLabel = new Label(status);
    	statusLabel.getStyleClass().add("statusStyle");
		return statusLabel;
    }
    
    private Node createChildNumberLabel() {
    	return new Label("("+ getItem().eContents().size()+")");	
    }

    private Node createSeparator() {
    	Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setValignment(VPos.CENTER);
		separator.setHalignment(HPos.CENTER);
		return separator;
    }
}
