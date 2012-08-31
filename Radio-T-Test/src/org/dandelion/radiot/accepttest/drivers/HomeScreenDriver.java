package org.dandelion.radiot.accepttest.drivers;


import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

import android.app.Activity;
import android.app.Instrumentation;

import com.jayway.android.robotium.solo.Solo;

// TODO HomeScreenDriver starts to become a more general driver
public class HomeScreenDriver extends Solo {
    private Instrumentation instrumentation;

    public HomeScreenDriver(Instrumentation inst, Activity activity) {
		super(inst, activity);
        this.instrumentation = inst;
	}

	public void assertOnHomeScreen() {
		assertCurrentActivity("Must be on the home screen", HomeScreenActivity.class);
	}

    // TODO: Get rid of visitMainShowPage()
	public PodcastListActivity visitMainShowPage() {
		clickOnText("Подкасты");
		assertCurrentActivity("Must be on the main show page", PodcastListActivity.class);
		return (PodcastListActivity) getCurrentActivity();
	}

	public void visitAfterShowPage() {
		clickOnText("После-шоу");
		assertCurrentActivity("Must be on the after show page", PodcastListActivity.class);
	}

	public void waitSomeTime() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void assertShowingPodcastList() {
		assertCurrentActivity("Must show podcast list", PodcastListActivity.class);
	}

    public void finish() {
        finishOpenedActivities();
    }

    public PodcastListDriver visitMainShowPage2() {
        PodcastListActivity activity = visitMainShowPage();
        return new PodcastListDriver(instrumentation, activity);
    }

    public void goToAboutScreen() {
        clickOnText("О программе");
    }

    public void clickActivityTitle() {
        clickOnActionBarHomeButton();
    }
}