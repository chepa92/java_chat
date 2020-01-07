package net.chepa.home6.client;

import net.chepa.home6.ConnectionProxy;
import net.chepa.home6.StringConsumer;
import net.chepa.home6.StringProducer;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

/**
* Client GUI components for Chat program
**/

public class ClientGUI implements StringConsumer, StringProducer {

  private JFrame frame;
  private JButton bt_connect, bt_send, bt_disconnect;
  private JTextArea ta_chat;
  private JTextField tf_port, tf_ip, tf_name, tf_message;
  private JPanel chat_panel, send_panel, connection_panel, right_panel;
  private StringConsumer sc, me;
  private StringProducer sp;
  private ActionListener listener;
  private JScrollPane scroller;
  private JLabel label, label1, label2, label3;

  //Building the GUI
  public ClientGUI() {

    frame = new JFrame("CHAT CLIENT");
    ta_chat = new JTextArea(18,35);
    tf_port = new JTextField(10);
    tf_ip = new JTextField(10);
    tf_name = new JTextField(10);
    tf_message = new JTextField(24);
    bt_connect = new JButton("Connect");
    bt_send = new JButton("Send");
    bt_disconnect = new JButton("Disconnect");

    chat_panel = new JPanel();
    right_panel = new JPanel();
    send_panel = new JPanel();
    connection_panel = new JPanel();
    scroller = new JScrollPane(ta_chat);
    label=new JLabel("Enter Your Message Here");
    label1=new JLabel("IP:");
    label2=new JLabel("Port:");
    label3=new JLabel("Your Name:");


    chat_panel.add(scroller, BorderLayout.EAST);

    send_panel.add(label);
    send_panel.add(tf_message);
    send_panel.add(bt_send);

    connection_panel.add(label1);
    connection_panel.add(tf_ip);
    connection_panel.add(label2);
    connection_panel.add(tf_port);
    connection_panel.add(label3);
    connection_panel.add(tf_name);
    connection_panel.add(bt_connect);
    connection_panel.add(bt_disconnect);


    tf_ip.setText("127.0.0.1"); //TODO delete
    tf_port.setText("1300"); //TODO delete
    tf_name.setText("test"); //TODO delete

    right_panel.setLayout(new GridLayout(4, 1));
    right_panel.add(send_panel);
    right_panel.add(connection_panel);

    frame.add(connection_panel, BorderLayout.NORTH);
    frame.add(chat_panel, BorderLayout.CENTER);
    frame.add(right_panel, BorderLayout.SOUTH);

    listener = new ButtonsListener();
    bt_send.addActionListener(listener);
    bt_connect.addActionListener(listener);
    bt_disconnect.addActionListener(listener);

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

      //Pressing connect button
      if (source == bt_connect)
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
            ta_chat.setText("Connected\n");
//            sc.consume(tf_name.getText() + " is now connected\n");
            bt_send.setEnabled(true);
            tf_ip.setEnabled(false);
            tf_port.setEnabled(false);
            tf_name.setEnabled(false);
            bt_connect.setEnabled(false);
          } catch (Exception e1) {
              System.out.println("Fail connection");
          }
          sp = connection;
          sp.addConsumer(me);
        }
      }
      //Pressing send button
      if (bt_send == source)
      {
        System.out.println("Trying to send message: " + tf_message.getText());
        txt_msg = tf_name.getText() + ": " + tf_message.getText() + "\n";
        sc.consume(txt_msg);
        tf_message.setText(null);
      }

      //Pressing Disconnect button
      if(bt_disconnect == source)
      {
        System.out.println( tf_name.getText() + " was disconnected");
        sc.consume(tf_name.getText() + " is now disconnected from the chat\n");
        bt_send.setEnabled(false);
        tf_ip.setEnabled(true);
        tf_port.setEnabled(true);
        tf_name.setEnabled(true);
        bt_connect.setEnabled(true);
        sp.removeConsumer(me);
        sp = null;
        sc = null;

      }
    }
  }

  @Override
  public void consume(String str) {
    if(str != null)
    {
      ta_chat.append(str);
    }
  }

  @Override
  public void addConsumer(StringConsumer sc) {

  }

  @Override
  public void removeConsumer(StringConsumer sc) {
  }

  public void go() {
    frame.setSize(700, 600);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    ClientGUI gui = new ClientGUI();
    gui.go();
  }
}