package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    //Настраивает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/admin-update/**").hasAnyRole("ADMIN")
                .antMatchers("/user-update/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();
    }

    // аутентификация inMemory
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.builder()
                        .username("user")
                        .password("{bcrypt}$2a$12$rRWkz7qVPdyna5AgC3IH6elcOOpz.p1PLNcg/SZ0pufZE08MuG7ne")        //пароль - user
                        //.password("User")
                        .roles("USER")
                        .build();
        UserDetails admin =
                User.builder()
                        .username("admin")
                        .password("{bcrypt}$2a$12$vDru3dUVPQOAMkyBE5er..dairXaBpGn26UAoR8fZs3kMdltp5ImC")        //пароль - admin
                        //.password("admin")
                        .roles("ADMIN", "USER")
                        .build();

        return new InMemoryUserDetailsManager(user, admin);
    }


}