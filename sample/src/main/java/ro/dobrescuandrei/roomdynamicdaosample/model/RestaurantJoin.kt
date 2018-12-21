package ro.dobrescuandrei.roomdynamicdaosample.model

import com.yatatsu.fieldschema.annotations.FieldSchemaClass

@FieldSchemaClass
class RestaurantJoin : Restaurant()
{
    val cityName : String = ""
    val countryName : String = ""
}