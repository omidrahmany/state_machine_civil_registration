package omid.ro.civil_registration.services;

import omid.ro.civil_registration.dto.RegisterInputDto;
import org.springframework.statemachine.StateMachine;

import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.Event;
import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.State;

public interface CivilRegistrationStateMachineHandler {

    StateMachine<State, Event> process(Long personId);
    StateMachine<State, Event> register(RegisterInputDto personId);




}
