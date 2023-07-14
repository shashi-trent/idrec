package speed.bite.idrec.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import speed.bite.idrec.enums.LinkPrecedence;
import speed.bite.idrec.mappers.ContactMapper;
import speed.bite.idrec.pojos.ContactInfo;
import speed.bite.idrec.pojos.models.ContactModel;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ContactService {

    @Autowired
    private ContactMapper contactMapper;

    public ContactInfo getReconciledContactResponse(String email, String phoneNumber) {
        ContactModel reconciledContact = reconcileIdentity(email, phoneNumber);

        ContactInfo contactInfo = new ContactInfo(reconciledContact);
        Set<String> emailsSeen = new HashSet<>(){{
            add(null);
            add(reconciledContact.getEmail());
        }};
        Set<String> phonesSeen = new HashSet<>(){{
            add(null);
            add(reconciledContact.getPhoneNumber());
        }};

        List<ContactModel> contactModels = contactMapper.getSecondaryContacts(reconciledContact.getId());
        contactModels.forEach(contactModel -> {
            if(!emailsSeen.contains(contactModel.getEmail())) {
                contactInfo.getEmails().add(contactModel.getEmail());
                emailsSeen.add(contactModel.getEmail());
            }
            if(!phonesSeen.contains(contactModel.getPhoneNumber())) {
                contactInfo.getPhoneNumbers().add(contactModel.getPhoneNumber());
                phonesSeen.add(contactModel.getPhoneNumber());
            }
            contactInfo.getSecondaryContactIds().add(contactModel.getId());
        });

        return contactInfo;
    }


    @Transactional
    private ContactModel reconcileIdentity(String email, String phoneNumber) {
        ContactModel emailContact = getEmailPrimaryContact(email);
        boolean emailNotExists = StringUtils.hasText(email) && Objects.isNull(emailContact);

        ContactModel phoneContact = getPhonePrimaryContact(phoneNumber);
        boolean phoneNotExists = StringUtils.hasText(phoneNumber) && Objects.isNull(phoneContact);

        ContactModel primaryContact = Optional.ofNullable(emailContact).orElse(phoneContact);

        if(emailNotExists || phoneNotExists) {
            ContactModel secondaryContact = createAndSaveNewContact(email, phoneNumber,
                    Objects.isNull(primaryContact) ? null : primaryContact.getId());

            if(Objects.isNull(primaryContact)) {
                primaryContact = secondaryContact;
            }
        } else {
            if(Objects.nonNull(emailContact) && Objects.nonNull(phoneContact)
                    && !emailContact.getId().equals(phoneContact.getId())) {
                primaryContact = emailContact.getCreatedAt().compareTo(phoneContact.getCreatedAt()) < 0
                        ? emailContact : phoneContact;
                int secondaryContactId = emailContact.getId().equals(primaryContact.getId())
                        ? phoneContact.getId() : emailContact.getId();

                contactMapper.updateLinkedId(secondaryContactId, primaryContact.getId());
                contactMapper.updateSecondaryLinking(secondaryContactId, primaryContact.getId(), LinkPrecedence.Secondary);
            }
        }

        return primaryContact;
    }

    private ContactModel getEmailPrimaryContact(String email) {
        if(!StringUtils.hasText(email)) return null;
        Integer primaryIdByEmail = Optional.ofNullable(contactMapper.getIdByPrimaryEmail(email))
                .orElse(contactMapper.getLinkedIdByEmail(email));
        return Objects.nonNull(primaryIdByEmail) ? contactMapper.getById(primaryIdByEmail) : null;
    }

    private ContactModel getPhonePrimaryContact(String phoneNumber) {
        if(!StringUtils.hasText(phoneNumber)) return null;
        Integer primaryIdByPhone = Optional.ofNullable(contactMapper.getIdByPrimaryPhone(phoneNumber))
                .orElse(contactMapper.getLinkedIdByPhone(phoneNumber));
        return Objects.nonNull(primaryIdByPhone) ? contactMapper.getById(primaryIdByPhone) : null;
    }

    private ContactModel createAndSaveNewContact(String email, String phoneNumber, Integer primaryId) {
        ContactModel contactModel = ContactModel.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .linkedId(primaryId)
                .linkPrecedence(Objects.isNull(primaryId) ? LinkPrecedence.Primary : LinkPrecedence.Secondary)
                .build();

        contactMapper.insert(contactModel);

        return contactModel;
    }
}
