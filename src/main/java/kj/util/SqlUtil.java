package kj.util;

import kj.mapper.DocMapper;
import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.function.Consumer;


@Log
public class SqlUtil {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        } catch (IOException e) {

        }
    }


    private SqlUtil() {}

    private static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession(true);
    }

    public static void doSqlWork(Consumer<DocMapper> consumer) {
        try(SqlSession sqlSession = getSqlSession()) {
            DocMapper docMapper = sqlSession.getMapper(DocMapper.class);
            consumer.accept(docMapper);
        }
    }

}
