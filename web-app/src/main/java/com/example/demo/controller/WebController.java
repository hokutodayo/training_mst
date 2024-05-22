package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

	@GetMapping("/")
	public String index(Model model) {

		model.addAttribute("message", "ようこそ");
		model.addAttribute("datetime", LocalDateTime.now());

		return "index";
	}

	@GetMapping("/ex1")
	public String NullPointerException() {
		String value = Math.random() < 1 ? null : "a";
		System.out.println(value.toLowerCase());
		return "";
	}

	@GetMapping("/ex2")
	public String NumberFormatException() {
		String value = "a";
		int num = Integer.parseInt(value);
		System.out.println(num);
		return "";
	}

	@GetMapping("/ex3")
	public String IndexOutOfBoundsException() {
		List<String> list = new ArrayList<>();
		list.get(0);
		return "";
	}
}
