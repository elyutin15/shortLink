package shortlink.demo.data;

import shortlink.demo.models.Link;

import java.util.Optional;

public interface LinksRepository {
    Optional<Link> findByUrlNew(String urlNew);
    Link save(Link link);
    Integer countRows();
}
