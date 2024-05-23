package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.OptimisticLockException;

/**
 * {@link UserService}のテストクラスです。
 */
@SpringBootTest
class UserServiceTest {

	@Autowired
	UserService userService;

	@MockBean
	private UserRepository userRepository;

	/**
	 * 全てのユーザー情報が取得されることをテストする。
	 */
	@Test
	@DisplayName("全ユーザー情報の取得")
	void testSearchAll() {

		// テストデータ
		List<User> userList = new ArrayList<>();
		userList.add(new User());
		userList.add(new User());

		// モック設定
		Mockito.when(userRepository.findAll()).thenReturn(userList);

		// テスト実行
		List<User> actual = userService.searchAll();

		// 検証
		Mockito.verify(userRepository, Mockito.atLeastOnce()).findAll();
		Assertions.assertEquals(userList, actual);
	}

	/**
	 * 指定されたユーザー情報が取得されることをテストする。
	 */
	@Test
	@DisplayName("指定ユーザー情報の取得")
	void testSearch() {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");

		// モック設定
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

		// テスト実行
		User actual = userService.search(user.getId());

		// 検証
		Mockito.verify(userRepository, Mockito.atLeastOnce()).findById(user.getId());
		Assertions.assertEquals(user, actual);
	}

	/**
	 * 指定されたユーザー情報が登録されることをテストする。
	 */
	@Test
	@DisplayName("指定ユーザー情報の登録")
	void testCreateUser() {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");
		user.setCreateDate(null);
		user.setUpdateDate(null);
		user.setDeleteDate(null);

		// モック設定
		Mockito.when(userRepository.save(user)).thenReturn(user);

		// テスト実行
		User actual = userService.createUser(user);

		// 検証
		Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
		Assertions.assertEquals(user, actual);
		// 作成日時と更新日時には、日時がセットされているか
		Assertions.assertNotNull(actual.getCreateDate());
		Assertions.assertNotNull(actual.getUpdateDate());
		// 削除日時はnullのままか
		Assertions.assertNull(actual.getDeleteDate());
	}

	/**
	 * 指定されたユーザー情報が削除されることをテストする。
	 */
	@Test
	@DisplayName("指定ユーザー情報の削除")
	void testDeleteUser() {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");

		// モック設定
		Mockito.doNothing().when(userRepository).deleteById(user.getId());

		// テスト実行
		userService.deleteUser(user.getId());

		// 検証
		Mockito.verify(userRepository, Mockito.atLeastOnce()).deleteById(user.getId());
	}

	/**
	 * ユーザー更新で正常系をテストする。
	 *
	 * @throws ParseException
	 */
	@Test
	@DisplayName("ユーザー更新：正常系")
	void testUpdateUser_Success() {

		// テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");
		LocalDateTime base = LocalDateTime.of(2021, 1, 1, 12, 0, 0);
		user.setCreateDate(base);
		user.setUpdateDate(base);
		user.setDeleteDate(base);

		// モック設定
		Mockito.when(userRepository.findOneForUpdate(user.getId())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);

		// テスト実行
		User actual = userService.updateUser(user);

		// 検証
		Mockito.verify(userRepository, Mockito.atLeastOnce()).findOneForUpdate(user.getId());
		Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
		Assertions.assertEquals(user, actual);
		// 作成日時は変化なし
		Assertions.assertEquals(base, actual.getCreateDate());
		// 更新日時が、更新されているか
		Assertions.assertTrue(actual.getUpdateDate().isAfter(base));
		// 削除日時は変化なし
		Assertions.assertEquals(base, actual.getDeleteDate());
	}

	/**
	 * ユーザー更新でOptimisticLockException発生をテストする。
	 *
	 * @throws ParseException
	 */
	@Test
	@DisplayName("ユーザー更新：異常系（OptimisticLockException）")
	void testUpdateUser_OptimisticLockException() {

		// テストデータ
		// User
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");
		LocalDateTime base = LocalDateTime.of(2021, 1, 1, 12, 0, 0);
		user.setUpdateDate(base);

		// Updated User
		User updatedUser = new User();
		updatedUser.setId(1L);
		updatedUser.setName("テスト太郎");
		updatedUser.setUpdateDate(LocalDateTime.now());

		// モック設定
		Mockito.when(userRepository.findOneForUpdate(user.getId())).thenReturn(updatedUser);
		Mockito.when(userRepository.save(user)).thenReturn(updatedUser);

		// テスト実行
		Throwable e = Assertions.assertThrows(OptimisticLockException.class, () -> userService.updateUser(user));

		// 検証
		Mockito.verify(userRepository, Mockito.atLeastOnce()).findOneForUpdate(user.getId());
		// saveメソッドが呼ばれていないことを確認
		Mockito.verify(userRepository, Mockito.never()).save(user);
		// 例外にセットされたメッセージの確認
		Assertions.assertEquals("データが他の方によって更新されたようです。一覧画面に戻ってから再実施してください。", e.getMessage());
	}

}
