package top.e404.socket.client

import com.google.gson.JsonParser
import top.e404.socket.SocketHandler
import top.e404.socket.log.Log
import top.e404.socket.packet.EPacketManager
import top.e404.socket.packet.list.HandshakePacket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

class SocketClient(
    val name: String,
    val address: String,
    val port: Int,
    val packetManager: EPacketManager,
    val log: Log
) : SocketHandler {
    private lateinit var socket: Socket
    private lateinit var input: InputStream
    private lateinit var output: OutputStream
    private lateinit var reader: BufferedReader
    private lateinit var writer: BufferedWriter

    private val close = AtomicBoolean(false)

    var onStart: (SocketClient) -> Unit = {}

    fun onStart(block: (SocketClient) -> Unit) {
        onStart = block
    }

    fun start() {
        do {
            try {
                socket = Socket(address, port)
            } catch (e: Exception) {
                log.warn("连接到${address}:${port}失败, 将在3秒后重试", e)
                Thread.sleep(3000)
            }
        } while (!this::socket.isInitialized)
        input = socket.getInputStream()
        output = socket.getOutputStream()
        reader = input.bufferedReader()
        writer = output.bufferedWriter()
        send(HandshakePacket(data = name).toJson())
        // 等待accept
        val jo = JsonParser.parseString(reader.readLine()).asJsonObject
        // 未接受到accept
        if (jo["type"]?.asString != "Handshake" || jo["data"]?.asString != "accept") {
            log.warn("服务端未发送accept握手包")
            stop()
            return
        }
        log.debug { "成功与服务端`${socket.remoteSocketAddress}`建立连接" }
        onStart(this)
        while (true) {
            try {
                packetManager.onPacket(
                    name = name,
                    packet = reader.readLine(),
                    handler = this
                )
            } catch (e: Exception) {
                if (close.get()) return
                log.warn("与服务端`${socket.remoteSocketAddress}`通信时出现异常", e)
            }
        }
    }

    override fun send(message: String) {
        log.debug { "client`$name`发送数据: `$message`" }
        writer.write("$message\n")
        writer.flush()
    }

    override fun stop() {
        log.debug { "关闭与服务端`${socket.remoteSocketAddress}`的连接" }
        close.set(true)
        reader.close()
        writer.close()
        input.close()
        output.close()
        socket.close()
    }
}