package io.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import org.funktionale.option.Option
import org.funktionale.option.Option.None
import org.funktionale.option.Option.Some
import com.sun.mail.imap.IMAPMessage as MailMessage

data class MessageHeader(val name: String, val value: String)

class Message(private val mailMessage: com.sun.mail.imap.IMAPMessage) {

    val from: String = mailMessage.from[0].toString()
    val bodyText: String = mailMessage.content as String
    val uid = mailMessage.getUID()

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

class MessageContainer(private val javaMailFolder: IMAPFolder) {
    operator fun get(i: Int): Option<Message> {
        val msg = javaMailFolder.getMessage(i) as com.sun.mail.imap.IMAPMessage? ?: return None
        return Some(Message(msg))
    }
}

operator fun Option<Message>.invoke(action: Message.() -> Unit) {
    when (this) {
        is Some -> this.map { it.action() }
        is None -> return
    }
}

fun com.sun.mail.imap.IMAPMessage.getUID() = (folder as IMAPFolder).getUID(this)