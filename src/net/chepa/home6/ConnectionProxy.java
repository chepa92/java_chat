package net.chepa.home6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer {

  private Socket sock;
  private DataInputStream dis;
  private DataOutputStream dos;
  private StringConsumer iConsumer;
  private StringProducer iProducer;

  /**
   * New Thread
   */
  public ConnectionProxy(Socket socket) throws IOException {
    this.sock = socket;
    dis = new DataInputStream(socket.getInputStream());
    dos = new DataOutputStream(socket.getOutputStream());
  }


  /**
   * get message form client and give it to server
   */
  @Override
  public void consume(String str) {
    String messageReceived = str;
    try {
      dos.writeUTF(messageReceived);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * adding client
   */
  @Override
  public void addConsumer(StringConsumer sc) {
    Object o = sc;
    iProducer = (StringProducer) o;
    iConsumer = sc;
  }

  /**
   * remove client from list
   **/
  @Override
  public void removeConsumer(StringConsumer sc) {
    try {
      this.wait();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      iProducer.removeConsumer(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
    iProducer = null;
    sc = null;
    try {
      dis.close();
      dos.close();
      sock.close();
      dis = null;
      dos = null;
      sock = null;
    } catch (IOException x) {
      x.printStackTrace();
    }
  }

  /**
   * reading info from server and give it to client
   */
  public synchronized void run() {
    String str = null;
    if (!sock.isConnected()) { //if client DC > remove it
      this.removeConsumer(iConsumer);
      return;
    }

    try {
      while (true) {
        str = dis.readUTF();
        if (str.isEmpty() == false) {
          iConsumer.consume(str);
        }
      }
    } catch (Exception e) {
    }
  }
}