# Room Dynamic DAO

### A simple query builder for Google's ROOM ORM for Android

### Import

```
allprojects {
    repositories {
        maven { url 'https://andob.io/repository/open_source' }
    }
}
```
```
dependencies {
    implementation 'ro.andob.roomdynamicdao:dyndao:1.2.4'
    implementation 'ro.andob.fieldschema:annotations:0.3.4'
    kapt 'ro.andob.fieldschema:fs-processor:0.3.4'
    kapt 'ro.andob.fieldschema:ts-processor-room:0.3.4'
}
```

### A query builder? Why?

Because in ROOM, DAO methods are very limited due to the fact that you write the query directly into an annotation's argument. Consider the following:

```kotlin
@Entity
class Restaurant
{
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id : Int = 0

    @ColumnInfo
    var name : String = ""

    @ColumnInfo
    var rating : Int = 0

    @ColumnInfo
    var latitude : Double = 0.0

    @ColumnInfo
    var longitude : Double = 0.0
}
```

```kotlin
class RestaurantFilter
{
    var rating : Int? = null
    var boundingBox : BoundingBox? = null
}
```

```kotlin
class BoundingBox
(
    val northWestLat : Double,
    val northWestLng : Double,
    val southEastLat : Double,
    val southEastLng : Double
)
```

We want to show a list with all restaurants. The user can filter them by rating and can search by restaurant's name. We also have a map, showing only restaurants in some specific zone (defined by the ``BoundingBox``). You will probably write something like:

```kolin
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
}
```

```kotlin
val restaurants = if (filter == null)
{
    if (search != null)
        database.restaurantDao().search(search)
    else database.restaurantDao().getAll()
}
else if (filter.boundingBox != null && filter.rating != null)
{
    if (search != null)
        database.restaurantDao().searchAroundPointWithRating(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0, search, filter.rating?:0)
    else database.restaurantDao().getAllAroundPointWithRating(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0, filter.rating?:0)
}
else if (filter.boundingBox != null)
{
    if (search != null)
        database.restaurantDao().searchAroundPoint(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0, search)
    else database.restaurantDao().getAllAroundPoint(filter.boundingBox?.northWestLat?:0.0, filter.boundingBox?.northWestLng?:0.0, filter.boundingBox?.southEastLat?:0.0, filter.boundingBox?.southEastLng?:0.0)
}
else
{
    if (search != null)
        database.restaurantDao().searchWithRating(filter.rating?:0, search)
    else database.restaurantDao().getAllWithRating(filter.rating?:0)
}
```

Very ugly and verbose! You cannot use if statements in DAOs. You cannot use complex types like ``BoundingBox`` as an argument to a method from DAO.

### Meet Dynamic Dao QueryBuilder

Annotate your model with ``FieldSchemaClass``. The annotation processor will generate a class called ``FS`` (FieldSchema) with constants containing class field names and class names and another class called ``TS`` (TableSchema) with table names and table column names.

```kotlin
@Entity
@FieldSchemaClass
class Restaurant
```

Create a filter model for your main model:

```kotlin
class RestaurantFilter : BaseFilter()
{
    var rating : Int? = null
    var boundingBox : BoundingBox? = null
}
```

``BaseFilter`` is a class from this library with the following structure:

```kotlin
open class BaseFilter
(
    var search : String? = null,
    var offset : Int = 0,
    var limit : Int = BaseFilterDefaults.limit
) : Serializable
```

Create a class that extends QueryBuilder:

```kotlin
class RestaurantListQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter : RestaurantFilter) : super(filter)

    override fun tableName() : String? = TS.Restaurant

    override fun where(conditions : QueryWhereConditions) : String?
    {
        if (filter.search != null)
            conditions.addSearchConditions(filter.search, onColumns = arrayOf(TS.Restaurant_name))

        if (filter.rating != null)
            conditions.add("${TS.Restaurant_rating} = ${filter.rating}")

        if (filter.boundingBox != null)
        {
            conditions.add("${TS.Restaurant_latitude}  <= ${filter.boundingBox?.northWestLat}")
            conditions.add("${TS.Restaurant_latitude}  >= ${filter.boundingBox?.southEastLat}")
            conditions.add("${TS.Restaurant_longitude} >= ${filter.boundingBox?.northWestLng}")
            conditions.add("${TS.Restaurant_longitude} <= ${filter.boundingBox?.southEastLng}")
        }

        return conditions.mergeWithAnd()
    }
}
```



```kotlin
@Dao
interface RestaurantDao
{
    @RawQuery
    fun getList(query : SupportSQLiteQuery) : List<Restaurant>
}
```



```
-- result:
select * from Restaurant where 1=1
select * from Restaurant where name like '%something%'
select * from Restaurant where rating = 5
select * from Restaurant where name like '%something%' and rating = 5
-- and so on, depending on the use case
```



```kotlin
val restaurants = database.restaurantDao().getList(
	RestaurantListQueryBuilder(filter).build())
```

Sweet! Zero boilerplate!

# [Documentation](https://github.com/andob/ROOM-Dynamic-Dao/blob/master/DOCUMENTATION.md)

### License

```java
Copyright 2019-2020 Andrei Dobrescu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

