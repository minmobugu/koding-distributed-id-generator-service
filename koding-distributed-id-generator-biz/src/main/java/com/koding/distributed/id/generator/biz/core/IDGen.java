package com.koding.distributed.id.generator.biz.core;


import com.koding.distributed.id.generator.biz.core.common.Result;

/**
 * @author huangshunyuan
 */
public interface IDGen {
    Result get(String key);

    boolean init();
}
