package com.git.michalszukala.christmasgifts;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

//Class contains data of every person we want send a gifts.  It's performing as well draw of gifts.
public class People {

    private String name = "NAME";
    private String phone ="PHONE";
    private String email = "EMAIL";
    private List<People> peopleList = new ArrayList<>();
    private List<String> luckyCouplesArray;



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

    //returns value of ArrayList with draw's Results
    public String getDrawResultAtIndex(int index){
        int size = luckyCouplesArray.size();

        if(index > -1 && index < size)
            return luckyCouplesArray.get(index);
        else
            return "Index Error";
    }

    //removes one person from peopleList
    public void removeFromPeopleList(People person){
        peopleList.remove(person);
    }


    public int getSizeDrawResults(){
        if(luckyCouplesArray.size() > -1)
            return luckyCouplesArray.size();
        else
            return -1;
    }


    //Takes first person from the list and draws random second person to it.  Sends both of them emails for whom they should buy gifts
    //then removes person's details from the list,and draws again until there is no or one person left on the list
    public void drawGifts(){
        List<People> drawList = new ArrayList<>();
        luckyCouplesArray = new ArrayList<>();
        String luckyCouple;

        for(People x : peopleList)
            drawList.add(x);

        while(drawList.size() > 1){

            People firstPerson = drawList.get(0);
            People secondPerson = drawList.get(randomNumber(drawList.size()));
            //sendEmail(firstPerson.getEmail());
            //sendEmail(secondPerson.getEmail());
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
    public void sendEmail(String receiverEmail){

        // sets SMTP server properties
        String host = "smtp.gmail.com";
        String port = "465";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host); //SMTP Host
        properties.put("mail.smtp.port", port);  //SMTP Port
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        properties.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        properties.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication


        String yourEmail = "";
        String password = "";

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(yourEmail, password);
            }
        };

        Session session = Session.getInstance(properties, auth);


        // creates a new e-mail message

        String emailTopic = "This year gift draw";
        String emailMessage = "You are going to buy gift to ";

        try{
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(yourEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
        msg.setSubject(emailTopic);
        msg.setText(emailMessage);

        // sends the e-mail
        Transport.send(msg);
        } catch (MessagingException exp) {
            throw new RuntimeException(exp);
        }

    }
}
