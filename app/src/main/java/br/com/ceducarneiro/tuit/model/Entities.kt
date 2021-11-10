package br.com.ceducarneiro.tuit.model

import com.google.gson.annotations.SerializedName

data class Entities(
    @field:SerializedName("media") val media: List<Media>?,
) {
    data class Media(
        @field:SerializedName("media_url_https") val url: String?,
        @field:SerializedName("type") val type: String?,
    )
}