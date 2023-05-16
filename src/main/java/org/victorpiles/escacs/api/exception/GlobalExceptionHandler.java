package org.victorpiles.escacs.api.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controla la forma de manejar les {@link Exception excepcions}.
 * <p>
 * Concretament aquesta implementació se centra en les {@link ConstraintViolationException excepcions de validació}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura les {@link ConstraintViolationException exepcions de validació}, extrau els missatges i amb aquests
     * construeix una {@link ResponseEntity resposta} amb l'estatus {@link HttpStatus#BAD_REQUEST}.
     *
     * @param exception L'{@link ConstraintViolationException excepció de validació}
     *
     * @return Una {@link ResponseEntity resposta} amb els missatges i l'estatus {@link HttpStatus#BAD_REQUEST}.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public @NotNull ResponseEntity<Map<String, List<String>>> handleValidationErrors(@NotNull ConstraintViolationException exception) {
        List<String> errors = exception
                .getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Afig el títol "errors" a la resposta.
     * <p>
     * És una forma d'interpretar la resposta de forma més clara al JSON.
     *
     * @param errors Llistat d'errors.
     *
     * @return Un {@link Map mapa} amb la clau "errors" i el llistat d'errors com a valor.
     */
    private @NotNull Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}