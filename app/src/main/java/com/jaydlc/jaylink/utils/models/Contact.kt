package com.jaydlc.jaylink.utils.models

data class Contact(
        val Name: String,
        val PhoneNumber: String,
) {
    val NormalizedPhoneNumber = Sms.normalizePhoneNumber(PhoneNumber)
}
