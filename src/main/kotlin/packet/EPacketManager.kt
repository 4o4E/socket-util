package top.e404.socket.packet

import com.google.gson.JsonParser
import top.e404.socket.SocketHandler
import top.e404.socket.log.Log
import top.e404.socket.server.SocketConnectHandler
import java.util.concurrent.ConcurrentHashMap

/**
 * 数据包处理器
 *
 * @property log 日志实现
 */
abstract class EPacketManager(private val log: Log) {
    private val packetHandlers = ConcurrentHashMap<String, SocketPacketHandler>()

    /**
     * 注册数据包处理器
     *
     * @param handler 数据包处理器
     */
    fun register(handler: SocketPacketHandler) {
        packetHandlers[handler.type] = handler
    }

    /**
     * 注销数据包处理器
     *
     * @param handler 数据包处理器
     */
    fun unregister(handler: SocketPacketHandler) =
        packetHandlers.remove(handler.type)

    /**
     * 处理client接受到的数据包
     *
     * @param name 连接名
     * @param handler 对应的连接处理器
     * @param packet 数据包字符串
     */
    fun onPacket(
        name: String,
        handler: SocketHandler,
        packet: String
    ) {
        log.debug { "${if (handler is SocketConnectHandler) "server" else "client"}`${name}`接收数据: `$packet`" }
        val jo = JsonParser.parseString(packet).asJsonObject
        val packetType = jo["type"].asString
        val packetHandler = packetHandlers[packetType]
        if (packetHandler == null) {
            log.warn("无法找到处理${packetType}数据包的处理器")
            return
        }
        val version = jo["version"].asString
        if (packetHandler.version != version) {
            log.warn("${packetType}数据包处理器的版本`${packetHandler.version}`与数据包版本`${version}`不一致")
            return
        }
        packetHandler.onRecv(jo["data"], handler)
    }
}