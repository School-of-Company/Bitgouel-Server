package team.msg.global.config

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.msg.global.config.properties.CacheProperties
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val simpleCacheManager = SimpleCacheManager()

        val caches = CacheProperties.values().map {
            CaffeineCache(it.cacheName, buildCaffeineCache(it))
        }

        simpleCacheManager.setCaches(caches)

        return simpleCacheManager
    }

    private fun buildCaffeineCache(cacheProperties: CacheProperties): Cache<Any,Any> =
        Caffeine.newBuilder()
            .maximumSize(cacheProperties.maximumSize)
            .expireAfterAccess(cacheProperties.expiredAfterWrite, TimeUnit.MINUTES)
            .recordStats()
            .build()
}