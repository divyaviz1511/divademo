package divademo.divademo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecuriyConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    /*http
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Permit all requests
            .csrf(AbstractHttpConfigurer::disable); // Disable CSRF for simplicity in temporary scenarios
     return http.build();*/
    http
        .authorizeHttpRequests((requests)-> requests
        .requestMatchers("/","/api/*").permitAll()
        .requestMatchers("/","/h2-console").permitAll()
        .anyRequest().permitAll()
        )

        .formLogin((form) -> form
        .loginPage("/login")
        .permitAll()
        )

        .logout((logout) -> logout.permitAll());
        
        //.csrf().disable(); // Added so I can continue to use PostMan

        return http.build();
}

@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = 
        User.withDefaultPasswordEncoder()
        .username("user")
        .password("password")
        .build();
    
        return new InMemoryUserDetailsManager(user);
}
}
