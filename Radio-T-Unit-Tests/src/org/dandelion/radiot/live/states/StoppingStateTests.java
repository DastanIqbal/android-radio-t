package org.dandelion.radiot.live.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.Stopping;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class StoppingStateTests {
    private LiveShowPlayer context = mock(LiveShowPlayer.class);
    private Stopping state = new Stopping(context);

    @Test
    public void startPlaybackDoesNothing() throws Exception {
        state.startPlayback();
        verifyZeroInteractions(context);
    }

    @Test
    public void stopPlaybackDoesNothing() throws Exception {
        state.stopPlayback();
        verifyZeroInteractions(context);
    }
}
