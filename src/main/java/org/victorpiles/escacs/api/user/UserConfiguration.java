package org.victorpiles.escacs.api.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuració inicial d'{@link User usuaris}. {@link UserService#register(User) Registra} un {@link User usuari} per
 * defecte.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Configuration
public class UserConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(UserService service) {
        return args -> {
            User user = new User(
                    "Administrator",
                    "admin@escacs.org",
                    "admin@escacs.org"
            );
            service.register(user);
        };
    }
}