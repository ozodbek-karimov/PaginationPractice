package pl.ozodbek.paginationpractice.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FakeStoreUsers(
    @SerialName("avatar")
    val avatar: String,
    @SerialName("creationAt")
    val creationAt: String,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String,
    @SerialName("role")
    val role: String,
    @SerialName("updatedAt")
    val updatedAt: String
)