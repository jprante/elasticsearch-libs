package com.carrotsearch.ant.tasks.junit4.test.listeners.antxml;

import com.carrotsearch.ant.tasks.junit4.listeners.antxml.XmlStringTransformer;

public class XmlStringTransformerAccess {
  public static XmlStringTransformer getInstance() {
    return new XmlStringTransformer();
  }
}
