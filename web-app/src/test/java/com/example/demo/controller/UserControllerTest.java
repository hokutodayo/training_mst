package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.persistence.OptimisticLockException;

/**
 * {@link UserController}のテストクラスです。
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	/**
	 * ユーザー一覧画面が表示されることをテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー一覧画面の表示")
	void testDisplayList() throws Exception {

		// テストデータ
		List<User> userList = new ArrayList<>();
		userList.add(new User());
		userList.add(new User());

		// モック設定
		when(userService.searchAll()).thenReturn(userList);

		// テスト実行
		mockMvc.perform(get("/user/list"))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("user/list"))
				.andExpect(model().attribute("userList", userList));

		// 検証
		verify(userService, atLeastOnce()).searchAll();
	}

	/**
	 * ユーザー詳細画面が表示されることをテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー詳細画面の表示")
	void testDisplayDetail() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);

		// モック設定
		when(userService.search(user.getId())).thenReturn(user);

		// テスト実行
		mockMvc.perform(get("/user/{id}", user.getId()))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("user/detail"))
				.andExpect(model().attribute("user", user));

		// 検証
		verify(userService, atLeastOnce()).search(user.getId());
	}

	/**
	 * ユーザー登録画面が表示されることをテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー登録画面の表示")
	void testDisplayAdd() throws Exception {

		// テスト実行
		mockMvc.perform(get("/user/add"))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("user/add"));

		// 検証
		verifyNoInteractions(userService);

	}

	/**
	 * ユーザー登録で正常系をテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー新規登録：正常系")
	void testCreateUser_Success() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");

		// モック設定
		when(userService.createUser(user)).thenReturn(user);

		// テスト実行
		mockMvc.perform(post("/user/create")
				// 引数の設定
				.flashAttr("user", user))
				// 検証
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/list"));

		// 検証
		verify(userService, atLeastOnce()).createUser(user);
	}

	/**
	 * ユーザー登録でバリデーションエラー発生をテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー新規登録：異常系（バリデーションエラー）")
	void testCreateUser_ValidationError() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName(null);

		// テスト実行
		mockMvc.perform(post("/user/create")
				// 引数の設定
				.flashAttr("user", user))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("user/add"));

		// 検証
		verifyNoInteractions(userService);
	}

	/**
	 * 指定したユーザーの削除をテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー削除")
	void testDeleteUser() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");

		// モック設定
		doNothing().when(userService).deleteUser(user.getId());

		// テスト実行
		mockMvc.perform(get("/user/{id}/delete", user.getId()))
				// 検証
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/list"));

		// 検証
		verify(userService, atLeastOnce()).deleteUser(user.getId());
	}

	/**
	 * ユーザー編集画面が表示されることをテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー編集画面の表示")
	void testDisplayEdit() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");

		// モック設定
		when(userService.search(user.getId())).thenReturn(user);

		// テスト実行
		mockMvc.perform(get("/user/{id}/edit", user.getId()))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("user/edit"))
				.andExpect(model().attribute("user", user));

		// 検証
		verify(userService, atLeastOnce()).search(user.getId());
	}

	/**
	 * ユーザー編集で正常系をテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー編集：正常系")
	void testUpdateUser_Success() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");

		// モック設定
		when(userService.updateUser(user)).thenReturn(user);

		// テスト実行
		mockMvc.perform(post("/user/update")
				// 引数の設定
				.flashAttr("user", user))
				// 検証
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/user/list"));

		// 検証
		verify(userService, atLeastOnce()).updateUser(user);
	}

	/**
	 * ユーザー編集でバリデーションエラー発生をテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー編集：異常系（バリデーションエラー）")
	void testUpdateUser_ValidationError() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName(null);

		// テスト実行
		mockMvc.perform(post("/user/update")
				// 引数の設定
				.flashAttr("user", user))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("user/edit"));

		// 検証
		verifyNoInteractions(userService);
	}

	/**
	 * ユーザー編集でOptimisticLockException発生をテストする。
	 *
	 * @throws Exception
	 */
	@Test
	@DisplayName("ユーザー編集：異常系（OptimisticLockException）")
	void testUpdateUser_OptimisticLockException() throws Exception {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");

		// モック設定
		// updateUserが呼び出されたときOptimisticLockExceptionを発生させるための設定
		String message = "test message";
		doThrow(new OptimisticLockException(message)).when(userService).updateUser(user);

		// テスト実行
		mockMvc.perform(post("/user/update")
				// 引数の設定
				.flashAttr("user", user))
				// 検証
				.andExpect(status().isOk())
				.andExpect(view().name("user/edit"))
				.andExpect(model().attribute("message", message));

		// 検証
		verify(userService, atLeastOnce()).updateUser(user);
	}

}
