package speed.bite.idrec.pojos.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import speed.bite.idrec.enums.LinkPrecedence;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ContactModel {
    private Integer id;
    private String email;
    private String phoneNumber;
    private Integer linkedId;
    private LinkPrecedence linkPrecedence;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
