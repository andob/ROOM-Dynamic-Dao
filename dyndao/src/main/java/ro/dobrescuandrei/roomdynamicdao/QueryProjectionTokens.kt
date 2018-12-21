package ro.dobrescuandrei.roomdynamicdao

import java.util.*

class QueryProjectionTokens : LinkedList<String>()
{
    fun allFieldsFromTable(tableName : String) : QueryProjectionTokens
    {
        add("$tableName.*")
        return this
    }

    fun field(fieldName : String, fromTable : String, projectAs : String) : QueryProjectionTokens
    {
        add("$fromTable.$fieldName as $projectAs")
        return this
    }

    fun build() : String =
        if (isEmpty()) "*"
        else SQLEscape.tokens(this, delimitedBy = " , ")
}
