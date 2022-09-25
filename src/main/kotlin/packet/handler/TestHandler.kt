package top.e404.socket.packet.handler

import com.google.gson.JsonElement
import top.e404.socket.SocketHandler
import top.e404.socket.packet.SocketPacketHandler
import top.e404.socket.packet.list.TestPacket

object TestHandler : SocketPacketHandler {
    override val type = "Test"
    override val version = "1.0.0"
    override fun onRecv(data: JsonElement, socketHandler: SocketHandler) {
        if (data.asString == "Ping") socketHandler.send(TestPacket(data = "Pong"))
    }
}