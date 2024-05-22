package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

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

		model.addAttribute("id", id);
		model.addAttribute("name", "サンプル太郎");
		model.addAttribute("address", "東京都新宿区1-2-3");
		model.addAttribute("phone", "090-1234-1234");
		model.addAttribute("updateDate", LocalDateTime.now());
		model.addAttribute("createDate", LocalDateTime.now());
		model.addAttribute("deleteDate", null);

		return "user/detail";
	}

}
