package speed.bite.idrec.pojos.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailVerificationModel {
    private Integer id;
    private String email;
    private String verificationCode;
}
