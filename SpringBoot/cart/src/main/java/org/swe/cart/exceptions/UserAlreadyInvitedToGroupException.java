package org.swe.cart.exceptions;

public class UserAlreadyInvitedToGroupException extends Exception {
    public UserAlreadyInvitedToGroupException(String errMessage){
        super(errMessage);
    }
}
