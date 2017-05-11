package com.artnaseef.jmsqueue;

import javax.jms.Message;

/**
 * Created by art on 5/11/17.
 */
public interface JmsQueuer {
  void enqueue(Message msg);
}
