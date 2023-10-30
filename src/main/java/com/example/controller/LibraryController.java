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
    private final LogService logService;

    @Autowired
    public LibraryController(LibraryService libraryService, LogService logService) {
        this.libraryService = libraryService;
        this.logService = logService;
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
    public String borrow(@RequestParam("id") Integer id, @RequestParam("return_due_date") String returnDueDate, @AuthenticationPrincipal LoginUser loginUser) {
    	Library library = this.libraryService.findById(id);
    	
    	if ( library.getUserId() == 0 ) {
    		library.setUserId(loginUser.getUser().getId());
    		libraryService.update(library);
    		
	    	Log log = new Log();
	    	log.setLibraryId(id);
	    	log.setUserId(loginUser.getUser().getId());
	    	log.setRentDate(LocalDateTime.now());
	    	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    	log.setReturnDueDate(LocalDateTime.parse(returnDueDate + " 00:00:00", format));
	    	log.setReturnDate(null);
	    	this.logService.create(log);
    	}
    	
    	return "redirect:/library";
    }
    
    /**
     * 返却ボタンを押した際に書籍の返却処理が行われるようにする処理
     * @param Integer id, 
     * @param LoginUser loginUser
     * @return リダイレクト先URL
     */
    @PostMapping("/return")
    public String returnBook(@RequestParam("id") Integer id, @AuthenticationPrincipal LoginUser loginUser) {
    	// リクエストパラメータで受け取った書籍IDを利用し Library モデルから該当の書籍情報を1件取得し代入する。
    	Library library = this.libraryService.findById(id);
    	
    	// 取得した書籍情報の USER_ID に0をセットし、LIBRARIES テーブルの情報を更新する。
    	library.setUserId(0);
    	libraryService.update(library);
    	
    	// Logs モデルを利用して、UPDATE処理を行い、ログの更新を行う。
    	// - LIBRARY_ID と USER_ID で対象を検索し、RENT_DATEが最新のレコードを取得する。
    	Log log = this.logService.findByUserIdAndLibraryId(loginUser.getUser().getId(), id);
    	
    	// - RETURN_DATE に現在の日付を登録する。
    	log.setReturnDate(LocalDateTime.now());
    	
    	// - save メソッドを利用して更新する。
    	this.logService.save(log);
    	
    	// redirect の機能を利用して http://localhost:8080/library にリダイレクトを行う。
    	return "redirect:/library";
    	
    	
    }
    
    /**
     * ユーザーに紐づいた貸し出しの履歴を取得、表示しなさい。
     * Model model
     * @AuthenticationPrincipal LoginUser loginUser
     */
    
    @GetMapping("/history")
    public String history(Model model, @AuthenticationPrincipal LoginUser loginUser) {
    	//ログインしているユーザーのIDを利用して Log モデルからこれまでの貸し出し履歴を取得する。
    	
    	List<Log> logs = this.logService.findByUserId(loginUser.getUser().getId());
    	//取得した履歴情報をModelクラスのaddAttributeメソッドを利用して borrowHistory.html に渡し画面を表示させる。

    	model.addAttribute("logs",logs);
    	
    	return "/library/borrowHistory";

    }
    
}

