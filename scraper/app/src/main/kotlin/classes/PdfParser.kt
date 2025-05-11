package classes

import Statement
import StatementParameters
import Transaction
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.math.BigDecimal

class PdfParser {
    val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
    val initialBalanceRegex = Regex("""Predhodno stanje na računu (v EUR): """)
    val inflowRegex = Regex("""Promet v dobro:""")
    val outflowRegex = Regex("""Promet v breme:""")
    val endBalanceRegex = Regex("""Novo stanje na računu (v EUR): """)

    /**
     * Parses a string representing a number in European format (e.g., "1.234,56") to a BigDecimal.
     *
     * @param str The string to parse.
     *
     * @return The parsed BigDecimal.
     */
    fun parseEuropeanNumber(str: String): BigDecimal {
        val output = str.replace(".", "").replace(",", ".")

        return BigDecimal(output)
    }

    /**
     * Parses the PDF file and extracts transactions.
     *
     * @param file The path to the PDF file.
     *
     * @return A list of transactions extracted from the PDF.
     */
    fun parse(parameters: StatementParameters): List<Transaction> {
        try {
            val statement = Statement()
            val transactions = mutableListOf<Transaction>()

            // Loads the PDF as a string
            val document = PDDocument.load(File(parameters.file))
            val pdfText = PDFTextStripper().getText(document)
            document.close()

            val lines = pdfText.lines()

            val cleaned = cleanLines(lines, statement)

            for (line in cleaned) {
                transactions.add(parseTransaction(line))
            }

            return transactions
        } catch (e: Exception) {
            println("Error parsing PDF: ${e.message}")
            return emptyList()
        }
    }

    fun cleanLines(lines: List<String>, statement: Statement): List<String> {
        val cleanedLines = mutableListOf<String>()

        var i = 0
        while (i < lines.size) {
            var currentLine = lines[i].trim()

            if (currentLine.isBlank()) {
                i++
                continue
            }

            if (dateRegex.matchesAt(currentLine, 0)) {
                i++
                while (i < lines.size && !dateRegex.matches(lines[i]) && lines[i].isNotBlank()) {
                    currentLine += " " + lines[i].trim()
                    i++
                }
                cleanedLines.add(currentLine)
            } else {
                i++
            }
        }

        return cleanedLines
    }

    fun parseTransaction(line: String): Transaction {
        val parts = line.trim().split(" ")

        if (parts.size >= 7) {
            val date = parts[0]
            val ref = parts[1]
            val change = parseEuropeanNumber(parts[parts.size - 2])
            val balance = parseEuropeanNumber(parts[parts.size - 1])

            val partner = parts.subList(2, 4).joinToString(" ")
            val description = parts.subList(4, parts.size - 2).joinToString(" ")

            return Transaction(
                date = date,
                reference = ref,
                partner = partner,
                description = description,
                change = change,
                balance = balance
            )
        }

        throw IllegalArgumentException("Transaction line with ref: ${parts[1]} is not valid")
    }
}