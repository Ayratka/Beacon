package io.github.ayratka.beacon.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class BeaconTest {

    @Test
    fun `beacons with same data are equal`() {
        val a = beacon(id = "1", name = "PS5")
        val b = beacon(id = "1", name = "PS5")
        assertEquals(a, b)
    }

    @Test
    fun `beacons with different ids are not equal`() {
        val a = beacon(id = "1", name = "PS5")
        val b = beacon(id = "2", name = "PS5")
        assertNotEquals(a, b)
    }

    private fun beacon(id: String, name: String) = Beacon(
        id = BeaconId(id),
        name = name,
        url = "https://example.com",
        source = SourceType.HTML,
        extractor = Extractor.CssSelector("#price"),
        condition = Condition.Below(400.0),
        intervalMinutes = 60,
        enabled = true,
    )
}