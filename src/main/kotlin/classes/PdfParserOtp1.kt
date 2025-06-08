import classes.Statement
import classes.StatementParameters
import classes.Transaction
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.math.BigDecimal

class PdfParserOtp1 {
    val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
    val ibanOtpRegex = Regex("""TRANSAKCIJSKEM RAČUNU ŠTEV\. (SI\d{2} \d{4} \d{4} \d{4} \d{3})""")
    val balanceLineRegex = Regex("""EUR\s+([-\d.,]+)\s+([-\d.,]+)\s+([-\d.,]+)\s+([-\d.,]+)""")

    fun parseEuropeanNumber(str: String): BigDecimal {
        val output = str.replace(".", "").replace(",", ".")
        return BigDecimal(output)
    }

    fun parse(parameters: StatementParameters): Statement {
        try {
            val statement = Statement(user = parameters.user)
            val document = PDDocument.load(File(parameters.file))
            val pdfText = PDFTextStripper().getText(document)
            document.close()
            val lines = pdfText.lines()
            println("Parsing file: ${parameters.file}")
            println("PDF content lines: ${lines.take(20)}")
            return parseStatementOtp(lines, statement, parameters)
        } catch (e: Exception) {
            println("Error parsing PDF: ${e.message}")
            return Statement(user = parameters.user)
        }
    }

    fun parseStatementOtp(lines: List<String>, statement: Statement, parameters: StatementParameters): Statement {
        var i = 0
        while (i < lines.size) {
            val line = lines[i].trim()

            if (ibanOtpRegex.containsMatchIn(line)) {
                statement.iban = ibanOtpRegex.find(line)?.groupValues?.get(1)?.trim() ?: ""
                println("Extracted IBAN: ${statement.iban}")
            }

            if (balanceLineRegex.containsMatchIn(line)) {
                val match = balanceLineRegex.find(line)!!
                statement.startBalance = parseEuropeanNumber(match.groupValues[1])
                statement.outflow = parseEuropeanNumber(match.groupValues[2])
                statement.inflow = parseEuropeanNumber(match.groupValues[3])
                statement.endBalance = parseEuropeanNumber(match.groupValues[4])
                println("Extracted balances: start=${statement.startBalance}, outflow=${statement.outflow}, inflow=${statement.inflow}, end=${statement.endBalance}")
            }

            // Novi način zbiranja transakcije preko več vrstic
            if (dateRegex.matches(line)) {
                val date = line
                val idLine = lines.getOrNull(i + 1)?.trim() ?: ""
                val id = if (idLine.matches(Regex("""\d{10}"""))) idLine else ""

                val parts = mutableListOf<String>()
                var j = i + 2

                // Zberi vse vrstice, dokler ne naletimo na novo transakcijo (datum) ali prazen niz
                while (j < lines.size && !(dateRegex.matches(lines[j].trim())) && lines[j].trim().isNotBlank()) {
                    parts.add(lines[j].trim())
                    j++
                }

                if (id.isNotBlank()) {
                    val allLines = listOf(date, id) + parts
                    val transaction = parseTransactionOtp(allLines, parameters, statement.transactions.lastOrNull())
                    statement.transactions.add(transaction)
                    i = j - 1 // premakni indeks na zadnjo obdelano vrstico
                    continue
                }
            }

            i++
        }

        if (statement.transactions.isNotEmpty()) {
            statement.startDate = statement.transactions.first().date
            statement.endDate = statement.transactions.last().date
            statement.transactions[0].outgoing = statement.startBalance > statement.transactions[0].balance
            println("Parsed ${statement.transactions.size} transactions.")
        } else {
            println("No transactions found.")
        }

        return statement
    }


    fun parseTransactionOtp(lines: List<String>, parameters: StatementParameters, previous: Transaction?): Transaction {
        val date = lines.firstOrNull { dateRegex.matches(it) } ?: ""
        val reference = lines.firstOrNull { it.matches(Regex("""\d{10}""")) } ?: ""

        // Poišči vse evropske številke v vrsticah
        val numberRegex = Regex("""-?\d{1,3}(\.\d{3})*,\d{2}""")
        val allNumbers = lines.flatMap { it.split(" ") }.filter { numberRegex.matches(it) }

        val change = allNumbers.getOrNull(0)?.let { parseEuropeanNumber(it) } ?: BigDecimal.ZERO
        val balance = allNumbers.getOrNull(1)?.let { parseEuropeanNumber(it) } ?: BigDecimal.ZERO

        val flat = lines.joinToString(" ")

        // Poišči znanega partnerja (če obstaja)
        val knownPartner = parameters.partners.find { flat.contains(it, ignoreCase = true) }

        val outgoing = previous?.let { it.balance > balance } ?: (change < BigDecimal.ZERO)

        // Partner je prva vrstica (po datumu in referenci), ki ni številka ali prazna
        val partnerCandidate = lines.drop(2).firstOrNull {
            it.isNotBlank() && !numberRegex.matches(it.trim()) && it.length < 40
        } ?: "NEZNANO"

        // Opis je vse kar sledi partnerju
        val descriptionStartIndex = lines.indexOfFirst { it == partnerCandidate }
        val description = if (descriptionStartIndex >= 0) {
            lines.drop(descriptionStartIndex).joinToString(" ").removePrefix(partnerCandidate).trim()
        } else {
            ""
        }

        println("Transaction:")
        println("  date = $date")
        println("  reference = $reference")
        println("  change = $change")
        println("  balance = $balance")
        println("  partner = $partnerCandidate")
        println("  known = ${knownPartner != null}")

        return Transaction(
            date = date,
            reference = reference,
            partner = partnerCandidate,
            description = description,
            change = change,
            balance = balance,
            outgoing = outgoing,
            known_partner = knownPartner != null
        )
    }


}
