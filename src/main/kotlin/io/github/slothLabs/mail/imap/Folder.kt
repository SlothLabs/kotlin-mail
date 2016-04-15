package io.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage
import org.funktionale.option.Option
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
        val builder = SearchBuilder()
        builder.block()
        val searchTerm = builder.build()

        val javaMailMessages = searchTerm.map { javaMailFolder.search(it) }

        javaMailMessages.map { javaMailFolder.fetch(it, preFetchInfo) }
        val messages = mutableListOf<Message>()
        javaMailMessages.map {
            for (jmm in it) {
                messages.add(Message(jmm as IMAPMessage))
            }
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

