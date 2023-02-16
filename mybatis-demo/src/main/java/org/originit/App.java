package org.originit;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.originit.mapper.DepartmentDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
@MapperScan
public class App 
{
    private static final int num = 20;

    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
        final DepartmentDao departmentDao = applicationContext.getBean(DepartmentDao.class);
        System.out.println(departmentDao.findAll());
    }
}
