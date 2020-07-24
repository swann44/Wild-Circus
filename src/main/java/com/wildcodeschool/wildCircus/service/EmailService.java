package com.wildcodeschool.wildCircus.service;

import com.wildcodeschool.wildCircus.entity.Cours;
import com.wildcodeschool.wildCircus.entity.Inscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendNewUserEmail(String email, String username) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Inscription à Wild Circus");
        msg.setText("Bonjour,\nVotre compte a bien été créé,\nVotre pseudo est : " + username);
        javaMailSender.send(msg);
    }

    public void sendNewInscription(String email, String username, Inscription inscription) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Validation de l'inscription à un cours");
        msg.setText(" Bonjour,\nMerci " + username + " " + "pour votre inscription à l'un de nos cours.\n" +
               "Récapitulatif de votre inscription :\nCours : " + inscription.getCours().getName() + "\nPersonne bénéficiaire : " + inscription.getFirstName().substring(0, 1).toUpperCase()+inscription.getFirstName().substring(1, inscription.getFirstName().length()) + " " + inscription.getLastName().substring(0, 1).toUpperCase()+inscription.getLastName().substring(1, inscription.getLastName().length()));
        javaMailSender.send(msg);

    }
}
