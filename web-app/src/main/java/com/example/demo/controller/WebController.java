package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホームページに関するコントローラークラスです。
 */
@Controller
public class WebController {

	/**
	 * index画面を表示する。
	 *
	 * @param model ビューに渡すモデルオブジェクト
	 * @return index画面
	 */
	@GetMapping("/")
	public String index(Model model) {

		model.addAttribute("message", "ようこそ");
		model.addAttribute("datetime", LocalDateTime.now());

		return "index";
	}

	/**
	 * NullPointerExceptionを発生させる。
	 *
	 * @return 無し
	 */
	@GetMapping("/ex1")
	public String NullPointerException() {
		String value = Math.random() < 1 ? null : "a";
		System.out.println(value.toLowerCase());
		return "";
	}

	/**
	 * NumberFormatExceptionを発生させる。
	 *
	 * @return 無し
	 */
	@GetMapping("/ex2")
	public String NumberFormatException() {
		String value = "a";
		int num = Integer.parseInt(value);
		System.out.println(num);
		return "";
	}

	/**
	 * IndexOutOfBoundsExceptionを発生させる。
	 *
	 * @return 無し
	 */
	@GetMapping("/ex3")
	public String IndexOutOfBoundsException() {
		List<String> list = new ArrayList<>();
		list.get(0);
		return "";
	}
}
