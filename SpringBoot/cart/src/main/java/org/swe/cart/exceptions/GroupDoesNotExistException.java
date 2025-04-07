package org.swe.cart.exceptions;

public class GroupDoesNotExistException extends Exception {
    public GroupDoesNotExistException(String errMessage){
        super(errMessage);
    }
}