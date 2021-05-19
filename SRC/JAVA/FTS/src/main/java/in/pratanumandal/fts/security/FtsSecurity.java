package in.pratanumandal.fts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import in.pratanumandal.fts.bean.FtsConfig.Credentials.Credential;

import in.pratanumandal.fts.util.FtsConstants;

@Configuration
public class FtsSecurity extends WebSecurityConfigurerAdapter {

	@Override  
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (FtsConstants.CREDENTIALS == null) {
			auth.inMemoryAuthentication()
				.withUser("admin")
				.password(passwordEncoder().encode("admin"))
				.roles("ADMIN");
		}
		else {
			for (Credential credential : FtsConstants.CREDENTIALS) {
				auth.inMemoryAuthentication()
					.withUser(credential.getUsername())
					.password(passwordEncoder().encode(credential.getPassword()))
					.roles(this.getRole(credential));
			}
		}
	}
	
	@Override  
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		
		.csrf()
		.disable()
		
		.antMatcher("/**")
		.authorizeRequests()
		.antMatchers("/favicon.ico").permitAll()
		.antMatchers("/css/style.css").permitAll()
		.antMatchers("/font/Roboto-Regular.ttf").permitAll()
		.antMatchers("/js/common.js").permitAll()
		.antMatchers("/js/jquery.*.js").permitAll()
		.antMatchers("/img/login.svg").permitAll()
		.anyRequest().hasAnyRole("ADMIN", "WRITER", "READER")
		
		.and()
		
		.formLogin()
		.loginPage("/login")
		.permitAll()
		
		.and()
		
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	private String getRole(Credential credential) {
		String role = credential.getRole();
		
		if (role == null) return "READER";
		if (role.equalsIgnoreCase("ADMIN")) return "ADMIN";
		if (role.equalsIgnoreCase("WRITER")) return "WRITER";
		return "READER";
	}
	
}
