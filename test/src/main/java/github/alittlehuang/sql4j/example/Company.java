package github.alittlehuang.sql4j.example;

import github.alittlehuang.sql4j.dsl.expression.path.Persistable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Company implements Persistable {

    @Id
    private Integer id;
    private String name;
    private String addr;

}
