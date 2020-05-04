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
data class RequestStatus(
    val status: Status,
    val errorMessage: String? = null
) {
    companion object {

        /**
         * Get a [Status.SUCCESS] request status
         */
        fun success() = RequestStatus(Status.SUCCESS)

        /**
         * Get a [Status.LOADING] request status
         */
        fun loading() = RequestStatus(Status.LOADING)

        /**
         * Get a [Status.FAILED] request status with error message
         *
         * @param msg The error message
         */
        fun failed(msg: String) = RequestStatus(Status.FAILED, msg)
    }
}