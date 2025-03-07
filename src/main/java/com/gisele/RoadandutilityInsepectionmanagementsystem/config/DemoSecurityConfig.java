package com.gisele.RoadandutilityInsepectionmanagementsystem.config;


import com.gisele.RoadandutilityInsepectionmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

	// add a reference to our user service
    @Autowired
    private UserService userService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.sessionManagement()
				.sessionFixation().newSession()
				.and()
			.authorizeRequests()
				.antMatchers("/templates/**", "/static/**").permitAll()
				.antMatchers("/Authentication/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/Manager/showRegistrationForm", "/Manager/forgotPassword", "/Manager/changePassword").permitAll()
				.antMatchers("/showForgotPassword", "/showResetPassword").permitAll()

				.antMatchers("/Manager/processForm").permitAll()
				.antMatchers("/Inspection/mainPage").hasRole("PROJECT_MANAGER")
				.antMatchers("/Inspection/list").hasRole("INSPECTOR")
				.antMatchers("/showRegistrationForm", "/Manager/users/**", "/Manager/listUsers").hasRole("OPERATIONS_MANAGER")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/showMyLoginPage")
				.loginProcessingUrl("/authenticateTheUser")
				.successHandler(customAuthenticationSuccessHandler)
				.permitAll()
				.and()
				.logout()
				.invalidateHttpSession(true)
				.permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/access-denied");

	}

	//beans
	//bcrypt bean definition
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//authenticationProvider bean definition
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService); //set the custom user details service
		auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
		return auth;
	}
	  
}






