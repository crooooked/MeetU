package com.example.meetu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
@ActiveProfiles("test")
@SpringBootTest(properties = "workerClassName=A")
class SpringbootJhApplicationTests {
	@Autowired
	DataSource dataSource;

	@Test
	void contextLoads() throws Exception {
		System.out.println("获取的数据库连接为:"+dataSource.getConnection());
	}


}

