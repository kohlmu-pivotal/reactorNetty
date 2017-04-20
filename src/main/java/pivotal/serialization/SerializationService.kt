package io.pivotal.serialization

import java.nio.ByteBuffer

interface SerializationService {
    fun serialize(dest: ByteBuffer, o: Any)
    fun <T> deserialize(src: ByteBuffer, clazz: Class<T>): T
}
