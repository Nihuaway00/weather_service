package entity.user;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;

    public static class builder{
        private String email;
        private String password;

        public builder(){}

        public builder email(String value){
            this.email = value;
            return this;
        }

        public builder password(String value) {
            this.password = value;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }

    private User(builder builder){
        this.email = builder.email;
        this.password = builder.password;
    }
}
