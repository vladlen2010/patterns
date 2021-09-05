package patterns.builder

fun main() {
    val mail = Mail("hello").message("How are you?")
    val mailBuilder = MailBuilder("hello").title("Summer letter").build()
    println(mail._message)
    println(mailBuilder.title)
}

private data class Mail(
    val to: String,
    var title: String = "",
    var _message: String = "",
    val cc: List<String> = listOf(),
    val bcc: List<String> = listOf(),
    val attachments: List<java.io.File> = listOf()
) {

    fun message(message: String) = apply {
        _message = message
    }
}

/**
 * builder design pattern
 */
private class MailBuilder(to: String) {
    private var mail: Mail = Mail(to)
    fun title(title: String): MailBuilder {
        mail.title = title
        return this
    }

    /*
    ...
     */

    fun build(): Mail {
        return mail
    }
}