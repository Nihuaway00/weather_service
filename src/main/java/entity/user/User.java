package entity.user;

import com.sun.istack.NotNull;
import lombok.*;

import jakarta.persistence.*;

import javax.validation.constraints.NotBlank;

@Entity
@Table
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;
}
