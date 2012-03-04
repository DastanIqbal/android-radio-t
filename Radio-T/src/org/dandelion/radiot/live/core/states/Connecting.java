package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Connecting extends PlaybackState {
    public Connecting(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void enter(ILiveShowService service) {
        service.goForeground(1);
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.PlaybackStateVisitor visitor) {
        visitor.onConnecting(this);
    }
}
