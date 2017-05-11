package com.artnaseef.jmsqueue.impl;

import java.util.function.Supplier;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

/**
 * Created by art on 5/11/17.
 */
public class JmsMessageSender implements Runnable {

  private MessageProducer jmsProducer;
  private Supplier<Message> messageSupplier;
  private boolean running = true;

  public void setJmsProducer(MessageProducer jmsProducer) {
    this.jmsProducer = jmsProducer;
  }

  public void setMessageSupplier(Supplier<Message> messageSupplier) {
    this.messageSupplier = messageSupplier;
  }

  public void run() {
    while (running) {
      Message msg = this.messageSupplier.get();

      try {
        this.jmsProducer.send(msg);
      } catch (JMSException e) {
        // TODO: how to handle when an JMS Exception occurs?  NOTE: Use the AMQ failover transport
        //  to minimize the possibility of this exception.
        e.printStackTrace();
      }
    }
  }
}
