package io.aiven.klaw.error;

import static io.aiven.klaw.error.KlawErrorMessages.REQ_FAILURE;

import io.aiven.klaw.model.ApiResponse;
import io.aiven.klaw.model.enums.ApiResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class KlawExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({KlawException.class})
  protected ResponseEntity<ApiResponse> handleKlawExceptionInternal(
      KlawException ex, WebRequest request) {
    log.error("Error ", ex);
    return new ResponseEntity<>(
        ApiResponse.builder().success(false).message(REQ_FAILURE).build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({KlawNotAuthorizedException.class})
  protected ResponseEntity<ApiResponse> handleKlawNotAuthoirzedExceptionInternal(
      KlawException ex, WebRequest request) {
    log.error("Error ", ex);
    return new ResponseEntity<>(
        ApiResponse.builder().success(false).message(ApiResultStatus.NOT_AUTHORIZED.value).build(),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({KlawRestException.class})
  protected ResponseEntity<ApiResponse> handleKlawRestExceptionInternal(
      KlawRestException ex, WebRequest request) {
    log.error("Error ", ex);
    return new ResponseEntity<>(
        ApiResponse.builder().success(false).message(ex.getMessage()).build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({KlawValidationException.class})
  protected ResponseEntity<ApiResponse> handleKlawValidationExceptionInternal(
      KlawValidationException ex, WebRequest request) {
    log.error("KlawValidationException handler: ", ex);
    return new ResponseEntity<>(
        ApiResponse.builder().success(false).message(ex.getMessage()).build(), HttpStatus.CONFLICT);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {
    log.error("Validation Error ", ex);
    return new ResponseEntity<>(
        ApiResponse.builder()
            .success(false)
            .message(ex.getAllErrors().get(0).getDefaultMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }
}
