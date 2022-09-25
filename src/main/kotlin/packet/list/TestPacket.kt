package top.e404.socket.packet.list

import top.e404.socket.packet.SocketPacket
import top.e404.socket.packet.handler.TestHandler

data class TestPacket(
    override val type: String = TestHandler.type,
    override val version: String = TestHandler.version,
    val data: String
) : SocketPacket