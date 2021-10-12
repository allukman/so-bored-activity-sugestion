package id.smartech.sobored.ui.cat.model

import com.google.gson.annotations.SerializedName

data class CatModel (
    @SerializedName("id") val id : String,
    @SerializedName("url") val url : String,
    @SerializedName("width") val width : Int,
    @SerializedName("height") val height : Int
)