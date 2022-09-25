package top.e404.socket.log

/**
 * 抽象的日志接口
 */
interface Log {
    fun debug(message: () -> String)
    fun info(message: String)
    fun warn(message: String, throwable: Throwable? = null)
}