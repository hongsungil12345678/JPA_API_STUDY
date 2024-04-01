package renew0304.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renew0304.jpashop.domain.item.Item;
import renew0304.jpashop.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    
    // 1. 저장
    @Transactional // readOnly이므로 따로 설정
    public void saveItem(Item item){
        itemRepository.save(item);
    }
    
    // 2. 조회
    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
    public List<Item> findItems(){
        return itemRepository.findAll();
    }
    /**
     * 영속성 컨텍스트가 자동 변경
     */
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity) {
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }
}
