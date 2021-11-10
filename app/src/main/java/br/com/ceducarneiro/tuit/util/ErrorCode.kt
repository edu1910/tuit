package br.com.ceducarneiro.tuit.util

import br.com.ceducarneiro.tuit.R
import retrofit2.HttpException

class ErrorCode {

    companion object {

        fun getErrorFromNetwork(error: Throwable): Int {
            var errorResId = R.string.internet_error_msg
            if (error is HttpException) {
                when (error.code()) {
                    401 -> errorResId = R.string.token_error_msg
                    403 -> errorResId = R.string.invalid_search_error_msg
                    429 -> errorResId = R.string.limit_error_msg
                }
            }
            return errorResId
        }

    }

}