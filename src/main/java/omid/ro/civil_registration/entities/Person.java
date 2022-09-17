package omid.ro.civil_registration.entities;

import lombok.*;
import omid.ro.civil_registration.enums.Gender;
import omid.ro.civil_registration.enums.MaritalStatus;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;

    private Boolean isApplicableForReward;

    private String maritalStatus;

    private String gender;
}