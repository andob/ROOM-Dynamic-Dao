package ro.dobrescuandrei.roomdynamicdaosample

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ro.dobrescuandrei.roomdynamicdaosample.dao.RestaurantDao
import ro.dobrescuandrei.roomdynamicdaosample.model.City
import ro.dobrescuandrei.roomdynamicdaosample.model.Country
import ro.dobrescuandrei.roomdynamicdaosample.model.Restaurant

@Database(entities = [Restaurant::class],
exportSchema = false,
version = 1)
abstract class AppDB : RoomDatabase()
{
    abstract fun restaurantDao() : RestaurantDao
}