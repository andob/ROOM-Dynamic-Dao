package ro.dobrescuandrei.roomdynamicdao.model

import ro.andreidobrescu.basefilter.BaseFilter

class RestaurantFilter : BaseFilter()
{
    var rating : Int? = null
    var boundingBox : BoundingBox? = null
}