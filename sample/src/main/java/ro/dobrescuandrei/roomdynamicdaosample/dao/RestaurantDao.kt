package ro.dobrescuandrei.roomdynamicdaosample.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import ro.dobrescuandrei.roomdynamicdaosample.model.Restaurant

@Dao
interface RestaurantDao
{
    @Query("select * from Restaurant")
    fun getAll() : List<Restaurant>

    @Query("select * from Restaurant where name like :search")
    fun search(search : String) : List<Restaurant>

    @Query("select * from Restaurant where rating = :rating")
    fun getAllWithRating(rating : Int) : List<Restaurant>

    @Query("select * from Restaurant where rating = :rating and name like :search")
    fun searchWithRating(rating : Int, search : String) : List<Restaurant>

    @Query("select * from Restaurant where latitude >= :southEastLat and latitude <= :northWestLat and longitude >= :northWestLng and longitude <= :southEastLng")
    fun getAllAroundPoint(northWestLat : Double, northWestLng : Double, southEastLat : Double, southEastLng : Double) : List<Restaurant>

    @Query("select * from Restaurant where latitude >= :southEastLat and latitude <= :northWestLat and longitude >= :northWestLng and longitude <= :southEastLng and name like :search")
    fun searchAroundPoint(northWestLat : Double, northWestLng : Double, southEastLat : Double, southEastLng : Double, search : String) : List<Restaurant>

    @Query("select * from Restaurant where latitude >= :southEastLat and latitude <= :northWestLat and longitude >= :northWestLng and longitude <= :southEastLng and rating = :rating")
    fun getAllAroundPointWithRating(northWestLat : Double, northWestLng : Double, southEastLat : Double, southEastLng : Double, rating : Int) : List<Restaurant>

    @Query("select * from Restaurant where latitude >= :southEastLat and latitude <= :northWestLat and longitude >= :northWestLng and longitude <= :southEastLng and name like :search and rating = :rating")
    fun searchAroundPointWithRating(northWestLat : Double, northWestLng : Double, southEastLat : Double, southEastLng : Double, search : String, rating : Int) : List<Restaurant>

    @RawQuery
    fun getList(query : SupportSQLiteQuery) : List<Restaurant>

    @Insert
    fun insert(dummyRestaurants : List<Restaurant>)
}