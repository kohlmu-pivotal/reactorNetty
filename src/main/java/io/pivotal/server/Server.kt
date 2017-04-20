package io.pivotal.server


import io.pivotal.server.transport.SocketTransportImpl
import io.pivotal.transport.Transport
import java.net.InetAddress
import java.nio.ByteBuffer

class Server(private val transport: Transport) {

    fun send(b: ByteArray) {
        transport.send(ByteBuffer.wrap(b))
    }

    fun send(b: ByteBuffer) {
        transport.send(b)

    }

    fun recv(b: ByteArray) {
        transport.recv(ByteBuffer.wrap(b))
    }

    fun recv(b: ByteBuffer) {
        transport.recv(b)
    }

    fun start()
    {
        transport.start()
    }
}

fun main(args: Array<String>) {
    val server = Server(transport = SocketTransportImpl(InetAddress.getByName("localhost"), 10101))
    server.start()
//    val byteBuffer = ByteBuffer.allocate(1000)
//    for (i in 0..Integer.MAX_VALUE - 1) {
//        server.recv(byteBuffer)
//        //      System.out.printf("%s", byteBuffer);
//        byteBuffer.flip()
//        server.send(byteBuffer)
//        byteBuffer.clear()
//    }
}
