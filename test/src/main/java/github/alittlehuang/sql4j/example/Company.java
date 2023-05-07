package github.alittlehuang.sql4j.example;

import github.alittlehuang.sql4j.dsl.expression.path.Persistable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Company implements Persistable {

    @Id
    private Integer id;
    private String name;
    private String addr;

}
