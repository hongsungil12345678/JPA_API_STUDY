package renew0304.jpashop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import renew0304.jpashop.domain.Category;
import renew0304.jpashop.exception.NotEnoughStockException;

import java.util.ArrayList;
import java.util.List;

/** abstract 공통속성 묶어서 처리
 * 상속관계 Mapping -> 상속관계 전략 지정 (부모 클래스에서)
 * 현재는 싱글 테이블 전략 사용
 * @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 */
@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorColumn 싱글 테이블 전략으로 설정 -> 저장시 구분하기 위하여 설정
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // M : M 확인
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 엔티티 자체가 해결 가능한 것은 비즈니스 로직을 안에 넣는게 효율적
    // == 비즈니스 로직

    // 1. 재고 증가
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }
    // 2. 재고 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        // 예외처리
        if(restStock<0){
            throw new NotEnoughStockException("재고 부족");
        }
        this.stockQuantity = restStock;

    }

}
