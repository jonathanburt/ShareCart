package org.swe.cart.exceptions;

public class UserNotInvitedToGroupException extends Exception {
    public UserNotInvitedToGroupException(String errMessage){
        super(errMessage);
    }
}
