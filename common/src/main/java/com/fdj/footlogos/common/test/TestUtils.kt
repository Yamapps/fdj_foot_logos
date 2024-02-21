package com.fdj.footlogos.common.test

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

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
}