package ro.dobrescuandrei.roomdynamicdaosample.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yatatsu.fieldschema.annotations.FieldSchemaClass

@Entity
@FieldSchemaClass
class City
{
    @PrimaryKey
    val id : Int = 0

    @ColumnInfo
    val name : String = ""

    @ColumnInfo
    val countryId : Int = 0
}