package org.victorpiles.escacs.api.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.victorpiles.escacs.api.gamerequest.GameRequestService;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.api.user.UserService;

/**
 * Configuració inicial.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Configuration
public class Configurator {

    @Bean
    CommandLineRunner commandLineRunner(
            UserService userService,
            GameRequestService gameRequestService
    ) {
        return args -> {
            User administrator = new User(
                    "Administrator",
                    "admin@escacs.org",
                    "admin@escacs.org"
            );
            User user = new User(
                    "User",
                    "user@escacs.org",
                    "user@escacs.org"
            );
            userService.register(administrator);
            userService.register(user);

            gameRequestService.send("Administrator", "User");
        };
    }
}