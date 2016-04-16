package io.github.slothLabs.mail.imap

import java.util.Date
import javax.mail.search.AndTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.NotTerm
import javax.mail.search.OrTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SentDateTerm
import javax.mail.search.SizeTerm

/**
 * Utilities to make building `ReceivedDateTerm` instances fun and exciting. Kinda.
 */
object ReceivedDate {
    /**
     * Creates a new `ReceivedDateTerm` for dates equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = ReceivedDate eq theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `ReceivedDateTerm` for dates equal to the specified date.
     */
    infix fun eq(date: Date) = ReceivedDateTerm(ComparisonTerm.EQ, date)

    /**
     * Creates a new `ReceivedDateTerm` for dates not equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = ReceivedDate ne theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `ReceivedDateTerm` for dates not equal to the specified date.
     */
    infix fun ne(date: Date) = ReceivedDateTerm(ComparisonTerm.NE, date)

    /**
     * Creates a new `ReceivedDateTerm` for dates less than the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = ReceivedDate lt theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `ReceivedDateTerm` for dates less than the specified date.
     */
    infix fun lt(date: Date) = ReceivedDateTerm(ComparisonTerm.LT, date)

    /**
     * Creates a new `ReceivedDateTerm` for dates less than or equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = ReceivedDate le theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `ReceivedDateTerm` for dates less than or equal to the specified date.
     */
    infix fun le(date: Date) = ReceivedDateTerm(ComparisonTerm.LE, date)

    /**
     * Creates a new `ReceivedDateTerm` for dates greater than the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = ReceivedDate gt theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `ReceivedDateTerm` for dates greater than the specified date.
     */
    infix fun gt(date: Date) = ReceivedDateTerm(ComparisonTerm.GT, date)

    /**
     * Creates a new `ReceivedDateTerm` for dates greater than or equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = ReceivedDate ge theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `ReceivedDateTerm` for dates greater than or equal to the specified date.
     */
    infix fun ge(date: Date) = ReceivedDateTerm(ComparisonTerm.GE, date)

    /**
     * Creates an `AndTerm` for items with a received date greater than or equal to the starting value
     * of the date range, and less than or equal to the end value of the date range. Basic usage would be:
     *
     * ```
     *     val theDates = getSomeStartDate() .. getSomeEndDate()
     *     val term = ReceivedDate between theDates
     *
     *     // or even...
     *     val term = ReceivedDate between getSomeStartDate() .. getSomeEndDate()
     * ```
     *
     * @param range the date range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the date range.
     */
    infix fun between(range: ClosedRange<Date>) = (this ge range.start) and (this le range.endInclusive)
}

/**
 * Utilities to make building `SentDateTerm` instances fun and exciting. Kinda.
 */
object SentDate {
    /**
     * Creates a new `SentDateTerm` for dates equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = SentDate eq theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `SentDateTerm` for dates equal to the specified date.
     */
    infix fun eq(date: Date) = SentDateTerm(ComparisonTerm.EQ, date)

    /**
     * Creates a new `SentDateTerm` for dates not equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = SentDate ne theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `SentDateTerm` for dates not equal to the specified date.
     */
    infix fun ne(date: Date) = SentDateTerm(ComparisonTerm.NE, date)

    /**
     * Creates a new `SentDateTerm` for dates less than the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = SentDate lt theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `SentDateTerm` for dates less than the specified date.
     */
    infix fun lt(date: Date) = SentDateTerm(ComparisonTerm.LT, date)

    /**
     * Creates a new `SentDateTerm` for dates less than or equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = SentDate le theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `SentDateTerm` for dates less than or equal to the specified date.
     */
    infix fun le(date: Date) = SentDateTerm(ComparisonTerm.LE, date)

    /**
     * Creates a new `SentDateTerm` for dates greater than the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = SentDate gt theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `SentDateTerm` for dates greater than the specified date.
     */
    infix fun gt(date: Date) = SentDateTerm(ComparisonTerm.GT, date)

    /**
     * Creates a new `SentDateTerm` for dates greater than or equal to the specified date. Basic usage would
     * be:
     *
     * ```
     *     val theDate = Date()
     *     val term = SentDate ge theDate
     * ```
     *
     * @param date the date for comparison
     *
     * @return a new `SentDateTerm` for dates greater than or equal to the specified date.
     */
    infix fun ge(date: Date) = SentDateTerm(ComparisonTerm.GE, date)

