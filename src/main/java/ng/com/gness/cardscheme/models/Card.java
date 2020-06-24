package ng.com.gness.cardscheme.models;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name ="verifiedCards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String scheme;
    private String type;
    private String brand;
    private String iin;
    private String bank;
    @Column(name = "verified")
    private boolean verified;

    public Card(String scheme, String type, String brand, String iin, String bank, boolean verified) {
        this.scheme = scheme;
        this.type = type;
        this.brand = brand;
        this.iin = iin;
        this.bank = bank;
        this.verified = verified;
    }
}
