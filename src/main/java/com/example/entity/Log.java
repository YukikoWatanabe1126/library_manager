package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LOGS")
public class Log {

	@Id
    @SequenceGenerator(name = "LOG_ID_GENERATOR", sequenceName = "LOGS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_ID_GENERATOR")
    
	 @Column(name = "ID")
    private Integer id;

    @Column(name = "LIBRARY_ID")
    private Integer libraryId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "RENT_DATE")
    private Date rentDate;

    @Column(name = "RETURN_DATE")
    private Date returnDate;

    @Column(name = "RETURN_DUE_DATE")
    private Date returnDueDate;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getReturnDueDate() {
        return returnDueDate;
    }

    public void setReturnDueDate(Date returnDueDate) {
        this.returnDueDate = returnDueDate;
    }

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Library library;
    
    public Library getLibrary() {
    	return this.library;
    }
    
    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User user;
    
    public User gerUser() {
    	return this.user;
    }
}
