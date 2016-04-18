package io.github.slothLabs.mail.imap

import com.sun.mail.imap.ModifiedSinceTerm
import com.sun.mail.imap.OlderTerm
import com.sun.mail.imap.YoungerTerm
import org.funktionale.option.Option
import org.funktionale.option.Option.None
import org.funktionale.option.Option.Some
import java.util.Date
import javax.mail.Flags
import javax.mail.Message
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage.RecipientType
import javax.mail.search.AndTerm
import javax.mail.search.BodyTerm
import javax.mail.search.FlagTerm
import javax.mail.search.FromStringTerm
import javax.mail.search.FromTerm
import javax.mail.search.HeaderTerm
import javax.mail.search.MessageIDTerm
import javax.mail.search.MessageNumberTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.RecipientStringTerm
import javax.mail.search.RecipientTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SentDateTerm
import javax.mail.search.SizeTerm
import javax.mail.search.SubjectTerm

/**
 * Builder class to make search terms a little easier to work with. It's
 * geared mainly towards use in tandem with the [Folder.search] method:
 *
 * ```
 *    folder("INBOX", FolderModes.ReadOnly) {
 *         preFetchBy(FetchProfileItem.MESSAGE)
 *         val results = search {
 *             withFrom(fromAddress)
 *             withSentOnOrBefore(Date())
 *         }
 *
 *         msgList.addAll(results)
 *     }
 * ```
 *
 * There are two variations of methods used to work with search terms. The first
 * set of methods are prefixed by `with` (i.e. `withFrom` as above). These methods
 * create the relevant `SearchTerm` instance and store it internally in the `SearchBuilder`.
 * They return nothing to the caller.
 *
 * The second set of methods are not prefixed by `with`, such as `from`. These return the
 * relevant `SearchTerm` instance, and do nothing inside of the the `SearchBuilder`. They're
 * mainly intended for use with the overloaded operators `+` and `-`. An equivalent version
 * of the above sample using those operators would be:
 *
 * ```
 *    folder("INBOX", FolderModes.ReadOnly) {
 *         preFetchBy(FetchProfileItem.MESSAGE)
 *         val results = search {
 *             +from(fromAddress)
 *             +sentOnOrBefore(Date())
 *         }
 *
 *         msgList.addAll(results)
 *     }
 * ```
 *
 * Functionally, there's no difference between the two; it's more a matter of developer preference.
 *
 * Various members build comparison-based search terms; for now, these take an `Int` value, defined
 * in `javax.mail.search.ComparisonTerm`. For quick reference, the terms are:
 *
 * * `ComparisonTerm.LE`: less than or equal
 * * `ComparisonTerm.LT`: less than
 * * `ComparisonTerm.EQ`: equal
 * * `ComparisonTerm.NE`: not equal
 * * `ComparisonTerm.GT`: greater than
 * * `ComparisonTerm.GE`: greater than or equal
 *
 */
class SearchBuilder {

    private val terms = mutableListOf<SearchTerm>()

    private val sortedBy = mutableListOf<Sort>()

    /**
     * Creates an `Option` containing either `None` or the combined
     * `SearchTerm` instance that results from merging all of the terms
     * created via the various build methods.
     *
     * @return an `Option` that's `None` if there were no search terms added,
     *         `Some` containing a single 'regular' search term if only one search term
     *         was added, or `Some` containing an `AndTerm` built from merging all of
     *         the added search terms.
     */
    fun build(): Option<SearchTerm> =
        if (terms.isEmpty()) None
        else if (terms.size == 1) Some(terms[0])
        else Some(terms.reduce { first, second -> AndTerm(first, second) })

    /**
     * Whether or not sort terms have been applied to the search.
     *
     * @return `true` if a sort has been applied; `false` otherwise.
     */
    fun hasSortTerms() = sortedBy.isNotEmpty()

    /**
     * Gets the sort terms (if any) that have been applied to the search.
     *
     * @return a `List` of [Sort] instances applied to the search, or an
     *         empty list if no sort has been applied.
     */
    fun getSortTerms(): List<Sort> = sortedBy

