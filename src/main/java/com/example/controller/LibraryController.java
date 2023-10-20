package com.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Library;
import com.example.entity.Log;
import com.example.service.LibraryService;
import com.example.service.LogService;
import com.example.service.LoginUser;

@Controller
@RequestMapping("library")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public String index(Model model) {
        List<Library> libraries = this.libraryService.findAll();
        model.addAttribute("libraries", libraries);
        return "library/index";
    }
    
    @GetMapping("/borrow")
    public String borrowingForm(Model model, @RequestParam("id") Integer id) {
    	Library library = libraryService.findById(id);
    	model.addAttribute("library", library);
    	return "/library/borrowingForm";
    }
    
    @PostMapping("/borrow")
    public String borrow(@RequestParam("id") Integer id, @RequestParam("return_due_date") String returnDueDate, @AuthenticationPrincipal LoginUser loginUser, LogService logService) {
    	Library library = this.libraryService.findById(id);
    	library.setUserId(loginUser.getUser().getId());
    	libraryService.upDate(library);
    	
    	Log log = new Log();
    	log.setId(id);
    	log.setUserId(loginUser.getUser().getId());
    	log.setRentDate(LocalDateTime.now());
    	log.setReturnDate(null);
    	DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
    	log.setRentDate(LocalDateTime.parse(returnDueDate +"00:00:00", fomatter));
    	log.setReturnDate(null);
		logService.create(log);
    	return "redirect:/library";
    }
}

