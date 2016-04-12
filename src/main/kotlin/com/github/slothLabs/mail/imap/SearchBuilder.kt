package com.github.slothLabs.mail.imap

import com.sun.mail.imap.ModifiedSinceTerm
import com.sun.mail.imap.OlderTerm
import com.sun.mail.imap.YoungerTerm
import java.util.Date
import javax.mail.Flags
import javax.mail.internet.InternetAddress
import javax.mail.Message
import javax.mail.internet.MimeMessage.RecipientType
import javax.mail.search.AndTerm
import javax.mail.search.BodyTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.FlagTerm
import javax.mail.search.FromStringTerm
import javax.mail.search.FromTerm
import javax.mail.search.HeaderTerm
import javax.mail.search.MessageIDTerm
import javax.mail.search.MessageNumberTerm
import javax.mail.search.NotTerm
import javax.mail.search.OrTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.RecipientStringTerm
import javax.mail.search.RecipientTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SentDateTerm
import javax.mail.search.SizeTerm
import javax.mail.search.SubjectTerm

class SearchBuilder {

    private val terms = mutableListOf<SearchTerm>()

    fun build(): SearchTerm = terms.reduce { first, second -> AndTerm(first, second) }

    fun from(address: InternetAddress) = FromTerm(address)

    fun from(str: String) = FromStringTerm(str)

    fun withFrom(address: InternetAddress) = with(from(address))

    fun withFrom(str: String) = with(from(str))

    fun recipient(recipientType: Message.RecipientType, address: InternetAddress) = RecipientTerm(recipientType, address)

    fun recipient(recipientType: Message.RecipientType, str: String) = RecipientStringTerm(recipientType, str)

    fun withRecipient(recipientType: RecipientType, address: InternetAddress) = with(recipient(recipientType, address))

    fun withRecipient(recipientType: RecipientType, address: String) = with(recipient(recipientType, address))

    fun to(address: InternetAddress) = recipient(RecipientType.TO, address)

    fun to(str: String) = recipient(RecipientType.TO, str)

    fun withTo(address: InternetAddress) = with(to(address))

    fun withTo(str: String) = with(to(str))

    fun cc(address: InternetAddress) = recipient(RecipientType.CC, address)

    fun cc(str: String) = recipient(RecipientType.CC, str)

    fun withCC(address: InternetAddress) = with(cc(address))

    fun withCC(str: String) = with(cc(str))

    fun bcc(address: InternetAddress) = recipient(RecipientType.BCC, address)

    fun bcc(str: String) = recipient(RecipientType.BCC, str)

    fun withBCC(address: InternetAddress) = with(bcc(address))

    fun withBCC(str: String) = with(bcc(str))

    fun subject(str: String) = SubjectTerm(str)

    fun withSubject(str: String) = with(subject(str))

    fun body(str: String) = BodyTerm(str)

    fun withBody(str: String) = with(body(str))

    fun header(headerName: String, str: String) = HeaderTerm(headerName, str)

    fun withHeader(headerName: String, str: String) = with(header(headerName, str))

    fun or(first: SearchTerm, second: SearchTerm) = first or second

    fun withOr(first: SearchTerm, second: SearchTerm) = with(or(first, second))

    fun and(first: SearchTerm, second: SearchTerm) = first + second

    fun withAnd(first: SearchTerm, second: SearchTerm) = with(and(first, second))

    fun received(comp: Int, date: Date) = ReceivedDateTerm(comp, date)

    fun withReceived(comp: Int, date: Date) = with(received(comp, date))

    fun receivedOn(date: Date) = received(ComparisonTerm.EQ, date)

    fun withReceivedOn(date: Date) = with(receivedOn(date))

    fun receivedOnOrAfter(date: Date) = received(ComparisonTerm.GE, date)

    fun withReceivedOnOrAfter(date: Date) = with(receivedOnOrAfter(date))

    fun receivedAfter(date: Date) = received(ComparisonTerm.GT, date)

    fun withReceivedAfter(date: Date) = with(receivedAfter(date))

    fun receivedOnOrBefore(date: Date) = received(ComparisonTerm.LE, date)

    fun withReceivedOnOrBefore(date: Date) = with(receivedOnOrBefore(date))

    fun receivedBefore(date: Date) = received(ComparisonTerm.LT, date)

    fun withReceivedBefore(date: Date) = with(receivedBefore(date))

    fun notReceivedOn(date: Date) = received(ComparisonTerm.NE, date)

    fun withNotReceivedOn(date: Date) = with(notReceivedOn(date))

