package io.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage
import org.funktionale.option.Option
import org.funktionale.option.toOption
import javax.mail.FetchProfile
import javax.mail.Folder

/**
 * A statically-typed `enum` to wrap the JavaMail Folder constants.
 */
enum class FolderModes(private val javaMailMode: Int) {
    /**
     * Indicates to open the folder in read-only mode.
     */
    ReadOnly(Folder.READ_ONLY),

    /**
     * Indicates to open the folder in read/write mode.
     */
    ReadWrite(Folder.READ_WRITE);

    internal fun toJavaMailMode() = javaMailMode
}

/**
 * Wrapper class to provide additional functionality over the JavaMail `IMAPFolder`
 * class.
 */
class Folder(private val javaMailFolder: IMAPFolder) {

    private var preFetchInfo = FetchProfile()

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
            searchTerm.map { javaMailFolder.getSortedMessages(sortTerms, it)}
        } else {
            searchTerm.map { javaMailFolder.search(it) }
        }

        javaMailMessages.map { javaMailFolder.fetch(it, preFetchInfo) }
        val messages = mutableListOf<Message>()
        javaMailMessages.map {
            for (jmm in it) {
                messages.add(Message(jmm as IMAPMessage))
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
        val javaMailMessages = javaMailFolder.getSortedMessages(sortTerms).toOption()

        javaMailMessages.map { javaMailFolder.fetch(it, preFetchInfo) }

        val messages = mutableListOf<Message>()
        javaMailMessages.map {
            for (jmm in it) {
                messages.add(Message(jmm as IMAPMessage))
            }
        }

        return messages
    }

    /**
     * Operator to allow accessing messages in this folder via bracket syntax. The supplied
     * index is expected to be a valid message number within this folder.
     */
    operator fun get(i: Int): Option<Message> = javaMailFolder[i];
}

