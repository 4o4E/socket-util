package top.e404.socket.packet.list

import top.e404.socket.packet.SocketPacket
import top.e404.socket.packet.handler.HandshakeHandler

data class HandshakePacket(
    override val type: String = HandshakeHandler.type,
    override val version: String = HandshakeHandler.version,
    val data: String
): SocketPacket