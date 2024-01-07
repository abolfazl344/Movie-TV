package ir.abolfazl.abolmovie.model.Local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val username : String,
    val email : String,
    val password : String
) : Parcelable
