package ro.dobrescuandrei.roomdynamicdao

fun String.removeUnnecessarySpaces() = this
    .trim()
    .replace("\n", " ")
    .replace("[ ]{2,}".toRegex(), " ")
    .replace(" ,", ",")
    .replace("( ", "(")
    .replace(" )", ")")
