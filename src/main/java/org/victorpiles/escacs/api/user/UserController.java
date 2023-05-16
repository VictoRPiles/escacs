package org.victorpiles.escacs.api.user;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Endpoints relacionats amb els {@link User usuaris}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final @NotNull UserService userService;

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} tots els {@link User usuaris}.
     *
     * @return Un llistat tots els {@link User usuaris}.
     *
     * @see UserService#list()
     */
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<List<User>> list() {
        List<User> userList = userService.list();
        return ResponseEntity.ok(userList);
    }

    /**
     * Consumeix la informació per a crear un nou {@link User usuari} i genera una {@link ResponseEntity resposta} amb
     * estatus 201 (created) si s'ha registrat amb èxit.
     *
     * @return {@link ResponseEntity Resposta} amb estatus 201 (created) si s'ha registrat amb èxit.
     */
    @PostMapping(path = "/register")
    public @NotNull ResponseEntity<User> register(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password) {
        User registered = userService.register(username, email, password);
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest().path("/register").buildAndExpand(registered).toUri()
                )
                .body(registered);
    }

    /**
     * Consumeix un {@link User#getEmail() correu} i una {@link User#getPassword() contrasenya} i genera una
     * {@link ResponseEntity resposta} amb estatus 200 (ok) si ha iniciat sessió amb èxit.
     *
     * @param email    El {@link User#getEmail() correu}.
     * @param password La {@link User#getPassword() contrasenya}.
     *
     * @return Una {@link ResponseEntity resposta} amb estatus 200 (ok) si ha iniciat sessió amb èxit.
     *
     * @see UserService#login(String, String)
     */
    @PostMapping(path = "/login")
    public @NotNull ResponseEntity<User> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        User logged = userService.login(email, password);
        return ResponseEntity.ok(logged);
    }

    @PutMapping(path = "/score")
    public @NotNull ResponseEntity<User> score(@RequestParam("id") @NotNull Long id, @RequestParam("score") int score) {
        User scored = userService.score(id, score);
        return ResponseEntity.ok(scored);
    }
}