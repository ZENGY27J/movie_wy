package com.stylefeng.guns;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.order.OrderServiceImpl;
import com.wuyan.order.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GunsRestApplicationTests.class)
public class GunsRestApplicationTests {


	@Test
	public void contextLoads() {

	}

}
