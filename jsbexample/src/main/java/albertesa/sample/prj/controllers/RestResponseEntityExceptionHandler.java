package albertesa.sample.prj.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<Object> handleNotFound(IllegalArgumentException ex, WebRequest request) {
		String bodyOfResponse = String.format("{\"status\":\"error\",\"message\":\"%s\"}", ex.getMessage());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
		String bodyOfResponse = String.format("{\"status\":\"error\",\"message\":\"%s\"}", ex.getMessage());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
