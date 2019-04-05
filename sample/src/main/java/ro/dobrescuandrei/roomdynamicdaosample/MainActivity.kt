package ro.dobrescuandrei.roomdynamicdaosample

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ro.dobrescuandrei.roomdynamicdaosample.dao.querybuilder.RestaurantListQueryBuilder
import ro.dobrescuandrei.roomdynamicdaosample.model.RestaurantFilter

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database=Room.inMemoryDatabaseBuilder(this, AppDB::class.java)
            .allowMainThreadQueries()
            .build()

        database.restaurantDao().insert(dummyRestaurants)

        val search : String? = null
        val filter : RestaurantFilter? = RestaurantFilter()
        filter?.boundingBox=null
        filter?.rating=5

        val restaurants=if (filter==null)
        {
            if (search!=null)
                database.restaurantDao().search(search)
            else database.restaurantDao().getAll()
        }
        else if (filter.boundingBox!=null&&filter.rating!=null)
        {
            if (search!=null)
                database.restaurantDao().searchAroundPointWithRating(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0, search, filter.rating?:0)
            else database.restaurantDao().getAllAroundPointWithRating(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0, filter.rating?:0)
        }
        else if (filter.boundingBox!=null)
        {
            if (search!=null)
                database.restaurantDao().searchAroundPoint(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0, search)
            else database.restaurantDao().getAllAroundPoint(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0)
        }
        else
        {
            if (search!=null)
                database.restaurantDao().searchWithRating(filter.rating?:0, search)
            else database.restaurantDao().getAllWithRating(filter.rating?:0)
        }

        if (filter!=null)
        {
            val restaurants1=database.restaurantDao().getList(RestaurantListQueryBuilder(filter).build())

            println(restaurants)
            println(restaurants1)
        }
    }
}
