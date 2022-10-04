package top.e404.socket.server

import com.google.gson.JsonParser
import top.e404.socket.SocketHandler
import top.e404.socket.log.Log
import top.e404.socket.log.SimpleLogImpl
import top.e404.socket.packet.EPacketManager
import top.e404.socket.packet.handler.HandshakeHandler
import top.e404.socket.packet.list.CloseReason
import top.e404.socket.packet.list.HandshakePacket
import java.net.ServerSocket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * socket服务器
 *
 * @property port 服务器端口
 */
class SocketServer(
    val port: Int,
    val log: Log,
    val packetManager: EPacketManager
) {
    private lateinit var server: ServerSocket

    /**
     * 连接处, 连接名字 to 连接
     */
    val connectPool = ConcurrentHashMap<String, SocketConnectHandler>()

    operator fun get(name: String) = connectPool[name]

    fun remove(handler: SocketConnectHandler) = remove(handler.name)

    fun remove(name: String) {
        connectPool.remove(name)
    }

    private val stop = AtomicBoolean(false)
    private lateinit var thread: Thread

    var onStart: (SocketServer) -> Unit = {}

    fun onStart(block: (SocketServer) -> Unit) {
        onStart = block
    }

    fun getOther(handler: SocketHandler) = connectPool.filter { it.value != handler }

    fun start() {
        thread = thread(true) {
            server = ServerSocket(port)
            while (!stop.get()) {
                val socket = server.accept()
                log.debug { "接收连接, addr: ${socket.remoteSocketAddress}" }
                thread(true) socketThread@{
                    // 读取第一个数据包
                    val br = socket.getInputStream().bufferedReader()
                    try {
                        val message = br.readLine()
                        log.debug { "接收到${socket.remoteSocketAddress}的数据: `$message`" }
                        val json = JsonParser.parseString(message).asJsonObject
                        // 第一个数据包不是握手
                        if (json["type"]?.asString != HandshakeHandler.type) {
                            log.warn("连接创建失败(第一个数据包不是握手), addr: ${socket.remoteSocketAddress}")
                            CloseReason.UNNAMED_SOCKET.writeToSocket(socket)
                            socket.close()
                            return@socketThread
                        }
                        val name = json["data"]?.asString
                        // name不合规
                        if (name.isNullOrBlank()) {
                            log.warn("连接创建失败(名称不合规), addr: ${socket.remoteSocketAddress}")
                            CloseReason.ILLEGAL_NAME.writeToSocket(socket)
                            socket.close()
                            return@socketThread
                        }
                        // 连接已存在
                        if (connectPool.containsKey(name)) {
                            log.warn("连接创建失败(连接已存在), addr: ${socket.remoteSocketAddress}")
                            CloseReason.CONNECTION_ALREADY_EXISTS.writeToSocket(socket)
                            socket.close()
                            return@socketThread
                        }
                        // 接受连接
                        val socketHandler = SocketConnectHandler(name, socket, packetManager, SimpleLogImpl)
                        socketHandler.send(HandshakePacket(data = "accept").toJson())
                        connectPool[name] = socketHandler
                        log.debug { "成功创建与`${socket.remoteSocketAddress}`的连接: `$name`" }
                        onStart(this)
                        // 开始处理
                        socketHandler.start()
                    } catch (e: Exception) {
                        log.warn("连接创建失败, addr: ${socket.remoteSocketAddress}", e)
                        CloseReason.EXCEPTION_ON_CONNECT.writeToSocket(socket)
                        socket.close()
                        return@socketThread
                    }
                }
            }
        }
    }

    fun stop() {
        stop.set(true)
        connectPool.values.forEach { it.stop() }
        server.close()
    }
}