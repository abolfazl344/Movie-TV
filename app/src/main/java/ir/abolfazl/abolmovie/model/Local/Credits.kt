package ir.abolfazl.abolmovie.model.Local

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class Credits(
    @SerializedName("cast")
    val cast: List<Cast>? = null,
    @SerializedName("crew")
    val crew: List<Crew>? = null,
    @SerializedName("id")
    val id: Int?
) : Parcelable {
    @Parcelize
    data class Cast(
        @SerializedName("cast_id")
        val castId: Int?,
        @SerializedName("character")
        val character: String?,
        @SerializedName("credit_id")
        val creditId: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("known_for_department")
        val knownForDepartment: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("popularity")
        val popularity: Double?,
        @SerializedName("profile_path")
        val profilePath: String?
    ) : Parcelable

    @Parcelize
    data class Crew(
        @SerializedName("credit_id")
        val creditId: String?,
        @SerializedName("department")
        val department: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("job")
        val job: String?,
        @SerializedName("known_for_department")
        val knownForDepartment: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("popularity")
        val popularity: Double?,
        @SerializedName("profile_path")
        val profilePath: String?
    ) : Parcelable
}