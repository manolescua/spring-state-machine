package ro.manolescua.statemachine;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import ro.manolescua.statemachine.events.CreditEvents;
import ro.manolescua.statemachine.states.CreditStates;

import java.util.EnumSet;


@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<CreditStates, CreditEvents> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<CreditStates, CreditEvents> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(false)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<CreditStates, CreditEvents> states) throws Exception {
        states
                .withStates()
                .initial(CreditStates.REQUESTED)
                .end(CreditStates.PAYED)
                .states(EnumSet.allOf(CreditStates.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<CreditStates, CreditEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(CreditStates.REQUESTED).target(CreditStates.CREDIT_CHECKING).event(CreditEvents.CHECK_CREDIT)
                .and()
                .withExternal()
                .source(CreditStates.CREDIT_CHECKING).target(CreditStates.APPROVED).event(CreditEvents.APPROVE)
                .and()
                .withExternal()
                .source(CreditStates.CREDIT_CHECKING).target(CreditStates.REJECTED).event(CreditEvents.REJECT)
                .and()
                .withExternal()
                .source(CreditStates.APPROVED).target(CreditStates.PAYED).event(CreditEvents.PAY);
    }


    @Bean
    public StateMachineListener<CreditStates, CreditEvents> listener() {
        return new StateMachineListenerAdapter<CreditStates, CreditEvents>() {
            @Override
            public void stateChanged(State<CreditStates, CreditEvents> from, State<CreditStates, CreditEvents> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}
