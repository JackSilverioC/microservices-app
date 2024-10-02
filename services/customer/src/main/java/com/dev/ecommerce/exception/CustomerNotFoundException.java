package com.dev.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true) //incluir los campos RuntimeExeption
@Data
public class CustomerNotFoundException extends RuntimeException {

    private final String msg;

}
