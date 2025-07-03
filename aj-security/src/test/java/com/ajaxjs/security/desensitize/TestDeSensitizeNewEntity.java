package com.ajaxjs.security.desensitize;


import com.ajaxjs.security.desensitize.entity.*;
import com.ajaxjs.util.ObjectHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体类对象脱敏单元测试
 */
public class TestDeSensitizeNewEntity {
    @Test
    public void simpleFieldTest() {
        People people = new People();
        people.setUsername("孙少平");
        people.setPassword("ssp");
        People p = DeSensitizeNewEntity.acquire(people);
        Assertions.assertEquals(p, people);
        Assertions.assertEquals(p.getUsername(), "--隐藏--");
    }

    @Test
    public void flexFieldTest() {
        People people = new People();
        people.setKey("email");
        people.setValue("1563919868@qq.com");
        People p = DeSensitizeNewEntity.acquire(people);
        Assertions.assertEquals(p, people);
        Assertions.assertEquals(p.getKey(), "email");
        Assertions.assertEquals(p.getValue(), "1***8@qq.com");

        people.setKey("phone");
        people.setValue("1563919868");
        p = DeSensitizeNewEntity.acquire(people);
        Assertions.assertEquals(p.getKey(), "phone");
        Assertions.assertEquals(people.getValue(), "15****9868");
    }

    @Test
    public void jsonNullFieldTest1() {
        People people = new People();
        people.setKey("email");
        people.setValue("1563919868@qq.com");
        people.setStr("测试null");
        People s = DeSensitizeNewEntity.acquire(people);
        Assertions.assertNull(s.getStr());
        Assertions.assertEquals(s.getAge(), 0);
        Assertions.assertEquals(s.getB(), (byte) 0);
        Assertions.assertEquals(s.getS(), (short) 0);
        Assertions.assertEquals(s.getL(), 0L);
    }

    @Test
    public void testFieldMap() {
        PeopleMap peopleMap = new PeopleMap();
        peopleMap.getParams().put("password", "12345");
        peopleMap.getParams().put("username", "田晓霞");
        peopleMap.getParams().put("phone", "15645562587");
        PeopleMap p = DeSensitizeNewEntity.acquire(peopleMap);
        System.out.println(p);
    }

    @Test
    public void test11() {
        PubResponse response = new PubResponse();
        response.password = "32433";
        response.username = "条消息";
        response.email = "1393619859@qq.com";
        response.idCard = "321455188625645686";
        response.bankCard = "325648956125656666";
        response.phone = "18254452658";
        response.mobile = "1234567";
        PubResponse.Job job = new PubResponse.Job();
        job.email = "1393619859@qq.com";
        job.work = "呵呵哈哈哈";
        response.job = job;
        response.jobs = new PubResponse.Job[]{job};
        response.jobList = Arrays.asList(job);
        BaseResponse<PubResponse> r = BaseResponse.<PubResponse>newBuilder().withData(response).build();
        BaseResponse<PubResponse> response1 = DeSensitizeNewEntity.acquire(r);
        Assertions.assertEquals(response1.getData().email, "1393619859@qq.com");
        BaseResponse<PubResponse> response2 = DeSensitizeNewEntity.acquire(r, BaseResponse.class);
        Assertions.assertEquals(response2.getData().email, "1***9@qq.com");
    }

    @Test
    public void wrapperClass() {
        Company company = new Company();
        company.setId(123L);
        company.setName("尤五");
        company.setAddress("湖州");
        Company.Worker worker = new Company.Worker();
        worker.setId(456L);
        worker.setName("甥王爷");
        company.setWorker(worker);
        Company company1 = DeSensitizeNewEntity.acquire(company, Company.class);
        Assertions.assertEquals(company1.getWorker().getName(), "--隐藏--");
    }

    @Test
    public void wrapperClassList() {
        Company company = new Company();
        company.setId(123L);
        company.setName("尤五");
        company.setAddress("湖州");
        Company.Worker worker = new Company.Worker();
        worker.setId(456L);
        worker.setName("甥王爷");
        company.setList(Arrays.asList(worker));
        Company company1 = DeSensitizeNewEntity.acquire(company, Company.class);
        Assertions.assertEquals(company1.getList().get(0).getName(), "--隐藏--");
    }

    @Test
    public void wrapperClassMap() {
        Company company = new Company();
        company.setId(123L);
        company.setName("尤五");
        company.setAddress("湖州");
        Company.Worker worker = new Company.Worker();
        worker.setId(456L);
        worker.setName("甥王爷");
        company.setDataMap(new HashMap<>(ObjectHelper.mapOf("test", worker)));
        Company company1 = DeSensitizeNewEntity.acquire(company, Company.class);
        Assertions.assertEquals(company1.getDataMap().get("test").getName(), "--隐藏--");
    }
}
