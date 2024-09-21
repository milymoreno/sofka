package com.prueba.sofka.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueSofkianoValidator.class)
@Documented
public @interface UniqueSofkiano {
    String message() default "Ya existe un cliente registrado con este tipo y número de identificación";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
