package com.jaydlc.jaylink.lib

interface ILogger {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String, error: Throwable?)
}