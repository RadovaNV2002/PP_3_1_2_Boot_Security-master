package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kata.spring.boot_security.demo.configs.handler.LogHandler;
import ru.kata.spring.boot_security.demo.configs.handler.LogoutHandler;
import ru.kata.spring.boot_security.demo.service.UserService;


@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private LogoutHandler logoutHandler;

    @Autowired
    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder, LogoutHandler logoutHandler) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // конфигурация для прохождения аутентификации
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                // указываем страницу с формой логина
                .loginPage("/user-form")
                //указываем логику обработки при логине
                .successHandler(new LogHandler())
                // указываем action с формы логина
                .loginProcessingUrl("/user-form")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll();

        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login?logout")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .csrf().disable() //выключаем кроссдоменную секьюрность
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/", "/css/*", "/js/*").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        http.formLogin()
                .loginPage("/") // указываем страницу с формой логина
                .permitAll()  // даем доступ к форме логина всем
                .usernameParameter("email") // Указываем параметры логина и пароля с формы логина
                .passwordParameter("password");

        http.logout()
                .permitAll() // разрешаем делать логаут всем
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true) // сделать невалидной текущую сессию
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/?logout") // указываем URL при удачном логауте
                .logoutSuccessHandler(logoutHandler);
    }
}