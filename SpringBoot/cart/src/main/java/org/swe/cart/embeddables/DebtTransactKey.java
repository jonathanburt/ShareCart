package org.swe.cart.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DebtTransactKey implements Serializable {

    @Column(name="creditor_id")
    private Integer creditorid;

    @Column(name="debtor_id")
    private Integer debtorid;

    public DebtTransactKey() {}

    public DebtTransactKey(Integer creditorid, Integer debtorid){
        this.creditorid = creditorid;
        this.debtorid = debtorid;
    }

    public Integer getCreditorid() {
        return creditorid;
    }

    public void setCreditorid(Integer creditorid) {
        this.creditorid = creditorid;
    }

    public Integer getDebtorid() {
        return debtorid;
    }

    public void setDebtorid(Integer debtorid) {
        this.debtorid = debtorid;
    }

    @Override
    public boolean equals(Object k){
        if(this == k) return true;
        if(k == null || getClass() != k.getClass()) return false;
        DebtTransactKey other = (DebtTransactKey) k;
        return Objects.equals(creditorid, other.creditorid) && Objects.equals(debtorid, other.creditorid);
    }

    @Override
    public int hashCode(){
        return Objects.hash(creditorid, debtorid);
    }
}
