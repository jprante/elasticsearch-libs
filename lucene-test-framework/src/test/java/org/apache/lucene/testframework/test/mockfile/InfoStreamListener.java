package org.apache.lucene.testframework.test.mockfile;

import org.apache.lucene.util.InfoStream;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class InfoStreamListener extends InfoStream {
    /** True if we saw the message */
    final AtomicBoolean seenMessage = new AtomicBoolean(false);
    /** Expected message */ 
    final String messageStartsWith;
    
    InfoStreamListener(String messageStartsWith) {
      this.messageStartsWith = messageStartsWith;
    }
    
    @Override
    public void close() throws IOException {}

    @Override
    public void message(String component, String message) {
      if ("FS".equals(component) && message.startsWith(messageStartsWith)) {
        seenMessage.set(true);
      }
    }

    @Override
    public boolean isEnabled(String component) {
      return true;
    }
    
    boolean sawMessage() {
      return seenMessage.get();
    }
  }