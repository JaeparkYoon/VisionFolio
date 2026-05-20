package jpyoon.example.visionfolio.shared

internal actual fun createIosAppComponentInstance(): IosAppComponent =
    IosAppComponent::class.create()
