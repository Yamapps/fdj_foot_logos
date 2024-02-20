package com.fdj.footlogos.common.test

import org.junit.Assert
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.reflect.KClass

object TestUtils {

    @Throws
    fun getFileContentFromRes(fileName: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream = javaClass.classLoader?.getResourceAsStream(fileName) as InputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = reader.readLine()
            while (str != null) {
                builder.append(str)
                str = reader.readLine()
            }
            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }
    fun assertThrowsException(
        expectedException: KClass<out Exception>,
        expectedMessage: String? = null,
        codeUnderTest: Runnable
    ) {
        try {
            codeUnderTest.run()
            Assert.fail("Expecting exception but none was thrown.")
        } catch (result: Throwable) {
            if (expectedException != result::class) {
                Assert.fail("Exception thrown was not the one unexpected. expected : $expectedException, got : ${result::class}")
            }
            expectedMessage?.let{
                Assert.assertEquals(it, result.message)
            }
        }
    }
}