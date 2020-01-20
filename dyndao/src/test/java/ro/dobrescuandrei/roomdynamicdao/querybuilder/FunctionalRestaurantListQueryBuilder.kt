package ro.dobrescuandrei.roomdynamicdao.querybuilder

import androidx.sqlite.db.SupportSQLiteQuery
import com.yatatsu.fieldschema.FS
import ro.dobrescuandrei.roomdynamicdao.model.RestaurantFilter
import ro.dobrescuandrei.roomdynamicdao.newQueryBuilder

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
