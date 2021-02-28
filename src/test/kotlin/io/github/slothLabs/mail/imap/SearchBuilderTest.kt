package io.github.slothLabs.mail.imap

import com.sun.mail.imap.ModifiedSinceTerm
import com.sun.mail.imap.OlderTerm
import com.sun.mail.imap.YoungerTerm
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertNotSame
import java.util.Date
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.search.AndTerm
import javax.mail.search.BodyTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.FlagTerm
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

class SearchBuilderTest : AnnotationSpec() {

    private var sb = SearchBuilder()

    override fun beforeTest(testCase: TestCase) {
        sb = SearchBuilder()
    }

    @Test
    fun searchBuilderBuildWithoutTermsShouldReturnNull() {
        val res = sb.build()
        res.shouldBeNull()
    }

    @Test
    fun searchBuilderBuildWithTermsShouldReturnNotNull() {
        sb.withFrom("test@drive.com")
        val res = sb.build()
        res.shouldNotBeNull()
    }

    @Test
    fun fromShouldConstructAppropriateInternetAddressInstance() {
        val address = InternetAddress("test@drive.com")
        val fromInternetAddress = sb.from(address)

        fromInternetAddress.address shouldBe address
    }

    @Test
    fun fromShouldConstructAppropriateStringInstance() {
        val str = "another@example.com"
        val fromString = sb.from(str)

        fromString.pattern shouldBe str
    }

    @Test
    fun withFromShouldProperlyFillWithInternetAddressBasedTerm() {
        val address = InternetAddress("test@drive.com")
        sb.withFrom(address)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val fromTerm = terms as FromTerm
        fromTerm.address shouldBe address
    }

    @Test
    fun recipientShouldConstructAppropriateInternetAddressInstance() {
        val recipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.recipient(recipientType, address)

        recipientInternetAddress.recipientType shouldBe recipientType
        recipientInternetAddress.address shouldBe address
    }

    @Test
    fun recipientShouldConstructAppropriateStringBasedTerm() {
        val recipientType = MimeMessage.RecipientType.TO

        val str = "another@example.com"
        val recipientString = sb.recipient(recipientType, str)

        recipientString.recipientType shouldBe recipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun withRecipientMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val recipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        sb1.withRecipient(recipientType, address)
        val terms1 = sb1.build()
        terms1.shouldNotBeNull()

        val recipientInternetAddress = terms1 as RecipientTerm
        recipientInternetAddress.recipientType shouldBe recipientType
        recipientInternetAddress.address shouldBe address

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withRecipient(recipientType, str)
        val terms2 = sb2.build()
        terms2.shouldNotBeNull()

        val recipientString = terms2 as RecipientStringTerm
        recipientString.recipientType shouldBe recipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun toMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.to(address)

        recipientInternetAddress.recipientType shouldBe expRecipientType
        recipientInternetAddress.address shouldBe address

        val str = "another@example.com"
        val recipientString = sb.to(str)

        recipientString.recipientType shouldBe expRecipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun withToMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.TO

        val address = InternetAddress("test@drive.com")
        sb1.withTo(address)
        val terms1 = sb1.build()
        terms1.shouldNotBeNull()

        val recipientInternetAddress = terms1 as RecipientTerm
        recipientInternetAddress.recipientType shouldBe expRecipientType
        recipientInternetAddress.address shouldBe address

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withTo(str)
        val terms2 = sb2.build()
        terms2.shouldNotBeNull()

        val recipientString = terms2 as RecipientStringTerm
        recipientString.recipientType shouldBe expRecipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun ccMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.CC

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.cc(address)

        recipientInternetAddress.recipientType shouldBe expRecipientType
        recipientInternetAddress.address shouldBe address

        val str = "another@example.com"
        val recipientString = sb.cc(str)

        recipientString.recipientType shouldBe expRecipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun withCCMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.CC

        val address = InternetAddress("test@drive.com")
        sb1.withCC(address)
        val terms1 = sb1.build()
        terms1.shouldNotBeNull()

        val recipientInternetAddress = terms1 as RecipientTerm
        recipientInternetAddress.recipientType shouldBe expRecipientType
        recipientInternetAddress.address shouldBe address

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withCC(str)
        val terms2 = sb2.build()
        terms2.shouldNotBeNull()

        val recipientString = terms2 as RecipientStringTerm
        recipientString.recipientType shouldBe expRecipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun bccMethodsShouldConstructAppropriateSearchTerms() {
        val sb = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.BCC

        val address = InternetAddress("test@drive.com")
        val recipientInternetAddress = sb.bcc(address)

        recipientInternetAddress.recipientType shouldBe expRecipientType
        recipientInternetAddress.address shouldBe address

        val str = "another@example.com"
        val recipientString = sb.bcc(str)

        recipientString.recipientType shouldBe expRecipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun withBCCMethodsShouldProperlyFillSearchBuilderTerms() {
        val sb1 = SearchBuilder()
        val expRecipientType = MimeMessage.RecipientType.BCC

        val address = InternetAddress("test@drive.com")
        sb1.withBCC(address)
        val terms1 = sb1.build()
        terms1.shouldNotBeNull()

        val recipientInternetAddress = terms1 as RecipientTerm
        recipientInternetAddress.recipientType shouldBe expRecipientType
        recipientInternetAddress.address shouldBe address

        val sb2 = SearchBuilder()
        val str = "another@example.com"
        sb2.withBCC(str)
        val terms2 = sb2.build()
        terms1.shouldNotBeNull()

        val recipientString = terms2 as RecipientStringTerm
        recipientString.recipientType shouldBe expRecipientType
        recipientString.pattern shouldBe str
    }

    @Test
    fun subjectMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val subject = "Test Subject"
        val subjectTerm = sb.subject(subject)

        subjectTerm.pattern shouldBe subject
    }

    @Test
    fun withSubjectMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val subject = "Test Subject"
        sb.withSubject(subject)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val subjectTerm = terms as SubjectTerm
        subjectTerm.pattern shouldBe subject
    }

