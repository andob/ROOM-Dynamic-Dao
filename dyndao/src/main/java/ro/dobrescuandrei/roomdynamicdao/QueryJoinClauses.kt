package ro.dobrescuandrei.roomdynamicdao

import java.util.*

class QueryJoinClauses : LinkedList<String>()
{
    fun addInnerJoin      (remoteTable : String, remoteColumn : String, table : String, column : String) = join("inner",       remoteTable, remoteColumn, table, column)
    fun addCrossJoin      (remoteTable : String, remoteColumn : String, table : String, column : String) = join("cross",       remoteTable, remoteColumn, table, column)
    fun addLeftOuterJoin  (remoteTable : String, remoteColumn : String, table : String, column : String) = join("left outer",  remoteTable, remoteColumn, table, column)
    fun addRightOuterJoin (remoteTable : String, remoteColumn : String, table : String, column : String) = join("right outer", remoteTable, remoteColumn, table, column)
    fun addFullOuterJoin  (remoteTable : String, remoteColumn : String, table : String, column : String) = join("full outer",  remoteTable, remoteColumn, table, column)

    private fun join(type : String, remoteTable : String, remoteColumn : String, table : String, column : String)
    {
        add(" $type join $remoteTable on $table.$column = $remoteTable.$remoteColumn")
    }

    fun merge() =
        if (isNotEmpty())
            this.joinToString(separator = " ")
        else null
}