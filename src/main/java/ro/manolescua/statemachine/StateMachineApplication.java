package ro.manolescua.statemachine;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import ro.manolescua.statemachine.events.CreditEvents;
import ro.manolescua.statemachine.states.CreditStates;

@SpringBootApplication
public class StateMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(StateMachineApplication.class, args);
	}

	@Component
	class Runner implements ApplicationRunner{
		private final StateMachineFactory<CreditStates, CreditEvents> factory;

		Runner(StateMachineFactory<CreditStates, CreditEvents> factory) {
			this.factory = factory;
		}

		@Override
		public void run(ApplicationArguments applicationArguments) throws Exception {
			StateMachine<CreditStates, CreditEvents> stateMachine = this.factory
					.getStateMachine("12345");
			stateMachine.start();
			System.out.println("Current state: "+stateMachine.getState().getId().name());
			stateMachine.sendEvent(CreditEvents.CHECK_CREDIT);
			System.out.println("Current state: "+stateMachine.getState().getId().name());
			stateMachine.sendEvent(CreditEvents.APPROVE);
			System.out.println("Current state: "+stateMachine.getState().getId().name());
		}
	}

}
