package com.visma.guardian.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault

@OptIn(ExperimentalForeignApi::class)
internal actual fun secureRandomBytes(size: Int): ByteArray =
    ByteArray(size).also { bytes ->
        if (size == 0) return@also
        val status = bytes.usePinned { SecRandomCopyBytes(kSecRandomDefault, size.convert(), it.addressOf(0)) }
        check(status == errSecSuccess) { "Unable to read secure random bytes (status $status)" }
    }
