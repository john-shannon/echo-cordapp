package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.UntrustworthyData;

import java.util.Set;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class EchoInitiator extends FlowLogic<Void> {
    private final String message;
    private final String counterparty;

    private final ProgressTracker progressTracker = new ProgressTracker();

    public EchoInitiator(String message, String counterparty) {
        this.message = message;
        this.counterparty = counterparty;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        Set<Party> identities = getServiceHub().getIdentityService().partiesFromName(counterparty, false);
        Party counterIdentity = (Party)identities.toArray()[0];

        FlowSession flowSession = initiateFlow(counterIdentity);
        flowSession.send(message);

        UntrustworthyData<String> packet1 = flowSession.receive(String.class);
        String reversed = packet1.unwrap(data -> {
            // Perform checking on the object received.
            // T O D O: Check the received object.
            // Return the object.
            return data;
        });

        System.out.println(reversed);

        return null;
    }
}
