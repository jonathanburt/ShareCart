package org.swe.cart.payload;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    private String name;
    private Integer groupId;
    private Instant createdAt;
    private List<GroupMemberDTO> members = new ArrayList<>();

    public void addMember(GroupMemberDTO newMember){
        members.add(newMember);
    }
}
