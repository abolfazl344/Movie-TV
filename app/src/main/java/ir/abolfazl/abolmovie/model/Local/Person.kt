package ir.abolfazl.abolmovie.model.Local

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class Person(
    @SerializedName("biography")
    val biography: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("profile_path")
    val profilePath: String?
) : Parcelable