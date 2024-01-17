package ir.abolfazl.abolmovie.model.Local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val email : String,
    val password : String,
    val username : String
) : Parcelable
