package io.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage
import javax.mail.FetchProfile
import javax.mail.Folder as JavaMailFolder

/**
 * A statically-typed `enum` to wrap the JavaMail Folder constants.
 */
enum class FolderModes(private val javaMailMode: Int) {
    /**
     * Indicates to open the folder in read-only mode.
     */
    ReadOnly(JavaMailFolder.READ_ONLY),

    /**
     * Indicates to open the folder in read/write mode.
     */
    ReadWrite(JavaMailFolder.READ_WRITE);

    internal fun toJavaMailMode() = javaMailMode
}

enum class FolderTypes(private val javaMailFolderType: Int) {
    HoldsFolders(JavaMailFolder.HOLDS_FOLDERS),
    HoldsMessages(JavaMailFolder.HOLDS_MESSAGES),
    HoldsBoth(JavaMailFolder.HOLDS_FOLDERS and JavaMailFolder.HOLDS_MESSAGES);

    internal fun toJavaMailFolderType() = javaMailFolderType

    companion object {
        fun fromJavaMailFolderType(javaMailFolderType: Int): FolderTypes {
            val holdsFolders = javaMailFolderType and JavaMailFolder.HOLDS_FOLDERS
            val holdsMessages = javaMailFolderType and JavaMailFolder.HOLDS_MESSAGES

            if ((holdsFolders != 0) && (holdsMessages != 0)) return HoldsBoth
            else if (holdsFolders != 0) return HoldsFolders
            return HoldsMessages
        }
    }
}

/**
 * Wrapper class to provide additional functionality over the JavaMail `IMAPFolder`
 * class.
 */
class Folder(private val javaMailFolder: IMAPFolder) {

    private var preFetchInfo = FetchProfile()

    private val seenFlags = Flags(Flag.Seen)

    /**
     * Search this folder using the [SearchBuilder] initialized within
     * the given block, using any pre-fetch information set via either
     * [preFetchBy] method. Any sorting applied to the `SearchBuilder`
     * within the given block is applied to the returned results.
     *
     * @param block the block to use to initialize the [SearchBuilder].
     *
     * @return a `List` of [Message] instances that match the search, pre-fetched
     *         if applicable, and sorted if applicable.
     */
    fun search(block: SearchBuilder.() -> Unit): List<Message> {
        val builder = SearchBuilder()
        builder.block()
        val searchTerm = builder.build()

        val javaMailMessages = if (builder.hasSortTerms()) {
            val sortTerms = builder.getSortTerms().map { it.toSortTerm() }.toTypedArray()
            searchTerm?.let { javaMailFolder.getSortedMessages(sortTerms, it) }
        } else {
            searchTerm?.let { javaMailFolder.search(it) }
        }

        javaMailMessages?.let { javaMailFolder.fetch(it, preFetchInfo) }
        val messages = javaMailMessages?.map { Message(it as IMAPMessage) } ?: mutableListOf()

        if (builder.markAsRead) {
            javaMailMessages?.forEach {
                javaMailFolder.setFlags(arrayOf(it), seenFlags.javaMailFlags, true)
            }
        }

        return messages
    }

    /**
     * Specifies a `FetchProfile` to use when fetching messages.
     *
     * @param fetchProfile the `FetchProfile` to fetch messages with.
     */
    fun preFetchBy(fetchProfile: FetchProfile) {
        preFetchInfo = fetchProfile
    }

    /**
     * Allows applying multiple `FetchProfileItem`s a little easier than
     * the standard `FetchProfile`-construction-then-add means.
     *
     * @param test the `FetchProfileItem`(s) to build the new prefetch information with.
     */
    fun preFetchBy(vararg test: FetchProfile.Item) {
        preFetchInfo = FetchProfile()
        test.forEach { preFetchInfo.add(it) }
    }

    /**
     * Closes the underlying JavaMail folder, expunging deleted messages or not depending
     * on the value of `expunge`.
     *
     * @param expunge whether or not to expunge the underlying messages.
     */
    fun close(expunge: Boolean) {
        javaMailFolder.close(expunge)
    }

    /**
     * Sorts the messages in the folder using the [SortBuilder] initialized
     * within the given block, using any pre-fetch information set via either
     * [preFetchBy] method. Note that this only sorts the messages as returned
     * from this method; the ordering within the IMAP server itself remains
     * unchanged.
     *
     * @param block the block to use to initialize the [SortBuilder]
     *
     * @return a `List` of [Message] instances, sorted per the terms applied
     *         via the [SortBuilder], and pre-fetched if applicable.
     */
    fun sortedBy(block: SortBuilder.() -> Unit): List<Message> {
        val sortBuilder = SortBuilder()
        sortBuilder.block()

        val sortTerms = sortBuilder.build().map { it.toSortTerm() }.toTypedArray()
        val javaMailMessages = javaMailFolder.getSortedMessages(sortTerms)

        javaMailFolder.fetch(javaMailMessages, preFetchInfo)

        return javaMailMessages.map { Message(it as IMAPMessage) }
    }

    fun getMessageCount(): Int = javaMailFolder.messageCount

    fun getUnreadMessageCount(): Int = javaMailFolder.unreadMessageCount

    fun getNewMessageCount(): Int = javaMailFolder.newMessageCount

    fun hasNewMessages(): Boolean = javaMailFolder.hasNewMessages()

    fun getFolderType(): FolderTypes = FolderTypes.fromJavaMailFolderType(javaMailFolder.type)

    /**
     * Operator to allow accessing messages in this folder via bracket syntax. The supplied
     * index is expected to be a valid message number within this folder.
     */
    operator fun get(i: Int): Message? = javaMailFolder[i]

    fun messagesIn(range: ClosedRange<Int>, prefetch: Boolean = true): List<Message> {
        val jmmMessages = javaMailFolder.getMessages(range.start, range.endInclusive)
        if (prefetch) {
            javaMailFolder.fetch(jmmMessages, preFetchInfo)
        }
        return jmmMessages.map { Message(it as IMAPMessage) }
    }
}
