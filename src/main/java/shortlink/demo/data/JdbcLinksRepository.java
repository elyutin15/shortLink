package shortlink.demo.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shortlink.demo.models.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcLinksRepository implements LinksRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Link> findByUrlNew(String urlNew) {
        List<Link> links = jdbcTemplate.query(
                "select id, urlOld, urlNew from Links where urlNew=?",
                this::mapRowToLink,
                urlNew
        );
        return links.size() == 0
                ? Optional.empty()
                : Optional.of(links.get(0));
    }


    @Override
    public Link save(Link link) {
        Link newLink = new Link("short" + link.getUrl().substring(0,2));
        jdbcTemplate.update(
                "insert into Links (urlOld, urlNew) values (?, ?)",
                link.getUrl(),
                newLink.getUrl()
        );
        return newLink;
    }

    private Link mapRowToLink(ResultSet row, int rowNum) throws SQLException {
        return new Link(
            row.getString("urlOld")
        );
    }
}
