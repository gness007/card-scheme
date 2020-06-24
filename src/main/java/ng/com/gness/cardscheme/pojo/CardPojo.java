package ng.com.gness.cardscheme.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CardPojo {
    private String scheme;
    private String type;
    private String brand;
    private String iin;
    private String bank;
    private boolean verified;

    public CardPojo(String scheme, String type, String brand, String iin, String bank, boolean verified) {
        this.scheme = scheme;
        this.type = type;
        this.brand = brand;
        this.iin = iin;
        this.bank = bank;
        this.verified = verified;
    }
}
