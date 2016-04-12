package com.github.slothLabs.mail.imap

import com.sun.mail.imap.IMAPFolder
import javax.mail.Folder

enum class FolderModes(private val javaMailMode: Int) {
    ReadOnly(Folder.READ_ONLY),
    ReadWrite(Folder.READ_WRITE);

    internal fun toJavaMailMode() = javaMailMode
}

class Folder(private val javaMailFolder: IMAPFolder) {

    val messages = MessageContainer(javaMailFolder)
}

