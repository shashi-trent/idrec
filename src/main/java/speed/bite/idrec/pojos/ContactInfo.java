package speed.bite.idrec.pojos;

import lombok.Getter;
import lombok.Setter;
import speed.bite.idrec.pojos.models.ContactModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ContactInfo {
    private Integer primaryContactId;
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<Integer> secondaryContactIds;

    public ContactInfo(ContactModel contactModel) {
        this.primaryContactId = contactModel.getId();

        this.emails = new ArrayList<>();
        if(Objects.nonNull(contactModel.getEmail())) this.emails.add(contactModel.getEmail());

        this.phoneNumbers = new ArrayList<>();
        if(Objects.nonNull(contactModel.getPhoneNumber())) this.phoneNumbers.add(contactModel.getPhoneNumber());

        this.secondaryContactIds = new ArrayList<>();
    }
}
