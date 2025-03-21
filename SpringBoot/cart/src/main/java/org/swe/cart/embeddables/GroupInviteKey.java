package org.swe.cart.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupInviteKey implements Serializable{
    private Integer userid;
    private Integer groupid;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        GroupInviteKey other = (GroupInviteKey) o;
        return Objects.equals(userid, other.userid) && Objects.equals(groupid, other.groupid);
    }

    @Override
    public int hashCode(){
        return Objects.hash(userid, groupid);
    }
}
