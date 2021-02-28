package io.github.slothLabs.mail.imap

import com.icegreen.greenmail.util.GreenMail
import java.time.Instant
import java.time.temporal.TemporalUnit
import java.util.Date

fun Instant.toDate(): Date = Date.from(this)

fun GreenMail.firstReceivedMailSentDatePlusOffset(amount: Long, units: TemporalUnit): Date {
    val firstReceivedMailSentDate = receivedMessages.first().sentDate
    return firstReceivedMailSentDate.toInstant().plus(amount, units).toDate()
}
