package renew0304.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

// 값 타입은 변경 불가능하게 설계, @Setter 제거, 생성자에서 값 초기화를 통해서
// 변경이 불가능하도록 설계
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // @Embeddable은 기본 생성자(default constructor)를 public, protected 설정
    protected Address(){
    }
    // 생성자에서 값 최기화를 통해서 변경이 불가능하도록 설계
    public Address(String city,String street,String zipcode){
        this.city = city;
        this.street=street;
        this.zipcode=zipcode;
    }
}
