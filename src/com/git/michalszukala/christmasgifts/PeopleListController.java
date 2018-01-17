package com.git.michalszukala.christmasgifts;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PeopleListController implements Initializable {

    private ObservableList<People> listOfPeople;
    private People people;


    @FXML private TableView<People> tableOfPeople;
    @FXML private TableColumn<People, String> tableColumnName;
    @FXML private TableColumn<People, String> tableColumnPhone;
    @FXML private TableColumn<People, String> tableColumnEmail;
    @FXML private TextField textFieldName;
    @FXML private TextField textFieldPhone;
    @FXML private TextField textFieldEmail;
    @FXML private TextField smtpHost;
    @FXML private TextField ownEmail;
    @FXML private PasswordField emailPassword;
    @FXML private Label loginStatus;
    @FXML private Button buttonSendEmail;


    // event handler for Add button, it is adding a new person to table
    @FXML
    public void addButton(ActionEvent event) {

        if (dataValidation()) {
            People addNewPerson = new People();

            addNewPerson.setName(textFieldName.getText());
            addNewPerson.setPhone(textFieldPhone.getText());
            addNewPerson.setEmail(textFieldEmail.getText());

            listOfPeople.add(addNewPerson);
            people.setPerson(addNewPerson);

            textFieldName.clear();
            textFieldPhone.clear();
            textFieldEmail.clear();
        }
    }

    // event handler for Remove button, it is deleting a person from the table
    @FXML
    public void removeButton(ActionEvent event){
        ObservableList<People> selectedRow = tableOfPeople.getSelectionModel().getSelectedItems();
        People person = tableOfPeople.getSelectionModel().getSelectedItem();
        listOfPeople.removeAll(selectedRow);
        people.removeFromPeopleList(person);
        if(listOfPeople.size() == 0)
            buttonSendEmail.setDisable(true);
    }

    // event handler for Draw button, it is calling method responsible for drawing gifts from the People class
    @FXML
    public void drawButton(ActionEvent event) {

        if(listOfPeople.size() != 0) {
            buttonSendEmail.setDisable(false);
            loginStatus.setText("");
            people.drawGifts();
            drawConfirmationWindow();
        }
        else{
            alertWindow("You have nobody on the present's list!!");
        }
    }

    // event handler for Send Emails button. It is sending email to everybody from the list about draw results
    @FXML
    public void sendEmailsButton(ActionEvent event) {
        String emailAddress = ownEmail.getText();
        String password = emailPassword.getText();
        String smtp = smtpHost.getText();

        people.sendAllEmails(emailAddress, password, smtp);
        loginStatus.setText("Emails are sent");
        confirmationWindow("Emails sent successfully!!");
    }


    // event handler for Menu "New",
    @FXML
    public void menuNew(ActionEvent event){
        Optional<ButtonType> result = confirmationWindow("Do you really want to create new list of people");
        if (result.get() == ButtonType.OK)
            initializeGUI();
    }

    // event handler for Menu "Close",
    @FXML
    public void menuClose(ActionEvent event){
        Optional<ButtonType> result = confirmationWindow("Do you really want to close the program");
        if (result.get() == ButtonType.OK)
            Platform.exit();
    }

    // event handler for Menu "Save",
    @FXML
    public void menuSave(ActionEvent event) {

        File file = selectingFile("Save to File");

        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file);

                for (People person : listOfPeople) {
                    String text = person.getName() + ";" + person.getPhone() + ";" + person.getEmail() + "\r\n";
                    writer.write(text);
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // event handler for Menu "Load",
    @FXML
    public void menuLoad(ActionEvent event) {

        File file = selectingFile("Load a File");

        if (file != null) {
            try {
                initializeGUI();
                People addNewPerson;
                String line = null;
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while ((line = bufferedReader.readLine()) != null) {
                    String[] values = line.split(";");
                    addNewPerson = new People();
                    addNewPerson.setName(values[0]);
                    addNewPerson.setPhone(values[1]);
                    addNewPerson.setEmail(values[2]);

                    listOfPeople.add(addNewPerson);
                    people.setPerson(addNewPerson);
                }
                fileReader.close();
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //opening file manager and selecting the file
    public File selectingFile(String boxMessage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(boxMessage);

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        return file;
    }


    //clear the GUI
    public void initializeGUI(){
        listOfPeople = FXCollections.observableArrayList();
        people = new People();
        tableOfPeople.setItems(listOfPeople);
        buttonSendEmail.setDisable(true);
        loginStatus.setText("");

    }


    //data validation
    private boolean dataValidation() {
        boolean test = true;
        String name = textFieldName.getText().trim();
        String phone = textFieldPhone.getText().trim();
        if(name.isEmpty()) {
            alertWindow("Provide a name!!");
            test = false;
        }
        else if(phone.isEmpty()){
            alertWindow("Provide a phone number!!");
            test = false;
        }
        else if(!emailValidation()){
            alertWindow("Provide correct email address!!");
            test = false;
        }

        return test;
    }

    //e-mails addresses validations
    private boolean emailValidation(){
        String email = textFieldEmail.getText().trim();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    //window with messages
    private void alertWindow(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //window with draw results
    private void drawResultsWindow(){
        Alert alertResults = new Alert(Alert.AlertType.INFORMATION);
        alertResults.setTitle("Draw Results");
        alertResults.setHeaderText(null);

        String results = "";
        int size = people.getSizeDrawResults();

        for(int i = 0; i < size; i++)
            results += people.getDrawResultAtIndex(i) + "\n";

        alertResults.setContentText(results);
        alertResults.showAndWait();
    }

    //confirmation window if user want to see draw results
    private void drawConfirmationWindow(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Box");
        alert.setHeaderText(null);
        alert.setContentText("Draw results are ready to send to everybody.\nDo you want to see results?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK)
            drawResultsWindow();
        else
            alert.close();

    }

    //general purpose confirmation window
    private Optional<ButtonType> confirmationWindow(String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Box");
        alert.setHeaderText(null);
        alert.setContentText(text);
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<People, String>("name"));
        tableColumnPhone.setCellValueFactory(new PropertyValueFactory<People, String>("phone"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<People, String>("email"));

        initializeGUI();
    }
}

