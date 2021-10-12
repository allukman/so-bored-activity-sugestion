package id.smartech.sobored.ui.jokes.model

data class JokesModel (
    val error: Boolean,
    val category: String?,
    val type: String?,
    val setup: String?,
    val delivery: String?,
    val joke: String?,
    val id: Int?,
    val lang: String?
)