    /**
     * Creates and returns a `FromTerm` given an `InternetAddress` instance.
     *
     * @param address the `InternetAddress` to generate the `FromTerm` with.
     *
     * @return a `FromTerm` based on the specified address.
     */
    fun from(address: InternetAddress) = FromTerm(address)

    /**
     * Creates and returns a `FromStringTerm` given a string pattern.
     *
     * @param str the string pattern to generate the `FromStringTerm` with.
     *
     * @return a `FromStringTerm` based on the given string.
     */
    fun from(str: String) = FromStringTerm(str)

    /**
     * Creates a `FromTerm` given an `InternetAddress` and stores it in this `SearchBuilder`.
     *
     * @param address the `InternetAddress` to generate the `FromTerm` with.
     */
    fun withFrom(address: InternetAddress) = with(from(address))

    /**
     * Creates a `FromStringTerm` given a string pattern and stores it in this `SearchBuilder`.
     *
     * @param str the string pattern to generate the `FromStringTerm` with.
     */
    fun withFrom(str: String) = with(from(str))

    /**
     * Creates and returns a `RecipientTerm` with the specified `RecipientType` and `InternetAddress`.
     *
     * @param recipientType the `RecipientType` for the generated term.
     * @param address the `InternetAddress` for the generated term.
     *
     * @return a `RecipientTerm` based on the given `recipientType` and `address`.
     */
    fun recipient(recipientType: Message.RecipientType, address: InternetAddress) = RecipientTerm(recipientType, address)

    /**
     * Creates and returns a `RecipientTerm` with the specified `RecipientType` and string pattern.
     *
     * @param recipientType the `RecipientType` for the generated term.
     * @param str the string pattern for the generated term.
     *
     * @return a `RecipientTerm` based on the given `recipientType` and string pattern.
     */
    fun recipient(recipientType: Message.RecipientType, str: String) = RecipientStringTerm(recipientType, str)

    /**
     * Creates a `RecipientTerm` with the specified `RecipientType` and `InternetAddress` and stores it in this `SearchBuilder`.
     *
     * @param recipientType the `RecipientType` for the generated term.
     * @param address the `InternetAddress` for the generated term.
     */
    fun withRecipient(recipientType: Message.RecipientType, address: InternetAddress) = with(recipient(recipientType, address))

    /**
     * Creates a `RecipientTerm` with the specified `RecipientType` and string pattern and stores it in this `SearchBuilder`.
     *
     * @param recipientType the `RecipientType` for the generated term.
     * @param str the string pattern for the generated term.
     */
    fun withRecipient(recipientType: Message.RecipientType, str: String) = with(recipient(recipientType, str))

    /**
     * Creates and returns a `RecipientTerm` specified as `RecipientType.TO`, using the given `InternetAddress`.
     *
     * @param address the `InternetAddress` for the generated term.
     *
     * @return a `RecipientType.TO`-based `RecipientTerm` for the given `address`.
     */
    fun to(address: InternetAddress) = recipient(RecipientType.TO, address)

    /**
     * Creates and returns a `RecipientTerm` specified as `RecipientType.TO`, using the given string pattern.
     *
     * @param str the string pattern for the generated term.
     *
     * @return a `RecipientType.TO`-based `RecipientTerm` for the given string pattern.
     */
    fun to(str: String) = recipient(RecipientType.TO, str)

    /**
     * Creates a `RecipientTerm` specified as `RecipientType.TO`, using the given `InternetAddress`, and stores it in this `SearchBuilder`.
     *
     * @param address the `InternetAddress` for the generated term.
     */
    fun withTo(address: InternetAddress) = with(to(address))

    /**
     * Creates a `RecipientTerm` specified as `RecipientType.TO`, using the given string pattern, and stores it in this `SearchBuilder`.
     *
     * @param str the string pattern for the generated term.
     */
    fun withTo(str: String) = with(to(str))

