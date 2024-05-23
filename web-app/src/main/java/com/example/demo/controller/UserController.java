package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.persistence.OptimisticLockException;

/**
 * ユーザーに関する画面の制御を行うコントローラークラスです。
 */
@Controller
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * ユーザーの一覧画面を表示する。
	 *
	 * @param model モデル
	 * @return ユーザー一覧画面
	 */
	@GetMapping("/user/list")
	public String displayList(Model model) {

		List<User> userList = userService.searchAll();
		model.addAttribute("userList", userList);

		return "user/list";
	}

	/**
	 * ユーザーの詳細画面を表示する。
	 *
	 * @param id ユーザーID
	 * @param model モデル
	 * @return ユーザー詳細画面
	 */
	@GetMapping("/user/{id}")
	public String displayDetail(@PathVariable Long id, Model model) {

		User user = userService.search(id);
		model.addAttribute("user", user);

		return "user/detail";
	}

	/**
	 * ユーザーの新規登録画面を表示する。
	 *
	 * @param model モデル
	 * @return ユーザー登録画面
	 */
	@GetMapping("/user/add")
	public String displayAdd(Model model) {

		User user = new User();
		model.addAttribute("user", user);

		return "user/add";
	}

	/**
	 * ユーザーを新規登録する。
	 * <p>
	 * 入力エラーがある場合は、もとの入力画面にエラー内容を表示する。
	 * </p>
	 *
	 * @param user ユーザー情報
	 * @param result 入力チェック結果
	 * @param model モデル
	 * @return ユーザー一覧画面にリダイレクトする
	 */
	@PostMapping("/user/create")
	public String createUser(@Validated User user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "user/add";
		}

		userService.createUser(user);

		return "redirect:/user/list";
	}

	/**
	 * 指定したユーザーを削除する。
	 *
	 * @param id ユーザーID
	 * @param model モデル
	 * @return ユーザー一覧画面にリダイレクトする
	 */
	@GetMapping("/user/{id}/delete")
	public String deleteUser(@PathVariable Long id, Model model) {

		userService.deleteUser(id);

		return "redirect:/user/list";
	}

	/**
	 * ユーザーの編集画面を表示する。
	 *
	 * @param id ユーザーID
	 * @param model モデル
	 * @return ユーザー編集画面
	 */
	@GetMapping("/user/{id}/edit")
	public String displayEdit(@PathVariable Long id, Model model) {

		User user = userService.search(id);
		model.addAttribute("user", user);

		return "user/edit";
	}

	/**
	 * 指定したユーザーの情報を更新する。
	 * <p>
	 * 入力エラー、または、排他制御エラーがある場合は、もとの入力画面にエラー内容を表示する。
	 * </p>
	 *
	 * @param user ユーザー情報
	 * @param result 入力値のチェック結果
	 * @param model モデル
	 * @return 一覧画面にリダイレクトする
	 */
	@PostMapping("/user/update")
	public String updateUser(@Validated User user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "user/edit";
		}

		try {
			userService.updateUser(user);
			return "redirect:/user/list";

		} catch (OptimisticLockException e) {
			model.addAttribute("message", e.getMessage());
			return "user/edit";
		}
	}
}
