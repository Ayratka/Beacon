package io.github.ayratka.beacon.local.mappers

import io.github.ayratka.beacon.local.BeaconCheckEntity
import io.github.ayratka.beacon.local.BeaconEntity
import io.github.ayratka.beacon.model.Beacon
import io.github.ayratka.beacon.model.BeaconCheck
import io.github.ayratka.beacon.model.BeaconId
import io.github.ayratka.beacon.model.Condition
import io.github.ayratka.beacon.model.Extractor
import io.github.ayratka.beacon.model.SourceType
import kotlin.error
import java.time.Instant

fun BeaconEntity.toDomain(): Beacon = Beacon(
    id = BeaconId(id),
    name = name,
    url = url,
    source = SourceType.valueOf(source),
    extractor = extractorFrom(type = extractorType, value = extractorValue),
    condition = conditionFrom(
        type = conditionType,
        value = conditionValue,
        text = conditionText,
    ),
    intervalMinutes = intervalMinutes,
    enabled = enabled,
)

fun Beacon.toEntity(): BeaconEntity = BeaconEntity(
    id = id.value,
    name = name,
    url = url,
    source = source.name,
    extractorType = extractorType(extractor),
    extractorValue = extractorValue(extractor),
    conditionType = conditionType(condition),
    conditionValue = conditionValue(condition),
    conditionText = conditionText(condition),
    intervalMinutes = intervalMinutes,
    enabled = enabled,
)

fun BeaconCheckEntity.toDomain(): BeaconCheck = BeaconCheck(
    beaconId = BeaconId(beaconId),
    checkedAt = Instant.ofEpochMilli(checkedAt),
    value = value,
    conditionFired = conditionFired,
)

fun BeaconCheck.toEntity(): BeaconCheckEntity = BeaconCheckEntity(
    beaconId = beaconId.value,
    checkedAt = checkedAt.toEpochMilli(),
    value = value,
    conditionFired = conditionFired,
)

private fun extractorType(extractor: Extractor): String = when (extractor) {
    is Extractor.CssSelector -> "CSS_SELECTOR"
    is Extractor.JsonPath -> "JSON_PATH"
    is Extractor.Regex -> "REGEX"
}

private fun extractorValue(extractor: Extractor): String = when (extractor) {
    is Extractor.CssSelector -> extractor.selector
    is Extractor.JsonPath -> extractor.path
    is Extractor.Regex -> extractor.pattern
}

private fun extractorFrom(type: String, value: String): Extractor = when (type) {
    "CSS_SELECTOR" -> Extractor.CssSelector(value)
    "JSON_PATH" -> Extractor.JsonPath(value)
    "REGEX" -> Extractor.Regex(value)
    else -> error("Unknown extractor type: $type")
}

private fun conditionType(condition: Condition): String = when (condition) {
    is Condition.Below -> "BELOW"
    is Condition.Above -> "ABOVE"
    is Condition.Contains -> "CONTAINS"
    Condition.Changed -> "CHANGED"
}

private fun conditionValue(condition: Condition): Double? = when (condition) {
    is Condition.Below -> condition.threshold
    is Condition.Above -> condition.threshold
    is Condition.Contains -> null
    Condition.Changed -> null
}

private fun conditionText(condition: Condition): String? = when (condition) {
    is Condition.Contains -> condition.text
    else -> null
}

private fun conditionFrom(type: String, value: Double?, text: String?): Condition = when (type) {
    "BELOW" -> Condition.Below(value ?: error("BELOW requires a value"))
    "ABOVE" -> Condition.Above(value ?: error("ABOVE requires a value"))
    "CONTAINS" -> Condition.Contains(text ?: error("CONTAINS requires text"))
    "CHANGED" -> Condition.Changed
    else -> error("Unknown condition type: $type")
}