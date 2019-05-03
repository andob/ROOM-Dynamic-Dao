package ro.dobrescuandrei.roomdynamicdaosample

import androidx.room.Database
import androidx.room.RoomDatabase
import ro.dobrescuandrei.roomdynamicdaosample.dao.RestaurantDao
import ro.dobrescuandrei.roomdynamicdaosample.model.Restaurant

@Database(entities = [Restaurant::class],
exportSchema = false,
version = 1)
abstract class AppDB : RoomDatabase()
{
    abstract fun restaurantDao() : RestaurantDao
}