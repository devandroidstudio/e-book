package com.eCommerce.service;

import java.util.List;

import com.eCommerce.domain.Book;


public interface BookService {
	List<Book> findAll ();
	
	Book findById(Long id);

	Book save(Book book);

	void delete(Long id);
	
	List<Book> findByCategory(String category);
	
	List<Book> blurrySearch(String title);

}
