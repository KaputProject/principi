package classes


import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.math.BigDecimal

class PdfParser {
    val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
    val startBalanceRegex = Regex("""^Predhodno stanje na računu.*?:""")
    val inflowRegex = Regex("""^Promet v dobro.*?:""")
    val outflowRegex = Regex("""^Promet v breme.*?:""")
    val endBalanceRegex = Regex("""^Novo stanje na računu.*?:""")
    val ibanRegex = Regex("""^Št. računa v IBAN strukturi:""")

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
     * Parses a PDF file and extracts the statement information.
     *
     * @param parameters The statement parameters.
     *
     * @return A Statement object containing the extracted information.
     */
    fun parse(parameters: StatementParameters): Statement {
        try {
            var statement = Statement()

            // Loads the PDF as a string
            val document = PDDocument.load(File(parameters.file))
            val pdfText = PDFTextStripper().getText(document)
            document.close()

            val lines = pdfText.lines()

            statement = parseStatement(lines, statement, parameters)

            return statement
        } catch (e: Exception) {
            println("Error parsing PDF: ${e.message}")
            return Statement()
        }
    }

    /**
     * Parses the lines of the PDF and extracts transactions and other information.
     *
     * @param lines The lines of the PDF.
     * @param statement The statement object to populate.
     * @param parameters The statement parameters.
     *
     * @return The populated statement object.
     */
    fun parseStatement(lines: List<String>, statement: Statement, parameters: StatementParameters): Statement {
        var i = 0
        while (i < lines.size) {
            var currentLine = lines[i].trim()

            if (currentLine.isBlank()) {
                i++
                continue
            }

            // Parses the lines depending on which regex matches or skips the line
            if (dateRegex.matchesAt(currentLine, 0)) {
                i++
                while (i < lines.size && !dateRegex.matches(lines[i]) && lines[i].isNotBlank()) {
                    currentLine += " " + lines[i].trim()
                    i++
                }

                statement.transactions.add(parseTransaction(currentLine, parameters, statement.transactions.lastOrNull()))
            } else if (startBalanceRegex.containsMatchIn(currentLine)) {
                statement.startBalance = parseEuropeanNumber(currentLine.replace(startBalanceRegex, "").trim())
                i++
            } else if (inflowRegex.containsMatchIn(currentLine)) {
                statement.inflow = parseEuropeanNumber(currentLine.replace(inflowRegex, "").trim())
                i++
            } else if (outflowRegex.containsMatchIn(currentLine)) {
                statement.outflow = parseEuropeanNumber(currentLine.replace(outflowRegex, "").trim())
                i++
            } else if (endBalanceRegex.containsMatchIn(currentLine)) {
                statement.endBalance = parseEuropeanNumber(currentLine.replace(endBalanceRegex, "").trim())
                i++
            } else if (ibanRegex.containsMatchIn(currentLine)) {
                statement.iban = currentLine.replace(ibanRegex, "").trim()
                i++
            } else {
                i++
            }
        }

        // Assigns the start and end date of the statement
        statement.startDate = statement.transactions[0].date
        statement.endDate = statement.transactions[statement.transactions.size - 1].date

        // Handles the first transactions direction
        if (statement.transactions.isNotEmpty()) {
            statement.transactions[0].outgoing = statement.startBalance > statement.transactions[0].balance
        }

        return statement
    }

    /**
     * Parses a transaction line and returns a Transaction object.
     *
     * @param line The line to parse.
     * @param parameters The statement parameters.
     * @param previous The previous transaction (if any).
     *
     * @return A Transaction object.
     */
    fun parseTransaction(line: String, parameters: StatementParameters, previous: Transaction?): Transaction {
        val parts = line.trim().split(" ")

        if (parts.size >= 7) {
            val date = parts[0]
            val ref = parts[1]
            val change = parseEuropeanNumber(parts[parts.size - 2])
            val balance = parseEuropeanNumber(parts[parts.size - 1])
            var outgoing = true

            val (partner, description, existing) = handlePartnerDescription(parts.subList(2, parts.size - 2), parameters)

            if (previous != null && previous.balance < balance) {
                outgoing = false
            }

            return Transaction(
                date = date,
                reference = ref,
                partner = partner,
                description = description,
                change = change,
                balance = balance,
                known_partner = existing,
                outgoing = outgoing
            )
        }

        throw IllegalArgumentException("Transaction line with ref: ${parts[1]} is not valid")
    }

    /**
     * Handles the partner description and returns the partner name, description, and whether the partner is known.
     *
     * @param parts The parts of the transaction line.
     * @param parameters The statement parameters.
     *
     * @return A Triple containing the partner name, description, and whether the partner is known.
     */
    fun handlePartnerDescription(parts: List<String>, parameters: StatementParameters): Triple<String, String, Boolean> {
        var i = 1
        var existing = false

        var temp = parts[0]
        var partner = ""
        var description = ""

        while (i < parts.size) {
            // Check if the current part is a known partner
            if (parameters.partners.contains(temp)) {
                existing = true
                partner = temp
                description = parts.subList(i, parts.size).joinToString(" ")
                break
            }

            temp += " " + parts[i]
            i++
        }

        if (!existing) {
            description = parts.subList(2, parts.size).joinToString(" ")

            if (parameters.partners.contains(description)) {
                existing = true
                partner = description
            }
        }

        // If no known partner is found, assign the first two parts as the partner and the rest as the description
        if (!existing) {
            partner = parts.subList(0, 2).joinToString(" ")
            description = parts.subList(2, parts.size).joinToString(" ")
        }

        return Triple(partner, description, existing)
    }
}