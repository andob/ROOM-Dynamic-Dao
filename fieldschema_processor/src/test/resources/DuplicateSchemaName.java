import com.yatatsu.fieldschema.annotations.FieldSchemaClass;


@FieldSchemaClass(name = "foo") public class DuplicateSchemaName {
  String bar;

  @FieldSchemaClass(name = "foo") static class Buz {
    String bar;
  }
}
