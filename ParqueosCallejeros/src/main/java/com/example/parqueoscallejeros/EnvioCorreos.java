package com.example.parqueoscallejeros;
import sun.util.resources.ext.CalendarData_ca;

import javax.mail.*;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSession;
import java.util.Properties;

public class EnvioCorreos {

    // Esto se hizo con un gmail
    // usuario y contraseña de ese correo
    private static String emailFrom = "parqueoscallejeros@gmail.com";
    private static String passwordFrom = "njvwhnohfgogersi";
    private String emailTo;
    private String subject;
    private String content;

    private Properties mProperties;
    private Session mSession;
    private MimeMessage mCorreo;

    public EnvioCorreos() {
        initComponents();
        mProperties = new Properties();
    }

    private void initComponents() {
    }

    /**
     * Se crea un email
     * @param _emailTo para quien es el email
     * @param _subject Cual es el tieulo
     * @param _content cual es el contenido
     * @throws MessagingException
     */
    public void createEmail(String _emailTo, String _subject, String _content) throws MessagingException {
        emailTo = _emailTo; // Tengo que poner la informacion, Paso como parametro?
        subject = _subject;
        content = _content;

        // Se hace un protocolo que permite mandar emails
        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.stmp.ssl.trust", "smtp.gmail.com");
        mProperties.setProperty("mail.smtp.starttls.enable", "true");
        mProperties.setProperty("mail.smtp.port", "587");
        mProperties.setProperty("mail.smtp.user", emailFrom);
        mProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mProperties.setProperty("mail.smtp.auth", "true");

        mSession = Session.getDefaultInstance(mProperties);

        mCorreo = new MimeMessage(mSession);
        mCorreo.setFrom(new InternetAddress(emailTo));
        mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        mCorreo.setSubject(subject);
        mCorreo.setText(content, "ISO-8859-1", "html");

    }

    /**
     * Funcion que toma la accion de mandar el email
     * @throws MessagingException
     */
    public void sendEmail() throws MessagingException {
        Transport mTransport = mSession.getTransport("smtp");
        mTransport.connect(emailFrom, passwordFrom);
        mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
        mTransport.close();
    }
}
// cambio