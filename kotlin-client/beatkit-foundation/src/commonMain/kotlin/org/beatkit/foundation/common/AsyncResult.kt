package org.beatkit.foundation.common

/**
 * Result of an asynchronous operation, used for callback based (Swift friendly) APIs.
 */
sealed class AsyncResult<out R : Any> {
    data class Success<out R : Any>(val value: R) : AsyncResult<R>()
    data class Failure(val error: Throwable) : AsyncResult<Nothing>()

    val valueOrNull: R?
        get() = (this as? Success<R>)?.value

    val errorOrNull: Throwable?
        get() = (this as? Failure)?.error
}
