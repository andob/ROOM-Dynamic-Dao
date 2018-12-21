package com.yatatsu.fieldschema.processor;

import javax.lang.model.element.Element;

public class FieldSchemaHolder {

  private final String name;
  private final String value;
  private final Element element;
  private final FieldSchemaClassHolder classHolder;

  public FieldSchemaHolder(Element element, FieldSchemaClassHolder classHolder, String prefix) {
    this.element = element;
    this.classHolder = classHolder;
    this.value = element.toString();
    this.name = prefix + "_" + element.toString();
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public Element getElement() {
    return element;
  }

  public FieldSchemaClassHolder getClassHolder() {
    return classHolder;
  }

  @Override public boolean equals(Object o) {
    return o == this
        || (o instanceof FieldSchemaHolder && ((FieldSchemaHolder) o).name.equals(name));
  }

  @Override public int hashCode() {
    return name.hashCode();
  }
}
