package com.github.slothLabs.mail.imap

import java.util.*
import javax.mail.Flags
import javax.mail.internet.MimeMessage.RecipientType
import javax.mail.search.*

class SearchBuilder {

    private val terms = mutableListOf<SearchTerm>()

    fun build(): SearchTerm = terms.reduce { first, second -> AndTerm(first, second) }

    fun from(addr: String) = add(FromStringTerm(addr))

    fun recipient(recipientType: RecipientType, addr: String) = add(RecipientStringTerm(recipientType, addr))

    fun to(addr: String) = add(RecipientStringTerm(RecipientType.TO, addr))

    fun cc(addr: String) = add(RecipientStringTerm(RecipientType.CC, addr))

    fun bcc(addr: String) = add(RecipientStringTerm(RecipientType.BCC, addr))

    fun subject(str: String) = add(SubjectTerm(str))

    fun body(str: String) = add(BodyTerm(str))

    fun header(headerName: String, str: String) = add(HeaderTerm(headerName, str))

    fun or(first: SearchTerm, second: SearchTerm) = add(first or second)

    fun and(first: SearchTerm, second: SearchTerm) = add(first and second)

    fun received(comp: Int, date: Date) = add(ReceivedDateTerm(comp, date))

    fun receivedOn(date: Date) = received(ComparisonTerm.EQ, date)

    fun receivedOnOrAfter(date: Date) = received(ComparisonTerm.GE, date)

    fun receivedAfter(date: Date) = received(ComparisonTerm.GT, date)

    fun receivedOnOrBefore(date: Date) = received(ComparisonTerm.LE, date)

    fun receivedBefore(date: Date) = received(ComparisonTerm.LT, date)

    fun notReceivedOn(date: Date) = received(ComparisonTerm.NE, date)

    fun sent(comp: Int, date: Date) = add(SentDateTerm(comp, date))

    fun sentOn(date: Date) = sent(ComparisonTerm.EQ, date)

    fun sentOnOrAfter(date: Date) = sent(ComparisonTerm.GE, date)

    fun sentAfter(date: Date) = sent(ComparisonTerm.GT, date)

    fun sentOnOrBefore(date: Date) = sent(ComparisonTerm.LE, date)

    fun sentBefore(date: Date) = sent(ComparisonTerm.LT, date)

    fun notSentOn(date: Date) = sent(ComparisonTerm.NE, date)

    fun messageId(str: String) = add(MessageIDTerm(str))

    fun not(term: SearchTerm) = add(term.not())

    fun messageNumber(number: Int) = add(MessageNumberTerm(number))

    fun size(comp: Int, size: Int) = add(SizeTerm(comp, size))

    fun sizeIs(size: Int) = size(ComparisonTerm.EQ, size)

    fun sizeIsAtLeast(size: Int) = size(ComparisonTerm.GE, size)

    fun sizeIsGreaterThan(size: Int) = size(ComparisonTerm.GT, size)

    fun sizeIsNoMoreThan(size: Int) = size(ComparisonTerm.LE, size)

    fun sizeIsLessThan(size: Int) = size(ComparisonTerm.LT, size)

    fun sizeIsNot(size: Int) = size(ComparisonTerm.NE, size)

    fun flags(flags: Flags, set: Boolean) = add(FlagTerm(flags, set))

    fun term(term: SearchTerm) = add(term)

    private fun add(term: SearchTerm) = terms.add(term)
}

infix fun SearchTerm.and(other: SearchTerm): SearchTerm = AndTerm(this, other)

infix fun SearchTerm.or(other: SearchTerm): SearchTerm = OrTerm(this, other)

fun SearchTerm.not(): SearchTerm = NotTerm(this)