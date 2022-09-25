package top.e404.socket.server

import top.e404.socket.SocketHandler
import top.e404.socket.log.Log
import top.e404.socket.packet.EPacketManager
import java.net.Socket

/**
 * 代表一个socket连接的处理器
 *
 * @property name 对应的连接名字
 * @property socket 连接
 * @property handler 处理接收到的数据包
 */
class SocketConnectHandler(
    val name: String,
    val socket: Socket,
    val handler: EPacketManager,
    val log: Log
) : SocketHandler {
    lateinit var server: SocketServer

    val input = socket.getInputStream()
    val output = socket.getOutputStream()
    val reader = input.bufferedReader()
    val writer = output.bufferedWriter()

    fun start() {
        while (true) {
            handler.onPacket(
                name = name,
                packet = reader.readLine(),
                handler = this
            )
        }
    }

    override fun send(message: String) {
        log.debug { "server`$name`发送数据: `$message`" }
        writer.write("$message\n")
        writer.flush()
    }

    override fun stop() {
        server.remove(this)
        reader.close()
        writer.close()
        socket.close()
    }
}