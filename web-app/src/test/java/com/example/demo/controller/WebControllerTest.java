package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * {@link WebController}のテストクラスです。
 */
@WebMvcTest(WebController.class)
class WebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * index画面が表示されることをテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("index画面の表示")
	void testIndex() throws Exception {

		// テスト実行
		mockMvc.perform(get("/"))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("message", "ようこそ"))
				.andExpect(model().attribute("datetime", instanceOf(LocalDateTime.class)));
	}

}
