package ro.dobrescuandrei.roomdynamicdaosample.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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