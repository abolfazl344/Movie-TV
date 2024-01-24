package ir.abolfazl.abolmovie.model.Local

data class Trailer(
    val id: Int,
    val results: List<Result>? = null
) {
    data class Result(
        val key: String,
        val name: String,
        val size: Int
    )
}
