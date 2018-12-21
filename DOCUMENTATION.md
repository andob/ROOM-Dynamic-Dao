# Room Dynamic DAO

## Documentation

### Table of contents

### QueryBuilder methods overview

```kotlin
class DemoQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(search: String?, filter: RestaurantFilter?, limit: Int = Int.MAX_VALUE, offset: Int = 0) : super(search, filter, limit, offset)

    //used to specify table name
    override fun tableName(): String? = FS.Restaurant

    //used to specify where conditions
    override fun where(tokens: QueryWhereTokens): String? = "1=1"

    //enable pagination. By default, pagination is DISABLED
    override fun enablePagination() : Boolean = true

    //column projections
    override fun projection(tokens: QueryProjectionTokens): String = "*"

    //use for joins
    override fun join(tokens: QueryJoinTokens): String? = null

    //specify order by
    override fun orderBy(): String? = "${FS.Restaurant_id} asc"
}
```

### How to build where conditions

Use ``addSearchTokens`` for search conditions:

```kotlin
override fun where(tokens: QueryWhereTokens) : String?
{
    tokens.addSearchTokens(search, onColumns = arrayOf(FS.Restaurant_name, FS.Restaurant_description))
    return tokens.and()
}
```

The result will be ``where name like '%search%' or description like '%search%'``

Use ``and`` to generate a where condition set delimited by and:

```kotlin
override fun where(tokens: QueryWhereTokens) : String?
{
    tokens.addSearchTokens(search, onColumns = arrayOf(FS.Restaurant_name, FS.Restaurant_description))
    tokens.add("${FS.Restaurant_id} = 1")
    tokens.add("${FS.Restaurant_rating} = 5")
    return tokens.and()
}
```

Result: ``where (name like '%search%' or description like '%search%') and id = 1 and rating = 5``

If you: ``return tokens.or()``

Result: ``where (name like '%search%' or description like '%search%') or id = 1 or rating = 5``

You can also create complex conditions with paranthesis:

```kotlin
override fun where(tokens: QueryWhereTokens) : String?
{
    val subtokens1=QueryWhereTokens()
    subtokens1.add("${FS.Restaurant_id} = 1")
    subtokens1.add("${FS.Restaurant_rating} = 5")
    tokens.add("(${subtokens1.and()})")

    val subtokens2=QueryWhereTokens()
    subtokens2.add("${FS.Restaurant_id} = 2")
    subtokens2.add("${FS.Restaurant_rating} = 3")
    tokens.add("(${subtokens2.and()})")
    
    return tokens.or()
}
```

Result: ``where (id = 1 and rating = 5) or (id = 2 and rating = 3)``

Don't forget to ESCAPE STRINGS:

``tokens.add("${FS.Restaurant_name} = '${SQLEscape.string("asdf")}'")``

Result: ``name = 'asdf'``

Or string arrays or collections:

``tokens.add("${FS.Restaurant_name} in (${SQLEscape.stringArray(arrayOf("asdf", "qwerty"))})")``

Result: ``name in ('asdf','qwerty')``

Or number arrays or collections:

``tokens.add("${FS.Restaurant_id} in (${SQLEscape.numberArray(intArrayOf(1, 5))})")``

Result: ``id in (1, 5)``

### How to join other tables

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
override fun join(tokens: QueryJoinTokens) : String?
{
    tokens.innerJoin(
        table = FS.Restaurant,
        column = FS.Restaurant_cityId,
        remoteTableName = FS.City,
        remoteTableColumn = FS.City_id)

    tokens.innerJoin(
        table = FS.City,
        column = FS.City_countryId,
        remoteTableName = FS.Country,
        remoteTableColumn = FS.Country_id)

    return tokens.build()
}
```

Result:

```sql
from Restaurant inner join City    on City.id    = Restaurant.cityId 
                inner join Country on Country.id = City.countryId
```

You can also use ``crossJoin``, ``leftOuterJoin``, ``rightOuterJoin`` or ``fullOuterJoin``.

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

### How to project columns from tables

```kotlin
override fun projection(tokens: QueryProjectionTokens): String
{
    tokens.allFieldsFromTable(FS.Restaurant)

    tokens.field(FS.City_name,
        fromTable = FS.City,
        projectAs = FS.RestaurantJoin_cityName)

    tokens.field(FS.Country_name,
        fromTable = FS.Country,
        projectAs = FS.RestaurantJoin_countryName)

    return tokens.build()
}
```

Result:

```sql
select Restaurant.*,
       City.name as cityName,
       Country.name as countryName
```