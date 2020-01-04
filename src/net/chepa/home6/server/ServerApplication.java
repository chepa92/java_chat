package net.chepa.home6.server;

import net.chepa.home6.ConnectionProxy;

import java.io.*;
import java.net.*;

public class ServerApplication {
  public static void main(String args[]) {
    ServerSocket server = null;
    MessageBoard mb = MessageBoard.getMessageBoard();
    try {
      server = new ServerSocket(1300, 5);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Socket socket = null;
    ClientDescriptor client = null;
    ConnectionProxy connection = null;
    while (true) {
      try {
        socket = server.accept();
        connection = new ConnectionProxy(socket);
        //client = new ClientDescriptor();
        connection.addConsumer(mb);
        //client.addConsumer(mb);
        mb.addConsumer(connection);
        connection.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}