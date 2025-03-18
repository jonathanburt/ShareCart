package org.swe.cart.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GroupMemberKey implements Serializable {
    
    @Column(name="user_id")
    private Integer userid;

   @Column(name="group_id")
    private Integer groupid;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        GroupMemberKey other = (GroupMemberKey) o;
        return Objects.equals(userid, other.userid) && Objects.equals(groupid, other.groupid);
    }

    @Override
    public int hashCode(){
        return Objects.hash(userid, groupid);
    }

}
