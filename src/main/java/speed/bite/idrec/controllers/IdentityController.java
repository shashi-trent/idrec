package speed.bite.idrec.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import speed.bite.idrec.pojos.IdentityRequest;
import speed.bite.idrec.pojos.IdentityResponse;
import speed.bite.idrec.services.ContactService;

import java.util.Objects;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/")
    public ResponseEntity<IdentityResponse> reconcileIdentity(@RequestBody IdentityRequest identityRequest) {
        if(Objects.isNull(identityRequest) || (!StringUtils.hasText(identityRequest.getEmail())
            && !StringUtils.hasText(identityRequest.getPhoneNumber()))) {
            throw new IllegalArgumentException("Request Body must contain email and/or phoneNumber");
        }


        try {
            return ResponseEntity.ok(contactService.getReconciledContactResponse(identityRequest.getEmail(),
                    identityRequest.getPhoneNumber()));
        } catch (Exception e) {
            throw new InternalError("Identity Reconcile error");
        }
    }

}
