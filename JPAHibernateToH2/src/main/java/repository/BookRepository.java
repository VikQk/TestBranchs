package repository;

import javax.persistence.EntityManager;

import model.Author;
import model.Book;

import java.util.List;
import java.util.Optional;

public class BookRepository {
	private EntityManager entityManager;

	public BookRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Optional<Book> findById(Integer id) {
		Book book = entityManager.find(Book.class, id);
		return book != null ? Optional.of(book) : Optional.empty();
	}

	public List<Book> findAll() {
		return entityManager.createQuery("from Book").getResultList();
	}

	public Optional<Book> findByTitle(String title) {
		Book book = entityManager.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class)
				.setParameter("title", title).getSingleResult();
		return book != null ? Optional.of(book) : Optional.empty();
	}

	public Optional<Book> findByTitleNamedQuery(String title) {
		Book book = entityManager.createNamedQuery("Book.findByTitle", Book.class).setParameter("title", title)
				.getSingleResult();
		return book != null ? Optional.of(book) : Optional.empty();
	}

	public Optional<Book> save(Book book) {
		
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(book);
			entityManager.getTransaction().commit();
			return Optional.of(book);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public Optional<Book> deleteByTitle(String title) {
		Book book = entityManager.createNamedQuery("Book.findByTitle", Book.class)
				.setParameter("title", title).getSingleResult();

		System.out.println(book);
		entityManager.getTransaction().begin();
		entityManager.remove(book);
		entityManager.getTransaction().commit();

		Optional<Book> resultDelete;
		if (book != null)
			resultDelete = Optional.of(book);
		else
			resultDelete = Optional.empty();
		return resultDelete;
	}
}