package omid.ro.civil_registration.services;

import lombok.RequiredArgsConstructor;
import omid.ro.civil_registration.entities.Person;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import static omid.ro.civil_registration.Util.PERSON_ID_HEADER;
import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.Event;
import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.State;

@Service
@RequiredArgsConstructor
public class CivilRegistrationAction {

    private final PersonService service;


    public Action<State, Event> doPreProcessorAction() {
        return context -> {
            Long personId = (Long) context.getMessageHeader(PERSON_ID_HEADER);
            Person person = service.getById(personId);
            if (person.getAge() <= 2 && person.getGender().equals("FEMALE")) {
                person.setIsApplicableForReward(true);
                context.getStateMachine()
                        .sendEvent(MessageBuilder.withPayload(Event.PROCESS_APPROVED)
                                .setHeader(PERSON_ID_HEADER, context.getMessageHeader(PERSON_ID_HEADER))
                                .build());
                context.getStateMachine().getExtendedState().getVariables().put("reward_status", true);
            } else {
                person.setIsApplicableForReward(false);
                context.getStateMachine().sendEvent(MessageBuilder.withPayload(Event.PROCESS_DECLINED)
                        .setHeader(PERSON_ID_HEADER, context.getMessageHeader(PERSON_ID_HEADER))
                        .build());
                context.getStateMachine().getExtendedState().getVariables().put("reward_status", false);
            }
        };
    }

    public Action<State, Event> doRegistrationAction() {
        return context -> {
            Long personId = (Long) context.getExtendedState().getVariables().get("personId");
            Boolean isValid = (Boolean) context.getExtendedState().getVariables().get("isValid");
            Person person = service.getById(personId);
            person.setIsApplicableForReward(isValid);
            service.insertPerson(person);
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(Event.REGISTRATION_APPROVED)
                    .setHeader(PERSON_ID_HEADER, context.getMessageHeader(PERSON_ID_HEADER))
                    .build());
        };
    }
}