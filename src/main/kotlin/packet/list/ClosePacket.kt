package top.e404.socket.packet.list

import top.e404.socket.packet.SocketPacket

/**
 * 服务器主动断开socket
 *
 * @property type 类型
 * @property version 版本
 * @property code 断开代码
 * @property data 原因
 */
data class ClosePacket(
    override val type: String = "Close",
    override val version: String = "1.0.0",
    val data: String
): SocketPacket {
    constructor(reason: CloseReason) : this(data = reason.message)
}