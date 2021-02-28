package io.github.slothLabs.mail.imap

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetupTest
import com.sun.mail.imap.IMAPFolder.FetchProfileItem
import com.sun.mail.imap.SortTerm
import io.github.slothLabs.mail.imap.Sort.From
import io.github.slothLabs.mail.imap.Sort.Subject
import io.github.slothLabs.mail.imap.Sort.To
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.temporal.ChronoUnit

class ImapConnectionTest : AnnotationSpec() {

    private val greenMail = GreenMail(ServerSetupTest.SMTP_IMAP)

    private val host: String
        get() = greenMail.imap.bindTo

    private val port: Int
        get() = greenMail.imap.port

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        greenMail.reset()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        greenMail.stop()
    }

    @Test
    fun shouldHandlePlainTextBody() {
        val emailAddress = "test@localhost.com"
        val fromAddress = "from@localhost.com"
        val password = "password"
        val subject = "Test Email"
        val testBodyText = "Body Text goes here!"

        greenMail.setUser(emailAddress, password)

        GreenMailUtil.sendTextEmailTest(emailAddress, fromAddress, subject, testBodyText)

        var processed = false
        imap(host, port, emailAddress, password) {
            folder("INBOX", FolderModes.ReadOnly) {
                val msg = this[1]
                msg.shouldNotBeNull()
                msg {
                    testBodyText shouldBe bodyText
                    headers.isEmpty() shouldBe false

                    processed = true
                }
            }
        }

        processed shouldBe true
    }

    @Test
    fun shouldHandlePlainTextBodyWithConnectionInformationObject() {
        val emailAddress = "test@localhost.com"
        val fromAddress = "from@localhost.com"
        val password = "password"
        val subject = "Test Email"
        val testBodyText = "Body Text goes here!"

        greenMail.setUser(emailAddress, password)

        GreenMailUtil.sendTextEmailTest(emailAddress, fromAddress, subject, testBodyText)

        var processed = false

        val conInfo = ConnectionInformation(host, port, emailAddress, password)
        imap(conInfo) {
            folder("INBOX", FolderModes.ReadOnly) {
                val msg = this[1]
                msg.shouldNotBeNull()
                msg {
                    testBodyText shouldBe bodyText
                    headers.isEmpty() shouldBe false

                    processed = true
                }
            }
        }

        processed shouldBe true
    }

    @Test
    fun searchStuff() {
        val emailAddress = "test@localhost.com"
        val fromAddress = "from@localhost.com"
        val password = "password"
        val subject = "Test Email"
        val testBodyText = "Body Text goes here!"

        greenMail.setUser(emailAddress, password)

        GreenMailUtil.sendTextEmailTest(emailAddress, fromAddress, subject, testBodyText)
        val mailSentDatePlusOneHour = greenMail.firstReceivedMailSentDatePlusOffset(1, ChronoUnit.HOURS)

        var processed = false

        val conInfo = ConnectionInformation(host, port, emailAddress, password)
        val msgList = mutableListOf<Message>()
        imap(conInfo) {
            folder("INBOX", FolderModes.ReadOnly) {
                preFetchBy(FetchProfileItem.MESSAGE)
                val results = search {
                    withFrom(fromAddress)
                    withSentOnOrBefore(mailSentDatePlusOneHour)
                }
                msgList.addAll(results)

                processed = true
            }
        }

        msgList.isNotEmpty() shouldBe true

        val first = msgList[0]
        fromAddress shouldBe first.from
        testBodyText.trim() shouldBe first.bodyText.trim()
        first.headers.isEmpty() shouldBe false

        processed shouldBe true
    }

    @Test
    fun otherSearchStuff() {
        val emailAddress = "test@localhost.com"
        val fromAddress = "from@localhost.com"
        val password = "password"
        val subject = "Test Email"
        val testBodyText = "Body Text goes here!"

        greenMail.setUser(emailAddress, password)

        GreenMailUtil.sendTextEmailTest(emailAddress, fromAddress, subject, testBodyText)
        val mailSentDatePlusOneHour = greenMail.firstReceivedMailSentDatePlusOffset(1, ChronoUnit.HOURS)

        var processed = false

        val conInfo = ConnectionInformation(host, port, emailAddress, password)
        val msgList = mutableListOf<Message>()
        imap(conInfo) {
            folder("INBOX", FolderModes.ReadOnly) {
                preFetchBy(FetchProfileItem.MESSAGE)
                val results = search {
                    +from(fromAddress)
                    +to(emailAddress)
                    -subject("Testing")

                    +sentOnOrBefore(mailSentDatePlusOneHour)
                }
                msgList.addAll(results)

                processed = true
            }
        }

        msgList.isNotEmpty() shouldBe true

        val first = msgList[0]
        fromAddress shouldBe first.from
        testBodyText.trim() shouldBe first.bodyText.trim()
        first.headers.isEmpty() shouldBe false

        processed shouldBe true
    }

    @Test
    fun searchStuffWithSorting() {
        val emailAddress = "test@localhost.com"
        val fromAddress = "from@localhost.com"
        val password = "password"
        val subject = "Test Email"
        val testBodyText = "Body Text goes here!"

        greenMail.setUser(emailAddress, password)

        GreenMailUtil.sendTextEmailTest(emailAddress, fromAddress, subject, testBodyText)
        val mailSentDatePlusOneHour = greenMail.firstReceivedMailSentDatePlusOffset(1, ChronoUnit.HOURS)

        var processed = false

        val conInfo = ConnectionInformation(host, port, emailAddress, password)
        val msgList = mutableListOf<Message>()
        imap(conInfo) {
            folder("INBOX", FolderModes.ReadOnly) {
                preFetchBy(FetchProfileItem.MESSAGE)
                val results = search {
                    +from(fromAddress)
                    +to(emailAddress)
                    -subject("Testing")

                    +sentOnOrBefore(mailSentDatePlusOneHour)

                    sortedBy {
                        +From
                        +To
                        -Sort.Size
                        -Subject
                    }
                }
                msgList.addAll(results)


                processed = true
            }
        }

        msgList.isNotEmpty() shouldBe true

        val first = msgList[0]
        fromAddress shouldBe first.from
        testBodyText.trim() shouldBe first.bodyText.trim()
        first.headers.isEmpty() shouldBe false

        processed shouldBe true
    }

    @Test
    fun sortingNoSearch() {
        val emailAddress = "test@localhost.com"
        val fromAddress = "from@localhost.com"
        val password = "password"
        val subject = "Test Email"
        val testBodyText = "Body Text goes here!"

        greenMail.setUser(emailAddress, password)

        GreenMailUtil.sendTextEmailTest(emailAddress, fromAddress, subject, testBodyText)

        var processed = false

        val conInfo = ConnectionInformation(host, port, emailAddress, password)
        val msgList = mutableListOf<Message>()
        imap(conInfo) {
            folder("INBOX", FolderModes.ReadOnly) {
                preFetchBy(FetchProfileItem.MESSAGE)
                val results = sortedBy {
                    +From
                    +To
                    -SortTerm.SIZE
                    -Subject
                    +SortTerm.ARRIVAL
                    -Sort.fromSortTerm(SortTerm.CC)!!
                }

                msgList.addAll(results)

                processed = true
            }
        }

        msgList.isNotEmpty() shouldBe true

        val first = msgList[0]
        fromAddress shouldBe first.from
        testBodyText.trim() shouldBe first.bodyText.trim()
        first.headers.isEmpty() shouldBe false

        processed shouldBe true
    }
}
