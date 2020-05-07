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