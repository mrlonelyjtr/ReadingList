package ReadingList.config;

import ReadingList.model.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/readingList").hasRole("READER")
                    .antMatchers("/management/**").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error=true");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .and()
                .inMemoryAuthentication()
                    .withUser("admin").password("4321").roles("ADMIN");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDetails userDetails = readerRepository.findOne(username);
            if (userDetails != null) {
                return userDetails;
            }
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        };
    }

}
