package tools.vitruv.framework.visualization.app;

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage;
import edu.kit.ipd.sdq.metamodels.insurance.InsurancePackage;
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tools.vitruv.framework.visualization.api.VSUMVisualizationAPI;

public class CaseStudiesApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Instantiate an API of the internal virtual model visualization for the tree packages FamiliesPackage, PersonsPackage and InsurancePackage
		VSUMVisualizationAPI<FamiliesPackage, PersonsPackage, InsurancePackage> vsumVisualizationAPI = VSUMVisualizationAPI.getVSUMFamiliesPersonsInsurancesAPI();
		Model model1 = new Model(vsumVisualizationAPI.getT1(), vsumVisualizationAPI.getView(vsumVisualizationAPI.getT1()));
		Model model2 = new Model(vsumVisualizationAPI.getT2(), vsumVisualizationAPI.getView(vsumVisualizationAPI.getT2()));
		Model model3 = new Model(vsumVisualizationAPI.getT3(), vsumVisualizationAPI.getView(vsumVisualizationAPI.getT3()));
		
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view.fxml"));
		loader.setController(new MainController<FamiliesPackage, PersonsPackage, InsurancePackage>(vsumVisualizationAPI, model1, model2, model3));
		Parent root = loader.load();
        Scene scene = new Scene(root, 1800, 750);
        scene.getStylesheets().add(getClass().getResource("Styles.css") .toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Case Studies Application");
        primaryStage.setResizable(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();
	}
}
