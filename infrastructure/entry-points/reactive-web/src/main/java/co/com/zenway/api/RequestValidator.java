package co.com.zenway.api;

import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RequestValidator {
    private final Validator validator;

    public <T> Mono<T> validarUsuario(T dto){
        return Mono.fromCallable(()-> {
            var errors = new BeanPropertyBindingResult(dto, dto.getClass().getName());
            validator.validate(dto, errors);
            if(errors.hasErrors()){
                throw new ValidationException(errors.toString());
            }
            return dto;
        });
    }
}
