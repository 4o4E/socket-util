package top.e404.socket.packet

import com.google.gson.JsonElement
import top.e404.socket.SocketHandler

/**
 * 代表一个 socket server 数据包处理器
 */
interface SocketPacketHandler {
    /**
     * 对应数据包的名字, 大小写敏感
     */
    val type: String

    /**
     * 数据包处理器的版本, 处理时要求handler和数据包的版本相同
     */
    val version: String

    /**
     * 处理从 socket server 接收到的data
     *
     * @param data 数据
     */
    fun onRecv(data: JsonElement, socketHandler: SocketHandler)
}