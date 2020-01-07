package net.chepa.home6.server;

import net.chepa.home6.StringConsumer;
import net.chepa.home6.StringProducer;

public class ClientDescriptor implements StringConsumer, StringProducer
{

  private StringConsumer consumer;
  private String nickName;

  @Override
  public void consume(String str) {
    //getting new message from connection proxy
    System.out.println( "Consume: " +str);

    //New client joined
    if(nickName == null){
      int nameString = str.indexOf(":");
      if(nameString != 1 ) {
        nickName = str.substring(0, nameString);
      }

      consumer.consume(nickName + " joined the chat \n");
      consumer.consume(str);

    }
    //Client all ready joined
    else {
      if (str != null) {
        consumer.consume(str);
      }
    }
  }

  @Override
  public void addConsumer(StringConsumer sc) {
        if( sc != null){
          consumer = sc;
        }
  }

  @Override
  public void removeConsumer(StringConsumer sc) {
    if ( sc != null){
      MessageBoard mb = (MessageBoard)consumer;
      mb.removeConsumer(sc);
      consumer = null;
      nickName = null;
    }

  }


}
