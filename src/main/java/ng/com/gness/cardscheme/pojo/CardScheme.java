package ng.com.gness.cardscheme.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class CardScheme {
    private String scheme;
    private String type;
    private String brand;
    private Bank bank;

    public class Bank {
        @Getter @Setter
        private String name;
    }
}


