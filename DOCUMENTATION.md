# Room Dynamic DAO

## Documentation

### Table of contents

1. [QueryBuilder methods overview](#overview)
2. [How to build where conditions](#where)
3. [How to join other tables](#join)
4. [How to project columns from tables](#project)
5. [OOP vs FP QueryBuilder styles](#oopfp)
6. [How to change default settings](#defaults)

### QueryBuilder methods overview <a name="overview"></a>

```kotlin
class DemoQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter: RestaurantFilter) : super(filter)

    //used to specify table name
    override fun tableName() : String? = FS.Restaurant

    //used to specify where conditions
    override fun where(conditions : QueryWhereConditions): String? = "1=1"

    //enable pagination. By default, pagination is DISABLED
    //to change this default behavior, see How to change default settings section
    override fun isPaginationEnabled() : Boolean = true

    //column projections
    override fun projection(clauses : QueryProjectionClauses): String = "*"

    //use for joins
    override fun join(clauses : QueryJoinClauses): String? = null

    //specify order by
    override fun orderBy(): String? = "${FS.Restaurant_id} asc"
}
```

### How to build where conditions <a name="where"></a>

Use ``addSearchConditions`` for search conditions:

```kotlin
override fun where(conditions: QueryWhereConditions) : String?
{
    conditions.addSearchConditions(search, onColumns = arrayOf(FS.Restaurant_name, FS.Restaurant_description))
    return conditions.mergeWithAnd()
}
```

The result will be ``where name like '%search%' or description like '%search%'``

Use ``mergeWithAnd`` to generate a where condition set delimited by and:

```kotlin
override fun where(conditions: QueryWhereConditions) : String?
{
    conditions.addSearchConditions(search, onColumns = arrayOf(FS.Restaurant_name, FS.Restaurant_description))
    conditions.add("${FS.Restaurant_id} = 1")
    conditions.add("${FS.Restaurant_rating} = 5")
    return conditions.mergeWithAnd()
}
```

Result: ``where (name like '%search%' or description like '%search%') and id = 1 and rating = 5``

If you: ``return conditions.mergeWithOr()``

Result: ``where (name like '%search%' or description like '%search%') or id = 1 or rating = 5``

You can also create complex conditions with paranthesis:

```kotlin
override fun where(conditions : QueryWhereConditions) : String?
{
    val firstConditionSet=QueryWhereConditions()
    firstConditionSet.add("${FS.Restaurant_id} = 1")
    firstConditionSet.add("${FS.Restaurant_rating} = 5")
    conditions.add("(${firstConditionSet.mergeWithAnd()})")

    val secondConditionSet=QueryWhereConditions()
    secondConditionSet.add("${FS.Restaurant_id} = 2")
    secondConditionSet.add("${FS.Restaurant_rating} = 3")
    conditions.add("(${secondConditionSet.mergeWithAnd()})")
    
    return conditions.mergeWithOr()
}
```

Result: ``where (id = 1 and rating = 5) or (id = 2 and rating = 3)``

### Don't forget to ESCAPE STRINGS:

``conditions.add("${FS.Restaurant_name} = ${"asdf".sqlEscaped}")``

Result: ``name = 'asdf'``

Or string arrays or collections:

``conditions.add("${FS.Restaurant_name} in ${arrayOf("asdf", "qwerty").sqlEscaped}")``

Result: ``name in ('asdf','qwerty')``

Or number arrays or collections:

``conditions.add("${FS.Restaurant_id} in ${intArrayOf(1, 5).sqlEscaped}")``

Result: ``id in (1, 5)``

Or booleans:

``conditions.add("${FS.Restaurant_isActive} = ${true.sqlEscaped}")``

Result: ``isActive = 1``

Note: before escaping string / number arrays / lists, you must check if the array is not empty, otherwise an exception will be throwed.

```
arrayOf("some", "strings").sqlEscaped //OK -> ('some', 'strings')
arrayOf<String>().sqlEscaped //RuntimeException
```

### How to join other tables <a name="join"></a>

Consider we add 

```kotlin
@ColumnInfo
var cityId : Int = 0
```

To ``Restaurant`` table. We also have:

```kotlin
@Entity
@FieldSchemaClass
class City
{
    @PrimaryKey
    val id : Int = 0

    @ColumnInfo
    val name : String = ""

    @ColumnInfo
    val countryId : Int = 0
}
```

```kotlin
@Entity
@FieldSchemaClass
class Country
{
    @PrimaryKey
    val id : Int = 0

    @ColumnInfo
    val name : String = ""
}
```

Create a class with join results:

```kotlin
@FieldSchemaClass
class RestaurantJoin : Restaurant()
{
    val cityName : String = ""
    val countryName : String = ""
}
```

Join tables:

```kotlin
override fun join(clauses : QueryJoinClauses) : String?
{
    clauses.addInnerJoin(
        table = FS.Restaurant,
        column = FS.Restaurant_cityId,
        remoteTable = FS.City,
        remoteColumn = FS.City_id)

    clauses.addInnerJoin(
        table = FS.City,
        column = FS.City_countryId,
        remoteTable = FS.Country,
        remoteColumn = FS.Country_id)

    return clauses.merge()
}
```

Result:

```sql
from Restaurant inner join City    on City.id    = Restaurant.cityId 
                inner join Country on Country.id = City.countryId
```

You can also use ``addCrossJoin``, ``addLeftOuterJoin``, ``addRightOuterJoin`` or ``addFullOuterJoin``.

On models, don't forget to add ``@ForeignKey``s. In DAO, don't forget to change

```kotlin
@RawQuery
fun getList(query : SupportSQLiteQuery) : List<Restaurant>
```

to

```kotlin
@RawQuery
fun getList(query : SupportSQLiteQuery) : List<RestaurantJoin>
```

### How to project columns from tables <a name="project"></a>

```kotlin
override fun projection(clauses : QueryProjectionClauses): String
{
    clauses.addAllFieldsFromTable(FS.Restaurant)

    clauses.addField(FS.City_name,
        fromTable = FS.City,
        projectAs = FS.RestaurantJoin_cityName)

    clauses.addField(FS.Country_name,
        fromTable = FS.Country,
        projectAs = FS.RestaurantJoin_countryName)

    return clauses.merge()
}
```

Result:

```sql
select Restaurant.*,
       City.name as cityName,
       Country.name as countryName
```

### OOP vs FP QueryBuilder styles <a name="oopfp"></a>

So far, your QueryBuilder looks something like this:

```kotlin
class DemoQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter: RestaurantFilter) : super(filter)

    override fun tableName() : String? = FS.Restaurant

    override fun where(conditions: QueryWhereConditions) : String?
    {
        conditions.addSearchConditions(search, onColumns = arrayOf(FS.Restaurant_name, FS.Restaurant_description))
        return conditions.mergeWithAnd()
    }

    override fun projection(clauses : QueryProjectionClauses): String
    {
        clauses.addAllFieldsFromTable(FS.Restaurant)
    
        clauses.addField(FS.City_name,
            fromTable = FS.City,
            projectAs = FS.RestaurantJoin_cityName)
    
        return clauses.merge()
    }

    override fun join(clauses : QueryJoinClauses) : String?
    {
        clauses.addInnerJoin(
            table = FS.City,
            column = FS.City_countryId,
            remoteTable = FS.Country,
            remoteColumn = FS.Country_id)
    
        return clauses.merge()
    }

    override fun orderBy() : String? = "${FS.Restaurant_id} asc"
}
```

If you need a more functional style, you can write the same query builder with the ``newQueryBuilder`` top level function:

```kotlin
fun RestaurantFilter.toSQLiteQuery() : SupportSQLiteQuery
{
    val filter=this

    return newQueryBuilder<RestaurantFilter>(
        tableName = FS.Restaurant,
        join = join@ { clauses ->
            clauses.addInnerJoin(
                table = FS.Restaurant,
                column = FS.Restaurant_cityId,
                remoteTable = FS.City,
                remoteColumn = FS.City_id)

            return@join clauses.merge()
        },
        projection = projection@ { clauses ->
            clauses.addAllFieldsFromTable(FS.Restaurant)

            clauses.addField(
                FS.City_name,
                fromTable = FS.City,
                projectAs = FS.RestaurantJoin_cityName)

            return@projection clauses.merge()
        },
        where = where@ { conditions ->
            if (filter.search!=null)
                conditions.addSearchConditions(filter.search, onColumns = arrayOf(FS.Restaurant_name))

            if (filter.rating!=null)
                conditions.add("${FS.Restaurant_rating} = ${filter.rating}")

            return@where conditions.mergeWithAnd()
        },
        orderBy = {
            "${FS.Restaurant_id} asc"
        }
    ).build()
}
``` 

### How to change default settings <a name="defaults"></a>

```kotlin
class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        BaseFilterDefaults.limit=10000
        QueryBuilderDefaults.isPaginationEnabled=true
    }
}
```