    /**
     * Creates and returns a `RecipientTerm` specified as `RecipientType.CC`, using the given `InternetAddress`.
     *
     * @param address the `InternetAddress` for the generated term.
     *
     * @return a `RecipientType.CC`-based `RecipientTerm` for the given `address`.
     */
    fun cc(address: InternetAddress) = recipient(RecipientType.CC, address)

    /**
     * Creates and returns a `RecipientTerm` specified as `RecipientType.CC`, using the given string pattern.
     *
     * @param str the string pattern for the generated term.
     *
     * @return a `RecipientType.CC`-based `RecipientTerm` for the given string pattern.
     */
    fun cc(str: String) = recipient(RecipientType.CC, str)

    /**
     * Creates a `RecipientTerm` specified as `RecipientType.CC`, using the given `InternetAddress`, and stores it in this `SearchBuilder`.
     *
     * @param address the `InternetAddress` for the generated term.
     */
    fun withCC(address: InternetAddress) = with(cc(address))

    /**
     * Creates a `RecipientTerm` specified as `RecipientType.CC`, using the given string pattern, and stores it in this `SearchBuilder`.
     *
     * @param str the string pattern for the generated term.
     */
    fun withCC(str: String) = with(cc(str))

    /**
     * Creates and returns a `RecipientTerm` specified as `RecipientType.BCC`, using the given `InternetAddress`.
     *
     * @param address the `InternetAddress` for the generated term.
     *
     * @return a `RecipientType.BCC`-based `RecipientTerm` for the given `address`.
     */
    fun bcc(address: InternetAddress) = recipient(RecipientType.BCC, address)

    /**
     * Creates and returns a `RecipientTerm` specified as `RecipientType.BCC`, using the given string pattern.
     *
     * @param str the string pattern for the generated term.
     *
     * @return a `RecipientType.BCC`-based `RecipientTerm` for the given string pattern.
     */
    fun bcc(str: String) = recipient(RecipientType.BCC, str)

    /**
     * Creates a `RecipientTerm` specified as `RecipientType.BCC`, using the given `InternetAddress`, and stores it in this `SearchBuilder`.
     *
     * @param address the `InternetAddress` for the generated term.
     */
    fun withBCC(address: InternetAddress) = with(bcc(address))

    /**
     * Creates a `RecipientTerm` specified as `RecipientType.BCC`, using the given string pattern, and stores it in this `SearchBuilder`.
     *
     * @param str the string pattern for the generated term.
     */
    fun withBCC(str: String) = with(bcc(str))

    /**
     * Creates and returns a `SubjectTerm` using the given string pattern.
     *
     * @param str the string pattern for the generated term.
     *
     * @return a `SubjectTerm` for the given string pattern.
     */
    fun subject(str: String) = SubjectTerm(str)

    /**
     * Creates a `SubjectTerm` using the given string pattern and stores it in this `SearchBuilder`.
     *
     * @param str the string pattern for the generated term.
     */
    fun withSubject(str: String) = with(subject(str))

    /**
     * Creates and returns a `BodyTerm` using the given string pattern.
     *
     * @param str the string pattern for the generated term.
     *
     * @return a `BodyTerm` for the given string pattern.
     */
    fun body(str: String) = BodyTerm(str)

    /**
     * Creates a `BodyTerm` using the given string pattern and stores it in this `SearchBuilder`.
     *
     * @param str the string pattern for the generated term.
     */
    fun withBody(str: String) = with(body(str))

    /**
     * Creates and returns a `HeaderTerm` using the given header name and string pattern.
     *
     * @param headerName the header name to search for.
     * @param str the string pattern for the generated term.
     *
     * @return a `HeaderTerm` for the given header name and string pattern.
     */
    fun header(headerName: String, str: String) = HeaderTerm(headerName, str)

    /**
     * Creates a `HeaderTerm` using the given header name and string pattern, and stores it in this `SearchBuilder`.
     *
     * @param headerName the header name to search for.
     * @param str the string pattern for the generated term.
     */
    fun withHeader(headerName: String, str: String) = with(header(headerName, str))

