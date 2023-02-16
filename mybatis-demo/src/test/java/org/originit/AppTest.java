package org.originit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.originit.entity.Department;
import org.originit.entity.User;
import org.originit.mapper.DepartmentDao;
import org.originit.mapper.UserMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    SqlSessionFactory sqlSessionFactory;

    @Before
    public void setUp() throws Exception {
        InputStream xml = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(xml);
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Department> departmentList = sqlSession.selectList("departmentMapper.findAll");
        departmentList.forEach(System.out::println);
        sqlSession.close();
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void insertShouldSuccess() throws IOException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            final Department department = new Department();
            department.setId("1asdasdas");
            department.setName("xxc");
            department.setTel("17779911413");
            final int insert = sqlSession.insert("departmentMapper.insertOne", department);
            assertEquals(insert,1);
            sqlSession.commit();
        }

    }


    @Test
    public void testImpl() {
    }

    @Test
    public void testProxy() throws InterruptedException {
        final Thread t1 = new Thread(() -> {
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                final DepartmentDao mapper = sqlSession.getMapper(DepartmentDao.class);
                System.out.println(mapper.findAll());
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        final Thread t2 = new Thread(() -> {
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                final DepartmentDao mapper = sqlSession.getMapper(DepartmentDao.class);
                System.out.println(mapper.findAll());
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void testUserFind() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.findAll();
            assertEquals(userList.size(),2);
        }

    }

    @Test
    public void testUserFindLazy() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.findLazyAll();
            assertEquals(userList.size(),2);
        }
    }

    @Test
    public void testDepartmentFindLazy() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            DepartmentDao departmentDao = sqlSession.getMapper(DepartmentDao.class);
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<Department> userList = departmentDao.findLazyAll();
            assertEquals(5,userList.size());
            System.out.println(userList);
        }
    }

    @Test
    public void testReflector() throws InvocationTargetException, IllegalAccessException {
        Reflector reflector = new Reflector(Department.class);
        final Department department = new Department();
        reflector.getSetInvoker("id").invoke(department, new Object[]{"1"});
        System.out.println(department.getId());
    }
}
