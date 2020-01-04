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

  public ClientGUI() {
    frame = new JFrame("CHAT CLIENT");
    ta_chat = new JTextArea(200, 300);
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

    right_panel.setLayout(new GridLayout(4, 1));
    right_panel.add(send_panel);
    right_panel.add(connection_panel);

    frame.setLayout(new GridLayout(1, 2));
    frame.add(chat_panel);
    frame.add(right_panel);

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