package top.e404.socket.packet.handler

import com.google.gson.JsonElement
import top.e404.socket.SocketHandler
import top.e404.socket.packet.SocketPacketHandler

object HandshakeHandler : SocketPacketHandler {
    override val type = "Handshake"
    override val version = "1.0.0"

    override fun onRecv(data: JsonElement, socketHandler: SocketHandler) {}
}