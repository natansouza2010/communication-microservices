package br.com.productapims.config.exception;

import lombok.Data;

@Data
public class ExceptionDetails {
    private int status;
    private String message;

}
