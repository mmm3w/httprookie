package com.mitsuki.armory.httprookie.convert

import okhttp3.Response
import java.io.File

class FileConvert(private val folder: String, private val fileName: String) : Convert<File> {

    var bufferSize: Int = 1024

    override fun convertResponse(response: Response): File? {
        val dir = File(folder).apply { createFolder() }
        val file = File(dir, fileName).apply { clear() }
        (response.body?.byteStream() ?: return null).use { inputStream ->
            var len: Int
            val buffer = ByteArray(bufferSize)
            file.outputStream().use {
                while (inputStream.read(buffer).apply { len = this } != -1) {
                    it.write(buffer, 0, len)
                }
                it.flush()
            }
        }
        return file
    }

    private fun File.createFolder() {
        if (exists()) {
            if (isDirectory) return
            delete()
        }
        mkdirs()
        return
    }

    private fun File.clear() {
        if (exists()) {
            if (isDirectory) {
                listFiles()?.forEach { it?.clear() }
            }
            delete()
        }
    }
}