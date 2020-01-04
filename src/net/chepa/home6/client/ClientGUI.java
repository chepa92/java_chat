package net.chepa.home6.client;

import net.chepa.home6.ConnectionProxy;
import net.chepa.home6.StringConsumer;
import net.chepa.home6.StringProducer;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

public class ClientGUI implements StringConsumer, StringProducer {

  private JFrame frame;
  private JButton bt_connect, bt_send;
  private JTextArea ta_chat;
  private JTextField tf_port, tf_ip, tf_name, tf_message;
  private JPanel chat_panel, send_panel, connection_panel, right_panel;
  private StringConsumer sc, sp, me;
  private ActionListener listener;

  public ClientGUI() {

    frame = new JFrame("CHAT CLIENT");
    ta_chat = new JTextArea(10,10);
    tf_port = new JTextField(10);
    tf_ip = new JTextField(10);
    tf_name = new JTextField(10);
    tf_message = new JTextField(5);
    bt_connect = new JButton("Connect");
    bt_send = new JButton("Send");

    chat_panel = new JPanel();
    right_panel = new JPanel();
    send_panel = new JPanel();
    connection_panel = new JPanel();

    chat_panel.add(ta_chat);

    send_panel.setLayout(new GridLayout(1, 2));
    send_panel.add(tf_message);
    send_panel.add(bt_send);

    connection_panel.setLayout(new GridLayout(1, 4));
    connection_panel.add(tf_ip);
    connection_panel.add(tf_port);
    connection_panel.add(tf_name);
    connection_panel.add(bt_connect);

    tf_ip.setText("127.0.0.1"); //TODO delete
    tf_port.setText("1300"); //TODO delete
    tf_name.setText("test"); //TODO delete

    right_panel.setLayout(new GridLayout(4, 1));
    right_panel.add(send_panel);
    right_panel.add(connection_panel);

    frame.setLayout(new GridLayout(1, 2));
    frame.add(chat_panel);
    frame.add(right_panel);

    listener = new ButtonsListener();
    bt_send.addActionListener(listener);
    bt_connect.addActionListener(listener);

    me = this;
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public class ButtonsListener implements ActionListener {

    String txt_ip, txt_port, txt_name, txt_msg;
    private ConnectionProxy connection;

    private Socket connect() {
      Socket socket = null;
      try {
        socket = new Socket(txt_ip, Integer.parseInt(txt_port));
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
      return socket;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      Object source = actionEvent.getSource();
      if (source == bt_connect) //pressing connect button
      {
        txt_msg = tf_message.getText();
        txt_ip = tf_ip.getText();
        txt_port = tf_port.getText();
        txt_name = tf_name.getText();
        System.out.println("Trying to connect");

        if (txt_ip.equals("") || (txt_port.equals(""))) {
          System.out.println("NULL PORT/IP");
        } else {
          try {
            connection = new ConnectionProxy(connect());
            connection.start();
            sc = connection;
            System.out.println("Connected");
            ta_chat.setText("Connected");
            sc.consume(tf_name.getText() + " is now connected\n");
            bt_send.setEnabled(true);
            tf_ip.setEnabled(false);
            tf_port.setEnabled(false);
            tf_name.setEnabled(false);
          } catch (Exception e1) {
            System.out.println("Fail connection");
          }
          sp = connection;
          sp.addConsumer(me);
        }
      }
      if (bt_send == source)// pressing send button
      {
        System.out.println("Trying to send message: " + tf_message.getText());
        txt_msg = tf_message + ": " + ta_chat.getText() + "\n";
        sc.consume(txt_msg);
        ta_chat.setText(null);
      }
    }
  }

  @Override
  public void consume(String str) {
    ta_chat.append(str);
  }

  @Override
  public void addConsumer(StringConsumer sc) {
  }

  @Override
  public void removeConsumer(StringConsumer sc) {
  }

  public void go() {
    frame.setSize(1024, 768);
    frame.setVisible(true);
  }

  public static void main(String args[]) {
    ClientGUI gui = new ClientGUI();
    gui.go();
  }
}