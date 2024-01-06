package speed.bite.idrec.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import speed.bite.idrec.mappers.EmailVerificationMapper;
import speed.bite.idrec.pojos.models.EmailVerificationModel;
import speed.bite.idrec.responses.BasicResponse;

import java.util.Objects;

@RestController
@RequestMapping("/email-verify")
@Slf4j
public class EmailVerificationController {
    final String token = "LwI6wTj+5swliV0NXcwDpJfZ3hxNuzgn1ijE4pZ9kftr6FaWExuQg88AGGZ+zryk4G0rrqYmoz5smcQEohEA==";

    @Autowired
    private EmailVerificationMapper emailVerificationMapper;

    @GetMapping(path = {"receive", "receive/"})
    public ResponseEntity query(@RequestHeader("x-dns-secret") String secretKey, @RequestParam("email") String email) {
        if (!token.equals(secretKey)) {
            return BasicResponse.err(HttpStatus.UNAUTHORIZED, "Server denied the request.");
        }

        if (Objects.isNull(email) || email.isBlank()) {
            return BasicResponse.err(HttpStatus.BAD_REQUEST, "Request was inappropriate.");
        }

        email = email.trim();

        try {
            return BasicResponse.ok(emailVerificationMapper.getLatestByEmail(email));
        } catch (Exception e) {
            log.error("EmailVerificationInsert error ", e);
            return BasicResponse.err(HttpStatus.INTERNAL_SERVER_ERROR, "EmailVerification register error");
        }
    }

    @PostMapping(path = {"register", "register/"})
    public ResponseEntity register(@RequestHeader("x-dns-secret") String secretKey, @RequestBody EmailVerificationModel otpRequest) {
        if (!token.equals(secretKey)) {
            return BasicResponse.err(HttpStatus.UNAUTHORIZED, "Server denied the request.");
        }

        if (Objects.isNull(otpRequest) || Objects.isNull(otpRequest.getEmail()) || Objects.isNull(otpRequest.getVerificationCode())) {
            return BasicResponse.err(HttpStatus.BAD_REQUEST, "Request was inappropriate.");
        }

        otpRequest.setId(null);

        try {
            emailVerificationMapper.insert(otpRequest);
            return BasicResponse.ok("query registered");
        } catch (Exception e) {
            log.error("EmailVerificationInsert error ", e);
            return BasicResponse.err(HttpStatus.INTERNAL_SERVER_ERROR, "EmailVerification register error");
        }
    }
}
