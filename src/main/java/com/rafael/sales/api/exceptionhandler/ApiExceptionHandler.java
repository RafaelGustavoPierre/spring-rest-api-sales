package com.rafael.sales.api.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.rafael.sales.domain.exception.EntityInUseException;
import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.exception.InsufficientStockException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.validator.internal.properties.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.ITEM_NOT_FOUND;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handleEntityInUse(EntityInUseException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTITY_IN_USE;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStockException(InsufficientStockException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.INSUFFICIENT_STOCK;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();

        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
                    String name = objectError.getObjectName();

                    if (objectError instanceof Field) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                }).toList();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();
        return handleExceptionInternal(ex, problem, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title(ex.getMessage())
                    .status(statusCode.value())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title((String) body)
                    .status(statusCode.value())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.ERROR_SYNTAX;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.MEDIA_TYPE_NOT_SUPPORTED;
        String detail = "O formato do objeto não é suportado pela API";

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = String.format("O recurso /%s que você tentou acessar, é inexistente.", ex.getResourcePath());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
                                                         HttpStatusCode status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = String.format("A propriedade '%s' não existe. " +
                "Corrija ou remova essa propriedade e tente novamente.", path);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage("Ocorreu um erro inesperado no sistema, tente novamente. Se o problema persistir entre em contato")
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.getPath().forEach(ref -> System.out.println(ref.getFieldName()));

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.ERROR_SYNTAX;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s' que é de um tipo Inválido. " +
                "Corrija e informe um valor compátivel com o tipo %s.", path, ex.getValue(), ex.getTargetType().getSimpleName());
        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType, String detail) {
        return Problem.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .title(problemType.getTitle())
                .type(problemType.getUri())
                .detail(detail);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(ref -> ref.getFieldName())
                .filter(fieldName -> fieldName != null && !fieldName.equals("null"))
                .collect(Collectors.joining("."));
    }
}
