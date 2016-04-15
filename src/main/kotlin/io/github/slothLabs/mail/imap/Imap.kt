package io.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import java.io.Closeable
import java.util.Properties
import javax.mail.Session
import javax.mail.Store
import javax.mail.Message as MailMessage

class Imap internal constructor(private val store: Store): Closeable, AutoCloseable {

    override fun close() {
        println("Closing")
        store.close()
    }

    fun folder(name: String, mode: FolderModes, action: Folder.() -> Unit) {
        val fldr = store.getFolder(name) as IMAPFolder
        fldr.open(mode.toJavaMailMode())
        val theFolder = Folder(fldr)
        theFolder.action()
        theFolder.close(false)
    }

    private fun isConnected() = store.isConnected
}

data class ConnectionInformation(val host: String, val port: Int, val user: String, val password: String)

fun imap(host: String, port: Int, user: String? = null, password: String? = null, props: Properties = Properties(), action: Imap.() -> Unit) {
    val session = Session.getInstance(props)
    val store = session.getStore("imap")
    store.connect(
            host,
            port,
            user,
            password
    )
    Imap(store).use {
        it.action()
    }
}

fun imap(connectionInformation: ConnectionInformation, props: Properties = Properties(), action: Imap.() -> Unit) {
    val session = Session.getInstance(props)
    val store = session.getStore("imap")
    store.connect(connectionInformation)
    Imap(store).use {
        it.action()
    }
}

fun Store.connect(connectionInformation: ConnectionInformation) = this.connect(
        connectionInformation.host,
        connectionInformation.port,
        connectionInformation.user,
        connectionInformation.password
)

