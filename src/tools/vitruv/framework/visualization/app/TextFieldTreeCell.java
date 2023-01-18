package tools.vitruv.framework.visualization.app;

import org.eclipse.emf.ecore.EObject;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextFieldTreeCell extends TreeCell<String>{
	
//	private TextField textField;

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
        
        		setText((String) getItem().toString());
                setGraphic(getTreeItem().getGraphic());
        }
    }
}


//public class TextFieldTreeCell extends TreeCell<String>{
//	
////	private TextField textField;
//
//    @Override
//    public void updateItem(String item, boolean empty) {
//        super.updateItem(item, empty);
//
//        if (empty) {
//            setText(null);
//            setGraphic(null);
//        } else {
//            setText((String) getItem());
//            setGraphic(getTreeItem().getGraphic());
//      
////            System.out.println( "/" + node.getBoundsInParent().getCenterY());
//        }
//    }
//}

