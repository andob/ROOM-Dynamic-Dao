package ro.dobrescuandrei.roomdynamicdao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yatatsu.fieldschema.annotations.FieldSchemaClass

@Entity
@FieldSchemaClass
class Country
{
    @PrimaryKey
    val id : Int = 0

    @ColumnInfo
    val name : String = ""
}