    /**
     * Creates an `AndTerm` for items with a received date greater than or equal to the starting value
     * of the date range, and less than or equal to the end value of the date range. Basic usage would be:
     *
     * ```
     *     val theDates = getSomeStartDate() .. getSomeEndDate()
     *     val term = SentDate between theDates
     *
     *     // or even...
     *     val term = SentDate between getSomeStartDate() .. getSomeEndDate()
     * ```
     *
     * @param range the date range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the date range.
     */
    infix fun between(range: ClosedRange<Date>) = (this ge range.start) and (this le range.endInclusive)
}

/**
 * Utilities to make building `SizeTerm` instances fun and exciting. Kinda.
 */
object Size {
    /**
     * Creates a new `SizeTerm` for sizes equal to the specified size. Basic usage would
     * be:
     *
     * ```
     *     val theSize = Size()
     *     val term = Size eq theSize
     * ```
     *
     * @param size the size for comparison
     *
     * @return a new `SizeTerm` for sizes equal to the specified size.
     */
    infix fun eq(size: Int) = SizeTerm(ComparisonTerm.EQ, size)

    /**
     * Creates a new `SizeTerm` for sizes not equal to the specified size. Basic usage would
     * be:
     *
     * ```
     *     val theSize = Size()
     *     val term = Size ne theSize
     * ```
     *
     * @param size the size for comparison
     *
     * @return a new `SizeTerm` for sizes not equal to the specified size.
     */
    infix fun ne(size: Int) = SizeTerm(ComparisonTerm.NE, size)

    /**
     * Creates a new `SizeTerm` for sizes less than the specified size. Basic usage would
     * be:
     *
     * ```
     *     val theSize = Size()
     *     val term = Size lt theSize
     * ```
     *
     * @param size the size for comparison
     *
     * @return a new `SizeTerm` for sizes less than the specified size.
     */
    infix fun lt(size: Int) = SizeTerm(ComparisonTerm.LT, size)

    /**
     * Creates a new `SizeTerm` for sizes less than or equal to the specified size. Basic usage would
     * be:
     *
     * ```
     *     val theSize = Size()
     *     val term = Size le theSize
     * ```
     *
     * @param size the size for comparison
     *
     * @return a new `SizeTerm` for sizes less than or equal to the specified size.
     */
    infix fun le(size: Int) = SizeTerm(ComparisonTerm.LE, size)

    /**
     * Creates a new `SizeTerm` for sizes greater than the specified size. Basic usage would
     * be:
     *
     * ```
     *     val theSize = Size()
     *     val term = Size gt theSize
     * ```
     *
     * @param size the size for comparison
     *
     * @return a new `SizeTerm` for sizes greater than the specified size.
     */
    infix fun gt(size: Int) = SizeTerm(ComparisonTerm.GT, size)

    /**
     * Creates a new `SizeTerm` for sizes greater than or equal to the specified size. Basic usage would
     * be:
     *
     * ```
     *     val theSize = Size()
     *     val term = Size ge theSize
     * ```
     *
     * @param size the size for comparison
     *
     * @return a new `SizeTerm` for sizes greater than or equal to the specified size.
     */
    infix fun ge(size: Int) = SizeTerm(ComparisonTerm.GE, size)

    /**
     * Creates an `AndTerm` for items with a received size greater than or equal to the starting value
     * of the size range, and less than or equal to the end value of the size range. Basic usage would be:
     *
     * ```
     *     val theSizes = getSomeStartSize() .. getSomeEndSize()
     *     val term = Size between theSizes
     *
     *     // or even...
     *     val term = Size between getSomeStartSize() .. getSomeEndSize()
     * ```
     *
     * @param range the size range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the size range.
     */
    infix fun between(range: ClosedRange<Int>) = (this ge range.start) and (this le range.endInclusive)
}

/**
 * Creates a new `AndTerm` applied to the `this` term and the parameter term.
 *
 * @param other the other `SearchTerm` to "and" against.
 *
 * @return an `AndTerm` referring to `this` and `other`.
 */
infix fun SearchTerm.and(other: SearchTerm): SearchTerm = AndTerm(this, other)

/**
 * Creates a new `OrTerm` applied to the `this` term and the parameter term.
 *
 * @param other the other `SearchTerm` to "or" against.
 *
 * @return an `OrTerm` referring to `this` and `other`.
 */
infix fun SearchTerm.or(other: SearchTerm): SearchTerm = OrTerm(this, other)

/**
 * Creates a new `NotTerm` applied to `this` term.
 *
 * @return a `NotTerm` referring to `this`.
 */
operator fun SearchTerm.not(): SearchTerm = if (this is NotTerm) term else NotTerm(this)

/**
 * Creates a new `AndTerm` applied to the `this` term and the parameter term.
 *
 * @param other the other `SearchTerm` to "and" against.
 *
 * @return an `AndTerm` referring to `this` and `other`.
 */
operator fun SearchTerm.plus(other: SearchTerm) = AndTerm(this, other)