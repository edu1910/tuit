package br.com.ceducarneiro.tuit.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Tweet(
    @field:SerializedName("id_str") val id: String?,
    @field:SerializedName("text") val text: String?,
    @field:SerializedName("user") val user: User?,
    @field:SerializedName("created_at") val date: String?,
    @field:SerializedName("entities") val entities: Entities?,
) {
    data class Entities(
        @field:SerializedName("media") val media: List<Media>?,
    ) {
        data class Media(
            @field:SerializedName("media_url_https") val url: String?,
            @field:SerializedName("type") val type: String?,
        )
    }

    data class User(
        @field:SerializedName("profile_image_url_https") val avatar: String?,
        @field:SerializedName("profile_banner_url") val cover: String?,
        @field:SerializedName("name") val name: String?,
        @field:SerializedName("screen_name") val username: String?,
        @field:SerializedName("description") val description: String?,
    ) : Serializable
}