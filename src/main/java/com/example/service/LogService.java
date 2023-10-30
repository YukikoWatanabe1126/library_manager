package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Log;
import com.example.repository.LogRepository;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Log create(Log log) {
        return this.logRepository.save(log);
    }
    
    public Log save(Log log) {
    	return this.logRepository.save(log);
    }
    
    public Log findByUserIdAndLibraryId(Integer userId, Integer libraryId){
    	Log log = this.logRepository.findByUserIdAndLibraryId(userId, libraryId);
    	
    	return log;
    }
    
    public List<Log> findByUserId(Integer userId) {
    	List<Log> logs = this.logRepository.findByUserId(userId);
    	return logs;
    }
}