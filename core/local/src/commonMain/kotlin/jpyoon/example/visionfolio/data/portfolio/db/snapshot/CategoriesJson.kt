@file:OptIn(kotlinx.serialization.InternalSerializationApi::class, kotlinx.serialization.ExperimentalSerializationApi::class)

package jpyoon.example.visionfolio.data.portfolio.db.snapshot

import jpyoon.example.visionfolio.domain.model.AssetCategory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive

object CategoriesJson {

    private val json = Json { ignoreUnknownKeys = true }

    fun encode(map: Map<AssetCategory, Long>): String {
        val obj = buildJsonObject {
            map.forEach { (category, value) -> put(category.name, JsonPrimitive(value)) }
        }
        return json.encodeToString(JsonObject.serializer(), obj)
    }

    fun decode(raw: String): Map<AssetCategory, Long> {
        if (raw.isBlank()) return emptyMap()
        val element = runCatching { json.parseToJsonElement(raw) }.getOrNull() ?: return emptyMap()
        val obj = element as? JsonObject ?: return emptyMap()
        return obj.entries.mapNotNull { (key, value) ->
            val category = runCatching { AssetCategory.valueOf(key) }.getOrNull() ?: return@mapNotNull null
            val amount = value.jsonPrimitive.content.toLongOrNull() ?: return@mapNotNull null
            category to amount
        }.toMap()
    }
}
