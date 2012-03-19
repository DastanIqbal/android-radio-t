package org.dandelion.radiot.podcasts.download;

public interface Downloader {
    long submit(DownloadTask task);
    DownloadTask query(long id);
}
