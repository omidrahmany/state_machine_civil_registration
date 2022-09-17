package omid.ro.civil_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage {

    private String message;
    private Map<String, ?> otherInfo;
}
