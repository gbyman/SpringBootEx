package org.zero.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration // Test for Controller
@ContextConfiguration(classes = { org.zero.config.RootConfig.class, org.zero.config.ServletConfig.class })
@Log4j
public class BoardControllerTests {
	
	@Setter(onMethod_ = { @Autowired })
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;

}
