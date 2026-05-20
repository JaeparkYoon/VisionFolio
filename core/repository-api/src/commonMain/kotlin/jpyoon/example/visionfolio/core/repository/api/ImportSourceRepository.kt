package jpyoon.example.visionfolio.core.repository.api

interface ImportSourceRepository {
    suspend fun resolveLabel(fingerprint: Long): String
}
