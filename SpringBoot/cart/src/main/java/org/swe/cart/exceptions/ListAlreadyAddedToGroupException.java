package org.swe.cart.exceptions;

public class ListAlreadyAddedToGroupException extends Exception {
    public ListAlreadyAddedToGroupException(String errMessage){
        super(errMessage);
    }
}
