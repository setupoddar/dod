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
@Table(name = "LISTING")
@EqualsAndHashCode(callSuper = false)
public class Listing implements GenericModel {

    @Id
    @Column(name = "ID")
    private String id;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "LISTING_OFFER", joinColumns = { @JoinColumn(name = "LISTING_ID") }, inverseJoinColumns = { @JoinColumn(name = "OFFER_ID") })
    private Set<Offer> offers = new HashSet<Offer>();

}
