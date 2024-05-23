package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

/**
 * {@link UserRepository}のテストクラスです。
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private EntityManager entityManager;

	// usersテーブルを初期化する			
	private static final Operation TRUNCATE_USERS = Operations.sql("TRUNCATE TABLE users");

	// テストデータ（idは自動採番）			
	private static final Operation INSERT_USERS = Operations.insertInto("users")
			.columns("user_name", "address", "phone", "create_date", "update_date", "delete_date")
			// user_id = 1	
			.values("テスト太郎", "東京都", "0120-99-1234", "2023-01-01 00:10:01", "2023-01-02 09:20:02", null)
			// user_id = 2	
			.values("テスト次郎", "神奈川県", "090-1111-2222", "2023-02-01 12:30:03", "2023-02-02 21:40:04", null)
			.build();

	/**
	 * 各テスト毎に、テスト用データベースへの接続と、テストデータの初期化をする。
	 */
	@BeforeEach
	public void setUp() {
		// DB接続情報を取得		
		Destination destination = new DataSourceDestination(dataSource);
		// テストデータの初期化		
		new DbSetup(destination, Operations.sequenceOf(TRUNCATE_USERS, INSERT_USERS)).launch();
	}

	/**
	 * CRUDテスト<br>
	 * <br>
	 * Create: 新しいデータを登録する<br>
	 * Read : id=1のデータを取得する<br>
	 * Update: id=1のデータを更新する<br>
	 * Delete: id=1のデータを削除する<br>
	 * ※Springよって提供されているメソッドのためテストする必要は無いが、テストコード例として実装<br>
	 */
	@Test
	@DisplayName("CRUDテスト")
	public void testCRUD() {

		/**
		 * C: 新しいデータを登録する
		 */
		// テストデータ		
		User user = new User();
		user.setId(null);
		user.setName("テスト三郎");
		user.setAddress("埼玉県");
		user.setPhone("03-9999-0000");
		LocalDateTime now = LocalDateTime.now();
		user.setCreateDate(now);
		user.setUpdateDate(now);
		user.setDeleteDate(null);

		// テスト実行（データ登録）		
		User createdUser = userRepository.save(user);

		// 検証		
		// id=3として登録されているか		
		assertEquals(3, createdUser.getId());
		// データの総件数が3件になっているか		
		List<User> userList = userRepository.findAll();
		assertEquals(3, userList.size());

		/**
		 * R: id=1のデータを取得する
		 */
		// テスト実行（データ取得）		
		User selectUser1 = userRepository.findById(1L).orElse(null);

		// 検証		
		// id=1のデータを取得できているか		
		assertEquals(1, selectUser1.getId());
		assertEquals("テスト太郎", selectUser1.getName());
		assertEquals("東京都", selectUser1.getAddress());
		assertEquals("0120-99-1234", selectUser1.getPhone());
		LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 0, 10, 01);
		assertTrue(createDate.equals(selectUser1.getCreateDate()));
		LocalDateTime updateDate = LocalDateTime.of(2023, 1, 2, 9, 20, 02);
		assertTrue(updateDate.equals(selectUser1.getUpdateDate()));
		assertEquals(null, selectUser1.getDeleteDate());

		/**
		 * U: id=1のデータを更新する
		 */
		// テストデータ		
		User editUser = new User();
		editUser.setId(1L);
		editUser.setName("更新太郎");
		editUser.setAddress("千葉県");
		editUser.setPhone("0120-99-9999");
		editUser.setCreateDate(now);
		editUser.setUpdateDate(now);
		editUser.setDeleteDate(now);

		// テスト実行（データ更新）		
		User updatedUser = userRepository.save(editUser);

		// 検証		
		// データ更新されているか		
		assertEquals(1, updatedUser.getId());
		assertEquals("更新太郎", updatedUser.getName());
		assertEquals("千葉県", updatedUser.getAddress());
		assertEquals("0120-99-9999", updatedUser.getPhone());
		assertTrue(updatedUser.getCreateDate().isAfter(createDate));
		assertTrue(updatedUser.getUpdateDate().isAfter(updateDate));
		assertNotNull(updatedUser.getDeleteDate());

		/**
		 * D: id=1のデータを削除する
		 */
		// テスト実行（データ削除）		
		userRepository.deleteById(1L);

		// 検証		
		// データの総件数が3件になっているか		
		User deletedUser = userRepository.findById(1L).orElse(null);
		assertNull(deletedUser);
	}

	/**
	 * 悲観的ロックモードを取得できるかテストする。 <br>
	 * ロックをするためにトランザクションの開始が必要なので、Transactional を付与する<br>
	 * テスト実行後にロックが開放されるように、Rollback を付与する<br>
	 */
	@Test
	@Transactional
	@Rollback
	@DisplayName("悲観ロックの取得")
	public void testFindOneForUpdate() {

		// テスト実行		
		User findUser = userRepository.findOneForUpdate(1L);

		// 検証		
		// id=1のデータを取得できているか		
		assertEquals(1, findUser.getId());
		assertEquals("テスト太郎", findUser.getName());
		assertEquals("東京都", findUser.getAddress());
		assertEquals("0120-99-1234", findUser.getPhone());
		LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 0, 10, 1);
		assertTrue(createDate.equals(findUser.getCreateDate()));
		LocalDateTime updateDate = LocalDateTime.of(2023, 1, 2, 9, 20, 2);
		assertTrue(updateDate.equals(findUser.getUpdateDate()));
		assertEquals(null, findUser.getDeleteDate());
		// ロックモードを確認		
		LockModeType lockMode = entityManager.getLockMode(findUser);
		assertEquals(LockModeType.PESSIMISTIC_WRITE, lockMode);
	}

}
