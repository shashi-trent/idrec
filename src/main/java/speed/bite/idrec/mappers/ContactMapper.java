package speed.bite.idrec.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import speed.bite.idrec.enums.LinkPrecedence;
import speed.bite.idrec.pojos.models.ContactModel;

import java.util.List;

@Mapper
public interface ContactMapper {

    @Select("select * from contact where id=#{id}")
    ContactModel getById(int id);

    @Select("select id from contact where deleted_at is null and linked_id is null and email=#{email} limit 1")
    Integer getIdByPrimaryEmail(String email);

    @Select("select id from contact where deleted_at is null and linked_id is null and phone_number=#{phoneNumber} limit 1")
    Integer getIdByPrimaryPhone(String phoneNumber);

    @Select("select distinct linked_id from contact where deleted_at is null and " +
            "linked_id is not null and email=#{email} limit 1")
    Integer getLinkedIdByEmail(String email);

    @Select("select distinct linked_id from contact where deleted_at is null and " +
            "linked_id is not null and phone_number=#{phoneNumber} limit 1")
    Integer getLinkedIdByPhone(String phoneNumber);


    @Select("select * from contact where deleted_at is null and linked_id=#{id}")
    List<ContactModel> getSecondaryContacts(int id);


    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into contact(email, phone_number, linked_id, link_precedence) " +
            "values(#{model.email}, #{model.phoneNumber}, #{model.linkedId}, #{model.linkPrecedence})"
    )
    void insert(@Param("model") ContactModel contactModel);

    @Update("update contact set linked_id=#{linkedId}, link_precedence=#{linkPrecedence} " +
                "where id=#{id}")
    void updateSecondaryLinking(@Param("id") int id, @Param("linkedId") int linkedId,
                          @Param("linkPrecedence")LinkPrecedence linkPrecedence);

    @Update("update contact set linked_id=#{newLinkedId} where deleted_at is null and linked_id=#{prvLinkedId}")
    void updateLinkedId(@Param("prvLinkedId") int prvLinkedId, @Param("newLinkedId") int newLinkedId);
}