    /**
     * Creates and returns an `OrTerm` using the given `SearchTerm`s.
     *
     * @param first the first `SearchTerm` for the generated term.
     * @param second the second `SearchTerm` for the generated term.
     *
     * @return an `OrTerm` for the given `SearchTerm`s.
     */
    fun or(first: SearchTerm, second: SearchTerm) = first or second

    /**
     * Creates an `OrTerm` using the given `SearchTerm`s, and stores it in this `SearchBuilder`.
     *
     * @param first the first `SearchTerm` for the generated term.
     * @param second the second `SearchTerm` for the generated term.
     */
    fun withOr(first: SearchTerm, second: SearchTerm) = with(or(first, second))

    /**
     * Creates and returns an `AndTerm` using the given `SearchTerm`s.
     *
     * @param first the first `SearchTerm` for the generated term.
     * @param second the second `SearchTerm` for the generated term.
     *
     * @return an `AndTerm` for the given `SearchTerm`s.
     */
    fun and(first: SearchTerm, second: SearchTerm) = first + second

    /**
     * Creates an `AndTerm` using the given `SearchTerm`s, and stores it in this `SearchBuilder`.
     *
     * @param first the first `SearchTerm` for the generated term.
     * @param second the second `SearchTerm` for the generated term.
     */
    fun withAnd(first: SearchTerm, second: SearchTerm) = with(and(first, second))

    /**
     * Creates and returns a `ReceivedDateTerm` using the given comparison type and the specified `Date`.
     *
     * @param comp the comparison type for the search term.
     * @param date the date for comparison
     *
     * @return a `ReceivedDateTerm` for the given comparison and `Date`.
     */
    fun received(comp: Int, date: Date) = ReceivedDateTerm(comp, date)

    /**
     * Creates a `ReceivedDateTerm` using the given comparison type and the specified `Date`,
     * and stores it within this `SearchBuilder`.
     *
     * @param comp the comparison type for the search term.
     * @param date the date for comparison
     */
    fun withReceived(comp: Int, date: Date) = with(received(comp, date))

    /**
     * Creates and returns a `ReceivedDateTerm` for items with a received date equal to the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `ReceivedDateTerm` for items equal to the given `Date`.
     */
    fun receivedOn(date: Date) = ReceivedDate eq date

    /**
     * Creates a `ReceivedDateTerm` for items with a received date equal to the specified `Date`, and stores it
     * in this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withReceivedOn(date: Date) = with(receivedOn(date))

    /**
     * Creates and returns a `ReceivedDateTerm` for items with a received date greater than or equal to the
     * specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `ReceivedDateTerm` for items greater than or equal to the given `Date`.
     */
    fun receivedOnOrAfter(date: Date) = ReceivedDate ge date

    /**
     * Creates a `ReceivedDateTerm` for items with a received date greater than or equal to the specified `Date`,
     * and stores it in this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withReceivedOnOrAfter(date: Date) = with(receivedOnOrAfter(date))

    /**
     * Creates and returns a `ReceivedDateTerm` for items with a received date greater than the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `ReceivedDateTerm` for items greater than the given `Date`.
     */
    fun receivedAfter(date: Date) = ReceivedDate gt date

    /**
     * Creates a `ReceivedDateTerm` for items with a received date greater than the specified `Date`, and stores it in
     * this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withReceivedAfter(date: Date) = with(receivedAfter(date))

    /**
     * Creates and returns a `ReceivedDateTerm` for items with a received date less than or equal to the specified
     * `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `ReceivedDateTerm` for items less than or equal to the given `Date`.
     */
    fun receivedOnOrBefore(date: Date) = ReceivedDate le date

    /**
     * Creates a `ReceivedDateTerm` for items with a received date less than or equal to the specified `Date`, and
     * stores it in this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withReceivedOnOrBefore(date: Date) = with(receivedOnOrBefore(date))

    /**
     * Creates and returns a `ReceivedDateTerm` for items with a received date less than the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `ReceivedDateTerm` for items less than the given `Date`.
     */
    fun receivedBefore(date: Date) = ReceivedDate lt date

