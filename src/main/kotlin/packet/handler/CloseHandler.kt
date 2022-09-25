package top.e404.socket.packet.handler

import com.google.gson.JsonElement
import top.e404.socket.SocketHandler
import top.e404.socket.packet.SocketPacketHandler

object CloseHandler : SocketPacketHandler {
    override val type = "Close"
    override val version = "1.0.0"

    override fun onRecv(data: JsonElement, socketHandler: SocketHandler) {
        socketHandler.stop()
    }
}