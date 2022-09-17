package omid.ro.civil_registration.services;

import lombok.RequiredArgsConstructor;
import omid.ro.civil_registration.dto.OutputMessage;
import omid.ro.civil_registration.dto.RegisterInputDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.Event;
import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.State;

@Service
@RequiredArgsConstructor
public class GeneralService {
    private final CivilRegistrationStateMachineHandler handler;

    public ResponseEntity<OutputMessage> giveReward(Long personId) {
        StateMachine<State, Event> stateMachine = handler.process(personId);
        Boolean rewardStatus = (Boolean) stateMachine.getExtendedState().getVariables().get("reward_status");
        org.springframework.statemachine.state.State<State, Event> state = stateMachine.getState();
        Map<String, String> otherInfo = new HashMap<>();
        otherInfo.put("current-state", state.getId().name());
        if (rewardStatus) {

            return new ResponseEntity<>(new OutputMessage("it's ok", otherInfo ), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OutputMessage("it's not ok", otherInfo), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<OutputMessage> register(RegisterInputDto dto) {

        StateMachine<State, Event> stateMachine = handler.register(dto);
        Map<String, String> otherInfo = new HashMap<>();
        otherInfo.put("current-state", stateMachine.getState().getId().name());
        return new ResponseEntity<>(new OutputMessage("registered successfully!", otherInfo ), HttpStatus.OK);
    }

}
