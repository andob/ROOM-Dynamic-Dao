package com.yatatsu.fieldschema.processor;

import javax.lang.model.element.Element;

public class ProcessingException extends RuntimeException {

  public final Element element;

  public ProcessingException(Element element, String message, Object... args) {
    super(String.format(message, args));
    this.element = element;
  }

  public ProcessingException(Element element, String message, Throwable throwable, Object... args) {
    super(String.format(message, args), throwable);
    this.element = element;
  }
}
