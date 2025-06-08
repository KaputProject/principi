package classes


import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.math.BigDecimal

class PdfParserOtp {
    val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
    val startBalanceRegex = Regex("""^Predhodno stanje na računu.*?:""")
    val inflowRegex = Regex("""^Promet v dobro.*?:""")
    val outflowRegex = Regex("""^Promet v breme.*?:""")
    val endBalanceRegex = Regex("""^Novo stanje na računu.*?:""")
    val ibanRegex = Regex("""^Št. računa v IBAN strukturi:""")
    val ibanOtpRegex = Regex("""TRANSAKCIJSKEM RAČUNU ŠTEV\. (SI\d{2} \d{4} \d{4} \d{4} \d{3})""")
    val balanceLineRegex = Regex("""EUR\s+([-\d.,]+)\s+([-\d.,]+)\s+([-\d.,]+)\s+([-\d.,]+)""")

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

            var statement = Statement(user = parameters.user)
            val document = PDDocument.load(File(parameters.file))
            val pdfText = PDFTextStripper().getText(document)
            println("Parsing file: ${parameters.file}")
            println("PDF content lines: ${pdfText.lines().take(20)}")
            document.close()
            val lines = pdfText.lines()
            statement = parseStatementOtp(lines, statement, parameters)
            return statement
        } catch (e: Exception) {
            println("Error parsing PDF: ${e.message}")
            return Statement(user = parameters.user)
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
    fun parseStatementOtp(lines: List<String>, statement: Statement, parameters: StatementParameters): Statement {
        var i = 0
        while (i < lines.size) {
            val line = lines[i].trim()

            if (ibanOtpRegex.containsMatchIn(line)) {
                statement.iban = ibanOtpRegex.find(line)?.groupValues?.get(1)?.trim() ?: ""
            }

            if (balanceLineRegex.containsMatchIn(line)) {
                val match = balanceLineRegex.find(line)!!
                statement.startBalance = parseEuropeanNumber(match.groupValues[1])
                statement.outflow = parseEuropeanNumber(match.groupValues[2])
                statement.inflow = parseEuropeanNumber(match.groupValues[3])

                statement.endBalance = parseEuropeanNumber(match.groupValues[4])
            }

            if (dateRegex.containsMatchIn(line) && line.contains(Regex("""\d{10}"""))) {
                val date = dateRegex.find(line)?.value ?: ""
                val id = line.split(" ").find { it.matches(Regex("""\d{10}""")) } ?: ""

                var j = i + 1
                val parts = mutableListOf<String>()
                while (j < lines.size && lines[j].trim().isNotBlank()) {
                    parts.add(lines[j].trim())
                    if (lines[j].contains(Regex("""[-\d.,]+"""))) break
                    j++
                }

                val allLines = (listOf(line) + parts)
                val transaction = parseTransactionOtp(allLines, parameters, statement.transactions.lastOrNull())
                statement.transactions.add(transaction)

                i = j
            }

            i++
        }

        if (statement.transactions.isNotEmpty()) {
            statement.startDate = statement.transactions.first().date
            statement.endDate = statement.transactions.last().date
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
    fun parseTransactionOtp(lines: List<String>, parameters: StatementParameters, previous: Transaction?): Transaction {
        val flat = lines.joinToString(" ")
        val date = dateRegex.find(flat)?.value ?: ""
        val reference = flat.split(" ").find { it.matches(Regex("""\d{10}""")) } ?: ""

        val amounts = flat.split(" ").filter { it.matches(Regex("""[-\d.,]+""")) }
        val change = parseEuropeanNumber(amounts.first())
        val balance = parseEuropeanNumber(amounts.last())

        val knownPartner = parameters.partners.find { flat.contains(it, ignoreCase = true) }
        val outgoing = previous?.let { it.balance > balance } ?: (change < BigDecimal.ZERO)

        val partner = knownPartner ?: flat.split(" ").take(3).joinToString(" ")
        val description = flat.replace(date, "").replace(reference, "").replace(partner, "").trim()

        return Transaction(
            date = date,
            reference = reference,
            partner = partner,
            description = description,
            change = change,
            balance = balance,
            outgoing = outgoing,
            known_partner = knownPartner != null
        )
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