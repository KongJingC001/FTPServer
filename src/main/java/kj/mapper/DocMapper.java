package kj.mapper;

import kj.entity.Doc;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DocMapper {


    @Insert("insert into document(absolute_path, created_time) values(#{absolutePath}, #{createdTime})")
    int addDoc(Doc doc);

    @Select("select * from document where absolute_path = #{absolutePath}")
    Doc getDocByAbsolutePath(String absolutePath);

    @Select("select * from document")
    List<Doc> getDocs();

}
