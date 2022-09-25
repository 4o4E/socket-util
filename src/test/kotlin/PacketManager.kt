package top.e404.socket.test

import top.e404.socket.log.SimpleLogImpl
import top.e404.socket.packet.EPacketManager
import top.e404.socket.packet.handler.CloseHandler
import top.e404.socket.packet.handler.HandshakeHandler
import top.e404.socket.packet.handler.TestHandler

object PacketManager : EPacketManager(SimpleLogImpl) {
    init {
        register(HandshakeHandler)
        register(TestHandler)
        register(CloseHandler)
    }
}