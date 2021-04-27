package ro.dobrescuandrei.roomdynamicdao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yatatsu.fieldschema.annotations.FieldSchemaClass
import org.jetbrains.annotations.NotNull

@Entity
@FieldSchemaClass
class City
{
    @PrimaryKey
    @NotNull
    val id : Int = 0

    @ColumnInfo
    val name : String = ""

    @ColumnInfo
    val countryId : Int = 0
}