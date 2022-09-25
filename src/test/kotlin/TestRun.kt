package top.e404.socket.test

import org.junit.jupiter.api.Test
import top.e404.socket.client.SocketClient
import top.e404.socket.log.SimpleLogImpl
import top.e404.socket.packet.list.ClosePacket
import top.e404.socket.packet.list.CloseReason
import top.e404.socket.packet.list.TestPacket
import top.e404.socket.server.SocketServer
import kotlin.concurrent.thread

class TestRun {
    @Test
    fun run() {
        SimpleLogImpl.debug = true
        SimpleLogImpl.info("开始")
        SimpleLogImpl.debug { "debug" }
        val server = SocketServer(88, SimpleLogImpl, PacketManager)
        thread(true) {
            SimpleLogImpl.info("server start")
            server.start()
        }
        Thread.sleep(1000)
        val client = SocketClient("test", "localhost", 88, PacketManager, SimpleLogImpl)
        thread(true) {
            SimpleLogImpl.info("client start")
            client.start()
        }
        Thread.sleep(2000)
        client.send(TestPacket(data = "Ping"))
        Thread.sleep(2_000)
        client.send(ClosePacket(CloseReason.DISCONNECT))
        client.stop()
        Thread.sleep(10_000)
    }
}