    /**
     * Creates a `ReceivedDateTerm` for items with a received date less than the specified `Date`, and stores it in
     * this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withReceivedBefore(date: Date) = with(receivedBefore(date))

    /**
     * Creates and returns a `ReceivedDateTerm` for items with a received date not equal to the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `ReceivedDateTerm` for items not equal to the given `Date`.
     */
    fun notReceivedOn(date: Date) = ReceivedDate ne date

    /**
     * Creates a `ReceivedDateTerm` for items with a received date not equal to the specified `Date`, and stores it
     * in this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withNotReceivedOn(date: Date) = with(notReceivedOn(date))

    /**
     * Creates and returns an `AndTerm` for items with a received date greater than or equal to the starting value
     * of the date range, and less than or equal to the end value of the date range.
     *
     * @param dateRange the date range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the date range.
     */
    fun receivedBetween(dateRange: ClosedRange<Date>) = ReceivedDate between dateRange

    /**
     * Creates and returns an `AndTerm` for items with a received date greater than or equal to `earliest`, and less
     * than or equal to `latest`.
     *
     * @param earliest the start value of the date range to check.
     * @param latest the end value of the date range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the date range.
     */
    fun receivedBetween(earliest: Date, latest: Date) = ReceivedDate between earliest .. latest

    /**
     * Creates an `AndTerm` for items with a received date greater than or equal to the starting value of the
     * date range, and less than or equal to the end value of the date range, and stores it in this `SearchBuilder`.
     *
     * @param dateRange the date range to check.
     */
    fun withReceivedBetween(dateRange: ClosedRange<Date>) = with(receivedBetween(dateRange))

    /**
     * Creates an `AndTerm` for items with a received date greater than or equal to `earliest`, and less than or equal to
     * `latest`, and stores it in this `SearchBuilder`.
     *
     * @param earliest the start value of the date range to check.
     * @param latest the end value of the date range to check.
     */
    fun withReceivedBetween(earliest: Date, latest: Date) = with(receivedBetween(earliest, latest))

    /**
     * Creates and returns a `SentDateTerm` using the given comparison type and the specified `Date`.
     *
     * @param comp the comparison type for the search term.
     * @param date the date for comparison
     *
     * @return a `SentDateTerm` for the given comparison and `Date`.
     */
    fun sent(comp: Int, date: Date) = SentDateTerm(comp, date)

    /**
     * Creates a `SentDateTerm` using the given comparison type and the specified `Date`, and stores
     * is within this `SearchBuilder`.
     *
     * @param comp the comparison type for the search term.
     * @param date the date for comparison
     */
    fun withSent(comp: Int, date: Date) = with(sent(comp, date))

    /**
     * Creates and returns a `SentDateTerm` for items with a sent date equal to the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `SentDateTerm` for items equal to the given `Date`.
     */
    fun sentOn(date: Date) = SentDate eq date

    /**
     * Creates a `SentDateTerm` for items with a sent date equal to the specified `Date`, and stores it
     * within this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withSentOn(date: Date) = with(sentOn(date))

    /**
     * Creates and returns a `SentDateTerm` for items with a sent date greater than or equal to the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `SentDateTerm` for items greater than or equal to the given `Date`.
     */
    fun sentOnOrAfter(date: Date) = SentDate ge date

    /**
     * Creates a `SentDateTerm` for items with a sent date greater than or equal to the specified `Date`, and stores
     * it within this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withSentOnOrAfter(date: Date) = with(sentOnOrAfter(date))

    /**
     * Creates and returns a `SentDateTerm` for items with a sent date greater than the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `SentDateTerm` for items greater than to the given `Date`.
     */
    fun sentAfter(date: Date) = SentDate gt date

