package io.github.slothLabs.mail.util

sealed class Result<T> {
    abstract val success: Boolean

    abstract val failure: Boolean

    abstract fun ifPresent(action: T.() -> Unit)

    companion object {
        fun <T> success(value: T): Result<T> {
            return Success(value)
        }

        fun <T> failure(message: String): Result<T> {
            return Failure(message)
        }
    }

    private class Success<T>(private val value: T) : Result<T>() {
        override val success: Boolean
            get() = true

        override val failure: Boolean
            get() = false

        override fun ifPresent(action: T.() -> Unit) {
            value.action()
        }
    }

    open class Failure<T>(private val message: String) : Result<T>() {
        override fun ifPresent(action: T.() -> Unit) { }

        override val success: Boolean
            get() = false

        override val failure: Boolean
            get() = true
    }
}