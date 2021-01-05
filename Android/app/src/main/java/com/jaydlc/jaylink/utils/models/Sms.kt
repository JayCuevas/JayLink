package com.jaydlc.jaylink.utils.models

import java.time.LocalDateTime

data class Sms(
        val body: String?,
        val read: Boolean?,
        val address: String?,
        val date: LocalDateTime,
        val person: Any?,
        val status: String?,
        val type: String?,
        val subject: String?,
        val seen: Boolean?,
        val contact: Contact?
) {
    companion object {
        fun normalizePhoneNumber(phoneNumber: String): String {
            return phoneNumber.removePrefix("+1")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("-", "")
                    .replace(Regex("\\s"), "")
        }
    }
}