package com.github.mars05.crud.intellij.plugin.exception;

/**
 * BizException
 *
 * @author yu.xiao
 */
public class BizException extends RuntimeException {

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

}

