package classes

data class StatementParameters(
    val file: String = "src/main/resources/statements/test.pdf",
    val user: String = "KUDER LUKA",
    val partners: List<String> = listOf(
        "LANA K.",
        "UNIFITNES, D.O.O.",
        "MDDSZ-DRZAVNE STIPENDIJE - ISCSD 2",
        "ASPIRIA d.o.o.",
        "HUMANITARNO DRUŠTVO LIONS KLUB KONJICE",
        "PayPal Europe S.a.r.l. et Cie S.C.A",
        "TELEKOM SLOVENIJE D.D.",
    )
)
