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
    val seen: Boolean?
) {
    companion object {
    }
}