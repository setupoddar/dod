package dod.dal.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by setu.poddar on 27/06/17.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
@EqualsAndHashCode(callSuper = false)
public class Product implements GenericModel {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "RATING", nullable = false)
    private Integer rating;

}
