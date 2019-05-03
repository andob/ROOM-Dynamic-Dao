package ro.andreidobrescu.basefilter

import java.io.Serializable

open class BaseFilter
(
    var search : String? = null,
    var offset : Int = 0,
    var limit : Int = DefaultLimit
) : Serializable
{
    companion object
    {
        var DefaultLimit : Int = 100
    }
}
