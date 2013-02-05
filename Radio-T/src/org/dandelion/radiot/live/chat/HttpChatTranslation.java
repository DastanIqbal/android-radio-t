package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private MessageConsumer messageConsumer;
    private ProgressListener progressListener;
    private final HttpChatClient chatClient;
    private boolean isActive;
    public Scheduler refreshScheduler;
    public Scheduler.Performer nextMessagePoller = new Scheduler.Performer() {
        @Override
        public void performAction() {
            requestNextMessages();
        }
    };

    public HttpChatTranslation(String baseUrl, Scheduler refreshScheduler) {
        this(new HttpChatClient(baseUrl), refreshScheduler);
    }

    public HttpChatTranslation(HttpChatClient chatClient, Scheduler refreshScheduler) {
        this.chatClient = chatClient;
        this.refreshScheduler = refreshScheduler;
        refreshScheduler.setPerformer(nextMessagePoller);
    }

    @Override
    public void start(MessageConsumer consumer, ProgressListener listener) {
        this.messageConsumer = consumer;
        this.progressListener = listener;
        isActive = true;
        requestLastMessages();
    }

    private void requestLastMessages() {
        new LastRecordsRequest(this).execute();
    }

    private void requestNextMessages() {
        new NextRecordsRequest(this).execute();
    }

    @Override
    public void stop() {
        isActive = false;
        refreshScheduler.cancel();
        chatClient.shutdown();

        messageConsumer = null;
        progressListener = null;
    }

    private void consumeMessages(List<Message> messages) {
        messageConsumer.appendMessages(messages);
        refreshScheduler.scheduleNext();
    }

    private static abstract class ChatTranslationTask extends AsyncTask<Void, Void, List<Message>> {
        protected final HttpChatTranslation translation;
        private Exception error;
        private String mode;

        protected ChatTranslationTask(HttpChatTranslation translation, String mode) {
            this.translation = translation;
            this.mode = mode;
        }

        @Override
        protected List<Message> doInBackground(Void... params) {
            try {
                return translation.chatClient.retrieveMessages(mode);
            } catch (Exception e) {
                error = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            if (!translation.isActive) {
                return;
            }

            if (error != null) {
                reportError();
            } else {
                reportSuccess();
                consumeMessages(messages);
            }
        }

        private void consumeMessages(List<Message> messages) {
            translation.consumeMessages(messages);

        }

        private void reportError() {
            translation.progressListener.onError();
        }

        protected void reportSuccess() {

        }

    }


    private static class LastRecordsRequest extends ChatTranslationTask {
        public LastRecordsRequest(HttpChatTranslation translation) {
            super(translation, "last");
        }

        @Override
        protected void onPreExecute() {
            translation.progressListener.onConnecting();
        }

        @Override
        protected void reportSuccess() {
            translation.progressListener.onConnected();
        }
    }

    private static class NextRecordsRequest extends ChatTranslationTask {
        private NextRecordsRequest(HttpChatTranslation translation) {
            super(translation, "next");
        }
    }
}
