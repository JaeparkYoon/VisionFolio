package jpyoon.example.visionfolio.core.common.di

import me.tatarka.inject.annotations.Scope

@Scope
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class AppSingleton
