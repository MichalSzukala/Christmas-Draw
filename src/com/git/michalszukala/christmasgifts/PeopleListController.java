package com.git.michalszukala.christmasgifts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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

    // even handler for Add button, it is adding a new person
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

    // even handler for Delete button, it is deleting a person from the table
    @FXML
    public void deleteButton(){
        ObservableList<People> selectedRow = tableOfPeople.getSelectionModel().getSelectedItems();
        People person = tableOfPeople.getSelectionModel().getSelectedItem();
        listOfPeople.removeAll(selectedRow);
        people.removeFromPeopleList(person);
    }

    // even handler for Draw button, it is calling method responsible for drawing gifts from the People class
    @FXML
    public void drawButton(ActionEvent event) {
        if(listOfPeople.size() != 0) {
            people.drawGifts();
            drawConfirmationWindow();
        }
        else{
            alertWindow("You have nobody on the present's list!!");
        }
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

    }

    //window with draw results
    private void drawResultsWindow(){

    }

    //confirmation window if user want to see draw results
    private void drawConfirmationWindow(){

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<People, String>("name"));
        tableColumnPhone.setCellValueFactory(new PropertyValueFactory<People, String>("phone"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<People, String>("email"));


        listOfPeople = FXCollections.observableArrayList();
        people = new People();

        tableOfPeople.setItems(listOfPeople);




    }
}

