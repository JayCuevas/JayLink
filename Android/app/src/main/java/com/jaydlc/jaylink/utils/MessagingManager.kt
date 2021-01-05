package com.jaydlc.jaylink.utils

// Resources:
// https://www.tutorialspoint.com/how-to-read-all-contacts-in-android

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import com.jaydlc.jaylink.utils.models.Contact
import com.jaydlc.jaylink.utils.models.Sms
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MessagingManager(
        private val contentResolver: ContentResolver
) {
    private fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val cur =
                contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID)
                )

                val name = cur.getString(
                        cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME
                        )
                )

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id), null
                    )!!
                    while (pCur.moveToNext()) {
                        val phoneNo = pCur.getString(
                                pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                        )

                        contacts.add(Contact(name, phoneNo))

                    }
                    pCur.close()
                }
            }
        }

        if (cur != null) {
            cur.close()
        }

        return contacts.toList().sortedBy {
            it.Name
        }
    }

    fun getSmsMessages(): List<Sms>? {
        val messages = mutableListOf<Sms>()
        val contacts = getAllContacts()

        val cursor = contentResolver.query(
                Uri.parse("content://sms/inbox"),
                null, null, null, null
        )!!

        fun getIndex(columnName: String): Int {
            return cursor.getColumnIndex(columnName)
        }

        fun getString(index: Int): String? {
            return cursor.getStringOrNull(index)
        }

        if (!cursor.moveToFirst()) {
            return null
        }

        do {
            val indexBody = getIndex("body")
            val indexAddress = getIndex("address")
            val indexDate = getIndex("date")
            val indexPerson = getIndex("person")
            val indexRead = getIndex("read")
            val indexStatus = getIndex("status")
            val indexType = getIndex("type")
            val indexSubject = getIndex("subject")
            val indexSeen = getIndex("seen")

            // if (indexBody < 0 || !cursor.moveToFirst()) {
            //     return null
            // }

            val address = getString(indexAddress)
            val body = getString(indexBody)
            val dateString = getString(indexDate)!!
            val date = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dateString.toLong()),
                    ZoneId.systemDefault()
            )
            val person = cursor.getBlob(indexPerson)
            val read = getString(indexRead) == "1"
            val status = getString(indexStatus)
            val type = getString(indexType)
            val subject = getString(indexSubject)
            val seen = getString(indexSeen) == "1"
            val contact = contacts.find {
                it.NormalizedPhoneNumber == Sms.normalizePhoneNumber(address!!)
            }

            messages.add(Sms(body, read, address, date, person, status, type, subject, seen, contact))
        } while (cursor.moveToNext())

        cursor.close()

        return messages.toList()
    }
}