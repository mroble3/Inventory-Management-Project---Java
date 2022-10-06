package c482.Controller;

import c482.Model.*;
import javafx.collections.FXCollections;
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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Controller class which will provide control logic for the Modify Product Window of the App. The location which is used to fix null (if the location is not known) or paths that are relative. Resources - Resources are used to null if root object is not localized, or to localize the root object.**/
public class ModifyProductController implements Initializable {

    private Stage stage;
    private Parent scene;

    //Un Associated Parts Table
    @FXML private TableView<Part> PartsTableView;
    @FXML private TableColumn<Part, Integer> PartsIDColumn;
    @FXML private TableColumn<Part, String> PartsNameColumn;
    @FXML private TableColumn<Part, Integer> PartsInventoryColumn;
    @FXML private TableColumn<Part, Double> PartsCostColumn;

    //Associated Parts Table
    @FXML private TableView<Part> AssociatedPartsTableView;
    @FXML private TableColumn<Product, Integer> AssociatedPartsIDColumn;
    @FXML private TableColumn<Product, String> AssociatedPartsNameColumn;
    @FXML private TableColumn<Product, Integer> AssociatedPartsInventoryColumn;
    @FXML private TableColumn<Product, Double> AssociatedPartsCostColumn;

    //Modify Fields
    @FXML private TextField Name;
    @FXML private TextField inventory;
    @FXML private TextField Price;
    @FXML private TextField Maximum;
    @FXML private TextField Minimum;
    @FXML private TextField ID;

    //Other Buttons/Fields
    @FXML private TextField SearchField;
    private Product selectedProduct;
    private Product modProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID;

    /** Sets the Product. **/
    public void setProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        productID = Inventory.getAllProducts().indexOf(selectedProduct);
        ID.setText(Integer.toString(selectedProduct.getId()));
        Name.setText(selectedProduct.getName());
        inventory.setText(Integer.toString(selectedProduct.getStock()));
        Price.setText(Double.toString(selectedProduct.getPrice()));
        Maximum.setText(Integer.toString(selectedProduct.getMax()));
        Minimum.setText(Integer.toString(selectedProduct.getMin()));
        associatedParts.addAll(selectedProduct.getAllAssociatedParts());
    }

    /** Searches Part Button. **/
    @FXML public void searchPartButton(ActionEvent event) {
        String term = SearchField.getText();
        ObservableList foundParts = Inventory.lookupPart(term);
        if(foundParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("NO MATCH");
            alert.setHeaderText("Unable to locate a c482.Model.Part name with: " + term);
            alert.showAndWait();
        } else {
            PartsTableView.setItems(foundParts);
        }
    }

    /** Modifies/Alters our Cancels/Restore. **/
    @FXML public void modifyProductCancel(ActionEvent event) throws IOException {
//        if (c482.Controller.MainWindowController.confirmDialog("Cancel?", "Are you sure?")) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
        stage.setTitle("Add Product");
        stage.setScene(new Scene(scene));
        stage.show();
//        }
    }

    @FXML void modifyProductSave(ActionEvent event) throws IOException {
        int productInventory = Integer.parseInt(inventory.getText());
        int productMinimum = Integer.parseInt(Minimum.getText());
        int productMaximum = Integer.parseInt(Maximum.getText());
        if(MainWindowController.confirmDialog("Save?", "Would you like to save this part?"))
            if(productMaximum < productMinimum) {
                MainWindowController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value." );
            }
            else if(productInventory < productMinimum || productInventory > productMaximum) {
                MainWindowController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum" );
            }
            else {
                this.modProduct = selectedProduct;
                selectedProduct.setId(Integer.parseInt(ID.getText()));
                selectedProduct.setName(Name.getText());
                selectedProduct.setStock(Integer.parseInt(inventory.getText()));
                selectedProduct.setPrice(Double.parseDouble(Price.getText()));
                selectedProduct.setMax(Integer.parseInt(Maximum.getText()));
                selectedProduct.setMin(Integer.parseInt(Minimum.getText()));
                selectedProduct.getAllAssociatedParts().clear();
                selectedProduct.addAssociatedPart(associatedParts);
                Inventory.updateProduct(productID, selectedProduct);

                //Back to Main Screen
                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
                stage.setTitle("Inventory Management System");
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
    }

    @FXML void addPartToProduct(ActionEvent event) {
        Part selectedPart = PartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            associatedParts.add(selectedPart);
            updateAssociatedPartTable();
        }

        else {
            MainWindowController.infoDialog("Select a part","Select a part", "Select a part to add to the Product");
        }
    }

    @FXML
    void deletePartFromProduct(ActionEvent event) {
        Part selectedPart = AssociatedPartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            MainWindowController.confirmDialog("Deleting Parts","Are you sure you want to delete " + selectedPart.getName() + " from the Product?");
            associatedParts.remove(selectedPart);
            updateAssociatedPartTable();
        }

        else {
            MainWindowController.infoDialog("No Selection","No Selection", "Please choose something to remove");
        }
    }

    /** Updates Part Table. **/
    public void updatePartTable() {
        PartsTableView.setItems(Inventory.getAllParts());
    }

    private void updateAssociatedPartTable() {
        AssociatedPartsTableView.setItems(associatedParts);
    }

    /** This Initializes the Controller along with generating the text fields with product being selected in MainWindowController.**/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Columns and Table for un-associated parts.
        PartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartsTableView.setItems(Inventory.getAllParts());

        //Columns and Table for associated parts
        AssociatedPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssociatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        AssociatedPartsTableView.setItems(associatedParts);

        updatePartTable();
        updateAssociatedPartTable();
    }
}
