package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.core.utilities.UntrustworthyData;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(EchoInitiator.class)
public class EchoResponder extends FlowLogic<Void> {
    private FlowSession counterpartySession;

    public EchoResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
//        String message = counterpartySession.receive()

        UntrustworthyData<String> packet1 = counterpartySession.receive(String.class);
        String message = packet1.unwrap(data -> {
            // Perform checking on the object received.
            // T O D O: Check the received object.
            // Return the object.
            return data;
        });

        String reversed = new StringBuilder(message).reverse().toString();
        System.out.println(reversed);
        counterpartySession.send(reversed);

        return null;
    }
}
