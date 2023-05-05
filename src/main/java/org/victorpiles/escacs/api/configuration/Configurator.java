package org.victorpiles.escacs.api.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.api.move.MoveService;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.api.user.UserService;

/**
 * Configuració inicial. {@link UserService#register(User) Registra} un {@link User usuari} per defecte amb dos
 * {@link Move moviments}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Configuration
public class Configurator {

    @Bean
    CommandLineRunner commandLineRunner(UserService userService, MoveService moveService) {
        return args -> {
            User user = new User(
                    "Administrator",
                    "admin@escacs.org",
                    "admin@escacs.org"
            );
            userService.register(user);

            moveService.execute("f4", "Administrator");
            moveService.execute("exf4", "Administrator");
        };
    }
}