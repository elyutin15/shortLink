package shortlink.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import shortlink.demo.data.JdbcLinksRepository;
import shortlink.demo.models.Link;

import java.util.Optional;

@Controller
@SessionAttributes("link")
public class HomeController {

    JdbcLinksRepository repository;

    public HomeController(JdbcLinksRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/{link}")
    public RedirectView redirect(@PathVariable("link") String urlNew) {
        Optional<Link> link = repository.findByUrlNew(urlNew);
        return new RedirectView(link.get().getUrl());
    }

    @PostMapping("/")
    public String handleForm(Link link, @ModelAttribute Link modelLink, Model model) {
        Link shortLink = repository.save(link);
        modelLink.setUrl(shortLink.getUrl());

        return "final";
    }


    @ModelAttribute(name="link")
    public Link link() { return new Link(); }
}
