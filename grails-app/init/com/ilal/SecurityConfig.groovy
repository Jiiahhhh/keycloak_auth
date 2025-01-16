package com.ilal

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

@KeycloakConfiguration
@EnableWebSecurity
class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return null
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http)
        http.csrf().disable() // Nonaktifkan CSRF untuk pengujian, aktifkan di produksi jika diperlukan
                .authorizeRequests()
                .antMatchers("/public/**").permitAll() // Endpoint public dapat diakses tanpa autentikasi
                .anyRequest().authenticated() // Endpoint lainnya membutuhkan autentikasi
                .and()
                .logout()
                .logoutUrl("/logout").permitAll() // Konfigurasi logout
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        def keycloakAuthenticationProvider = keycloakAuthenticationProvider() // Gunakan 'def' untuk deklarasi variabel
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper())
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }
}
