package speed.bite.idrec.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import speed.bite.idrec.pojos.models.EmailVerificationModel;

@Mapper
public interface EmailVerificationMapper {
    @Select("select * from email_verification where email=#{email} and deleted_at is null order by created_at desc limit 1")
    EmailVerificationModel getLatestByEmail(String email);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into email_verification(email, verification_code) " +
            "values(#{model.email}, #{model.verificationCode})"
    )
    void insert(@Param("model") EmailVerificationModel emailVerificationModel);
}
