package org.dandelion.radiot.unittest;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.RssFeedModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RssFeedModelTestCase extends TestCase {
	private RssFeedModel model;
	private String feedContent;
	private List<PodcastItem> parsedItems;
	private PodcastItem firstParsedItem;
	protected boolean streamClosed;
	protected String requestedImageUrl;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		model = createTestModel();
		feedContent = "";
	}

	protected RssFeedModel createTestModel() {
		return new RssFeedModel(null) {
			@Override
			protected InputStream openContentStream() throws IOException {
				return new ByteArrayInputStream(getCompleteFeed().getBytes()) {
					@Override
					public void close() throws IOException {
						super.close();
						streamClosed = true;
					}
				};
			}

			@Override
			protected InputStream openImageStream(String url) {
				requestedImageUrl = url;
				return null;
			}
		};
	}

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		newFeedItem("");
		newFeedItem("");

		parseRssFeed();
		assertEquals(2, parsedItems.size());
		assertNotNull(parsedItems.get(0));
	}

	public void testExtractingPodcastNumber() throws Exception {
		newFeedItem("<title>Radio 192</title>");
		parseRssFeed();
		assertEquals("#192", firstParsedItem.getNumberString());
	}

	public void testExtractPodcastDate() throws Exception {
		newFeedItem("<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>");
		parseRssFeed();
		assertEquals("13.06.2010", firstParsedItem.getPubDate());
	}

	public void testExtractShowNotes() throws Exception {
		newFeedItem("<itunes:summary>Show notes</itunes:summary>");
		parseRssFeed();
		assertEquals("Show notes", firstParsedItem.getShowNotes());
	}

	public void testExtractPodcastLink() throws Exception {
		newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>");
		parseRssFeed();
		assertEquals("http://podcast-link",	firstParsedItem.getAudioUri());
	}

	public void testSkipNonAudioEnsclosures() throws Exception {
		newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>"
				+ "<enclosure url=\"http://yet-another-link\" type=\"text/xml\"/>");

		parseRssFeed();

		assertEquals("http://podcast-link",	firstParsedItem.getAudioUri());
	}

    public void testExtractThumbnailUrl() throws Exception {
        newFeedItem("<description>" +
                "&lt;p&gt;&lt;img src=\"http://www.radio-t.com/images/radio-t/rt302.jpg\" alt=\"\" /&gt;&lt;/p&gt;\n" +
                "</description>");
        parseRssFeed();
        assertEquals("http://www.radio-t.com/images/radio-t/rt302.jpg", firstParsedItem.getThumbnailUrl());
    }

    public void testEnsureStreamIsClosed() throws Exception {
		streamClosed = false;
		parseRssFeed();
		assertTrue(streamClosed);
	}

	public void testHandleParsingErrors() throws Exception {
		newFeedItem("<number>102");
		try {
			parseRssFeed();
		} catch (Exception e) {
			return;
		}
		fail("Should have raised the exception");
	}

	private void newFeedItem(String itemContent) {
		feedContent = feedContent + "<item>" + itemContent + "</item>";
	}

	private void parseRssFeed() throws Exception {
		parsedItems = model.retrievePodcasts();
		if (!parsedItems.isEmpty()) {
			firstParsedItem = parsedItems.get(0);
		}
	}

	private String getCompleteFeed() {
		return "<rss xmlns:media=\"http://search.yahoo.com/mrss/\" " +
                "xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\"><channel>"
				+ feedContent + "</channel></rss>";
	}
}
