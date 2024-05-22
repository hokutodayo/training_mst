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

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/user/list")
	public String displayList(Model model) {

		List<User> userList = userService.searchAll();
		model.addAttribute("userList", userList);

		return "user/list";
	}

	@GetMapping("/user/{id}")
	public String displayDetail(@PathVariable Long id, Model model) {

		User user = userService.search(id);
		model.addAttribute("user", user);

		return "user/detail";
	}

	@GetMapping("/user/add")
	public String displayAdd(Model model) {

		User user = new User();
		model.addAttribute("user", user);

		return "user/add";
	}

	@PostMapping("/user/create")
	public String createUser(@Validated User user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "user/add";
		}

		userService.createUser(user);

		return "redirect:/user/list";
	}

	@GetMapping("/user/{id}/delete")
	public String deleteUser(@PathVariable Long id, Model model) {

		userService.deleteUser(id);

		return "redirect:/user/list";
	}

	@GetMapping("/user/{id}/edit")
	public String displayEdit(@PathVariable Long id, Model model) {

		User user = userService.search(id);
		model.addAttribute("user", user);

		return "user/edit";
	}

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
