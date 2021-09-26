package patterns.decorator

import java.io.*
import java.util.*
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

fun main() {
    val salaryRecords = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000"
    val encoded = CompressorDecorator(
        EncryptionDecorator(
            FileDataSource("out/OutputDemo.txt")
        )
    )
    encoded.writeData(salaryRecords)
    val plain = FileDataSource("out/OutputDemo.txt")

    println("- Input --------------------")
    println(salaryRecords)
    println("- Encoded ------------------")
    println(plain.readData())
    println("- Decoded ------------------")
    println(encoded.readData())
}

//region decorators

private interface DataSource {
    fun writeData(data: String)
    fun readData(): String
}

private class FileDataSource(
    private val name: String
) : DataSource {

    override fun writeData(data: String) {
        val file = File(name)
        try {
            val fos = FileOutputStream(file)
            fos.write(data.toByteArray(), 0, data.length)
        } catch (ex: IOException) {
            println(ex.message)
        }
    }

    override fun readData(): String {
        val buffer: CharArray
        val file = File(name)
        return try {
            val reader = FileReader(file)
            buffer = CharArray(file.length().toInt())
            reader.read(buffer)
            String(buffer) //not .toString
        } catch (ex: IOException) {
            throw IOException(ex.message, ex)
        }
    }
}

private open class DataSourceDecorator(
    private val wrappee: DataSource
) : DataSource {

    override fun writeData(data: String) {
        wrappee.writeData(data)
    }

    override fun readData(): String {
        return wrappee.readData()
    }
}

private class EncryptionDecorator(source: DataSource) : DataSourceDecorator(source) {

    override fun writeData(data: String) {
        super.writeData(encode(data))
    }

    override fun readData(): String {
        return decode(super.readData())
    }

    private fun encode(data: String): String {
        val result = data.toByteArray()
        result.forEachIndexed { index, byte ->
            result[index] = byte.inc()
        }
        return Base64.getEncoder().encodeToString(result)
    }

    private fun decode(data: String): String {
        val result: ByteArray = Base64.getDecoder().decode(data)
        result.forEachIndexed { index, byte ->
            result[index] = byte.dec()
        }
        return String(result) //not .toString
    }
}

private class CompressorDecorator(source: DataSource) : DataSourceDecorator(source) {

    var compLevel: Int = 6

    override fun writeData(data: String) {
        super.writeData(compress(data))
    }

    override fun readData(): String {
        return decompress(super.readData())
    }

    private fun compress(data: String): String {
        val dataBytes = data.toByteArray()
        return try {
            val bout = ByteArrayOutputStream(512)
            val dos = DeflaterOutputStream(bout, Deflater(compLevel))
            dos.write(dataBytes)
            dos.close()
            bout.close()
            Base64.getEncoder().encodeToString(bout.toByteArray())
        } catch (ex: IOException) {
            throw IOException(ex.message, ex)
        }
    }

    private fun decompress(data: String): String {
        val dataBytes = Base64.getDecoder().decode(data)
        return try {
            val bis = ByteArrayInputStream(dataBytes)
            val iin = InflaterInputStream(bis)
            val bout = ByteArrayOutputStream(512)
            var bytesRead: Int
            while (iin.read().also { bytesRead = it } != -1) {
                bout.write(bytesRead)
            }
            bis.close()
            iin.close()
            bout.close()
            String(bout.toByteArray()) //not .toString
        } catch (ex: IOException) {
            throw IOException(ex.message, ex)
        }
    }
}

//endregion