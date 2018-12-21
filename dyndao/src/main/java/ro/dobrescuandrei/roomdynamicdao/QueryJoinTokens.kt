package ro.dobrescuandrei.roomdynamicdao

import java.util.*

class QueryJoinTokens : LinkedList<String>()
{
    fun innerJoin      (remoteTableName : String, remoteTableColumn : String, table : String, column : String) = join("inner",       remoteTableName, remoteTableColumn, table, column)
    fun crossJoin      (remoteTableName : String, remoteTableColumn : String, table : String, column : String) = join("cross",       remoteTableName, remoteTableColumn, table, column)
    fun leftOuterJoin  (remoteTableName : String, remoteTableColumn : String, table : String, column : String) = join("left outer",  remoteTableName, remoteTableColumn, table, column)
    fun rightOuterJoin (remoteTableName : String, remoteTableColumn : String, table : String, column : String) = join("right outer", remoteTableName, remoteTableColumn, table, column)
    fun fullOuterJoin  (remoteTableName : String, remoteTableColumn : String, table : String, column : String) = join("full outer",  remoteTableName, remoteTableColumn, table, column)

    private fun join(type : String, remoteTableName : String, remoteTableColumn : String, table : String, column : String)
    {
        add(" $type join $remoteTableName on $table.$column = $remoteTableName.$remoteTableColumn")
    }

    fun build() : String? =
        if (isEmpty()) null
        else SQLEscape.tokens(this, delimitedBy = " ")
}