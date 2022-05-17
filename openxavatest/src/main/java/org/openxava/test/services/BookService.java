package org.openxava.test.services;

import javax.json.bind.annotation.*;
import javax.ws.rs.client.*;

import org.openxava.test.model.*;

/**
 * 
 * @author Javier Paniza
 */
public class BookService {
	
	static public class BookResponse {
		
		@JsonbProperty("ISBN:9780201835953")
		private Book book;

		public Book getBook() {
			return book;
		}

		public void setBook(Book book) {
			this.book = book;
		}

	}	
		

	public static Book get() throws Exception {
		BookResponse response = ClientBuilder.newClient()
			.target("http://openlibrary.org/")
			.path("/api/books")
			.queryParam("bibkeys", "ISBN:9780201835953")
			.queryParam("jscmd", "data")
			.queryParam("format", "json")
			.request()
			.get(BookResponse.class);
		return response.getBook();
	}

}
