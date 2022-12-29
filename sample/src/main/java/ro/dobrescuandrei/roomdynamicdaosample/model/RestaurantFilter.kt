package ro.dobrescuandrei.roomdynamicdaosample.model

import ro.dobrescuandrei.roomdynamicdao.BaseFilter

class RestaurantFilter : BaseFilter()
{
    var rating : Int? = null
    var boundingBox : BoundingBox? = null
}