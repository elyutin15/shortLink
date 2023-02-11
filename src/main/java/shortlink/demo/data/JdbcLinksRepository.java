package shortlink.demo.data;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final String alphabet;
    private Logger log = LoggerFactory.getLogger(JdbcLinksRepository.class);
    private Integer sizeDB = 0;


    @Autowired
    public JdbcLinksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; ++i) {
            sb.append((char)('a' + i));
        }
        for (int i = 0; i < 26; ++i) {
            sb.append((char)('A' + i));
        }
        for (int i = 0; i < 10; ++i) {
            sb.append((char)('0' + i));
        }
        this.alphabet = sb.toString();
        this.sizeDB = countRows();
    }

    @Override
    public Integer countRows() {
        return jdbcTemplate.query(
                "select 1",
                (rowSet, rowNum) -> { return rowNum; }
        ).size();
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
        Optional<Link> linkInDB = findByUrlNew(link.getUrl());
        if (linkInDB.isPresent()) {
            log.warn(link.getUrl() + " already in use");
            return null;
        }
        Link newLink = shortLink(link);
        jdbcTemplate.update(
                "insert into Links (urlOld, urlNew) values (?, ?)",
                link.getUrl(),
                newLink.getUrl()
        );
        sizeDB++;
        return newLink;
    }

    private Link mapRowToLink(ResultSet row, int rowNum) throws SQLException {
        return new Link(
            row.getString("urlOld")
        );
    }

    private Link shortLink (Link link) {
        StringBuilder sb = new StringBuilder();
        int curNumber = sizeDB - 1;
        for (int i = 0; i < 6; ++i) {
            sb.append(alphabet.charAt(curNumber%alphabet.length()));
            curNumber /= alphabet.length();
        }
        return new Link(sb.toString());
    }
}
