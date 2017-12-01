package cn.artviewer.gallery.advice;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import cn.artviewer.gallery.dto.ResponseInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * springmvc异常处理
 * 
 * @author turkey
 *
 */
@Slf4j(topic = "adminLogger")
@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseInfo badRequestException(IllegalArgumentException exception) {
		return ResponseInfo.builder().code(HttpStatus.BAD_REQUEST.value() + "").message(exception.getMessage()).build();
	}

	@ExceptionHandler({ UnknownAccountException.class, IncorrectCredentialsException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseInfo loginException(Exception exception) {
		return ResponseInfo.builder().code(HttpStatus.UNAUTHORIZED.value() + "").message(exception.getMessage())
				.build();
	}

	@ExceptionHandler({ UnauthorizedException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseInfo forbidden(Exception exception) {
		return ResponseInfo.builder().code(HttpStatus.FORBIDDEN.value() + "").message(exception.getMessage()).build();
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
			UnsatisfiedServletRequestParameterException.class, MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseInfo badRequestException(Exception exception) {
		return ResponseInfo.builder().code(HttpStatus.BAD_REQUEST.value() + "").message(exception.getMessage()).build();
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseInfo exception(Throwable throwable) {
		log.error("系统异常", throwable);
		return ResponseInfo.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value() + "")
				.message(throwable.getMessage()).build();

	}

}
