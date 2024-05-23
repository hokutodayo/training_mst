package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * {@link WebAppApplication}のテストクラスです。
 */
@SpringBootTest
class WebAppApplicationTests {

	/**
	 * アプリケーションの起動をテストします。
	 */
	@Test
	@DisplayName("Springの起動")
	void contextLoads() {
		// 起動できない場合はテストが失敗するので、実装不要
	}

}
