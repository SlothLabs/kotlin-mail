package com.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage as MailMessage

import org.funktionale.option.Option
import org.funktionale.option.Option.*
import javax.mail.FetchProfile
import javax.mail.Folder as MailFolder
import javax.mail.Header

data class MessageHeader(val name: String, val value: String)

class Message(private val mailMessage: MailMessage) {

    val bodyText: String = mailMessage.content as String
    val uid = mailMessage.getUID()

    internal val folder: MailFolder
        get() = mailMessage.folder

    internal val imapMessage: MailMessage
        get() = mailMessage

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

fun MailMessage.detach() {
    val fp = FetchProfile()
    fp.add(IMAPFolder.FetchProfileItem.MESSAGE)
    folder.fetch(arrayOf(this), fp)
}

fun Iterable<MailMessage>.detach() {
    // get a little tricky here; while all the messages in the collection
    // are most likely from the same folder, we can't guarantee it.
    val theMap = mutableMapOf<MailFolder, MutableSet<MailMessage>>()
    forEach {
        val fld = it.folder
        val msgSet = theMap.getOrPut(fld, { mutableSetOf<MailMessage>() })
        msgSet.add(it)
    }

    val fp = FetchProfile()
    fp.add(IMAPFolder.FetchProfileItem.MESSAGE)

    theMap.forEach {
        val fld = it.key
        val msgArray = it.value.toTypedArray()
        fld.fetch(msgArray, fp)
    }
}

fun Collection<Message>.detach() {
    // get a little tricky here; while all the messages in the collection
    // are most likely from the same folder, we can't guarantee it.
    val theMap = mutableMapOf<MailFolder, MutableSet<MailMessage>>()
    forEach {
        val fld = it.folder
        val msgSet = theMap.getOrPut(fld, { mutableSetOf<MailMessage>() })
        msgSet.add(it.imapMessage)
    }

    val fp = FetchProfile()
    fp.add(IMAPFolder.FetchProfileItem.MESSAGE)

    theMap.forEach {
        val fld = it.key
        val msgArray = it.value.toTypedArray()
        fld.fetch(msgArray, fp)
    }
}

fun Array<MailMessage>.detach() {
    // get a little tricky here; while all the messages in the collection
    // are most likely from the same folder, we can't guarantee it.
    val theMap = mutableMapOf<MailFolder, MutableSet<MailMessage>>()
    forEach {
        val fld = it.folder
        val msgSet = theMap.getOrPut(fld, { mutableSetOf<MailMessage>() })
        msgSet.add(it)
    }

    val fp = FetchProfile()
    fp.add(IMAPFolder.FetchProfileItem.MESSAGE)

    theMap.forEach {
        val fld = it.key
        val msgArray = it.value.toTypedArray()
        fld.fetch(msgArray, fp)
    }
}