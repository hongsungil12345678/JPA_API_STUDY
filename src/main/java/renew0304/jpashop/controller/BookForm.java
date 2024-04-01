package renew0304.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class BookForm {
    // Item 공통속성
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // Book 개별속성
    private String author;
    private String isbn;
}
