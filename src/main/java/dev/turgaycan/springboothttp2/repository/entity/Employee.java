package dev.turgaycan.springboothttp2.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Entity
@ToString
@Table(name = "EMPLOYEES")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "FIRST_NAME", length = 250)
    private String firstName;

    @Column(name = "LAST_NAME", length = 250)
    private String lastName;

    @Column(name = "EMAIL", length = 250)
    private String email;

    @Column(name = "SALARY")
    private BigDecimal salary;

    @Column(name = "START_DATE")
    private Date startDate;

    public String buildFullname() {
        return getFirstName() + " " + getLastName();
    }
}
