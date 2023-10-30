package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.entity.User;
import com.example.service.LoginUser;

@Controller
public class LoginController {

	@GetMapping("/loginForm")
	public String LoginForm() {
		return "/loginForm";
	}

	@GetMapping("home")
	public String getHome(Model model, @AuthenticationPrincipal LoginUser loginUser) {
		// loginUserからユーザー情報を取得してModelに追加する
		User user = loginUser.getUser();
		model.addAttribute("user", user);
		return "library/index";
	}

	
}