    @Test
    fun bodyMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val body = "Test Body"
        val bodyTerm = sb.body(body)

        bodyTerm.pattern shouldBe body
    }

    @Test
    fun withBodyMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val body = "Test Body"
        sb.withBody(body)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val bodyTerm = terms as BodyTerm
        bodyTerm.pattern shouldBe body
    }

    @Test
    fun headerMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val headerName = "TheHeader"
        val headerValue = "Header Value"
        val headerTerm = sb.header(headerName, headerValue)

        headerTerm.headerName shouldBe headerName
        headerTerm.pattern shouldBe headerValue
    }

    @Test
    fun withHeaderMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val headerName = "TheHeader"
        val headerValue = "Header Value"
        sb.withHeader(headerName, headerValue)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val headerTerm = terms as HeaderTerm
        headerTerm.headerName shouldBe headerName
        headerTerm.pattern shouldBe headerValue
    }

    @Test
    fun orMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        val orTerm = sb.or(firstFrom, secondFrom) as OrTerm

        orTerm.terms[0] shouldBe firstFrom
        orTerm.terms[1] shouldBe secondFrom
    }

    @Test
    fun withOrMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        sb.withOr(firstFrom, secondFrom)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val orTerm = terms as OrTerm

        orTerm.terms[0] shouldBe firstFrom
        orTerm.terms[1] shouldBe secondFrom
    }

    @Test
    fun andMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        val andTerm = sb.and(firstFrom, secondFrom)

        andTerm.terms[0] shouldBe firstFrom
        andTerm.terms[1] shouldBe secondFrom

    }

    @Test
    fun withAndMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val firstFrom = sb.from("test@drive.com")
        val secondFrom = sb.from("another@example.com")
        sb.withAnd(firstFrom, secondFrom)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm


        andTerm.terms[0] shouldBe firstFrom
        andTerm.terms[1] shouldBe secondFrom
    }

    @Test
    fun receivedMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        val receivedTerm = sb.received(compTerm, date)

        receivedTerm.comparison shouldBe compTerm
        receivedTerm.date shouldBe date

    }

    @Test
    fun withReceivedMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        sb.withReceived(compTerm, date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val receivedTerm = terms as ReceivedDateTerm

        receivedTerm.comparison shouldBe compTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun receivedOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        val receivedTerm = sb.receivedOn(date)

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun withReceivedOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        sb.withReceivedOn(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val receivedTerm = terms as ReceivedDateTerm

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun receivedOnOrAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        val receivedTerm = sb.receivedOnOrAfter(date)

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun withReceivedOnOrAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        sb.withReceivedOnOrAfter(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val receivedTerm = terms as ReceivedDateTerm

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun receivedAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        val receivedTerm = sb.receivedAfter(date)

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun withReceivedAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        sb.withReceivedAfter(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val receivedTerm = terms as ReceivedDateTerm

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun receivedOnOrBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        val receivedTerm = sb.receivedOnOrBefore(date)

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun withReceivedOnOrBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        sb.withReceivedOnOrBefore(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val receivedTerm = terms as ReceivedDateTerm

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun receivedBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        val receivedTerm = sb.receivedBefore(date)

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun withReceivedBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        sb.withReceivedBefore(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val receivedTerm = terms as ReceivedDateTerm

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun notReceivedOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        val receivedTerm = sb.notReceivedOn(date)

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun withNotReceivedOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        sb.withNotReceivedOn(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val receivedTerm = terms as ReceivedDateTerm

        receivedTerm.comparison shouldBe expCompTerm
        receivedTerm.date shouldBe date
    }

    @Test
    fun receivedBetweenMethodWithTwoParamsShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.receivedBetween(startDate, endDate) as AndTerm

        val startTerm = andTerm.terms[0] as ReceivedDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as ReceivedDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun withReceivedBetweenMethodWithTwoParamsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withReceivedBetween(startDate, endDate)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm

        val startTerm = andTerm.terms[0] as ReceivedDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as ReceivedDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun receivedBetweenMethodWithRangeShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.receivedBetween(startDate..endDate) as AndTerm

        val startTerm = andTerm.terms[0] as ReceivedDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as ReceivedDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun withReceivedBetweenMethodWithRangeShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withReceivedBetween(startDate..endDate)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm

        val startTerm = andTerm.terms[0] as ReceivedDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as ReceivedDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun sentMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        val sentTerm = sb.sent(compTerm, date)

        sentTerm.comparison shouldBe compTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun withSentMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val date = Date()
        sb.withSent(compTerm, date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sentTerm = terms as SentDateTerm

        sentTerm.comparison shouldBe compTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun sentOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        val sentTerm = sb.sentOn(date)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun withSentOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val date = Date()
        sb.withSentOn(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sentTerm = terms as SentDateTerm

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun sentOnOrAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        val sentTerm = sb.sentOnOrAfter(date)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun withSentOnOrAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val date = Date()
        sb.withSentOnOrAfter(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sentTerm = terms as SentDateTerm

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun sentAfterMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        val sentTerm = sb.sentAfter(date)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun withSentAfterMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val date = Date()
        sb.withSentAfter(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sentTerm = terms as SentDateTerm

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun sentOnOrBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        val sentTerm = sb.sentOnOrBefore(date)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun withSentOnOrBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val date = Date()
        sb.withSentOnOrBefore(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sentTerm = terms as SentDateTerm

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun sentBeforeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        val sentTerm = sb.sentBefore(date)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun withSentBeforeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val date = Date()
        sb.withSentBefore(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sentTerm = terms as SentDateTerm

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun notSentOnMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        val sentTerm = sb.notSentOn(date)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun withNotSentOnMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val date = Date()
        sb.withNotSentOn(date)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sentTerm = terms as SentDateTerm

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.date shouldBe date
    }

    @Test
    fun sentBetweenMethodWithTwoParamsShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.sentBetween(startDate, endDate) as AndTerm

        val startTerm = andTerm.terms[0] as SentDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as SentDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun withSentBetweenMethodWithTwoParamsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withSentBetween(startDate, endDate)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm

        val startTerm = andTerm.terms[0] as SentDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as SentDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun sentBetweenMethodWithRangeShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        val andTerm = sb.sentBetween(startDate..endDate) as AndTerm

        val startTerm = andTerm.terms[0] as SentDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as SentDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun withSentBetweenMethodWithRangeShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startDate = Date()
        val endDate = Date()
        assertNotSame(startDate, endDate)
        sb.withSentBetween(startDate..endDate)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm

        val startTerm = andTerm.terms[0] as SentDateTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.date shouldBe startDate

        val endTerm = andTerm.terms[1] as SentDateTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.date shouldBe endDate
    }

    @Test
    fun messageIdMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val id = "1234"
        val messageIdTerm = sb.messageId(id)

        messageIdTerm.pattern shouldBe id
    }

    @Test
    fun withMessageIdMethodShouldProperlyFillSearchBuilderTerm() {
        val sb = SearchBuilder()

        val id = "1234"
        sb.withMessageId(id)
        val terms = sb.build()
        terms.shouldNotBeNull()

        val messageIdTerm = terms as MessageIDTerm

        messageIdTerm.pattern shouldBe id
    }

    @Test
    fun messageNumberMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val number = 1234
        val messageNumberTerm = sb.messageNumber(number)

        messageNumberTerm.number shouldBe number
    }

    @Test
    fun withMessageNumberMethodShouldProperlyFillSearchBuilderTerm() {
        val sb = SearchBuilder()

        val number = 1234
        sb.withMessageNumber(number)
        val terms = sb.build()
        terms.shouldNotBeNull()

        val messageNumberTerm = terms as MessageNumberTerm
        messageNumberTerm.number shouldBe number
    }

    @Test
    fun sizeMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val size = 1234
        val sizeTerm = sb.size(compTerm, size)

        sizeTerm.comparison shouldBe compTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun withSizeMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val compTerm = ComparisonTerm.GE
        val size = 1234
        sb.withSize(compTerm, size)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sizeTerm = terms as SizeTerm

        sizeTerm.comparison shouldBe compTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun sizeIsMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val size = 1234
        val sentTerm = sb.sizeIs(size)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.number shouldBe size
    }

    @Test
    fun withSizeIsMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.EQ
        val size = 1234
        sb.withSizeIs(size)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sizeTerm = terms as SizeTerm

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun sizeIsAtLeastMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val size = 1234
        val sizeTerm = sb.sizeIsAtLeast(size)

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun withSizeIsAtLeastMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GE
        val size = 1234
        sb.withSizeIsAtLeast(size)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sizeTerm = terms as SizeTerm

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun sizeIsGreaterThanMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val size = 1234
        val sizeTerm = sb.sizeIsGreaterThan(size)

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun withSizeIsGreaterThanMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.GT
        val size = 1234
        sb.withSizeIsGreaterThan(size)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sizeTerm = terms as SizeTerm

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun sizeIsNoMoreThanMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val size = 1234
        val sizeTerm = sb.sizeIsNoMoreThan(size)

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun withSizeIsNoMoreThanMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LE
        val size = 1234
        sb.withSizeIsNoMoreThan(size)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sizeTerm = terms as SizeTerm

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun sizeIsLessThanMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val size = 1234
        val sizeTerm = sb.sizeIsLessThan(size)

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun withSizeIsLessThanMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.LT
        val size = 1234
        sb.withSizeIsLessThan(size)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sizeTerm = terms as SizeTerm

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun sizeIsNotMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val size = 1234
        val sentTerm = sb.sizeIsNot(size)

        sentTerm.comparison shouldBe expCompTerm
        sentTerm.number shouldBe size
    }

    @Test
    fun withSizeIsNotMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val expCompTerm = ComparisonTerm.NE
        val size = 1234
        sb.withSizeIsNot(size)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sizeTerm = terms as SizeTerm

        sizeTerm.comparison shouldBe expCompTerm
        sizeTerm.number shouldBe size
    }

    @Test
    fun sizeBetweenMethodWithTwoParamsShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        val andTerm = sb.sizeBetween(startSize, endSize) as AndTerm

        val startTerm = andTerm.terms[0] as SizeTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.number shouldBe startSize

        val endTerm = andTerm.terms[1] as SizeTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.number shouldBe endSize
    }

    @Test
    fun withSizeBetweenMethodWithTwoParamsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        sb.withSizeBetween(startSize, endSize)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm

        val startTerm = andTerm.terms[0] as SizeTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.number shouldBe startSize

        val endTerm = andTerm.terms[1] as SizeTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.number shouldBe endSize
    }

    @Test
    fun sizeBetweenMethodWithRangeShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        val andTerm = sb.sizeBetween(startSize..endSize) as AndTerm

        val startTerm = andTerm.terms[0] as SizeTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.number shouldBe startSize

        val endTerm = andTerm.terms[1] as SizeTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.number shouldBe endSize
    }

    @Test
    fun withSizeBetweenMethodWithRangeShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val startSize = 1234
        val endSize = 4321
        assertNotSame(startSize, endSize)
        sb.withSizeBetween(startSize..endSize)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm

        val startTerm = andTerm.terms[0] as SizeTerm
        startTerm.comparison shouldBe ComparisonTerm.GE
        startTerm.number shouldBe startSize

        val endTerm = andTerm.terms[1] as SizeTerm
        endTerm.comparison shouldBe ComparisonTerm.LE
        endTerm.number shouldBe endSize
    }

    @Test
    fun flagsMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()
        val flags = Flags(Flag.Draft, Flag.Recent)
        val set = false
        val flagTerm = sb.flags(flags, set)

        flagTerm.flags shouldBe flags.javaMailFlags
        flagTerm.testSet shouldBe set
    }

    @Test
    fun withFlagsMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val flags = Flags(Flag.Draft, Flag.Recent)
        val set = true

        sb.withFlags(flags, set)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val flagTerm = terms as FlagTerm
        flagTerm.flags shouldBe flags.javaMailFlags
        flagTerm.testSet shouldBe set
    }

    @Test
    fun modifiedSinceMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val since = 1234L
        val sinceTerm = sb.modifiedSince(since)

        sinceTerm.modSeq shouldBe since
    }

    @Test
    fun withModifiedSinceMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val since = 1234L
        sb.withModifiedSince(since)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val sinceTerm = terms as ModifiedSinceTerm

        sinceTerm.modSeq shouldBe since
    }

    @Test
    fun olderMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val interval = 1234
        val olderTerm = sb.older(interval)

        olderTerm.interval shouldBe interval
    }

    @Test
    fun withOlderMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        sb.withOlder(interval)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val olderTerm = terms as OlderTerm
        olderTerm.interval shouldBe interval
    }

    @Test
    fun youngerMethodShouldConstructAppropriateSearchTerm() {
        val sb = SearchBuilder()

        val interval = 1234
        val youngerTerm = sb.younger(interval)

        youngerTerm.interval shouldBe interval
    }

    @Test
    fun withYoungerMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        sb.withYounger(interval)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val youngerTerm = terms as YoungerTerm

        youngerTerm.interval shouldBe interval
    }

    @Test
    fun withMethodShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        val youngerTerm = YoungerTerm(interval)
        sb.with(youngerTerm)

        val terms = sb.build()
        terms.shouldNotBeNull()

        val youngerTermFromBuilder = terms as YoungerTerm

        youngerTermFromBuilder shouldBe youngerTerm
    }

    @Test
    fun unaryOperatorsShouldProperlyFillSearchBuilderTerms() {
        val sb = SearchBuilder()

        val interval = 1234
        val youngerTerm = YoungerTerm(interval)
        val olderTerm = OlderTerm(interval)
        with(sb) {
            +youngerTerm
            -olderTerm
        }

        val terms = sb.build()
        terms.shouldNotBeNull()

        val andTerm = terms as AndTerm

        val youngerTermFromBuilder = andTerm.terms[0] as YoungerTerm
        youngerTermFromBuilder shouldBe youngerTerm

        val notOlderTermFromBuilder = andTerm.terms[1] as NotTerm
        val olderTermFromBuilder = notOlderTermFromBuilder.term as OlderTerm
        olderTermFromBuilder shouldBe olderTerm
    }

    @Test
    fun markAsReadShouldProperlySetShouldSetSeenFlag() {
        val sb1 = SearchBuilder()
        with(sb1) {
            markAsRead(true)
        }
        sb1.shouldSetSeenFlag shouldBe true

        val sb2 = SearchBuilder()
        with(sb2) {
            markAsRead(false)
        }
        sb2.shouldSetSeenFlag shouldBe false

        val sb3 = SearchBuilder()
        sb3.shouldSetSeenFlag shouldBe false
    }
}
