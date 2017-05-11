package com.artnaseef.jmsqueue.impl;

import com.artnaseef.jmsqueue.JmsQueuer;

import java.util.Deque;
import java.util.LinkedList;

import javax.jms.Message;

/**
 * Created by art on 5/11/17.
 */
public class ExampleJmsQueuer implements JmsQueuer {

  public static final long DEFAULT_MAX_MESSAGES_QUEUED = 10;

  private final Deque<Message> messageQueue = new LinkedList<Message>();
  private long maxMessagesQueued = DEFAULT_MAX_MESSAGES_QUEUED;
  private JmsMessageSender messageSender;

  public void setMessageSender(JmsMessageSender messageSender) {
    this.messageSender = messageSender;
  }

  public void setMaxMessagesQueued(long maxMessagesQueued) {
    this.maxMessagesQueued = maxMessagesQueued;
  }

  public void start() {
    if (this.messageSender == null) {
      this.messageSender = new JmsMessageSender();
    }

    this.messageSender.setMessageSupplier(this::dequeue);

    // TODO: prevent more than one from being started.
    new Thread(this.messageSender).start();
  }

  public void enqueue(Message msg) {
    synchronized(this.messageQueue) {
      if (this.messageQueue.size() < this.maxMessagesQueued) {
        this.messageQueue.add(msg);
        this.messageQueue.notifyAll();
      } else {
        // TODO: log, report!
      }
    }
  }

  private Message dequeue() {
    Message result = null;

    while (result == null) {
      synchronized (this.messageQueue) {
        result = this.messageQueue.poll();

        if (result == null) {
          try {
            this.messageQueue.wait();
          } catch (InterruptedException e) {
            // TODO - error handling case
            e.printStackTrace();
          }
        }

      }
    }

    return result;
  }
}
