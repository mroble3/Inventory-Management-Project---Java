package c482.Controller;

import c482.Model.*;
import c482.Controller.MainWindowController;
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

/** Controller class which provides control logic for the Add Product Window of the app. The location which is used to fix null (if the location is not known) or paths that are relative. Resources - Resources are used to null if root object is not localized, or to localize the root object.**/
public class AddProductController implements Initializable {

    @FXML private RadioButton outsourced;
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

    //Add Fields
    @FXML private TextField Name;
    @FXML private TextField inventoryTxt;
    @FXML private TextField Price;
    @FXML private TextField Maximum;
    @FXML private TextField Minimum;

    //Other Buttons/Fields
    @FXML private TextField SearchField;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> originalParts = FXCollections.observableArrayList();

    /**
     * This searches the Part table.
     *
     * @param event
     */
    @FXML public void searchPartButton(ActionEvent event) {
        String term = SearchField.getText();
        ObservableList foundParts = Inventory.lookupPart(term);
        if(foundParts.isEmpty()) {
            MainWindowController.confirmDialog("No Match", "Unable to locate a c482.Model.Part name with: " + term);
        } else {
            PartsTableView.setItems(foundParts);
        }
    }

    @FXML void addPartToProduct(ActionEvent event) {
        Part selectedPart = PartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            associatedParts.add(selectedPart);
            updatePartTable();
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
            updatePartTable();
            updateAssociatedPartTable();
        }

        else {
            MainWindowController.infoDialog("No Selection","No Selection", "Please choose something to remove");
        }
    }

    /** This cancels Add Product. **/
    @FXML public void cancelAddProduct(ActionEvent event) throws IOException {
       // if (MainWindowController.confirmDialog("Cancel?", "Are you sure?")) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
            stage.setTitle("Add Product");
            stage.setScene(new Scene(scene));
            stage.show();
       // }
    }

    @FXML void saveProduct(ActionEvent event) throws IOException {

        //Test if valid product
        if (associatedParts.isEmpty()){
            MainWindowController.infoDialog("Input Error", "Please add one ore more parts", "A product must have one or more parts associated with it.");
            return;}

        if (Name.getText().isEmpty() || Minimum.getText().isEmpty() || Maximum.getText().isEmpty() || Maximum.getText().isEmpty() || Price.getText().isEmpty()) {
            MainWindowController.infoDialog("Input Error", "Cannot have blank fields", "Check all the fields.");
            return;
        }

        Integer inv = Integer.parseInt(this.inventoryTxt.getText());
        Integer min = Integer.parseInt(this.Minimum.getText());
        Integer max = Integer.parseInt(this.Maximum.getText());

        if (max < min) {
            MainWindowController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            return;
        }

        if (inv < min || inv > max) {
            MainWindowController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            return;
        }

        //Add Product
        if (MainWindowController.confirmDialog("Save?", "Would you like to save this part?")) {
            Product product = new Product(0, null, 0.0, 0, 0, 0);
            product.setId(getNewID());
            product.setName(this.Name.getText());
            product.setStock(Integer.parseInt(this.inventoryTxt.getText()));
            product.setMin(Integer.parseInt(this.Minimum.getText()));
            product.setMax(Integer.parseInt(this.Maximum.getText()));
            product.setPrice(Double.parseDouble(this.Price.getText()));
            product.addAssociatedPart(associatedParts);
            Inventory.addProduct(product);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }

    }


    private int getNewID(){
        int newID = 1;
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            if (Inventory.getAllProducts().get(i).getId() == newID) {
                newID++;
            }
        }
        return newID;
    }

    /** This updates the part table. **/
    public void updatePartTable() {
        PartsTableView.setItems(Inventory.getAllParts());
    }

    private void updateAssociatedPartTable() {
        AssociatedPartsTableView.setItems(associatedParts);
    }

    /** Initializes the Controller.  **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        originalParts = Inventory.getAllParts();

        /** Columns and Table for un-associated parts. **/
        PartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartsTableView.setItems(originalParts);

        /** Columns and Table for associated parts. **/
        AssociatedPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssociatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        AssociatedPartsTableView.setItems(associatedParts);

        updatePartTable();
        updateAssociatedPartTable();
    }
}
