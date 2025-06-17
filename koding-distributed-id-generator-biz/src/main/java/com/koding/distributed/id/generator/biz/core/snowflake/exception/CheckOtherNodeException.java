package com.koding.distributed.id.generator.biz.core.snowflake.exception;

/**
 * @author huangshunyuan
 */
public class CheckOtherNodeException extends RuntimeException {
    public CheckOtherNodeException(String message) {
        super(message);
    }
}
