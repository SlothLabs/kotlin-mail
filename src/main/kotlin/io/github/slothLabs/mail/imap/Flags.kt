package io.github.slothLabs.mail.imap

import javax.mail.Flags as JavaMailFlags

enum class Flag(var javaMailFlag: JavaMailFlags.Flag) {
    Answered(JavaMailFlags.Flag.ANSWERED),
    Deleted(JavaMailFlags.Flag.DELETED),
    Draft(JavaMailFlags.Flag.DRAFT),
    Flagged(JavaMailFlags.Flag.FLAGGED),
    Recent(JavaMailFlags.Flag.RECENT),
    Seen(JavaMailFlags.Flag.SEEN),
    User(JavaMailFlags.Flag.USER);
}

class Flags(vararg flags: Flag) : HashSet<Flag>() {
    private val flagItems = mutableSetOf<Flag>()

    val javaMailFlags: JavaMailFlags
        get() = flagItems.fold(JavaMailFlags()) { acc: JavaMailFlags, flag: Flag ->
            acc.add(flag.javaMailFlag)
            acc
        }

    init {
        flags.forEach { flagItems.add(it) }
    }
}
