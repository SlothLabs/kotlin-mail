package io.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import java.io.Closeable
import java.util.Properties
import javax.mail.Session
import javax.mail.Store
import javax.mail.Message as MailMessage

/**
 * Class for performing basic IMAP access functionality.
 */
class Imap internal constructor(private val store: Store): Closeable, AutoCloseable {

    /**
     * Closes the session and disconnects from the IMAP server.
     */
    override fun close() {
        store.close()
    }

    /**
     * Opens the specified folder, with the specified mode, applies the given
     * action to it, and closes the folder on exit. Two key things to note:
     *
     * 0. The folder is closed without expunging any deleted messages. If this is needed,
     *    you must manually expunge deleted messages in your action.
     * 0. Unless you explicitly pre-fetched any messages retrieved, they will not be
     *    accessible outside of your action method (i.e. requesting any property on
     *    them will throw a `FolderClosedException`). This is inherent in JavaMail;
     *    most message properties are lazy-loaded, meaning they make an additional
     *    call to the IMAP server the first time they're requested. When the folder
     *    is closed, they can't make that call, and blow up on you. You can either
     *    ensure you do everything you need to do with in your action, and/or use
     *    [Folder.preFetchBy] to pre-fetch message data (using both is highly recommended).
     *
     * @param name the name of the folder to open.
     * @param mode the mode to open the folder in.
     * @param action the action to perform against the folder.
     */
    fun folder(name: String, mode: FolderModes, action: Folder.() -> Unit) {
        val imapFolder = store.getFolder(name) as IMAPFolder
        imapFolder.open(mode.toJavaMailMode())
        val theFolder = Folder(imapFolder)
        theFolder.action()
        theFolder.close(false)
    }
}

/**
 * Class for basic IMAP connectivity information.
 */
data class ConnectionInformation(
        /**
         * The host to connect to.
         */
        val host: String,

        /**
         * The port to connect to.
         */
        val port: Int,

        /**
         * The user name to connect with.
         */
        val user: String,

        /**
         * The password to connect with.
         */
        val password: String)

/**
 * Connects to an IMAP server using the specified connection information and properties, and performs the give action
 * against it. General usage would be:
 *
 * ```
 *     val host = "my.imap.host"
 *     val port = 143
 *     val user = "someone@somewhere.net"
 *     val password = "hackmeifyoucan"
 *     imap(host, port, user, password) {
 *         folder("INBOX", FolderModes.ReadOnly) {
 *             // do something awesomely emailish here.
 *         }
 *     }
 * ```
 *
 * @param host the host to connect to
 * @param port the port to connect to
 * @param user the user name to connect with
 * @param password the password to connect with
 * @param props the properties to pass to the JavaMail store
 * @param action the action to perform against the IMAP server.
 */
fun imap(host: String, port: Int, user: String, password: String, props: Properties = Properties(), action: Imap.() -> Unit) {
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

/**
 * Connects to an IMAP server using the specified connection information and properties, and performs the give action
 * against it. General usage would be:
 *
 * ```
 *     val host = "my.imap.host"
 *     val port = 143
 *     val user = "someone@somewhere.net"
 *     val password = "hackmeifyoucan"
 *     val connectionInfo = ConnectionInformation(host, port, user, password)
 *     imap(connectionInfo) {
 *         folder("INBOX", FolderModes.ReadOnly) {
 *             // do something awesomely emailish here.
 *         }
 *     }
 * ```
 *
 * @param connectionInformation the [ConnectionInformation] to connect with
 * @param props the properties to pass to the JavaMail store
 * @param action the action to perform against the IMAP server.
 */
fun imap(connectionInformation: ConnectionInformation, props: Properties = Properties(), action: Imap.() -> Unit) {
    val session = Session.getInstance(props)
    val store = session.getStore("imap")
    store.connect(connectionInformation)
    Imap(store).use {
        it.action()
    }
}

/**
 * Utility extension to allow connecting a JavaMail store instance using a [ConnectionInformation]
 * instance.
 *
 * @param connectionInformation the [ConnectionInformation] to connect with.
 */
fun Store.connect(connectionInformation: ConnectionInformation) = this.connect(
        connectionInformation.host,
        connectionInformation.port,
        connectionInformation.user,
        connectionInformation.password
)