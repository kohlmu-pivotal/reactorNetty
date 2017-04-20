package io.pivotal.client.transport

import io.pivotal.transport.Transport
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.WorkQueueProcessor
import reactor.ipc.netty.tcp.TcpClient
import java.net.InetAddress
import java.nio.ByteBuffer

class ClientSocketTransport : Transport {
    private var remoteAddress: InetAddress
    private var client: TcpClient
    private val incomingWorkQueueProcessor = WorkQueueProcessor.create<Any>("incomingWorkQueue")
    private val incomingSink = incomingWorkQueueProcessor.connectSink()
    private val outgoingWorkQueueProcessor = WorkQueueProcessor.create<ByteBuffer>("outgoingWorkQueue")
    private val outgoingSink = outgoingWorkQueueProcessor.connectSink()

    // outbound (client)
    constructor(addr: InetAddress, port: Int) {
        this.remoteAddress = addr
        client = TcpClient.create(remoteAddress.hostName, port)
    }

    override fun start()
    {
        outgoingSink.start()
        incomingSink.start()
        Flux.from(incomingWorkQueueProcessor).log().subscribe(::println)
        client.newHandler({ `in`, out ->
            Flux.from(outgoingWorkQueueProcessor).log().subscribe { out.sendByteArray(Mono.just(it.array()))}
            `in`.receive().log().map { incomingSink.emit(it) }
            Flux.never()

        }).block().channel().closeFuture()
    }

    override fun send(message: ByteBuffer) {
        outgoingSink.emit(message)
    }

    override fun recv(receiver: ByteBuffer) {
    }
}