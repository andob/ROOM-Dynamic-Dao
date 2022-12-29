package ro.dobrescuandrei.roomdynamicdao

import java.io.Serializable

open class BaseFilter
(
    var search : String? = null,
    var offset : Int = 0,
    var limit : Int = BaseFilterDefaults.limit
) : Serializable
