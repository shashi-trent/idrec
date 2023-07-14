package speed.bite.idrec.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import speed.bite.idrec.pojos.IdentityRequest;
import speed.bite.idrec.responses.BasicResponse;
import speed.bite.idrec.services.ContactService;

import java.util.Objects;

@RestController
@RequestMapping("/")
@Slf4j
public class IdentityController {

    @Autowired
    private ContactService contactService;

    @PostMapping(path = {"identify", "identify/"})
    public ResponseEntity reconcileIdentity(@RequestBody IdentityRequest identityRequest) {
        if(Objects.isNull(identityRequest)) {
            return BasicResponse.err(HttpStatus.BAD_REQUEST, "Request Body must not be null");
        }

        identityRequest.setEmail(StringUtils.hasText(identityRequest.getEmail())
                ? identityRequest.getEmail().trim() : null);
        identityRequest.setPhoneNumber(StringUtils.hasText(identityRequest.getPhoneNumber())
                ? identityRequest.getPhoneNumber().trim() : null);

        if(Objects.isNull(identityRequest.getEmail()) && Objects.isNull(identityRequest.getPhoneNumber())) {
            return BasicResponse.err(HttpStatus.BAD_REQUEST, "Request Body must contain email and/or phoneNumber");
        }


        try {
            return BasicResponse.ok(contactService.getReconciledContactResponse(identityRequest.getEmail(),
                    identityRequest.getPhoneNumber()));
        } catch (Exception e) {
            log.error("Identity Reconcile error ", e);
            return BasicResponse.err(HttpStatus.INTERNAL_SERVER_ERROR, "Identity Reconcile error");
        }
    }

}
