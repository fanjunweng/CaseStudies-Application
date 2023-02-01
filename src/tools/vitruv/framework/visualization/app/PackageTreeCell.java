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
	private String createdLabelStyle = "-fx-background-color: gold; -fx-font: 9px bold";
	
    @Override
    public void updateItem(EObject item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
        	HBox hbox = new HBox(4);
        	//If this cell is not as the root tree item and have some containing feature
        	if(getItem().eContainingFeature() != null) {
        		//A feature and the type of the feature
        		Label featureLabel = new Label(getItem().eContainingFeature().getName());
        		featureLabel.setStyle(featureLabelStyle);
        		Label typeLabel = new Label( " of type ");
        		Label classLabel = new Label(getItem().eClass().getName());
        		classLabel.setStyle(classLabelStyle);
        		hbox.getChildren().addAll(featureLabel, typeLabel, classLabel);
        		createSeparator(hbox);	
        		//Create a label of the attribute of the feature with the value
        		createAttributeLabel(hbox);
        		//Show the number of the children
        		if(!getTreeItem().isLeaf()) {
        			hbox.getChildren().add(new Label("("+ item.eContents().size()+")"));
        		}else {
        			Label createdLabel = new Label("Created");
            		createdLabel.setStyle(createdLabelStyle);
            		hbox.getChildren().add(createdLabel);
        		}
        	}else{
        		//Show the root content
        		Label classLabel= new Label(getItem().eClass().getName());
        		classLabel.setStyle(classLabelStyle);
        		hbox.getChildren().addAll(classLabel);
        	
        		if(getItem().eClass().getEAllAttributes().size() > 0) {
        			createSeparator(hbox);
        		}
        		createAttributeLabel(hbox);
        		//Show the number of the children
        		hbox.getChildren().add(new Label("("+item.eContents().size()+")"));
        	}
            setGraphic(hbox);
        }
    }
    
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
 
    private void createSeparator(HBox hbox) {
    	Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setValignment(VPos.CENTER);
		separator.setHalignment(HPos.CENTER);
		hbox.getChildren().add(separator);
    }
}
