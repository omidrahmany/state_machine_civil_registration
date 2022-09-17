package omid.ro.civil_registration.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("MALE")
    MALE,

    @JsonProperty("FEMALE")
    FEMALE;

//    Gender(String gender) {
//    }
}
