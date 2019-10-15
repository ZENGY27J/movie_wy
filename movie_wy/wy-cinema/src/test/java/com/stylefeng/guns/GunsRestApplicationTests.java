package com.stylefeng.guns;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.wuyan.cinema.CinemaService;
import com.wuyan.vo.CinemaGetFieldsVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableDubboConfiguration
@SpringBootTest(classes = GunsRestApplicationTests.class)
public class GunsRestApplicationTests {
	@Reference(interfaceClass = CinemaService.class)
	CinemaService cinemaService;

	@Test
	public void contextLoads() {
		CinemaGetFieldsVo fields = cinemaService.getFields(1);
	}

}
