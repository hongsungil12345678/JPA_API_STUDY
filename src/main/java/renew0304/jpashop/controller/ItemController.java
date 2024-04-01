package renew0304.jpashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import renew0304.jpashop.domain.item.Book;
import renew0304.jpashop.domain.item.Item;
import renew0304.jpashop.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form",new BookForm());//빈 BookForm 전달 -> html에서 추적 가능, 유지보수 용이
        return "items/createItemForm";
    }
    @PostMapping("/items/new")
    public String createItemForm(BookForm bookForm){
        Book book = new Book();// 객체 생성
    // 실제로 개발 시 set 사용하지 말고 파라미터로 넘겨서 생성자 매서드로 구현
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        //저장
        itemService.saveItem(book);
        return "redirect:/";
    }
    @GetMapping("/items")
    public String itemList(Model model){
        List<Item> items = itemService.findItems(); // 조회
        model.addAttribute("items",items);
        return "items/itemList";
    }
    @GetMapping("/items/{itemId}/edit") // @PathVariable("XXX") -> URL {"XXX"} 매핑
    public String updateItemForm(@PathVariable("itemId")Long itemId,Model model){
        //Item 객체로 반환되는데 Book만 사용할거니까 Casting해서 받음, 편의상
        Book item = (Book)itemService.findOne(itemId); // casting

        // set
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form",form);
        return "items/updateItemForm";
    }
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId")Long itemId,@ModelAttribute("form")BookForm form){
//        Book book = new Book();
//
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//
//        itemService.saveItem(book);
        itemService.updateItem(itemId,form.getName(),form.getPrice(),form.getStockQuantity());
        return "redirect:/items";
    }
}
