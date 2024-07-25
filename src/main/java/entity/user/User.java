package entity.user;

import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    public static class Builder{
        private String name = "";
        private String email = "";

        public Builder(){}

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }

    private User(Builder builder){
        name = builder.name;
        email = builder.email;
    }
}
