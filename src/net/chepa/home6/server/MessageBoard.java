package net.chepa.home6.server;

import net.chepa.home6.StringConsumer;
import net.chepa.home6.StringProducer;

import java.util.LinkedList;

public class MessageBoard implements StringConsumer, StringProducer
{

  private static MessageBoard board;
  private static LinkedList<StringConsumer> list;

  private MessageBoard (){}

  public static MessageBoard getMessageBoard()
  {
    if (board == null )
    {
      board = new MessageBoard();
      list = new LinkedList<StringConsumer>();
    }
    return board;
  }

  /**Sending the message to all clients*/
  @Override
  public void consume(String str) {
    System.out.println(str);
    for(StringConsumer consumer: list)
    {
      consumer.consume(str);
    }
  }

  /** add client to list */
  @Override
  public void addConsumer(StringConsumer sc) {
    list.add(sc);
  }

  /** remove client from list */
  @Override
  public void removeConsumer(StringConsumer sc) {
    list.remove(list.indexOf(sc));
  }
}