package c482.Controller;

import c482.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static c482.Model.Inventory.getAllParts;

/** Controller class which provides control logic for the Add Part Window of the app. location - The location which is used to fix null (if the location is not known) or paths that are relative. Resources - Resources are used to null if root object is not localized, or to localize the root object.**/
public class AddPartController implements Initializable {

    /** TextField for Maximum. **/
    @FXML public TextField maxTxt;

    /** TextField for Price. **/
    @FXML public TextField priceTxt;

    /** TextField for Name. **/
    @FXML public TextField nameTxt;

    /** TextField for Minimum. **/
    @FXML public TextField minTxt;

    /** TextField for stock. **/
    @FXML public TextField stockTxt;
    @FXML private RadioButton inHouseRB;
    @FXML private RadioButton outSourcedRB;

    @FXML private Label inhouseoroutsourced;

    private Stage stage;
    private Parent scene;
    //Add Fields

    @FXML private TextField companyORmachineID;
    /** Action Event for when Radio Button is pressed by user. **/
    public void radio(ActionEvent actionEvent) {
        if (outSourcedRB.isSelected()) {
            this.companyORmachineID.setPromptText("Company Name");
            inhouseoroutsourced.setText("Company Name");
        }
        if (inHouseRB.isSelected()) {
            this.companyORmachineID.setPromptText("Machine ID");
        inhouseoroutsourced.setText("Machine ID"); }

    }



//    if(InHouse)
    /** Cancels/Restores to its original value**/
    @FXML public void onActionCancel(ActionEvent event) throws IOException {
        // if(MainWindowController.confirmDialog("Cancel?", "Are you sure?")) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
            stage.setTitle("Add Product");
            stage.setScene(new Scene(scene));
            stage.show();

        }
    // }

    /** Save's part. **/
    @FXML public void onActionSave(ActionEvent event) {
        try {

            int id = getNewID();
            String name = nameTxt.getText();
            double price = Double.parseDouble(priceTxt.getText());
            int stock = Integer.parseInt(stockTxt.getText());
            int min = Integer.parseInt(minTxt.getText());
            int max = Integer.parseInt(maxTxt.getText());
            if (min > max) {
                MainWindowController.infoDialog("Input Error", "There's an Error in minimum and maximum field", "Check Min and Max value." );

            }
            else if(stock > max || stock < min) {
                MainWindowController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum" );

            }

            else {
                if(inHouseRB.isSelected()) {
                    int machineID = Integer.parseInt(companyORmachineID.getText());
                    InHouse part = new InHouse( id,  name,  price,  stock,  min,  max,  machineID);
                    Inventory.addPart(part);
                }
                if(outSourcedRB.isSelected()) {
                    String companyName = companyORmachineID.getText();
                    OutSourced part = new OutSourced( id,  name,  price,  stock,  min,  max,  companyName);
                    Inventory.addPart(part);
                }
                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
                stage.setTitle("Inventory Management System");
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }

//            if(MainWindowController.confirmDialog("Save?", "Do you want to save this part?"))
//                if(partMax < partMin) {
//                    MainWindowController.infoDialog("Input Error", "There's an Error in minimum and maximum field", "Check Min and Max value." );
//                }
//                else if(partInventory < partMin || partInventory> partMax) {//                    MainWindowController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum" );
//                }
//                else{
//                    int newID = getNewID();
//                    String name = Name.getText();
//                    if(name.isBlank()) {
//                        //COME BACK HERE TO ADD MORE//
//                    }
//                    int stock = partInventory;
//                    double price = Double.parseDouble(Price.getText());
//                    int min = partMin;
//                    int max = partMax;
//                    if (OutSourced.isSelected()) {
//                        String companyName = companyORmachineID.getText();
//                        OutSourced temp = new OutSourced(newID, name, price, stock, min, max, companyName);
//                        c482.Model.Inventory.addPart(temp);
//                    } else {
//                        int machineID = Integer.parseInt(companyORmachineID.getText());
//                        InHouse temp = new InHouse(newID, name, price, stock, min, max, machineID);
//                        c482.Model.Inventory.addPart(temp);
//                    }


        }
        catch (Exception e){
            MainWindowController.infoDialog("Input Error", "Error in adding part", "Invalid input in min, max, price, inventory, or MachineID fields." );
        }
    }
    /** Getter for ID. **/
    public static int getNewID(){
        int newID = 1;
        for (int i = 0; i < getAllParts().size(); i++) {
            newID++;
        }
        return newID;
    }

    /** Initializes the controller.**/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}