    /**
     * Creates a `SentDateTerm` for items with a sent date greater than the specified `Date`, and stores it within
     * this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withSentAfter(date: Date) = with(sentAfter(date))

    /**
     * Creates and returns a `SentDateTerm` for items with a sent date less than or equal to the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `SentDateTerm` for items less than or equal to the given `Date`.
     */
    fun sentOnOrBefore(date: Date) = SentDate le date

    /**
     * Creates a `SentDateTerm` for items with a sent date less than or equal to the specified `Date`, and stores it
     * within this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withSentOnOrBefore(date: Date) = with(sentOnOrBefore(date))

    /**
     * Creates and returns a `SentDateTerm` for items with a sent date less than the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `SentDateTerm` for items less than the given `Date`.
     */
    fun sentBefore(date: Date) = SentDate lt date

    /**
     * Creates a `SentDateTerm` for items with a sent date less than the specified `Date`, and stores it within
     * this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withSentBefore(date: Date) = with(sentBefore(date))

    /**
     * Creates and returns a `SentDateTerm` for items with a sent date not equal to the specified `Date`.
     *
     * @param date the date for comparison
     *
     * @return a `SentDateTerm` for items not equal to the given `Date`.
     */
    fun notSentOn(date: Date) = SentDate ne date

    /**
     * Creates a `SentDateTerm` for items with a sent date not equal to the specified `Date`, and stores it within
     * this `SearchBuilder`.
     *
     * @param date the date for comparison
     */
    fun withNotSentOn(date: Date) = with(notSentOn(date))

    /**
     * Creates and returns an `AndTerm` for items with a sent date greater than or equal to the starting value of the
     * date range, and less than or equal to the end value of the date range.
     *
     * @param dateRange the date range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the date range.
     */
    fun sentBetween(dateRange: ClosedRange<Date>) = SentDate between dateRange

    /**
     * Creates and returns an `AndTerm` for items with a received date greater than or equal to `earliest`, and less
     * than or equal to `latest`.
     *
     * @param earliest the start value of the date range to check.
     * @param latest the end value of the date range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the date range.
     */
    fun sentBetween(earliest: Date, latest: Date) = SentDate between earliest .. latest

    /**
     * Creates an `AndTerm` for items with a sent date greater than or equal to the starting value of the
     * date range, and less than or equal to the end value of the date range, and stores it in this `SearchBuilder`.
     *
     * @param dateRange the date range to check.
     */
    fun withSentBetween(dateRange: ClosedRange<Date>) = with(sentBetween(dateRange))

    /**
     * Creates an `AndTerm` for items with a sent date greater than or equal to `earliest`, and less than or equal to
     * `latest`, and stores it in this `SearchBuilder`.
     *
     * @param earliest the start value of the date range to check.
     * @param latest the end value of the date range to check.
     */
    fun withSentBetween(earliest: Date, latest: Date) = with(sentBetween(earliest, latest))

    /**
     * Creates and returns a `MessageIDTerm` using the given message id string.
     *
     * @param msgId the message id to search.
     *
     * @return a `MessageIDTerm` for the given message id.
     */
    fun messageId(msgId: String) = MessageIDTerm(msgId)

    /**
     * Creates a `MessageIDTerm` using the given message id string, and stores it within
     * this `SearchBuilder`.
     *
     * @param msgId the message id to search.
     */
    fun withMessageId(msgId: String) = with(messageId(msgId))

    /**
     * Creates and returns a `MessageNumberTerm` using the given message number.
     *
     * @param number the message number to search.
     *
     * @return a `MessageNumberTerm` for the given message number.
     */
    fun messageNumber(number: Int) = MessageNumberTerm(number)

    /**
     * Creates a `MessageNumberTerm` using the given message number, and stores it within
     * this `SearchBuilder`.
     *
     * @param number the message id to search.
     */
    fun withMessageNumber(number: Int) = with(messageNumber(number))

    /**
     * Creates and returns a `SizeTerm` using the given comparison type and the specified size.
     *
     * @param comp the comparison type for the search term.
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for the given comparison and size.
     */
    fun size(comp: Int, size: Int) = SizeTerm(comp, size)

