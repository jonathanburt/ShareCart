package org.swe.cart.exceptions;

public class GroupMismatchException extends Exception {
    public GroupMismatchException(String errMessage){
        super(errMessage);
    }
}
