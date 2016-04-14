package com.github.slothLabs.mail.imap

import com.icegreen.greenmail.junit.GreenMailRule
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetupTest
import org.funktionale.option.Option
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ImapConnectionTest {
    @get:Rule
    val greenMail = GreenMailRule(ServerSetupTest.SMTP_IMAP)

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
        imap(host = greenMail.imap.bindTo, port = greenMail.imap.port, user = emailAddress, password = password) {
            folder("INBOX", FolderModes.ReadOnly) {
                assertTrue(messages[1] is Option.Some)
                messages[1] {
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

        val conInfo = ConnectionInformation(greenMail.imap.bindTo, greenMail.imap.port, emailAddress, password)
        imap(conInfo) {
            folder("INBOX", FolderModes.ReadOnly) {
                assertTrue(messages[1] is Option.Some)
                messages[1] {
                    assertEquals(testBodyText, bodyText)
                    assertNotNull(uid)
                    assertFalse(headers.isEmpty())

                    processed = true
                }
            }
        }

        assertTrue(processed)
    }
}