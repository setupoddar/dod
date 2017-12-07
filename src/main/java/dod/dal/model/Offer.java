package dod.dal.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by setu.poddar on 27/06/17.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "OFFER")
@EqualsAndHashCode(callSuper = false)
public class Offer implements GenericModel {

    @Id
    @Column(name = "ID")
    private String id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "OFFER_TAG",
            joinColumns = {@JoinColumn(name = "OFFER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TAG_ID")})
    private Set<Tag> tags = new HashSet<Tag>();

}
