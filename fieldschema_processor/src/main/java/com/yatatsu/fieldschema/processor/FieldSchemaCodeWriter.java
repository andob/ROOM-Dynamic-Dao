package com.yatatsu.fieldschema.processor;

import com.squareup.javapoet.FieldSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

public class FieldSchemaCodeWriter extends BaseCodeWriter<FieldSchemaHolder> {

  FieldSchemaCodeWriter(List<FieldSchemaHolder> holders)
  {
    super(holders);
  }

  @Override
  public FieldSpec createFieldSpec(FieldSchemaHolder holder)
  {
    return FieldSpec.builder(String.class, holder.getName(), Modifier.PUBLIC, Modifier.STATIC,
        Modifier.FINAL).initializer("$S", holder.getValue()).build();
  }
}
