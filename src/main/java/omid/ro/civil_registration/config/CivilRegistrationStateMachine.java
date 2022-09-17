package omid.ro.civil_registration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omid.ro.civil_registration.services.CivilRegistrationAction;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@Slf4j
@RequiredArgsConstructor
public class CivilRegistrationStateMachine extends
        StateMachineConfigurerAdapter<CivilRegistrationStateMachine.State, CivilRegistrationStateMachine.Event> {

    private final CivilRegistrationAction civilRegistrationAction;

    @Override
    public void configure(StateMachineStateConfigurer<State, Event> states) throws Exception {
        states.withStates()
                .initial(State.START)
                .states(EnumSet.allOf(State.class))
                .end(State.PRE_PROCESSOR_REJECTION)
                .end(State.SUCCESSFUL)
                .end(State.ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<State, Event> transitions) throws Exception {

        transitions.withExternal()
                .source(State.START).target(State.REGISTRATION_PRE_PROCESSOR).event(Event.PROCESS)
                .action(civilRegistrationAction.doPreProcessorAction())
                .and().withExternal()
                .source(State.REGISTRATION_PRE_PROCESSOR).target(State.PRE_PROCESSOR_APPROVAL).event(Event.PROCESS_APPROVED)
                .and().withExternal()
                .source(State.REGISTRATION_PRE_PROCESSOR).target(State.PRE_PROCESSOR_REJECTION).event(Event.PROCESS_DECLINED)
                .and().withExternal()
                .source(State.PRE_PROCESSOR_APPROVAL).target(State.SUCCESSFUL).event(Event.REGISTRATION_APPROVED)
                .action(civilRegistrationAction.doRegistrationAction())
                .and().withExternal()
                .source(State.PRE_PROCESSOR_APPROVAL).target(State.ERROR).event(Event.REGISTRATION_DECLINED)
        ;

    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<CivilRegistrationStateMachine.State,
            CivilRegistrationStateMachine.Event> config) throws Exception {
        StateMachineListenerAdapter<State, Event> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(org.springframework.statemachine.state.State<State, Event> from,
                                     org.springframework.statemachine.state.State<State, Event> to) {
                log.info(String.format("state changed: from %s to %s", from, to));
            }
            /*@Override
            public void stateChanged(State<State, Event> from, State<State, Event> to) {
                log.info(String.format("state changed: from %s to %s", from, to));
            }*/
        };
        config.withConfiguration().listener(adapter);
    }

    public enum State {
        START, REGISTRATION_PRE_PROCESSOR, PRE_PROCESSOR_APPROVAL, PRE_PROCESSOR_REJECTION, SUCCESSFUL, ERROR
    }

    public enum Event {
        PROCESS("1"),
        PROCESS_APPROVED("2"),
        PROCESS_DECLINED("3"),
        REGISTRATION_APPROVED("4"),
        REGISTRATION_DECLINED("5");

        Event(String code) {
        }
    }


}
