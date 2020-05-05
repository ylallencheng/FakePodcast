package com.ylallencheng.fakepodcast.io.model

/**
 * The enum class of network status
 */
enum class Status {

    /**
     * Indicate the request is in loading state
     */
    LOADING,

    /**
     * Indicate the request has been successful
     */
    SUCCESS,

    /**
     * Indicate the request has bee failed for some reason
     */
    FAILED
}

/**
 * The wrapper class for request status.
 *
 * @property status The status of the network request
 * @property errorMessage The error message to indicate the reason of failed request
 */
//data class IOStatus<T>(
//    val status: Status,
//    val data: T? = null,
//    val errorMessage: String? = null
//) {
//    companion object {
//
//        /**
//         * Get a [Status.SUCCESS] request status
//         */
//        fun <T> success(data: T?) = IOStatus<T>(status = Status.SUCCESS, data = data)
//
//        /**
//         * Get a [Status.LOADING] request status
//         */
//        fun <T> loading() = IOStatus<T>(status = Status.LOADING)
//
//        /**
//         * Get a [Status.FAILED] request status with error message
//         *
//         * @param msg The error message
//         */
//        fun <T> failed(msg: String) = IOStatus<T>(status = Status.FAILED, errorMessage = msg)
//    }
//}

sealed class IOStatus<T>(
    val status: Status,
    val data: T? = null,
    val errorMessage: String? = null
) {

    class Loading<T> : IOStatus<T>(
        status = Status.LOADING
    )

    class Successful<T>(data: T?) : IOStatus<T>(
        status = Status.SUCCESS,
        data = data
    )

    class Failed<T>(errorMessage: String?) : IOStatus<T>(
        status = Status.FAILED,
        errorMessage = errorMessage
    )
}