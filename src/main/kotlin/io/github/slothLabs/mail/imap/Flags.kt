package io.github.slothLabs.mail.imap

import javax.mail.Flags

enum class Flag(private val javaMailFlag: Flags.Flag) {
    Answered(Flags.Flag.ANSWERED),
    Deleted(Flags.Flag.DELETED),
    Draft(Flags.Flag.DRAFT),
    Flagged(Flags.Flag.FLAGGED),
    Recent(Flags.Flag.RECENT),
    Seen(Flags.Flag.SEEN),
    User(Flags.Flag.USER);

    internal fun toJavaMailFlag() = javaMailFlag

    companion object {
        fun fromJavaMailFlag(javaMailFlag: Flags.Flag): Flag = values().filter { it.toJavaMailFlag() == javaMailFlag }.first()
    }
}

class Flags() {
    private val flags = mutableSetOf<Flag>()


}