    fun receivedBetween(earliest: Date, latest: Date) = receivedOnOrAfter(earliest) and receivedOnOrBefore(latest)

    fun withReceivedBetween(earliest: Date, latest: Date) = with(receivedBetween(earliest, latest))

    fun sent(comp: Int, date: Date) = SentDateTerm(comp, date)

    fun withSent(comp: Int, date: Date) = with(sent(comp, date))

    fun sentOn(date: Date) = sent(ComparisonTerm.EQ, date)

    fun withSentOn(date: Date) = with(sentOn(date))

    fun sentOnOrAfter(date: Date) = sent(ComparisonTerm.GE, date)

    fun withSentOnOrAfter(date: Date) = with(sentOnOrAfter(date))

    fun sentAfter(date: Date) = sent(ComparisonTerm.GT, date)

    fun withSentAfter(date: Date) = with(sentAfter(date))

    fun sentOnOrBefore(date: Date) = sent(ComparisonTerm.LE, date)

    fun withSentOnOrBefore(date: Date) = with(sentOnOrBefore(date))

    fun sentBefore(date: Date) = sent(ComparisonTerm.LT, date)

    fun withSentBefore(date: Date) = with(sentBefore(date))

    fun notSentOn(date: Date) = sent(ComparisonTerm.NE, date)

    fun withNotSentOn(date: Date) = with(notSentOn(date))

    fun sentBetween(earliest: Date, latest: Date) = sentOnOrAfter(earliest) and sentOnOrBefore(latest)

    fun withSentBetween(earliest: Date, latest: Date) = with(sentBetween(earliest, latest))

    fun messageId(str: String) = MessageIDTerm(str)

    fun withMessageId(str: String) = with(messageId(str))

    fun messageNumber(number: Int) = MessageNumberTerm(number)

    fun withMessageNumber(number: Int) = with(messageNumber(number))

    fun size(comp: Int, size: Int) = SizeTerm(comp, size)

    fun withSize(comp: Int, size: Int) = with(size(comp, size))

    fun sizeIs(size: Int) = size(ComparisonTerm.EQ, size)

    fun withSizeIs(size: Int) = with(sizeIs(size))

    fun sizeIsAtLeast(size: Int) = size(ComparisonTerm.GE, size)

    fun withSizeIsAtLeast(size: Int) = with(sizeIsAtLeast(size))

    fun sizeIsGreaterThan(size: Int) = size(ComparisonTerm.GT, size)

    fun withSizeIsGreaterThan(size: Int) = with(sizeIsGreaterThan(size))

    fun sizeIsNoMoreThan(size: Int) = size(ComparisonTerm.LE, size)

    fun withSizeIsNoMoreThan(size: Int) = with(sizeIsNoMoreThan(size))

    fun sizeIsLessThan(size: Int) = size(ComparisonTerm.LT, size)

    fun withSizeIsLessThan(size: Int) = with(sizeIsLessThan(size))

    fun sizeIsNot(size: Int) = size(ComparisonTerm.NE, size)

    fun withSizeIsNot(size: Int) = with(sizeIsNot(size))

    fun sizeBetween(smallest: Int, largest: Int) = sizeIsAtLeast(smallest) and sizeIsNoMoreThan(largest)

    fun withSizeBetween(smallest: Int, largest: Int) = with(sizeBetween(smallest, largest))

    fun flags(flags: Flags, set: Boolean) = FlagTerm(flags, set)

    fun withFlags(flags: Flags, set: Boolean) = with(flags(flags, set))

    fun modifiedSince(modSequence: Long) = ModifiedSinceTerm(modSequence)

    fun withModifiedSince(modSequence: Long) = with(modifiedSince(modSequence))

    fun older(interval: Int) = OlderTerm(interval)

    fun withOlder(interval: Int) = with(older(interval))

    fun younger(interval: Int) = YoungerTerm(interval)

    fun withYounger(interval: Int) = with(younger(interval))

    fun with(term: SearchTerm) = terms.add(term)

    operator fun SearchTerm.unaryPlus() = terms.add(this)

    operator fun SearchTerm.unaryMinus() = terms.add(!this)
}

infix fun SearchTerm.and(other: SearchTerm): SearchTerm = AndTerm(this, other)

infix fun SearchTerm.or(other: SearchTerm): SearchTerm = OrTerm(this, other)

operator fun SearchTerm.not(): SearchTerm = if (this is NotTerm) this.term else NotTerm(this)

operator fun SearchTerm.plus(other: SearchTerm) = AndTerm(this, other)

