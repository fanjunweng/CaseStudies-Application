package tools.vitruv.framework.visualization.app;

import org.eclipse.emf.ecore.EObject;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;

/**
 * This class refers to the custom tree cell from the tree view.
 *
 */
public class PackageTreeCell extends TreeCell<EObject>{
	private String classLabelStyle = "-fx-font: normal bold 14px 'serif'";
	private String featureLabelStyle = "-fx-font: normal bold 15px 'serif'; -fx-text-fill: dimgray"; 
	private String attributeLabelStyle = "-fx-font: normal bold 13px; -fx-text-fill: midnightblue";
	private String valueLabelStyle = "-fx-font: normal bold 14px 'serif'";
	private String statusLabelStyle = "-fx-background-color: gold; -fx-font: 9px bold";

	
    @Override
    public void updateItem(EObject item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
        	//A horizonal container for each tree cell
        	HBox hbox = new HBox(4);
        	//If this cell is not as the root tree item and have some containing feature
        	if(getItem().eContainingFeature() != null) {
        		createFeatureLabel(hbox);
        		hbox.getChildren().addAll(new Label( " of type "));
        	}
        	createClassLabel(hbox);
    		if(getItem().eClass().getEAllAttributes().size()>0) {
    			createSeparator(hbox);
    		}	
    		createAttributeLabel(hbox);
    		createStatusLabel(hbox, "Created");
    		createStatusLabel(hbox, "Deleted");
    		createStatusLabel(hbox, "Updated");
    		if(!getTreeItem().isLeaf()) {
    			createChildNumberLabel(hbox);
    		}
            setGraphic(hbox);
        }
    }
    
    /**
     * This method adds a class name label to the layout of the tree cell.
     * @param hbox A layout component which positions all its child nodes (components) in a horizontal row.
     */
    private void createClassLabel(HBox hbox) {
    	Label classLabel= new Label(getItem().eClass().getName());
		classLabel.setStyle(classLabelStyle);
		hbox.getChildren().add(classLabel);
    }
    
    /**
     * This method adds an attribute name label and an attribute value label to the layout of the tree cell.
     * @param hbox A layout component which positions all its child nodes (components) in a horizontal row.
     */
    private void createAttributeLabel(HBox hbox) {
    	//An attribute of the feature and the attribute value
		getItem().eClass().getEAllAttributes().forEach(attribute -> {
			Label attriLabel = new Label(attribute.getName() +":");
			attriLabel.setStyle(attributeLabelStyle);
			hbox.getChildren().add(attriLabel);
			if(getItem().eGet(attribute) != null) {
				Label valueLabel = new Label(getItem().eGet(attribute).toString());
				valueLabel.setStyle(valueLabelStyle);
				hbox.getChildren().addAll(valueLabel);
			}
			createSeparator(hbox);
		});
    }
 
    /**
     * This method adds a containing feature name label to the layout of the tree cell.
     * @param hbox A layout component which positions all its child nodes (components) in a horizontal row.
     */
    private void createFeatureLabel(HBox hbox) {
    	Label featureLabel = new Label(getItem().eContainingFeature().getName());
		featureLabel.setStyle(featureLabelStyle);
		hbox.getChildren().add(featureLabel);	
    }

    /**
     * This method adds a status label to the layout of the tree cell.
     * @param hbox A layout component which positions all its child nodes (components) in a horizontal row.
     * @param status A string about the status of the EObject 
     */
    private void createStatusLabel(HBox hbox, String status) {
    	Label statusLabel = new Label(status);
    	statusLabel.setStyle(statusLabelStyle);
		hbox.getChildren().add(statusLabel);	
    }
    
    /**
     * This method adds a child number of labels to the layout of a tree cell.
     * @param hbox A layout component which positions all its child nodes (components) in a horizontal row.
     */
    private void createChildNumberLabel(HBox hbox) {
    	hbox.getChildren().add(new Label("("+ getItem().eContents().size()+")"));	
    }

    /**
     * This method adds a vertical separator line to the layout of a tree cell.
     * @param hbox A layout component which positions all its child nodes (components) in a horizontal row.
     */
    private void createSeparator(HBox hbox) {
    	Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setValignment(VPos.CENTER);
		separator.setHalignment(HPos.CENTER);
		hbox.getChildren().add(separator);
    }
}
