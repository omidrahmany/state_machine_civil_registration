package omid.ro.civil_registration.services;

import lombok.RequiredArgsConstructor;
import omid.ro.civil_registration.config.CivilRegistrationStateMachine;
import omid.ro.civil_registration.dto.RegisterInputDto;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static omid.ro.civil_registration.Util.PERSON_ID_HEADER;
import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.Event;
import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.State;

@Service
@RequiredArgsConstructor
public class CivilRegistrationStateMachineHandlerImpl implements CivilRegistrationStateMachineHandler {

    private final StateMachineFactory<State, Event> factory;

    @Override
    public StateMachine<State, Event> process(Long personId) {
        StateMachine<State, Event> stateMachine = build(personId, State.START, Event.PROCESS, null);
        sendEvent(personId, stateMachine, Event.PROCESS);
        return stateMachine;
    }

    @Override
    public StateMachine<State, Event> register(RegisterInputDto dto) {
        Map<Object, Object> map = new HashMap<>();
        map.put("personId", dto.getPersonId());
        map.put("isValid", dto.getIsValid());
        DefaultExtendedState extendedState = new DefaultExtendedState(map);
        StateMachine<State, Event> stateMachine =
                build(dto.getPersonId(), State.PRE_PROCESSOR_APPROVAL, Event.REGISTRATION_APPROVED, extendedState);
        sendEvent(dto.getPersonId(), stateMachine, Event.REGISTRATION_APPROVED);
        return stateMachine;
    }


    private StateMachine<State, Event> build(Long personId, State state, Event event, ExtendedState extendedState) {
        StateMachine<CivilRegistrationStateMachine.State, CivilRegistrationStateMachine.Event> stateMachine =
                factory.getStateMachine(Long.toString(personId));
        stateMachine.stop();
        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
//            sma.addStateMachineInterceptor();
            sma.resetStateMachine(new DefaultStateMachineContext<>(state, null, null, extendedState));
        });
        stateMachine.start();
        return stateMachine;
    }

    private void sendEvent(Long personId, StateMachine<State, Event> stateMachine, Event event) {
        Message<Event> eventMessage = MessageBuilder.withPayload(event)
                .setHeader(PERSON_ID_HEADER, personId)
                .build();
        stateMachine.sendEvent(eventMessage);

    }


}
