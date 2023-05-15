package org.victorpiles.escacs.api.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.victorpiles.escacs.api.gamerequest.GameRequestService;
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
            userService.register("Administrator", "admin@escacs.com", "admin@escacs.com");
            userService.register("User", "user@escacs.com", "user@escacs.com");

            gameRequestService.send("Administrator", "User");
        };
    }
}