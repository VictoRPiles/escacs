package org.victorpiles.escacs.api.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.victorpiles.escacs.api.exception.user.BadCredentialsException;
import org.victorpiles.escacs.api.exception.user.EmailAlreadyInUseException;
import org.victorpiles.escacs.api.exception.user.EmailNotFoundException;
import org.victorpiles.escacs.api.exception.user.UsernameAlreadyInUseException;
import org.victorpiles.escacs.api.security.PasswordEncoder;

import java.util.List;
import java.util.Optional;

/**
 * Lògica de negoci relacionada amb els {@link User usuaris}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Busca tots els {@link User usuaris} presents a la base de dades.
     *
     * @return Un {@link List llistat} amb els {@link User usuaris} presents a la base de dades.
     */
    public List<User> list() {
        return userRepository.findAll();
    }

    /**
     * Registra un nou {@link User usuari} en la base de dades.
     * <p>
     * Un {@link User usuari} es podrà registrar si el seu {@link User#getEmail() email} no està sent utilitzat per
     * altre.
     * <p>
     * La {@link User#getPassword() contrasenya} s'{@link PasswordEncoder#encode(String) encriptarà} abans de guardar-lo
     * en la base de dades.
     *
     * @param user La informació de l'{@link User usuari}.
     *
     * @return La informació de l'{@link User usuari} si s'ha registrat exitosament.
     */
    public User register(User user) {
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByUsername.isPresent()) {
            throw new UsernameAlreadyInUseException("There’s already an account with the username " + user.getUsername() + ". Use a different username.");
        }

        if (userByEmail.isPresent()) {
            throw new EmailAlreadyInUseException("There’s already an account with the email " + user.getEmail() + ". Use a different email or sign in with this address.");
        }

        /* Encripta la contrasenya abans de guardar en la base de dades */
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return user;
    }

    /**
     * Permet a un {@link User usuari} iniciar sessió mitjançant el {@link User#getEmail() email} i la
     * {@link User#getPassword() contrasenya}.
     * <p>
     * Comprova si el {@link User#getEmail() email} existeix i si la {@link User#getPassword() contrasenya} està
     * associada a aquest.
     *
     * @param email    El {@link User#getEmail() email}.
     * @param password La {@link User#getPassword() contrasenya}.
     *
     * @return La informació de l'{@link User usuari} si ha iniciat sessió exitosament.
     *
     * @see PasswordEncoder#match(String, String)
     */
    public User login(String email, String password) {
        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isEmpty()) {
            throw new EmailNotFoundException("We don't have an account for the email " + email + ". Try creating an account instead.");
        }

        String storedPassword = userByEmail.get().getPassword();
        if (!PasswordEncoder.match(password, storedPassword)) {
            throw new BadCredentialsException("That email and password combination didn't work. Try again.");
        }

        return userByEmail.get();
    }
}