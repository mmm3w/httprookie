package com.mitsuki.armory.httprookie.convert

import okhttp3.Response
import java.io.File

class FileConvert(private val saveFile: File) : Convert<File> {

    constructor(folder: File, name: String) : this(File(folder, name))
    constructor(folder: String, name: String) : this(File(folder, name))

    var bufferSize: Int = 1024

    override fun convertResponse(response: Response): File? {
        saveFile.parentFile?.createFolder()
        saveFile.clear()

        (response.body?.byteStream() ?: return null).use { inputStream ->
            var len: Int
            val buffer = ByteArray(bufferSize)
            saveFile.outputStream().use {
                while (inputStream.read(buffer).apply { len = this } != -1) {
                    it.write(buffer, 0, len)
                }
                it.flush()
            }
        }
        return saveFile
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