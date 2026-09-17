package org.kuznetsov.rssnews.di

import io.ktor.client.engine.HttpClientEngine
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsState
import kotlin.test.Test

class KoinModulesCheckTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun allModulesResolveTheirDependencyGraph() {
        val allModules = module {
            includes(dataModule, domainModule, presentationModule, platformModule)
        }

        allModules.verify(
            extraTypes = listOf(String::class, NewsState::class, NewsId::class, HttpClientEngine::class),
        )
    }
}
