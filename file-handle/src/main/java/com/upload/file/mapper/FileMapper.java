package com.upload.file.mapper;

import com.upload.file.bean.Upload;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper {

    @Select("select count(*) from user_info")
    Integer selectCountUserInfo();

    @Insert("insert into images_upload(title,\"content\",organ,img_url,img_path) values" +
            "(#{title},#{content},#{organ},#{img_url},#{img_path})")
    Integer saveImageUrl(Upload upload);
}
