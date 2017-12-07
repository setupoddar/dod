package dod.dal.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by setu.poddar on 17/11/17.
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TAG")
public class Tag implements GenericModel {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME", nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", nullable = false)
    private Date endTime;

    @Column(name = "TAG", nullable = false)
    private String tag;
}
