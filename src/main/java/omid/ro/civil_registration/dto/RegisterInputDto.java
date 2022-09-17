package omid.ro.civil_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInputDto {

    private Long personId;
    private Boolean isValid;
}
