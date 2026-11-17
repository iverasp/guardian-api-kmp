package com.visma.guardian.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread

@OptIn(ExperimentalForeignApi::class)
internal actual fun secureRandomBytes(size: Int): ByteArray {
    val file = checkNotNull(fopen("/dev/urandom", "rb")) { "Unable to open /dev/urandom" }
    return try {
        ByteArray(size).also { bytes ->
            val read = bytes.usePinned { fread(it.addressOf(0), 1.convert(), size.convert(), file) }
            check(read.toInt() == size) { "Unable to read secure random bytes" }
        }
    } finally {
        fclose(file)
    }
}
