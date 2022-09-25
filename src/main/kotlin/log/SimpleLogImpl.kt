package top.e404.socket.log

import java.text.SimpleDateFormat
import java.util.*

object SimpleLogImpl : Log {
    private val sdf = SimpleDateFormat("HH:mm:ss")
    var debug = false

    private fun time() = "[${sdf.format(Date())}]"

    override fun debug(message: () -> String) {
        if (debug) println("${time()} [DEBUG] ${message()}")
    }

    override fun info(message: String) {
        println("${time()} [INFO] $message")
    }

    override fun warn(message: String, throwable: Throwable?) {
        println("${time()} [WARN] $message")
        throwable?.printStackTrace()
    }
}