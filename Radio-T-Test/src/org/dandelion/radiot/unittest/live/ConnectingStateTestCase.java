package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Connecting;

public class ConnectingStateTestCase extends PlaybackStateTestCase {

    private Connecting state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Connecting(null);
    }

    public void testGoesForegroundWhenEntersConnectingState() throws Exception {
        state.enter(service);
        assertTrue(serviceIsForeground);
    }
}
