package entity.user;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String password;
}
