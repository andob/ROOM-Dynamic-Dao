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

    fun addField(builder : ProjectionClauseBuilder)
    {
        addField(fieldName = builder.fieldName, fromTable = builder.fromTable, projectAs = builder.projectAs)
    }

    fun merge() : String
    {
        if (isNotEmpty())
            return this.joinToString(separator = " , ")
        return "*"
    }
}

class ProjectionClauseBuilder
{
    internal lateinit var fieldName : String
    fun fieldName(fieldName : String) = also { this.fieldName = fieldName }

    internal lateinit var fromTable : String
    fun fromTable(fromTable : String) = also { this.fromTable = fromTable }

    internal lateinit var projectAs : String
    fun projectAs(projectAs : String) = also { this.projectAs = projectAs }
}
