package com.example.englishapp.model.data.grammar

import com.google.gson.annotations.SerializedName
import java.lang.Exception


data class ItemInfo(
    @SerializedName("content")val contents: List<Any>?, //List<LinkedTreeMap<String, String>> || List<Content>
    val header: String?,
    @SerializedName("type") val typeString: String
) {
    val type: Type
        get() = when (typeString) {
            "text" -> Type.TEXT
            "table_one_column" -> Type.TABLE_ONE_COLUMN
            "header" ->  Type.HEADER
            "table_two_columns" ->Type.TABLE_TWO_COLUMNS
            "text_example" -> Type.TEXT_EXAMPLE
            else -> throw  Exception("illegal type")
        }

    enum class Type {
        TEXT, TABLE_ONE_COLUMN,HEADER,TABLE_TWO_COLUMNS, TEXT_EXAMPLE
    }
}