    /**
     * Creates a `SizeTerm` using the given comparison type and the specified size, and stores it
     * within this `SearchBuilder`.
     *
     * @param comp the comparison type for the search term.
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for the given comparison and size.
     */
    fun withSize(comp: Int, size: Int) = with(size(comp, size))

    /**
     * Creates and returns a `SizeTerm` for items with a size equal to the specified size.
     *
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for items equal to the given size.
     */
    fun sizeIs(size: Int) = Size eq size

    /**
     * Creates a `SizeTerm` for items with a size equal to the specified size, and stores it within this
     * `SearchBuilder`.
     *
     * @param size the size for comparison
     */
    fun withSizeIs(size: Int) = with(sizeIs(size))

    /**
     * Creates and returns a `SizeTerm` for items with a size greater than or equal to the specified size.
     *
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for items greater than or equal to the given size.
     */
    fun sizeIsAtLeast(size: Int) = Size ge size

    /**
     * Creates a `SizeTerm` for items with a size greater than or equal to the specified size, and stores it within
     * this `SearchBuilder`.
     *
     * @param size the size for comparison
     */
    fun withSizeIsAtLeast(size: Int) = with(sizeIsAtLeast(size))

    /**
     * Creates and returns a `SizeTerm` for items with a size greater than the specified size.
     *
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for items greater than the given size.
     */
    fun sizeIsGreaterThan(size: Int) = Size gt size

    /**
     * Creates a `SizeTerm` for items with a size greater than the specified size, and stores it within this
     * `SearchBuilder`.
     *
     * @param size the size for comparison
     */
    fun withSizeIsGreaterThan(size: Int) = with(sizeIsGreaterThan(size))

    /**
     * Creates and returns a `SizeTerm` for items with a size less than or equal to the specified size.
     *
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for items less than or equal to the given size.
     */
    fun sizeIsNoMoreThan(size: Int) = Size le size

    /**
     * Creates a `SizeTerm` for items with a size less than or equal to the specified size, and stores it within
     * this `SearchBuilder`.
     *
     * @param size the size for comparison
     */
    fun withSizeIsNoMoreThan(size: Int) = with(sizeIsNoMoreThan(size))

    /**
     * Creates and returns a `SizeTerm` for items with a size less than the specified size.
     *
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for items less than the given size.
     */
    fun sizeIsLessThan(size: Int) = Size lt size

    /**
     * Creates a `SizeTerm` for items with a size less than the specified size, and stores it within this
     * `SearchBuilder`.
     *
     * @param size the size for comparison
     */
    fun withSizeIsLessThan(size: Int) = with(sizeIsLessThan(size))

    /**
     * Creates and returns a `SizeTerm` for items with a size not equal to the specified size.
     *
     * @param size the size for comparison
     *
     * @return a `SizeTerm` for items not equal to the given size.
     */
    fun sizeIsNot(size: Int) = Size ne size

    /**
     * Creates a `SizeTerm` for items with a size not equal to the specified size, and stores it within this
     * `SearchBuilder`.
     *
     * @param size the size for comparison
     */
    fun withSizeIsNot(size: Int) = with(sizeIsNot(size))

    /**
     * Creates and returns an `AndTerm` for items with a size greater than or equal to the start value of the size range,
     * and less than or equal to the end value of the size range.
     *
     * @param sizeRange the size range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the size range.
     */
    fun sizeBetween(sizeRange: IntRange) = Size between sizeRange

    /**
     * Creates and returns an `AndTerm` for items with a size greater than or equal to `smallest`, and less
     * than or equal to `largest`.
     *
     * @param smallest the start value of the size range to check.
     * @param largest the end value of the size range to check.
     *
     * @return an `AndTerm` applied to the start and end values of the size range.
     */
    fun sizeBetween(smallest: Int, largest: Int) = Size between smallest .. largest

    /**
     * Creates an `AndTerm` for items with a size greater than or equal to the start value of the size range, and less
     * than or equal to the end value of the size range; the created term is stored within this `SearchBuilder`.
     *
     * @param sizeRange the size range to check.
     */
    fun withSizeBetween(sizeRange: IntRange) = with(sizeBetween(sizeRange))

