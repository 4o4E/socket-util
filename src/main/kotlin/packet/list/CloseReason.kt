package top.e404.socket.packet.list

import java.net.Socket

enum class CloseReason(
    val message: String
) {
    CONNECTION_ALREADY_EXISTS("connection already exists"),
    UNNAMED_SOCKET("unnamed socket"),
    ILLEGAL_NAME("illegal name"),
    EXCEPTION_ON_CONNECT("exception on connect"),
    DISCONNECT("disconnect"),
    ;

    fun toPacket() = ClosePacket(this).toPacket()
    fun writeToSocket(socket: Socket) = socket.getOutputStream().apply {
        write(toPacket())
        flush()
    }
}