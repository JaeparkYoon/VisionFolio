package jpyoon.example.visionfolio.data.repository

interface ImportSourceRepository {
    suspend fun resolveLabel(fingerprint: Long): String
}
