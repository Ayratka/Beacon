package io.github.ayratka.beacon.model

sealed interface Extractor {
    data class CssSelector(val selector: String) : Extractor
    data class JsonPath(val path: String) : Extractor
    data class Regex(val pattern: String) : Extractor
}