//    --------
//
//
//    //Other Buttons/Fields
//    @FXML private TextField SearchField;
//    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
//    private ObservableList<Part> originalParts = FXCollections.observableArrayList();
//
//    @FXML public void searchPartButton(ActionEvent event) {
//        String term = SearchField.getText();
//        ObservableList foundParts = Inventory.lookupPart(term);
//        if(foundParts.isEmpty()) {
//            MainWindowController.confirmDialog("No Match", "Unable to locate a c482.Model.Part name with: " + term);
//        }
//
//    }
//
//    @FXML void addPartToProduct(ActionEvent event) {
//        Part selectedPart = PartsTableView.getSelectionModel().getSelectedItem();
//
//        if(selectedPart != null) {
//            associatedParts.add(selectedPart);
//            updatePartTable();
//            updateAssociatedPartTable();
//        }
//
//        else {
//            MainWindowController.infoDialog("Select a part","Select a part", "Select a part to add to the Product");
//        }
//    }
//
//    @FXML
//    void deletePartFromProduct(ActionEvent event) {
//        Part selectedPart = AssociatedPartsTableView.getSelectionModel().getSelectedItem();
//
//        if(selectedPart != null) {
//            MainWindowController.confirmDialog("Deleting Parts","Are you sure you want to delete " + selectedPart.getName() + " from the Product?");
//            associatedParts.remove(selectedPart);
//            updatePartTable();
//            updateAssociatedPartTable();
//        }
//
//        else {
//            MainWindowController.infoDialog("No Selection","No Selection", "Please choose something to remove");
//        }
//    }
//
//    @FXML public void cancelAddProduct(ActionEvent event) throws IOException {
//        if (MainWindowController.confirmDialog("Cancel?", "Are you sure?")) {
//            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//            scene = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
//            stage.setTitle("Inventory Management System");
//            stage.setScene(new Scene((Parent) scene));
//            stage.show();
//        }
//    }
//
//    @FXML void saveProduct(ActionEvent event) throws IOException {
//
//        //Test if valid product
//        if (associatedParts.isEmpty()){
//            MainWindowController.infoDialog("Input Error", "Please add one ore more parts", "A product must have one or more parts associated with it.");
//            return;}
//
//        if (APRName.getText().isEmpty() || APRMinimum.getText().isEmpty() || APRMaximum.getText().isEmpty() || APRMaximum.getText().isEmpty() || APRPrice.getText().isEmpty()) {
//            MainWindowController.infoDialog("Input Error", "Cannot have blank fields", "Check all the fields.");
//            return;
//        }
//
//        Integer inv = Integer.parseInt(this.APRInventory.getText());
//        Integer min = Integer.parseInt(this.APRMinimum.getText());
//        Integer max = Integer.parseInt(this.APRMaximum.getText());
//
//        if (max < min) {
//            MainWindowController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
//            return;
//        }
//
//        if (inv < min || inv > max) {
//            MainWindowController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
//            return;
//        }
//
//        //Add Product
//        if (MainWindowController.confirmDialog("Save?", "Would you like to save this part?")) {
//            Product product = new Product(0, null, 0.0, 0, 0, 0);
//            product.setId(getNewID());
//            product.setName(this.APRName.getText());
//            product.setStock(Integer.parseInt(this.APRInventory.getText()));
//            product.setMin(Integer.parseInt(this.APRMinimum.getText()));
//            product.setMax(Integer.parseInt(this.APRMaximum.getText()));
//            product.setPrice(Double.parseDouble(this.APRPrice.getText()));
//            product.addAssociatedPart(associatedParts);
//            Inventory.addProduct(product);
//
//            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//            scene = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
//            stage.setTitle("Inventory Management System");
//            stage.setScene(new Scene((Parent) scene));
//            stage.show();
//        }
//
//    }
//
//
//    private int getNewID(){
//        int newID = 1;
//        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
//            if (Inventory.getAllProducts().get(i).getId() == newID) {
//                newID++;
//            }
//        }
//        return newID;
//    }
//
//    public void updatePartTable() {
//        PartsTableView.setItems(Inventory.getAllParts());
//    }
//
//    private void updateAssociatedPartTable() {
//        AssociatedPartsTableView.setItems(associatedParts);
//    }
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        PartsTableView.setItems(Inventory.getAllParts());
//        PartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//
////        originalParts = Inventory.getAllParts();
////
////        //Columns and Table for un-associated parts.
////        PartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
////        PartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
////        PartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
//
////
////        //Columns and Table for associated parts
////        AssociatedPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
////        AssociatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
////        AssociatedPartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
////        AssociatedPartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
////        AssociatedPartsTableView.setItems(associatedParts);
////
////        updatePartTable();
////        updateAssociatedPartTable();
//    }
//
//    public void radioadd(ActionEvent actionEvent) {
//    }
//
//    public void onActionSave(ActionEvent actionEvent) {
//    }
//
//    public void onActionCancel(ActionEvent actionEvent) {
//    }
//}
