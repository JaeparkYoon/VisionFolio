package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.ParsedReturnEntry

interface ReturnEntryParser {
    suspend fun parse(text: String): List<ParsedReturnEntry>
}
