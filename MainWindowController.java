package c482.Controller;

import c482.Model.Inventory;
import c482.Model.Part;
import c482.Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/** Controller class which will provide control logic for the Main Window of the App. The location which is used to fix null (if the location is not known) or paths that are relative. Resources - Resources are used to null if root object is not localized, or to localize the root object.**/
public class MainWindowController implements Initializable {

    // Parts Table
    @FXML private TableView<Part> partsTableView;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInvLevelColumn;
    @FXML private TableColumn<Part, Double> partCostColumn;
    @FXML private TextField partSearchArea;

    //Products Table
    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInvLevelColumn;
    @FXML private TableColumn<Product, Double> productCostColumn;
    @FXML private TextField productSearchArea;

    /** Buttons and Fields **/
    private Parent scene;

    /** Action Event for when the Add Part Button is Pushed. **/
    public void addpartbuttonpushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/PartAdd.fxml"));

        stage.setTitle("Add c482.Model.Part");
        stage.setScene(new Scene(scene));
        stage.show();

//        Parent root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));


    //I TRIED THIS BUT IT DIDN'T WORK AS WELL.
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/view/AddPart.fxml"));
//        loader.load();
//        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//        Parent scene = loader.getRoot();
//        stage.setScene(new Scene(scene));
//        stage.show();


    }

    /** This gets the part object selected in the part table by the end user. Returns - This will Return null, when there is no part object that is selected by the user.**/
    public void modifypartbuttonpushed(ActionEvent event){
        try {
            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {
                return;
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Mod2.fxml"));
            scene = loader.load();
            ModifyPartController controller = loader.getController();
            controller.setParts(selectedPart);
            stage.setTitle("Modify c482.Model.Part");
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
//
        }
    }

    /** This gets the product object selected in the part table by the end user. Returns - This will Return null, when there is no product object that is selected by the user.**/
    public void modifyproductbuttonpushed(ActionEvent event){
        try {
            Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                return;
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
            scene = loader.load();
            ModifyProductController controller = loader.getController();
            controller.setProduct(selectedProduct);
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (IOException e) {
//            Logger logger = Logger.getLogger(getClass().getName());
//            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    /** Searches Part Button. **/
    public void searchPartButton(ActionEvent event) {
        String term = partSearchArea.getText();
        ObservableList foundParts = Inventory.lookupPart(term);
        if(foundParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Match");
            alert.setHeaderText("Unable to locate a part name with: " + term);
            alert.showAndWait();
        } else {
            partsTableView.setItems(foundParts);
        }
    }

    /** Searches Product Button. **/
    public void searchProductButton(ActionEvent event) {
        String term = productSearchArea.getText();
        ObservableList foundProducts = Inventory.lookupProduct(term);
        if(foundProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Match");
            alert.setHeaderText("Unable to locate a product name with: " + term);
            alert.showAndWait();
        } else {
            productsTableView.setItems(foundProducts);
        }
    }
    /** Action Event for Delete Part Button is Pushed. **/
    public void deletePartButton(ActionEvent event) {
        if (partsTableView.getSelectionModel().isEmpty()){
            infoDialog("Warning!", "No c482.Model.Part Selected","Please choose part from the list above");
            return;
        }
        if (confirmDialog("Warning!", "Do you like to delete this part?")){
            int selectedPart = partsTableView.getSelectionModel().getSelectedIndex();
            partsTableView.getItems().remove(selectedPart);
        }
    }

    /** Action Event for Delete Product Button is Pushed. **/
    public void deleteProductButton(ActionEvent actionEvent) {
        Product productToDelete = productsTableView.getSelectionModel().getSelectedItem();

        if(!Inventory.deleteProduct(productToDelete)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to delete");
            alert.setContentText("Can not delete product with associated parts, please remove and try again.");
            alert.showAndWait();
        }
    }



    /** Alert that Confirms the dialog. **/
    public static boolean confirmDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        else {
            return false;
        }
    }

    /** Info for the dialog. **/
    public static void infoDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /** Action Event for when the Add Product Button is Pushed. **/
    @FXML public void addProductButtonPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setTitle("Add Product");
        stage.setScene(new Scene(scene));
        stage.show();
    }



    /** Action Event for our Exit Button. **/
    @FXML public void exitButton(ActionEvent event) throws IOException{
        confirmDialog("Exit", "Are you sure you would like to exit the program?");
        {
            System.exit(0);
        }
    }

    /** This Initializes the Controller.**/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partsTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
//
        productsTableView.setItems((Inventory.getAllProducts()));
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

}
