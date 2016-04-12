package com.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage as MailMessage

import org.funktionale.option.Option
import org.funktionale.option.Option.*
import javax.mail.Header

data class MessageHeader(val name: String, val value: String)

class Message(private val mailMessage: MailMessage) {

    val bodyText: String = mailMessage.content as String
    val uid = mailMessage.getUID()

    val headers: List<MessageHeader> by lazy {
        val res = mutableListOf<MessageHeader>()
        val headersEnum = mailMessage.allHeaders
        headersEnum.iterator().forEach {
            val hdr = it as Header
            val messageHeader = MessageHeader(hdr.name, hdr.value)
            res.add(messageHeader)
        }

        res
    }
}

class MessageContainer(private val javaMailFolder: IMAPFolder) {
    operator fun get(i: Int): Option<Message> {
        val msg = javaMailFolder.getMessage(i) as com.sun.mail.imap.IMAPMessage
        return Some(Message(msg))
    }
}

operator fun Option<Message>.invoke(action: Message.() -> Unit) {
    when (this) {
        is Some -> this.map { it.action() }
        is None -> return
    }
}

fun MailMessage.getUID() = (folder as IMAPFolder).getUID(this)