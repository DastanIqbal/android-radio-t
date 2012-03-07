package org.dandelion.radiot.unittest.live;


import junit.framework.Assert;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.live.service.LiveShowService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.test.ServiceTestCase;

public class LiveShowServiceTestCase extends ServiceTestCase<LiveShowService> {
	private LiveShowService service;

	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setApplication(new RadiotApplication() {
			@Override
			public MediaPlayer getMediaPlayer() {
				return new MockMediaPlayer();
			}
		});
		bindService(new Intent());
		service = getService();
	}
	
	public void testSendsBroadcastsWhenStateChanged() throws Exception {
		final LiveShowState state = new LiveShowState();
		
		(new BroadcastCatcher(getContext(),
				LiveShowService.PLAYBACK_STATE_CHANGED) {
			@Override
			public void run() {
				service.onChangedState(state, state);
			}
		}).assertCaught();
	}

	@Override
	public void testServiceTestCaseSetUpProperly() throws Exception {
		// Stupid method
	}
}

abstract class BroadcastCatcher extends BroadcastReceiver {
	private boolean broadcastReceived = false;
	private IntentFilter filter;
	private Context context;

	public BroadcastCatcher(Context context, String action) {
		filter = new IntentFilter(action);
		this.context = context;
	}

	public void onReceive(Context context, Intent intent) {
		broadcastReceived = true;
	}

	public abstract void run();

	public void assertCaught() {
		context.registerReceiver(this, filter);
		try {
			run();
			Thread.yield();
			Assert.assertTrue("No broadcast received", broadcastReceived);
		} finally {
			context.unregisterReceiver(this);
		}
	}
}