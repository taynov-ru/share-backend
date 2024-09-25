package ru.taynov.share.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors(Customizer.withDefaults())
            .httpBasic { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/**").permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling(Customizer.withDefaults())
        return http.build()
    }

    @Bean
     fun corsConfigurationSource(): CorsConfigurationSource {
         val source = UrlBasedCorsConfigurationSource()
         val configuration = CorsConfiguration()
         configuration.addAllowedMethod("*")
         configuration.addAllowedHeader("*")
         configuration.addAllowedOrigin("*")
         configuration.addExposedHeader("*")
         source.registerCorsConfiguration("/**", configuration)
         return source
     }
}