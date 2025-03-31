package org.swe.cart.exceptions;

public class UserAlreadyInGroupException extends Exception {
    public UserAlreadyInGroupException(String errorMessage){
        super(errorMessage);
    }
}
