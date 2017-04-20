package io.pivotal.transport

import java.nio.ByteBuffer

interface Transport {
    fun send(message: ByteBuffer)
    fun recv(receiver: ByteBuffer)
    fun start()
}

