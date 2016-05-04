package com.mindtree.brew;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by M1036144 on 14-Apr-16.
 */
public class SendMail {
    private static String username;
    private static String password;
    private static Context context;

    public SendMail(Context context) {
        SendMail.context = context;
    }

    public void sendMail(String title, String subject, String messageBody) {
        try {
            password = Util.getProperty("password", context);
            username = Util.getProperty("username", context);
        } catch (IOException ignored) {
        }
        Properties properties = new Properties();
        properties.put(Constants.MAIL_SMTP_AUTH, Constants.TRUE);
        properties.put(Constants.MAIL_SMTP_STARTTLS_ENABLE, Constants.TRUE);
        properties.put(Constants.MAIL_SMTP_HOST, Constants.SMTP_GMAIL_COM);
        properties.put(Constants.MAIL_SMTP_PORT, "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = createMessage(title, subject, messageBody, session);
            new SendMailTask().execute(message);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Message createMessage(String title, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(username, title));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(username));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }


    public class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, Constants.PLEASE_WAIT, Constants.SENDING_INFORMATION, true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, Constants.INFORMATION_SENT_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }
    }
}
