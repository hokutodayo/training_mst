package com.example.demo.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link User}のテストクラスです。
 */
class UserTest {

	/**
	 * 値のセットと取得ができることをテストする。
	 */
	@Test
	@DisplayName("各項目のsetter/getterの確認")
	void test() {
		// テスト値
		Long id = 1L;
		String name = "テスト太郎";
		String address = "東京都新宿区1-2-3";
		String phone = "0120-1234-1234";
		LocalDateTime updateDate = LocalDateTime.now();
		LocalDateTime createDate = LocalDateTime.now();
		LocalDateTime deleteDate = LocalDateTime.now();

		// 値のセット
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setAddress(address);
		user.setPhone(phone);
		user.setUpdateDate(updateDate);
		user.setCreateDate(createDate);
		user.setDeleteDate(deleteDate);

		// 値の検証
		assertEquals(id, user.getId());
		assertEquals(name, user.getName());
		assertEquals(address, user.getAddress());
		assertEquals(phone, user.getPhone());
		assertEquals(updateDate, user.getUpdateDate());
		assertEquals(createDate, user.getCreateDate());
		assertEquals(deleteDate, user.getDeleteDate());
	}

}
