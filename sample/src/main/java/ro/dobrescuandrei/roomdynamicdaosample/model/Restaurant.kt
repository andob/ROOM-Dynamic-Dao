package ro.dobrescuandrei.roomdynamicdaosample.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.yatatsu.fieldschema.annotations.FieldSchemaClass

@Entity
@FieldSchemaClass
open class Restaurant
{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    @ColumnInfo
    var name : String = ""

    @ColumnInfo
    var rating : Int = 0

    @ColumnInfo
    var latitude : Double = 0.0

    @ColumnInfo
    var longitude : Double = 0.0

    @ColumnInfo
    var cityId : Int = 0

    constructor()

    @Ignore
    constructor(id: Int, name: String, rating: Int, latitude: Double, longitude: Double)
    {
        this.id = id
        this.name = name
        this.rating = rating
        this.latitude = latitude
        this.longitude = longitude
    }
}