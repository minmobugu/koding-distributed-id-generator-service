package com.koding.distributed.id.generator.biz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author huangshunyuan
 */
@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR,reason="Key is none")
public class NoKeyException extends RuntimeException {
}
