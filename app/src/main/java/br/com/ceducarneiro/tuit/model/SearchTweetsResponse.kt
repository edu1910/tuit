package br.com.ceducarneiro.tuit.model

import com.google.gson.annotations.SerializedName

data class SearchTweetsResponse(
    val statuses: List<Tweet>,
    val search_metadata: Metadata?
) {
    data class Metadata(
        @field:SerializedName("since_id_str") val since: String?,
        @field:SerializedName("next_results") val next: String?,
        @field:SerializedName("refresh_url") val refresh: String?,
        val count: Int,
    )
}