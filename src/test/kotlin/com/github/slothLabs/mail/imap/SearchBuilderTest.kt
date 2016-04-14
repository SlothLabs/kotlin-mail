package com.github.slothLabs.mail.imap

import com.sun.mail.imap.ModifiedSinceTerm
import com.sun.mail.imap.OlderTerm
import com.sun.mail.imap.YoungerTerm
import org.funktionale.option.Option.*
import org.junit.Assert.*
import org.junit.Test
import java.util.Date
import javax.mail.Flags
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
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
import javax.mail.search.SentDateTerm
import javax.mail.search.SizeTerm
import javax.mail.search.SubjectTerm


class BasicSearchBuilderTest {

    @Test fun buildOnNewSearchBuilderShouldReturnNone() {
        val sb = SearchBuilder()

        val res = sb.build()

        assertTrue(res is None)
    }

    @Test fun fromMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()

        val address = InternetAddress("test@drive.com")
        val fromInternetAddress = sb.from(address)
        assertTrue(fromInternetAddress is FromTerm)
        assertEquals(fromInternetAddress.address, address)

        val str = "another@example.com"
        val fromString = sb.from(str)
        assertTrue(fromString is FromStringTerm)
        assertEquals(fromString.pattern, str)
    }

    @Test fun withFromMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()

        val address = InternetAddress("test@drive.com")
        sb1.withFrom(address)
        val terms1 = sb1.build()
        assertTrue(terms1 is Some)
        val fromTerm: FromTerm = terms1.get() as FromTerm
        assertEquals(fromTerm.address, address)

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withFrom(str)
        val terms2 = sb2.build()
        assertTrue(terms2 is Some)
        val fromStringTerm: FromStringTerm = terms2.get() as FromStringTerm
        assertEquals(fromStringTerm.pattern, str)
    }

    @Test fun recipientMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()
        val recipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.recipient(recipientType, address)
        assertTrue(recipientInternetAddress is RecipientTerm)
        assertEquals(recipientInternetAddress.recipientType, recipientType)
        assertEquals(recipientInternetAddress.address, address)

        val str = "another@example.com"
        val recipientString = sb.recipient(recipientType, str)
        assertTrue(recipientString is RecipientStringTerm)
        assertEquals(recipientString.recipientType, recipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun withRecipientMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val recipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        sb1.withRecipient(recipientType, address)
        val terms1 = sb1.build()
        assertTrue(terms1 is Some)
        val recipientInternetAddress: RecipientTerm = terms1.get() as RecipientTerm
        assertEquals(recipientInternetAddress.recipientType, recipientType)
        assertEquals(recipientInternetAddress.address, address)

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withRecipient(recipientType, str)
        val terms2 = sb2.build()
        assertTrue(terms2 is Some)
        val recipientString: RecipientStringTerm = terms2.get() as RecipientStringTerm
        assertEquals(recipientString.recipientType, recipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun toMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.to(address)
        assertTrue(recipientInternetAddress is RecipientTerm)
        assertEquals(recipientInternetAddress.recipientType, expRecipientType)
        assertEquals(recipientInternetAddress.address, address)

        val str = "another@example.com"
        val recipientString = sb.to(str)
        assertTrue(recipientString is RecipientStringTerm)
        assertEquals(recipientString.recipientType, expRecipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun withToMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        sb1.withTo(address)
        val terms1 = sb1.build()
        assertTrue(terms1 is Some)
        val recipientInternetAddress: RecipientTerm = terms1.get() as RecipientTerm
        assertEquals(recipientInternetAddress.recipientType, expRecipientType)
        assertEquals(recipientInternetAddress.address, address)

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withTo(str)
        val terms2 = sb2.build()
        assertTrue(terms2 is Some)
        val recipientString: RecipientStringTerm = terms2.get() as RecipientStringTerm
        assertEquals(recipientString.recipientType, expRecipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun ccMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.CC

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.cc(address)
        assertTrue(recipientInternetAddress is RecipientTerm)
        assertEquals(recipientInternetAddress.recipientType, expRecipientType)
        assertEquals(recipientInternetAddress.address, address)

        val str = "another@example.com"
        val recipientString = sb.cc(str)
        assertTrue(recipientString is RecipientStringTerm)
        assertEquals(recipientString.recipientType, expRecipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun withCCMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.CC

        val address = InternetAddress("test@drive.com")
        sb1.withCC(address)
        val terms1 = sb1.build()
        assertTrue(terms1 is Some)
        val recipientInternetAddress: RecipientTerm = terms1.get() as RecipientTerm
        assertEquals(recipientInternetAddress.recipientType, expRecipientType)
        assertEquals(recipientInternetAddress.address, address)

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withCC(str)
        val terms2 = sb2.build()
        assertTrue(terms2 is Some)
        val recipientString: RecipientStringTerm = terms2.get() as RecipientStringTerm
        assertEquals(recipientString.recipientType, expRecipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun bccMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.BCC

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.bcc(address)
        assertTrue(recipientInternetAddress is RecipientTerm)
        assertEquals(recipientInternetAddress.recipientType, expRecipientType)
        assertEquals(recipientInternetAddress.address, address)

        val str = "another@example.com"
        val recipientString = sb.bcc(str)
        assertTrue(recipientString is RecipientStringTerm)
        assertEquals(recipientString.recipientType, expRecipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun withBCCMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.BCC

        val address = InternetAddress("test@drive.com")
        sb1.withBCC(address)
        val terms1 = sb1.build()
        assertTrue(terms1 is Some)
        val recipientInternetAddress: RecipientTerm = terms1.get() as RecipientTerm
        assertEquals(recipientInternetAddress.recipientType, expRecipientType)
        assertEquals(recipientInternetAddress.address, address)

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withBCC(str)
        val terms2 = sb2.build()
        assertTrue(terms2 is Some)
        val recipientString: RecipientStringTerm = terms2.get() as RecipientStringTerm
        assertEquals(recipientString.recipientType, expRecipientType)
        assertEquals(recipientString.pattern, str)
    }

    @Test fun subjectMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val subject = "Test Subject"
        val subjectTerm = sb.subject(subject)

        assertTrue(subjectTerm is SubjectTerm)
        assertEquals(subjectTerm.pattern, subject)
    }

    @Test fun withSubjectMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val subject = "Test Subject"
        sb.withSubject(subject)

        val terms = sb.build()
        assertTrue(terms is Some)
        val subjectTerm: SubjectTerm = terms.get() as SubjectTerm
        assertEquals(subjectTerm.pattern, subject)
    }

    @Test fun bodyMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val body = "Test Body"
        val bodyTerm = sb.body(body)

        assertTrue(bodyTerm is BodyTerm)
        assertEquals(bodyTerm.pattern, body)
    }

    @Test fun withBodyMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val body = "Test Body"
        sb.withBody(body)

        val terms = sb.build()
        assertTrue(terms is Some)
        val bodyTerm: BodyTerm = terms.get() as BodyTerm
        assertEquals(bodyTerm.pattern, body)
    }

    @Test fun headerMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val headerName = "TheHeader"
        val headerValue = "Header Value"
        val headerTerm = sb.header(headerName, headerValue)

        assertTrue(headerTerm is HeaderTerm)
        assertEquals(headerTerm.headerName, headerName)
        assertEquals(headerTerm.pattern, headerValue)
    }

    @Test fun withHeaderMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val headerName = "TheHeader"
        val headerValue = "Header Value"
        sb.withHeader(headerName, headerValue)

        val terms = sb.build()
        assertTrue(terms is Some)
        val headerTerm: HeaderTerm = terms.get() as HeaderTerm
        assertEquals(headerTerm.headerName, headerName)
        assertEquals(headerTerm.pattern, headerValue)
    }

    @Test fun orMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        val orTerm = sb.or(firstFrom, secondFrom)

        assertTrue(orTerm is OrTerm)
        with (orTerm as OrTerm) {
            assertEquals(orTerm.terms[0], firstFrom)
            assertEquals(orTerm.terms[1], secondFrom)
        }

    }

    @Test fun withOrMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        sb.withOr(firstFrom, secondFrom)

        val terms = sb.build()
        assertTrue(terms is Some)
        val orTerm: OrTerm = terms.get() as OrTerm

        assertTrue(orTerm is OrTerm)
        assertEquals(orTerm.terms[0], firstFrom)
        assertEquals(orTerm.terms[1], secondFrom)
    }

    @Test fun andMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        val andTerm = sb.and(firstFrom, secondFrom)

        assertTrue(andTerm is AndTerm)
        assertEquals(andTerm.terms[0], firstFrom)
        assertEquals(andTerm.terms[1], secondFrom)

    }

    @Test fun withAndMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        sb.withAnd(firstFrom, secondFrom)

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm: AndTerm = terms.get() as AndTerm

        assertTrue(andTerm is AndTerm)
        assertEquals(andTerm.terms[0], firstFrom)
        assertEquals(andTerm.terms[1], secondFrom)
    }

    @Test fun receivedMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        val receivedTerm = sb.received(compTerm, date)

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, compTerm)
        assertEquals(receivedTerm.date, date)

    }

    @Test fun withReceivedMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        sb.withReceived(compTerm, date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val receivedTerm: ReceivedDateTerm = terms.get() as ReceivedDateTerm

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, compTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun receivedOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        val receivedTerm = sb.receivedOn(date)

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun withReceivedOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        sb.withReceivedOn(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val receivedTerm: ReceivedDateTerm = terms.get() as ReceivedDateTerm

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun receivedOnOrAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        val receivedTerm = sb.receivedOnOrAfter(date)

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun withReceivedOnOrAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        sb.withReceivedOnOrAfter(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val receivedTerm: ReceivedDateTerm = terms.get() as ReceivedDateTerm

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun receivedAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        val receivedTerm = sb.receivedAfter(date)

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun withReceivedAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        sb.withReceivedAfter(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val receivedTerm: ReceivedDateTerm = terms.get() as ReceivedDateTerm

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun receivedOnOrBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        val receivedTerm = sb.receivedOnOrBefore(date)

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun withReceivedOnOrBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        sb.withReceivedOnOrBefore(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val receivedTerm: ReceivedDateTerm = terms.get() as ReceivedDateTerm

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun receivedBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        val receivedTerm = sb.receivedBefore(date)

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)

    }

    @Test fun withReceivedBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        sb.withReceivedBefore(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val receivedTerm: ReceivedDateTerm = terms.get() as ReceivedDateTerm

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun notReceivedOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        val receivedTerm = sb.notReceivedOn(date)

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun withNotReceivedOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        sb.withNotReceivedOn(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val receivedTerm: ReceivedDateTerm = terms.get() as ReceivedDateTerm

        assertTrue(receivedTerm is ReceivedDateTerm)
        assertEquals(receivedTerm.comparison, expCompTerm)
        assertEquals(receivedTerm.date, date)
    }

    @Test fun receivedBetweenMethodWithTwoParamsShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.receivedBetween(startDate, endDate)

        assertTrue(andTerm is AndTerm)
        with (andTerm as AndTerm) {
            val startTerm = andTerm.terms[0] as ReceivedDateTerm
            assertEquals(startTerm.comparison, ComparisonTerm.GE)
            assertEquals(startTerm.date, startDate)

            val endTerm = andTerm.terms[1] as ReceivedDateTerm
            assertEquals(endTerm.comparison, ComparisonTerm.LE)
            assertEquals(endTerm.date, endDate)
        }
    }

    @Test fun withReceivedBetweenMethodWithTwoParamsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withReceivedBetween(startDate, endDate)

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm: AndTerm = terms.get() as AndTerm

        val startTerm = andTerm.terms[0] as ReceivedDateTerm
        assertEquals(startTerm.comparison, ComparisonTerm.GE)
        assertEquals(startTerm.date, startDate)

        val endTerm = andTerm.terms[1] as ReceivedDateTerm
        assertEquals(endTerm.comparison, ComparisonTerm.LE)
        assertEquals(endTerm.date, endDate)
    }

    @Test fun receivedBetweenMethodWithRangeShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.receivedBetween(startDate .. endDate)

        assertTrue(andTerm is AndTerm)
        with (andTerm as AndTerm) {
            val startTerm = andTerm.terms[0] as ReceivedDateTerm
            assertEquals(startTerm.comparison, ComparisonTerm.GE)
            assertEquals(startTerm.date, startDate)

            val endTerm = andTerm.terms[1] as ReceivedDateTerm
            assertEquals(endTerm.comparison, ComparisonTerm.LE)
            assertEquals(endTerm.date, endDate)
        }
    }

    @Test fun withReceivedBetweenMethodWithRangeShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withReceivedBetween(startDate .. endDate)

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm: AndTerm = terms.get() as AndTerm

        val startTerm = andTerm.terms[0] as ReceivedDateTerm
        assertEquals(startTerm.comparison, ComparisonTerm.GE)
        assertEquals(startTerm.date, startDate)

        val endTerm = andTerm.terms[1] as ReceivedDateTerm
        assertEquals(endTerm.comparison, ComparisonTerm.LE)
        assertEquals(endTerm.date, endDate)
    }

    @Test fun sentMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        val sentTerm = sb.sent(compTerm, date)

        assertEquals(sentTerm.comparison, compTerm)
        assertEquals(sentTerm.date, date)

    }

    @Test fun withSentMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        sb.withSent(compTerm, date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sentTerm: SentDateTerm = terms.get() as SentDateTerm

        assertEquals(sentTerm.comparison, compTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun sentOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        val sentTerm = sb.sentOn(date)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun withSentOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        sb.withSentOn(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sentTerm: SentDateTerm = terms.get() as SentDateTerm

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun sentOnOrAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        val sentTerm = sb.sentOnOrAfter(date)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun withSentOnOrAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        sb.withSentOnOrAfter(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sentTerm: SentDateTerm = terms.get() as SentDateTerm

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun sentAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        val sentTerm = sb.sentAfter(date)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun withSentAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        sb.withSentAfter(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sentTerm: SentDateTerm = terms.get() as SentDateTerm

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun sentOnOrBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        val sentTerm = sb.sentOnOrBefore(date)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun withSentOnOrBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        sb.withSentOnOrBefore(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sentTerm: SentDateTerm = terms.get() as SentDateTerm

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun sentBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        val sentTerm = sb.sentBefore(date)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)

    }

    @Test fun withSentBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        sb.withSentBefore(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sentTerm: SentDateTerm = terms.get() as SentDateTerm

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun notSentOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        val sentTerm = sb.notSentOn(date)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun withNotSentOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        sb.withNotSentOn(date)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sentTerm: SentDateTerm = terms.get() as SentDateTerm

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.date, date)
    }

    @Test fun sentBetweenMethodWithTwoParamsShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.sentBetween(startDate, endDate)

        assertTrue(andTerm is AndTerm)
        with (andTerm as AndTerm) {
            val startTerm = andTerm.terms[0] as SentDateTerm
            assertEquals(startTerm.comparison, ComparisonTerm.GE)
            assertEquals(startTerm.date, startDate)

            val endTerm = andTerm.terms[1] as SentDateTerm
            assertEquals(endTerm.comparison, ComparisonTerm.LE)
            assertEquals(endTerm.date, endDate)
        }
    }

    @Test fun withSentBetweenMethodWithTwoParamsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withSentBetween(startDate, endDate)

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm: AndTerm = terms.get() as AndTerm

        val startTerm = andTerm.terms[0] as SentDateTerm
        assertEquals(startTerm.comparison, ComparisonTerm.GE)
        assertEquals(startTerm.date, startDate)

        val endTerm = andTerm.terms[1] as SentDateTerm
        assertEquals(endTerm.comparison, ComparisonTerm.LE)
        assertEquals(endTerm.date, endDate)
    }

    @Test fun sentBetweenMethodWithRangeShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.sentBetween(startDate .. endDate)

        assertTrue(andTerm is AndTerm)
        with (andTerm as AndTerm) {
            val startTerm = andTerm.terms[0] as SentDateTerm
            assertEquals(startTerm.comparison, ComparisonTerm.GE)
            assertEquals(startTerm.date, startDate)

            val endTerm = andTerm.terms[1] as SentDateTerm
            assertEquals(endTerm.comparison, ComparisonTerm.LE)
            assertEquals(endTerm.date, endDate)
        }
    }

    @Test fun withSentBetweenMethodWithRangeShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withSentBetween(startDate .. endDate)

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm: AndTerm = terms.get() as AndTerm

        val startTerm = andTerm.terms[0] as SentDateTerm
        assertEquals(startTerm.comparison, ComparisonTerm.GE)
        assertEquals(startTerm.date, startDate)

        val endTerm = andTerm.terms[1] as SentDateTerm
        assertEquals(endTerm.comparison, ComparisonTerm.LE)
        assertEquals(endTerm.date, endDate)
    }

    @Test fun messageIdMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val id = "1234"
        val messageIdTerm = sb.messageId(id)
        assertTrue(messageIdTerm is MessageIDTerm)
        assertEquals(messageIdTerm.pattern, id)
    }

    @Test fun withMessageIdMethodShouldProperlyFillSearchBuilderTerm() {
        val sb = SearchBuilder()

        val id = "1234"
        sb.withMessageId(id)
        val terms = sb.build()
        assertTrue(terms is Some)
        val messageIdTerm: MessageIDTerm = terms.get() as MessageIDTerm
        assertEquals(messageIdTerm.pattern, id)
    }

    @Test fun messageNumberMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val number = 1234
        val messageNumberTerm = sb.messageNumber(number)
        assertTrue(messageNumberTerm is MessageNumberTerm)
        assertEquals(messageNumberTerm.number, number)
    }

    @Test fun withMessageNumberMethodShouldProperlyFillSearchBuilderTerm() {
        val sb = SearchBuilder()

        val number = 1234
        sb.withMessageNumber(number)
        val terms = sb.build()
        assertTrue(terms is Some)
        val messageNumberTerm: MessageNumberTerm = terms.get() as MessageNumberTerm
        assertEquals(messageNumberTerm.number, number)
    }

    @Test fun sizeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val size = 1234
        val sizeTerm = sb.size(compTerm, size)

        assertEquals(sizeTerm.comparison, compTerm)
        assertEquals(sizeTerm.number, size)

    }

    @Test fun withSizeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val size = 1234
        sb.withSize(compTerm, size)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sizeTerm: SizeTerm = terms.get() as SizeTerm

        assertEquals(sizeTerm.comparison, compTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun sizeIsMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val size = 1234
        val sentTerm = sb.sizeIs(size)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.number, size)
    }

    @Test fun withSizeIsMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val size = 1234
        sb.withSizeIs(size)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sizeTerm: SizeTerm = terms.get() as SizeTerm

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun sizeIsAtLeastMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val size = 1234
        val sizeTerm = sb.sizeIsAtLeast(size)

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun withSizeIsAtLeastMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val size = 1234
        sb.withSizeIsAtLeast(size)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sizeTerm: SizeTerm = terms.get() as SizeTerm

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun sizeIsGreaterThanMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val size = 1234
        val sizeTerm = sb.sizeIsGreaterThan(size)

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun withSizeIsGreaterThanMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val size = 1234
        sb.withSizeIsGreaterThan(size)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sizeTerm: SizeTerm = terms.get() as SizeTerm

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun sizeIsNoMoreThanMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val size = 1234
        val sizeTerm = sb.sizeIsNoMoreThan(size)

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun withSizeIsNoMoreThanMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val size = 1234
        sb.withSizeIsNoMoreThan(size)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sizeTerm: SizeTerm = terms.get() as SizeTerm

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun sizeIsLessThanMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val size = 1234
        val sizeTerm = sb.sizeIsLessThan(size)

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)

    }

    @Test fun withSizeIsLessThanMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val size = 1234
        sb.withSizeIsLessThan(size)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sizeTerm: SizeTerm = terms.get() as SizeTerm

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun sizeIsNotMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val size = 1234
        val sentTerm = sb.sizeIsNot(size)

        assertEquals(sentTerm.comparison, expCompTerm)
        assertEquals(sentTerm.number, size)
    }

    @Test fun withSizeIsNotMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val size = 1234
        sb.withSizeIsNot(size)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sizeTerm: SizeTerm = terms.get() as SizeTerm

        assertEquals(sizeTerm.comparison, expCompTerm)
        assertEquals(sizeTerm.number, size)
    }

    @Test fun sizeBetweenMethodWithTwoParamsShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        val andTerm = sb.sizeBetween(startSize, endSize)

        assertTrue(andTerm is AndTerm)
        with (andTerm as AndTerm) {
            val startTerm = andTerm.terms[0] as SizeTerm
            assertEquals(startTerm.comparison, ComparisonTerm.GE)
            assertEquals(startTerm.number, startSize)

            val endTerm = andTerm.terms[1] as SizeTerm
            assertEquals(endTerm.comparison, ComparisonTerm.LE)
            assertEquals(endTerm.number, endSize)
        }
    }

    @Test fun withSizeBetweenMethodWithTwoParamsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        sb.withSizeBetween(startSize, endSize)

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm: AndTerm = terms.get() as AndTerm

        val startTerm = andTerm.terms[0] as SizeTerm
        assertEquals(startTerm.comparison, ComparisonTerm.GE)
        assertEquals(startTerm.number, startSize)

        val endTerm = andTerm.terms[1] as SizeTerm
        assertEquals(endTerm.comparison, ComparisonTerm.LE)
        assertEquals(endTerm.number, endSize)
    }

    @Test fun sizeBetweenMethodWithRangeShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        val andTerm = sb.sizeBetween(startSize .. endSize)

        assertTrue(andTerm is AndTerm)
        with (andTerm as AndTerm) {
            val startTerm = andTerm.terms[0] as SizeTerm
            assertEquals(startTerm.comparison, ComparisonTerm.GE)
            assertEquals(startTerm.number, startSize)

            val endTerm = andTerm.terms[1] as SizeTerm
            assertEquals(endTerm.comparison, ComparisonTerm.LE)
            assertEquals(endTerm.number, endSize)
        }
    }

    @Test fun withSizeBetweenMethodWithRangeShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        sb.withSizeBetween(startSize .. endSize)

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm: AndTerm = terms.get() as AndTerm

        val startTerm = andTerm.terms[0] as SizeTerm
        assertEquals(startTerm.comparison, ComparisonTerm.GE)
        assertEquals(startTerm.number, startSize)

        val endTerm = andTerm.terms[1] as SizeTerm
        assertEquals(endTerm.comparison, ComparisonTerm.LE)
        assertEquals(endTerm.number, endSize)
    }

    @Test fun flagsMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val flags = Flags()
        flags.add(Flags.Flag.DRAFT)
        flags.add(Flags.Flag.RECENT)
        val set = false
        val flagTerm = sb.flags(flags, set)

        assertTrue(flagTerm is FlagTerm)
        assertEquals(flagTerm.flags, flags)
        assertEquals(flagTerm.testSet, set)
    }

    @Test fun withFlagsMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val flags = Flags()
        flags.add(Flags.Flag.DRAFT)
        flags.add(Flags.Flag.RECENT)
        val set = true

        sb.withFlags(flags, set)

        val terms = sb.build()
        assertTrue(terms is Some)
        val flagTerm: FlagTerm = terms.get() as FlagTerm
        assertEquals(flagTerm.flags, flags)
        assertEquals(flagTerm.testSet, set)
    }

    @Test fun modifiedSinceMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val since = 1234L
        val sinceTerm = sb.modifiedSince(since)

        assertTrue(sinceTerm is ModifiedSinceTerm)
        assertEquals(sinceTerm.modSeq, since)
    }

    @Test fun withModifiedSinceMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val since = 1234L
        sb.withModifiedSince(since)

        val terms = sb.build()
        assertTrue(terms is Some)
        val sinceTerm: ModifiedSinceTerm = terms.get() as ModifiedSinceTerm
        assertEquals(sinceTerm.modSeq, since)
    }

    @Test fun olderMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val interval = 1234
        val olderTerm = sb.older(interval)

        assertTrue(olderTerm is OlderTerm)
        assertEquals(olderTerm.interval, interval)
    }

    @Test fun withOlderMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        sb.withOlder(interval)

        val terms = sb.build()
        assertTrue(terms is Some)
        val olderTerm: OlderTerm = terms.get() as OlderTerm
        assertEquals(olderTerm.interval, interval)
    }

    @Test fun youngerMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val interval = 1234
        val youngerTerm = sb.younger(interval)

        assertTrue(youngerTerm is YoungerTerm)
        assertEquals(youngerTerm.interval, interval)
    }

    @Test fun withYoungerMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        sb.withYounger(interval)

        val terms = sb.build()
        assertTrue(terms is Some)
        val youngerTerm: YoungerTerm = terms.get() as YoungerTerm
        assertEquals(youngerTerm.interval, interval)
    }

    @Test fun withMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        val youngerTerm = YoungerTerm(interval)
        sb.with(youngerTerm)

        val terms = sb.build()
        assertTrue(terms is Some)
        val youngerTermFromBuilder: YoungerTerm = terms.get() as YoungerTerm
        assertEquals(youngerTermFromBuilder, youngerTerm)
    }

    @Test fun unaryOperatorsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        val youngerTerm = YoungerTerm(interval)
        val olderTerm = OlderTerm(interval)
        with (sb) {
            + youngerTerm
            - olderTerm
        }

        val terms = sb.build()
        assertTrue(terms is Some)
        val andTerm = terms.get() as AndTerm

        val youngerTermFromBuilder: YoungerTerm = andTerm.terms[0] as YoungerTerm
        assertEquals(youngerTermFromBuilder, youngerTerm)

        val notOlderTermFromBuilder: NotTerm = andTerm.terms[1] as NotTerm
        val olderTermFromBuilder = notOlderTermFromBuilder.term as OlderTerm
        assertEquals(olderTermFromBuilder, olderTerm)
    }
}