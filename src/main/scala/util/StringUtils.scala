package util

object StringUtils {

  implicit class StringImprovements(s: String) {
    def removeNumberPrefix = s.replaceFirst("^\\d*\\.?\\d*", "")

    def removeUnwantedSymbolsXML = s.replaceAll("\"\"", "\"").replaceFirst("Request>\"", "Request>")
  }

}
