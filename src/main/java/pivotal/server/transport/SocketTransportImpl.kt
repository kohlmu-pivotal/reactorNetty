package io.pivotal.server.transport


import io.netty.channel.nio.NioEventLoopGroup
import io.pivotal.transport.Transport
import reactor.ipc.netty.tcp.TcpServer
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer


class SocketTransportImpl : Transport {
    private var nettyTcpServer: TcpServer


    constructor(inetAddress: InetAddress = InetAddress.getLocalHost(), port: Int) {
        val group = NioEventLoopGroup(1)
        nettyTcpServer = TcpServer.create { options -> options.eventLoopGroup(group).listen(InetSocketAddress(inetAddress, port)) }
    }

    override fun send(message: ByteBuffer) {
    }

    override fun recv(receiver: ByteBuffer) {
    }

    override fun start() {
        val nettyContext = nettyTcpServer.newHandler { `in`, out ->
            out.sendString(`in`.receive().asByteArray().log().map { someInput ->
                val inputString = String(someInput)
                val response = "$inputString response"
                println(inputString)
                response
            })
        }
        nettyContext.block().channel().closeFuture().sync()
    }

}
