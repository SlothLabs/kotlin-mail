package com.github.slothLabs.mail.imap

import org.funktionale.option.Option.*
import org.junit.Assert.*
import org.junit.Test
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.search.BodyTerm
import javax.mail.search.FromStringTerm
import javax.mail.search.FromTerm
import javax.mail.search.RecipientStringTerm
import javax.mail.search.RecipientTerm
import javax.mail.search.SubjectTerm


class BasicSearchBuilderTests {

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
}