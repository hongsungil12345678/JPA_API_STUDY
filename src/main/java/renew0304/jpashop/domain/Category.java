package renew0304.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import renew0304.jpashop.domain.item.Item;

import java.util.ArrayList;
import java.util.List;
/**
 * M : M 양방향 관계 매핑, 1:M , M:1로 풀어서 해결
 * 즉, 중간 테이블로 각각 풀어서 매핑
 * M : M 컬럼 추가도 어렵고 데이터 추가하기 복잡해짐.
 */
@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;
/**
     M:M 관계 M:1, 1:M로 풀어서 해결 (중간 테이블)
     @JoinTable(name = "중간테이블",
                joinColumns = @JoinColumn(name = "해당 테이블"),
                inverseJoinColumns = @JoinColumn(name = "조인 테이블"))
 */
    @ManyToMany
    @JoinTable(name = "category_item", 
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();
    
    // --------- 계층구조 (부모 - 자식) 셀프로 양방향 연관관계 설정 (같은 엔티티)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    //==== 연관관계 편의 메서드 , 양방향일때 코드 넣어줘야하는걸 간결하게
    // child 추가시 부모, 자식 둘다 들어가야 된다.
    // 부모 컬렉션에도, 자식에서도 부모가 누군지
    public void addChildCategory(Category child){
        this.child.add(child); //부모 컬렉션에 추가
        child.setParent(this); //부모 설정
    }
}
