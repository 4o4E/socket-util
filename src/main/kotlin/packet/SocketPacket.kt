package top.e404.socket.packet

import top.e404.socket.gson

/**
 * 数据包需要实现的接口
 */
interface SocketPacket {
    val type: String
    val version: String

    fun toJson() = gson.toJson(this)!!

    fun toPacket() = toJson().toByteArray()
}