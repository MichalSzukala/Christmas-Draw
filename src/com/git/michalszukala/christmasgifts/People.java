package com.git.michalszukala.christmasgifts;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Class contains data of every person we want send gifts.  It's performing a gifts draw as well.
public class People {

    private String name = "NAME";
    private String phone ="PHONE";
    private String email = "EMAIL";
    private List<People> peopleList = new ArrayList<>();
    private List<String> luckyCouplesArray = new ArrayList<>();



    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name != null)
            this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if(name != null)
            this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(name != null)
            this.email = email;
    }

    public void setPerson(People person){
        if(person != null)
            peopleList.add(person);
    }


    //Takes first person from the list and draws random second person to it.  Sends both of them emails for whom they should buy gifts
    //then removes person's details from the list,and draws again until there is no or one person left on the list
    public void drawGifts(){
        List<People> drawList = new ArrayList<>();
        String luckyCouple;

        for(People x : peopleList)
            drawList.add(x);

        while(drawList.size() > 1){

            People firstPerson = drawList.get(0);
            People secondPerson = drawList.get(randomNumber(drawList.size()));
            sendEmail(firstPerson.getEmail());
            sendEmail(secondPerson.getEmail());
            luckyCouple = (firstPerson.getName() + " --- " + secondPerson.getName());
            luckyCouplesArray.add(luckyCouple);
            drawList.remove(firstPerson);
            drawList.remove(secondPerson);
        }
        if(drawList.size() > 0 )
            luckyCouplesArray.add(drawList.get(0).getName() + " --- Don't have to buy any gift" );
    }

    //Generates random number
    public int randomNumber(int maxNumber){

        Random random = new Random();
        int number = 0;
        do {
          number = random.nextInt(maxNumber);
        } while(number == 0);

        return number;
    }

    //Send email to picked person
    public void sendEmail(String emailAddress){

    }
}
