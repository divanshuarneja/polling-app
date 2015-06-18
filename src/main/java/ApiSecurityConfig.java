import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;


@Configuration
//@ComponentScan
@EnableWebSecurity
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

@Override
protected void configure(HttpSecurity http) throws Exception{
	http
		.authorizeRequests()
		.antMatchers("/api/v1/moderators","/api/v1/polls/*").permitAll()
		.anyRequest().authenticated()
		.and()
		
	    .csrf().disable()
	    .authorizeRequests()
	    .antMatchers("/api/v1/moderators/*").hasRole("USER").and()
	    .httpBasic();
}


@Autowired
//@Override
protected void configure(AuthenticationManagerBuilder builder) throws Exception
{
	builder
			.inMemoryAuthentication()
			.withUser("foo").password("bar").roles("USER");
}


}