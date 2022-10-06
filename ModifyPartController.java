package c482.Controller;

import c482.Model.InHouse;
import c482.Model.Inventory;
import c482.Model.OutSourced;
import c482.Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Controller class which will provide control logic for the Modify Part Window of the App. The location which is used to fix null (if the location is not known) or paths that are relative. Resources - Resources are used to null if root object is not localized, or to localize the root object.**/
public class ModifyPartController implements Initializable {

    @FXML private RadioButton outsourced;
    @FXML private RadioButton inHouse;
    @FXML private Label InHouseorOutSourced;
    @FXML private TextField ID;
    @FXML private TextField Name;
    @FXML private TextField inventoryTxt;
    @FXML private TextField Price;
    @FXML private TextField Maximum;
    @FXML private TextField Minimum;
    @FXML private TextField companyORmachineID;

    private Stage stage;
    private Parent scene;
    public Part selectedPart;
    private int partIndex;

    /** The radio button. **/
    public void radio()
    {
        if (outsourced.isSelected())
            this.InHouseorOutSourced.setText("Company Name");
        else
            this.InHouseorOutSourced.setText("Machine ID");
    }

    /** Sets the parts. **/
    public void setParts(Part selectedPart) {
        this.selectedPart = selectedPart;
        partIndex = Inventory.getAllParts().indexOf(selectedPart);
        ID.setText(Integer.toString(selectedPart.getId()));
        Name.setText(selectedPart.getName());
        inventoryTxt.setText(Integer.toString(selectedPart.getStock()));
        Price.setText(Double.toString(selectedPart.getPrice()));
        Maximum.setText(Integer.toString(selectedPart.getMax()));
        Minimum.setText(Integer.toString(selectedPart.getMin()));
        if(selectedPart instanceof InHouse){
            InHouse ih = (InHouse) selectedPart;
            inHouse.setSelected(true);
            this.InHouseorOutSourced.setText("Machine ID");
            companyORmachineID.setText(Integer.toString(ih.getMachineID()));
        }
        else{
            OutSourced os = (OutSourced) selectedPart;
            outsourced.setSelected(true);
            this.InHouseorOutSourced.setText("Company Name");
            companyORmachineID.setText(os.getCompanyName());
        }
    }

    @FXML void onActionSave(ActionEvent event) throws IOException {
        int partInventory = Integer.parseInt(inventoryTxt.getText());
        int partMin = Integer.parseInt(Minimum.getText());
        int partMax = Integer.parseInt(Maximum.getText());
        if (MainWindowController.confirmDialog("Save?", "Would you like to save this part?"))
            if (partMax < partMin) {
                MainWindowController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            } else if (partInventory < partMin || partInventory > partMax) {
                MainWindowController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            } else {
                int id = Integer.parseInt(ID.getText());
                String name = Name.getText();
                double price = Double.parseDouble(Price.getText());
                int stock = Integer.parseInt(inventoryTxt.getText());
                int min = Integer.parseInt(Minimum.getText());
                int max = Integer.parseInt(Maximum.getText());
                if (inHouse.isSelected()) {
                    try {
                        int machineID = Integer.parseInt(companyORmachineID.getText());
                        InHouse temp = new InHouse(id, name, price, stock, min, max, machineID);
                        Inventory.updatePart(partIndex, temp);
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
                        stage.setTitle("Inventory Management System");
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                    }
                    catch (NumberFormatException e){
                        MainWindowController.infoDialog("Input Error", "Check Machine ID ", "Machine ID can only contain numbers 0-9");
                    }
                }
                else {
                    String companyName = companyORmachineID.getText();
                    OutSourced temp = new OutSourced(id, name, price, stock, min, max, companyName);
                    c482.Model.Inventory.updatePart(partIndex, temp);
                    stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
                    stage.setTitle("Inventory Management System");
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }
            }
    }
    /** Action Event that Cancels/Restores to its original value. **/
    @FXML public void onActionCancel(ActionEvent event) throws IOException {
//        if(MainWindowController.confirmDialog("Cancel?", "Are you sure?")) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
            stage.setTitle("Add Product");
            stage.setScene(new Scene(scene));
            stage.show();
       // }

    }

    /** This Initializes the Controller along with generating the text fields with part being selected in MainWindowController.**/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
