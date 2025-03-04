package org.swe.cart.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ListItemKey implements Serializable {
    
    @Column(name="item_id")
    private Integer itemid;

    @Column(name="list_id")
    private Integer listid;

    @Column(name="user_id")
    private Integer userid;

    public ListItemKey(){}

    public ListItemKey(Integer itemid, Integer listid, Integer userid){
        this.itemid = itemid;
        this.listid = listid;
        this.userid = userid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getListid() {
        return listid;
    }

    public void setListid(Integer listid) {
        this.listid = listid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    
    @Override
    public boolean equals(Object k){
        if(this == k) return true;
        if(k == null || getClass() != k.getClass()) return false;
        ListItemKey other = (ListItemKey) k;
        return Objects.equals(itemid, other.itemid) && Objects.equals(listid, other.listid) && Objects.equals(userid, other.userid);
    }

    @Override
    public int hashCode(){
        return Objects.hash(itemid, listid, userid);
    }

}
