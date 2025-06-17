package com.koding.distributed.id.generator.biz.core.snowflake.exception;

/**
 * @author huangshunyuan
 */
public class ClockGoBackException extends RuntimeException {
    public ClockGoBackException(String message) {
        super(message);
    }
}
