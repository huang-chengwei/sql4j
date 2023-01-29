package github.sql4j.test.entity;

import github.sql4j.dsl.expression.path.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@SuppressWarnings("JpaDataSourceORMInspection")
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
        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        if (getRandomNumber() != user.getRandomNumber()) return false;
        if (isValid() != user.isValid()) return false;
        if (!Objects.equals(getUsername(), user.getUsername())) return false;
        // if (!Objects.equals(time, user.time)) return false;
        return Objects.equals(getPid(), user.getPid());
    }

    @Override
    public int hashCode() {
        int result = getRandomNumber();
        String username = getUsername();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        Date time = getTime();
        result = 31 * result + (time != null ? time.hashCode() : 0);
        Integer pid = getPid();
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        boolean valid = isValid();
        result = 31 * result + (valid ? 1 : 0);
        return result;
    }
}
