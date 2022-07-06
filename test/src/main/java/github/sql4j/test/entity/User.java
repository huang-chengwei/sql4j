package github.sql4j.test.entity;

import github.sql4j.dsl.expression.path.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Objects;

@javax.persistence.Entity
@ToString
@Getter
@Setter
public class User implements Entity {

    @Id
    private int id;

    private int randomNumber;

    private String username;

    private Date time;

    private Integer pid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", insertable = false, updatable = false)
    @ToString.Exclude
    private User parentUser;

    private boolean valid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (randomNumber != user.randomNumber) return false;
        if (valid != user.valid) return false;
        if (!Objects.equals(username, user.username)) return false;
        // if (!Objects.equals(time, user.time)) return false;
        return Objects.equals(pid, user.pid);
    }

    @Override
    public int hashCode() {
        int result = randomNumber;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + (valid ? 1 : 0);
        return result;
    }
}
