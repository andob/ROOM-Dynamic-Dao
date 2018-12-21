package com.yatatsu.fieldschema.processor;

import com.squareup.javapoet.FieldSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

public class ClassNameCodeWriter extends BaseCodeWriter<FieldSchemaClassHolder> {

    ClassNameCodeWriter(List<FieldSchemaClassHolder> holders)
    {
        super(holders);
    }

    @Override
    public FieldSpec createFieldSpec(FieldSchemaClassHolder holder)
    {
        return FieldSpec.builder(String.class, holder.getName(), Modifier.PUBLIC, Modifier.STATIC,
                Modifier.FINAL).initializer("$S", holder.getName()).build();
    }
}