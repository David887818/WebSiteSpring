package com.example.demo.config;

import com.example.demo.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetails myUserDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/adminPage").hasAnyAuthority("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successForwardUrl("/loginUser")
                .permitAll();
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
////                .antMatchers("/userPage").hasAnyAuthority("USER")
////                .antMatchers("/adminPage").hasAuthority("ADMIN")
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .successForwardUrl("/loginUser")
//                .permitAll();
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetails)
                .passwordEncoder(passwordEncoder);
    }

}
