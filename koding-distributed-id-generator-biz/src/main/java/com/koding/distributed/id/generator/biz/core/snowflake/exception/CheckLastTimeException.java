package com.koding.distributed.id.generator.biz.core.snowflake.exception;

/**
 * @author huangshunyuan
 */
public class CheckLastTimeException extends RuntimeException {
    public CheckLastTimeException(String msg){
        super(msg);
    }
}
