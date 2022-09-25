package top.e404.socket

import top.e404.socket.packet.SocketPacket

interface SocketHandler {
    fun send(message: String)
    fun send(packet: SocketPacket) = send(packet.toJson())
    fun stop()
}