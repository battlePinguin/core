package repository;

import com.javacode.core.entity.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("books")
                .usingGeneratedKeyColumns("id");
    }
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publicationYear")
                ));
    }

    public Book findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publicationYear")
                ));
    }

    public Book save(Book book) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", book.getTitle());
        parameters.put("author", book.getAuthor());
        parameters.put("publicationYear", book.getPublicationYear());

        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        book.setId(newId.longValue());

        return book;
    }

    public int update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publicationYear = ? WHERE id = ?";
        return jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());

    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
