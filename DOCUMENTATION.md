# Room Dynamic DAO

## Documentation

### Table of contents

1. [QueryBuilder methods overview](#overview)
2. [How to build where conditions](#where)
3. [How to join other tables](#join)
4. [How to project columns from tables](#project)
6. [How to change default settings](#defaults)
7. [Custom table / column names and FieldSchema](#fsvsts)
8. [Best practices](#bestpractices)

### QueryBuilder methods overview <a name="overview"></a>

```kotlin
class DemoQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter : RestaurantFilter) : super(filter)

    //used to specify table name
    override fun tableName() : String? = TS.Restaurant

    //used to specify where conditions
    override fun where(conditions : QueryWhereConditions) : String? = "1=1"

    //enable pagination. By default, pagination is DISABLED
    //to change this default behavior, see How to change default settings section
    override fun isPaginationEnabled() : Boolean = true

    //column projections
    override fun projection(clauses : QueryProjectionClauses) : String = "*"

    //use for joins
    override fun join(clauses : QueryJoinClauses) : String? = null

    //specify order by
    override fun orderBy() : String? = "${TS.Restaurant_id} asc"
}
```

### How to build where conditions <a name="where"></a>

Use ``addSearchConditions`` on search conditions:

```kotlin
override fun where(conditions : QueryWhereConditions) : String?
{
    conditions.addSearchConditions(search, onColumns = arrayOf(TS.Restaurant_name, TS.Restaurant_description))
    return conditions.mergeWithAnd()
}
```

The result will be ``where name like '%search%' or description like '%search%'``

Use ``mergeWithAnd`` to generate a where condition set delimited by and:

```kotlin
override fun where(conditions : QueryWhereConditions) : String?
{
    conditions.addSearchConditions(search, onColumns = arrayOf(TS.Restaurant_name, TS.Restaurant_description))
    conditions.add("${TS.Restaurant_id} = 1")
    conditions.add("${TS.Restaurant_rating} = 5")
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
    val firstConditionSet = QueryWhereConditions()
    firstConditionSet.add("${TS.Restaurant_id} = 1")
    firstConditionSet.add("${TS.Restaurant_rating} = 5")
    conditions.add("(${firstConditionSet.mergeWithAnd()})")

    val secondConditionSet = QueryWhereConditions()
    secondConditionSet.add("${TS.Restaurant_id} = 2")
    secondConditionSet.add("${TS.Restaurant_rating} = 3")
    conditions.add("(${secondConditionSet.mergeWithAnd()})")
    
    return conditions.mergeWithOr()
}
```

Result: ``where (id = 1 and rating = 5) or (id = 2 and rating = 3)``

### Don't forget to ESCAPE STRINGS:

``conditions.add("${TS.Restaurant_name} = ${"asdf".sqlEscaped}")``

Result: ``name = 'asdf'``

Or string arrays or collections:

``conditions.add("${TS.Restaurant_name} in ${arrayOf("asdf", "qwerty").sqlEscaped}")``

Result: ``name in ('asdf','qwerty')``

Or number arrays or collections:

``conditions.add("${TS.Restaurant_id} in ${intArrayOf(1, 5).sqlEscaped}")``

Result: ``id in (1, 5)``

Or booleans:

``conditions.add("${TS.Restaurant_isActive} = ${true.sqlEscaped}")``

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
    @NotNull
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
    @NotNull
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

Then, in your QueryBuilder class:

```kotlin
override fun join(clauses : QueryJoinClauses) : String?
{
    clauses.addInnerJoin(
        table = TS.Restaurant,
        column = TS.Restaurant_cityId,
        remoteTable = TS.City,
        remoteColumn = TS.City_id)

    clauses.addInnerJoin(
        table = TS.City,
        column = TS.City_countryId,
        remoteTable = TS.Country,
        remoteColumn = TS.Country_id)

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
    clauses.addAllFieldsFromTable(TS.Restaurant)

    clauses.addField(TS.City_name,
        fromTable = TS.City,
        projectAs = TS.RestaurantJoin_cityName)

    clauses.addField(TS.Country_name,
        fromTable = TS.Country,
        projectAs = TS.RestaurantJoin_countryName)

    return clauses.merge()
}
```

Result:

```sql
select Restaurant.*,
       City.name as cityName,
       Country.name as countryName
```

### How to change default settings <a name="defaults"></a>

```kotlin
class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        BaseFilterDefaults.limit = 10000
        QueryBuilderDefaults.isPaginationEnabled = true
    }
}
```

### Custom table / column names and FieldSchema <a name="fsvsts"></a>

The [FieldSchema](https://github.com/andob/FieldSchema) annotation processor library generates constants for classes annotated with ``@FieldSchemaClass``. This library will generate two classes: ``FS``, containing the schema based on class and class fields names, and ``TS``, containing the database schema (table names and table column names).

**If you are using custom table or column names (if table names differ from class names or column names differ from field names), please use ``TS.*`` instead of ``FS.*``**; for instance:

```kotlin
@Entity(tableName = "CitiesTable")
@FieldSchemaClass
class City
{
    @PrimaryKey
    @NotNull
    @ColumnInfo
    val id : Int = 0

    @ColumnInfo
    val name : String = ""

    @ColumnInfo(name = "country_id")
    val countryId : Int = 0
}
```

```kotlin
val sql1="select * from ${FS.City} where ${FS.City_countryId} = 3 or ${FS.City_id} = 4"
//result: select * from City where countryId = 3 or id = 4 <-- incorrect sql
val sql2="select * from ${TS.City} where ${TS.City_countryId} = 3 or ${TS.City_id} = 4"
//result: select * from CitiesTable where country_id = 3 or id = 4 <-- correct sql
```

```kotlin
@Entity
@FieldSchemaClass
class City
{
    @PrimaryKey
    @NotNull
    @ColumnInfo
    val id : Int = 0

    @ColumnInfo
    val name : String = ""

    @ColumnInfo
    val countryId : Int = 0
}
```

```kotlin
val sql1="select * from ${FS.City} where ${FS.City_countryId} = 3 or ${FS.City_id} = 4"
//result: select * from City where countryId = 3 or id = 4 <-- correct sql
val sql2="select * from ${TS.City} where ${TS.City_countryId} = 3 or ${TS.City_id} = 4"
//result: select * from City where countryId = 3 or id = 4 <-- correct sql
```

### Best practices <a name="bestpractices"></a>

1. For each domain model, create a filter model, a DAO interface and a QueryBuilder class (ex: ``Restaurant``, ``RestaurantFilter``, ``RestaurantDao``, ``RestaurantListQueryBuilder``, ``Cat``, ``CatFilter``, ``CatDao``, ``CatListQueryBuilder`` and so on).
2. Use QueryBuilder classes only to transform the filter into a select query returning a list of entities (ex: ``ResturantFilter`` is transformed into a query via ``RestaurantListQueryBuilder``, executed with ``RestaurantDao`` which gives us a ``List<Restaurant>``)
3. All other queries, such as inserts, updates, deletions, selection of a single object or complex report-style select queries must be written as DAO methods with ``@Query``, ``@Update``, ``@Insert``, ``@Delete`` annotations (in ROOM's standard way).
4. In order to prevent SQL injection, don't forget to escape string values!!!
5. Always use ``TS.*`` constants if you are using custom names for tables or columns. If your table names are identical to model classes names and column names are identical to model class's field names, use ``FS.*``.
