package io.pivotal.client

import io.pivotal.client.transport.ClientSocketTransport
import io.pivotal.transport.Transport
import java.net.InetAddress
import java.nio.ByteBuffer


class Client(private val transport: Transport) {

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
    val client = Client(ClientSocketTransport(InetAddress.getByName("localhost"), 10101))
    client.start()
    val inBuf = ByteBuffer.allocate(256)
    for (i in 0..Integer.MAX_VALUE - 1) {
        client.send(ByteBuffer.wrap("test message $i".toByteArray()))
//        client.recv(inBuf)
//        inBuf.rewind()

        if (i % 100000 == 0) {
            println("iteration $i, received $inBuf, time ${System.currentTimeMillis()}")
        }
    }
}
