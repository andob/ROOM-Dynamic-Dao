package ro.dobrescuandrei.roomdynamicdao

import java.util.*

class QueryJoinTokens : LinkedList<String>()
{
    fun innerJoin      (remoteTable : String, remoteColumn : String, table : String, column : String) = join("inner",       remoteTable, remoteColumn, table, column)
    fun crossJoin      (remoteTable : String, remoteColumn : String, table : String, column : String) = join("cross",       remoteTable, remoteColumn, table, column)
    fun leftOuterJoin  (remoteTable : String, remoteColumn : String, table : String, column : String) = join("left outer",  remoteTable, remoteColumn, table, column)
    fun rightOuterJoin (remoteTable : String, remoteColumn : String, table : String, column : String) = join("right outer", remoteTable, remoteColumn, table, column)
    fun fullOuterJoin  (remoteTable : String, remoteColumn : String, table : String, column : String) = join("full outer",  remoteTable, remoteColumn, table, column)

    private fun join(type : String, remoteTable : String, remoteColumn : String, table : String, column : String)
    {
        add(" $type join $remoteTable on $table.$column = $remoteTable.$remoteColumn")
    }

    fun build() : String? =
        if (isEmpty()) null
        else SQLEscape.tokens(this, delimitedBy = " ")
}