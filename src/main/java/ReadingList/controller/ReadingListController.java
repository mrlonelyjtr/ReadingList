package ReadingList.controller;

import ReadingList.config.AmazonProperties;
import ReadingList.model.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.buffer.BufferCounterService;
import org.springframework.boot.actuate.metrics.buffer.BufferGaugeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ReadingList.model.Book;
import ReadingList.model.ReadingListRepository;

import java.util.List;

@Controller
@RequestMapping("/readingList")
public class ReadingListController {

    @Autowired
    private ReadingListRepository readingListRepository;

    @Autowired
    private AmazonProperties amazonProperties;

    @Autowired
    private BufferCounterService counterService;

    @Autowired
    private BufferGaugeService gaugeService;

    @GetMapping
    public String readersBooks(Reader reader, Model model){
        List<Book> readingList =  readingListRepository.findByReader(reader);

        if (readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
            model.addAttribute("amazonID", amazonProperties.getAssociateId());
        }

        return "readingList";
    }

    @PostMapping
    public String addToReadingList(Reader reader, Book book){
        book.setReader(reader);
        counterService.increment("books.saved");
        gaugeService.submit("books.last.saved", System.currentTimeMillis());
        readingListRepository.save(book);
        return "redirect:/readingList";
    }

}
