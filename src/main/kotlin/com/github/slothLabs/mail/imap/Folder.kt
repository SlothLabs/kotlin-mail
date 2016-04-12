package com.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage
import javax.mail.FetchProfile
import javax.mail.Folder

enum class FolderModes(private val javaMailMode: Int) {
    ReadOnly(Folder.READ_ONLY),
    ReadWrite(Folder.READ_WRITE);

    internal fun toJavaMailMode() = javaMailMode
}

class Folder(private val javaMailFolder: IMAPFolder) {

    val messages = MessageContainer(javaMailFolder)

    private var preFetchInfo = FetchProfile()

    fun search(block: SearchBuilder.() -> Unit): List<Message> {
        val bldr = SearchBuilder()
        bldr.block()
        val searchTerm = bldr.build()

        val javaMailMessages = javaMailFolder.search(searchTerm)

        javaMailFolder.fetch(javaMailMessages, preFetchInfo)
        val messages = mutableListOf<Message>()
        for (jmm in javaMailMessages) {
            messages.add(Message(jmm as IMAPMessage))
        }
        return messages
    }

    fun preFetchBy(fetchProfile: FetchProfile) {
        preFetchInfo = fetchProfile
    }

    fun preFetchBy(vararg test: IMAPFolder.FetchProfileItem) {
        preFetchInfo = FetchProfile()
        test.forEach { preFetchInfo.add(it) }
    }

    fun close(expunge: Boolean) {
        javaMailFolder.close(expunge)
    }
}

