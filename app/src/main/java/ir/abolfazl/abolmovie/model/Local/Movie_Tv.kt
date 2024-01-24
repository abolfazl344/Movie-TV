package ir.abolfazl.abolmovie.model.Local


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Movie_Tv(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
) : Parcelable {
    @Parcelize
    data class Result(
        @SerializedName("id")
        val id: Int,
        @SerializedName("backdrop_path")
        val backdropPath: String? = null,
        @SerializedName("original_language")
        val originalLanguage: String?,
        @SerializedName("overview")
        val overview: String?,
        @SerializedName("popularity")
        val popularity: Double?,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("release_date")
        val releaseDate: String? = null,
        @SerializedName("first_air_date")
        val firstAirDate: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("vote_average")
        val voteAverage: Double?,
    ) : Parcelable
}