package io.github.slothLabs.mail.imap

import com.sun.mail.imap.SortTerm
import org.funktionale.option.toOption

/**
 * Builder class to make sorting messages a little easier to work with.
 */
class SortBuilder {

    private val sortTerms = mutableListOf<Sort>()

    /**
     * Gets all of the sort terms built with this instance.
     *
     * @return a `List` of [Sort] instances built with this builder.
     */
    fun build() : List<Sort> = sortTerms

    /**
     * Adds the specified [Sort] term to this builder.
     *
     * @param sort the [Sort] term to add.
     */
    fun add(sort: Sort) = sortTerms.add(sort)

    /**
     * Adds the specified `SortTerm` to this builder.
     *
     * @param sort the `SortTerm` to add.
     */
    fun add(sort: SortTerm) = Sort.fromSortTerm(sort).map { add(it) }

    /**
     * Shorthand to add a `SortTerm` to this builder.
     */
    operator fun SortTerm.unaryPlus() {
        add(this)
    }

    /**
     * Shorthand to add a Reversed `SortTerm` to this builder.
     */
    operator fun SortTerm.unaryMinus() {
        add(Sort.Reverse)
        add(this)
    }

    /**
     * Shorthand to add a [Sort] to this builder.
     */
    operator fun Sort.unaryPlus() {
        add(this)
    }

    /**
     * Shorthand to add a reversed [Sort] to this builder.
     */
    operator fun Sort.unaryMinus() {
        add(Sort.Reverse)
        add(this)
    }
}

/**
 * Enum to wrap the standard JavaMail `SortTerm` constants.
 */
enum class Sort(private val javaMailSortTerm: SortTerm) {
    /**
     * Indicates sorting by the first from address.
     */
    From(SortTerm.FROM),

    /**
     * Indicates sorting by the first TO recipient address.
     */
    To(SortTerm.TO),

    /**
     * Indicates sorting by the message subject.
     */
    Subject(SortTerm.SUBJECT),

    /**
     * Indicates sorting by the message arrival date/time.
     */
    Arrival(SortTerm.ARRIVAL),

    /**
     * Indicates sorting by the message sent date/time (analogous with `SortTerm.DATE`).
     */
    Sent(SortTerm.DATE),

    /**
     * Indicates sorting by the first CC recipient address.
     */
    CC(SortTerm.CC),

    /**
     * Indicates to reverse the sort order of the next term in
     * the sort terms.
     */
    Reverse(SortTerm.REVERSE),

    /**
     * Indicates sorting by message size.
     */
    Size(SortTerm.SIZE);

    /**
     * Gets the associated JavaMail `SortTerm` for this value.
     */
    fun toSortTerm() = this.javaMailSortTerm

    companion object {
        /**
         * Creates an `Option<Sort>` from the given JavaMail `SortTerm`. This message *should*
         * always return a `Some<Sort>` instance; if it returns `None`, that would likely indicate
         * an unexpected (read: new) `SortTerm` was supplied and the [Sort] enum hasn't been updated
         * to reflect the change.
         *
         * @param javaMailSortTerm the `SortTerm` to find the `Sort` value for.
         *
         * @return an `Option<Sort>` that matches the specified sort term.
         */
        fun fromSortTerm(javaMailSortTerm: SortTerm) = Sort.values().find { it.javaMailSortTerm == javaMailSortTerm }.toOption()
    }
}