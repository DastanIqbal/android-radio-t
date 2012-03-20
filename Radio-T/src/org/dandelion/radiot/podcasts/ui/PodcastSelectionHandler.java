package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class PodcastSelectionHandler {
    private static final int DOWNLOAD_ACTION = 0;
    private static final int PLAY_ACTION = 1;

    private Context context;
    private PodcastProcessor player;
    private PodcastProcessor downloader;

    public PodcastSelectionHandler(PodcastProcessor player, PodcastProcessor downloader) {
        this.player = player;
        this.downloader = downloader;
    }

    public void process(Context context, PodcastItem podcast) {
        this.context = context;
        showActionSelector(podcast.getTitle(),
                onClickListener(podcast));
    }

    private void showActionSelector(String title, DialogInterface.OnClickListener listener) {
        (new AlertDialog.Builder(context))
                .setTitle(title)
                .setItems(R.array.podcast_actions, listener)
                .show();
    }

    private DialogInterface.OnClickListener onClickListener(final PodcastItem podcast) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                PodcastProcessor processor = selectProcessor(index);
                processor.process(context, podcast);
            }
        };
    }

    private PodcastProcessor selectProcessor(int index) {
        switch(index) {
            case DOWNLOAD_ACTION: return downloader;
            case PLAY_ACTION: return player;
        }
        throw new RuntimeException("Unexpected action");
    }

}