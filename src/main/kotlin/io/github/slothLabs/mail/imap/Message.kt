package io.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import org.funktionale.option.Option
import org.funktionale.option.Option.None
import org.funktionale.option.Option.Some
import com.sun.mail.imap.IMAPMessage as MailMessage

/**
 * Wrapper class for working with message headers.
 */
data class MessageHeader(
        /**
         * The name of the message header.
         */
        val name: String,

        /**
         * The value of the message header.
         */
        val value: String)

/**
 * Wrapper around the standard JavaMail `IMAPMessage` class to make
 * things a little easier to work with.
 */
class Message(private val mailMessage: com.sun.mail.imap.IMAPMessage) {

    /**
     * Gets the first "from" address in the message.
     */
    val from: String by lazy {
        mailMessage.from[0].toString()
    }

    /**
     * Gets the message content from the message as a String.
     */
    val bodyText: String by lazy {
        mailMessage.content as String
    }

    /**
     * Gets the message's UID value.
     */
    val uid: Long by lazy {
        mailMessage.getUID()
    }

    /**
     * Gets the collection of headers from the message (as an immutable [List]).
     */
    val headers: List<MessageHeader> by lazy {
        val res = mutableListOf<MessageHeader>()
        val headersEnum = mailMessage.allHeaders
        headersEnum.iterator().forEach {
            val messageHeader = MessageHeader(it.name, it.value)
            res.add(messageHeader)
        }

        res
    }
}

/**
 * Operator to allow working with a message in a function block.
 */
operator fun Option<Message>.invoke(action: Message.() -> Unit) {
    when (this) {
        is Some -> this.map { it.action() }
        is None -> return
    }
}

/**
 * Extension function to allow getting a UID from a message directly, instead of
 * having to remember to get it from the folder first.
 */
fun com.sun.mail.imap.IMAPMessage.getUID() = (folder as IMAPFolder).getUID(this)

/**
 * Operator to allow accessing messages in an `IMAPFolder` via bracket syntax. The supplied
 * index is expected to be a valid message number within this folder.
 */
operator fun IMAPFolder.get(i: Int): Option<Message> {
    val msg = this.getMessage(i) as com.sun.mail.imap.IMAPMessage? ?: return None
    return Some(Message(msg))
}