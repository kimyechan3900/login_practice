package hello.hellospring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table(name = "table_demo")
@Entity
public class Demo {

    @Id
    @GeneratedValue
    private Integer id;
    private String email;
    private String password;

}