    /**
     * Creates an `AndTerm` for items with a size greater than or equal to `smallest`, and less than or equal to
     * `largest`; the created term is stored within this `SearchBuilder`.
     *
     * @param smallest the start value of the size range to check.
     * @param largest the end value of the size range to check.
     */
    fun withSizeBetween(smallest: Int, largest: Int) = with(sizeBetween(smallest, largest))

    /**
     * Creates and returns a `FlagTerm` using the given flags and set value. For example, searching with a `Flags`
     * object containing `Flags.Flag.DRAFT` and a `set` value of `true` would return everything with the `DRAFT` flag
     * set to `true`; searching with the `set` value of `false` instead would return everything with the `DRAFT` flag
     * set to `false`.
     *
     * @param flags the flags to search
     * @param set the set value to search
     *
     * @return a `FlagTerm` for the given flags and set value.
     */
    fun flags(flags: Flags, set: Boolean) = FlagTerm(flags, set)

    /**
     * Creates a `FlagTerm` using the given flags and set value, and stores it within this `SearchBuilder`. For example,
     * searching with a `Flags` object containing `Flags.Flag.DRAFT` and a `set` value of `true` would return
     * everything with the `DRAFT` flag set to `true`; searching with the `set` value of `false` instead would return
     * everything with the `DRAFT` flag set to `false`.
     *
     * @param flags the flags to search
     * @param set the set value to search
     */
    fun withFlags(flags: Flags, set: Boolean) = with(flags(flags, set))

    /**
     * Creates and returns a `ModifiedSinceTerm` using the given modification sequence.
     *
     * @param modSequence the modification sequence to search.
     *
     * @return a `ModifiedSinceTerm` for the given modification sequence.
     */
    fun modifiedSince(modSequence: Long) = ModifiedSinceTerm(modSequence)

    /**
     * Creates a `ModifiedSinceTerm` using the given modification sequence, and stores it within this `SearchBuilder`.
     *
     * @param modSequence the modification sequence to search.
     */
    fun withModifiedSince(modSequence: Long) = with(modifiedSince(modSequence))

    /**
     * Creates and returns an `OlderTerm` using the given interval.
     *
     * @param interval the message number to search.
     *
     * @return a `YoungerTerm` for the given interval.
     */
    fun older(interval: Int) = OlderTerm(interval)

    /**
     * Creates an `OlderTerm` using the given interval, and stores it within
     * this `SearchBuilder`.
     *
     * @param interval the interval to search.
     */
    fun withOlder(interval: Int) = with(older(interval))

    /**
     * Creates and returns a `YoungerTerm` using the given interval.
     *
     * @param interval the message number to search.
     *
     * @return a `YoungerTerm` for the given interval.
     */
    fun younger(interval: Int) = YoungerTerm(interval)

    /**
     * Creates a `YoungerTerm` using the given interval, and stores it within
     * this `SearchBuilder`.
     *
     * @param interval the interval to search.
     */
    fun withYounger(interval: Int) = with(younger(interval))

    /**
     * Adds the specified `SearchTerm` to this `SearchBuilder`.
     *
     * @param term the `SearchTerm` to add.
     */
    fun with(term: SearchTerm) = terms.add(term)

    /**
     * Sorts the search results by the terms applied in the block.
     *
     * @param block the block to use to initialize the [SortBuilder]
     */
    fun sortedBy(block: SortBuilder.() -> Unit) {
        val sortBuilder = SortBuilder()

        sortBuilder.block()

        sortedBy.addAll(sortBuilder.build())
    }

    /**
     * Adds the `SearchTerm` operand to this `SearchBuilder`.
     */
    operator fun SearchTerm.unaryPlus() = terms.add(this)

    /**
     * Adds a `NotTerm` for the `SearchTerm` operand to this `SearchBuilder`.
     */
    operator fun SearchTerm.unaryMinus() = terms.add(!this)
}

