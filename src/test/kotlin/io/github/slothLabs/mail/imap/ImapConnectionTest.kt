package io.github.slothLabs.mail.imap

import com.icegreen.greenmail.junit.GreenMailRule
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetupTest
import com.sun.mail.imap.IMAPFolder.FetchProfileItem
import org.funktionale.option.Option
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import io.github.slothLabs.mail.imap.Sort.*

class ImapConnectionTest {
    @get:Rule
    val greenMail = GreenMailRule(ServerSetupTest.SMTP_IMAP)

    val host: String
        get() = greenMail.imap.bindTo

    val port: Int
        get() = greenMail.imap.port

    @Before fun setup() {
        greenMail.start()
    }

    @After fun tearDown() {
        greenMail.stop()
    }

    @Test fun shouldHandlePlainTextBody() {
        val emailAddress = "test@localhost.com"
        val fromAddress = "from@localhost.com"
        val password = "password"
        val subject = "Test Email"
        val testBodyText = "Body Text goes here!"

        greenMail.setUser(emailAddress, password)

        GreenMailUtil.sendTextEmailTest(emailAddress, fromAddress, subject, testBodyText)

        var processed = false
        imap(host, port, user = emailAddress, password = password) {
            folder("INBOX", FolderModes.ReadOnly) {
                val msg = this[1]
                assertTrue(msg is Option.Some)
                msg {
                    assertEquals(testBodyText, bodyText)
                    assertNotNull(uid)
                    assertFalse(headers.isEmpty())

                    processed = true
                }
            }
        }

        assertTrue(processed)
    }

    @Test fun shouldHandlePlainTextBodyWithConnectionInformationObject() {
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
                assertTrue(msg is Option.Some)
                msg {
                    assertEquals(testBodyText, bodyText)
                    assertNotNull(uid)
                    assertFalse(headers.isEmpty())

                    processed = true
                }
            }
        }

        assertTrue(processed)
    }

    @Test fun searchStuff() {
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
                val results = search {
                    withFrom(fromAddress)
                    withSentOnOrBefore(Date())
                }
                msgList.addAll(results)


                processed = true
            }
        }

        assertTrue(msgList.isNotEmpty())

        val first = msgList[0]
        assertEquals(fromAddress, first.from)
        assertEquals(testBodyText.trim(), first.bodyText.trim())
        assertNotNull(first.uid)
        assertFalse(first.headers.isEmpty())

        assertTrue(processed)
    }

    @Test fun otherSearchStuff() {
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
                val results = search {
                    +from(fromAddress)
                    +to(emailAddress)
                    -subject("Testing")

                    +sentOnOrBefore(Date())
                }
                msgList.addAll(results)


                processed = true
            }
        }

        assertTrue(msgList.isNotEmpty())

        val first = msgList[0]
        assertEquals(fromAddress, first.from)
        assertEquals(testBodyText.trim(), first.bodyText.trim())
        assertNotNull(first.uid)
        assertFalse(first.headers.isEmpty())

        assertTrue(processed)
    }

    @Test fun searchStuffWithSorting() {
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
                val results = search {
                    +from(fromAddress)
                    +to(emailAddress)
                    -subject("Testing")

                    +sentOnOrBefore(Date())

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

        assertTrue(msgList.isNotEmpty())

        val first = msgList[0]
        assertEquals(fromAddress, first.from)
        assertEquals(testBodyText.trim(), first.bodyText.trim())
        assertNotNull(first.uid)
        assertFalse(first.headers.isEmpty())

        assertTrue(processed)
    }

    @Test fun sortingNoSearch() {
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
                    -Sort.Size
                    -Subject
                }

                msgList.addAll(results)

                processed = true
            }
        }

        assertTrue(msgList.isNotEmpty())

        val first = msgList[0]
        assertEquals(fromAddress, first.from)
        assertEquals(testBodyText.trim(), first.bodyText.trim())
        assertNotNull(first.uid)
        assertFalse(first.headers.isEmpty())

        assertTrue(processed)
    }
}