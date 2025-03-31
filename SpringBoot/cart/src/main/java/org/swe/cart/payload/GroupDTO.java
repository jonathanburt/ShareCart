package org.swe.cart.payload;

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
    private String createdAtFormatted;
    private List<GroupMemberDTO> members = new ArrayList<>();
    private List<GroupInviteDTO> invites = new ArrayList<>();

    public void addMember(GroupMemberDTO newMember){
        members.add(newMember);
    }

    public void addInvite(GroupInviteDTO newInvite){
        addInvite(newInvite);
    }
}
