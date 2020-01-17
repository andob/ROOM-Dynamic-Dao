package ro.dobrescuandrei.roomdynamicdao

import java.util.*

class QueryProjectionClauses : LinkedList<String>()
{
    fun addAllFieldsFromTable(tableName : String) : QueryProjectionClauses
    {
        add("$tableName.*")
        return this
    }

    fun addField(fieldName : String, fromTable : String, projectAs : String) : QueryProjectionClauses
    {
        add("$fromTable.$fieldName as $projectAs")
        return this
    }

    fun merge() : String =
        if (isNotEmpty())
            this.joinToString(separator = " , ")
        else "*"
}
