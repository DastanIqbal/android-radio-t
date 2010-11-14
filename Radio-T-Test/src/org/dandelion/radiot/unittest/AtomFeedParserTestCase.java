package org.dandelion.radiot.unittest;

import java.util.ArrayList;

import org.dandelion.radiot.helpers.FakeFeedSource;
import org.dandelion.radiot.rss.AtomFeedParser;
import org.dandelion.radiot.rss.IFeedParser;
import org.dandelion.radiot.rss.RssItem;

import junit.framework.TestCase;

class AtomFeedSource extends FakeFeedSource {
	@Override
	public String getCompleteFeed() {
		return "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
				+ getFeedContent() + "</feed>";
	}
}

public class AtomFeedParserTestCase extends TestCase {

	private AtomFeedSource feedSource;
	private AtomFeedParser parser;
	private ArrayList<RssItem> items;
	private IFeedParser.ParserListener listener;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		feedSource = new AtomFeedSource();
		parser = new AtomFeedParser(feedSource);
		items = new ArrayList<RssItem>();
		listener = new IFeedParser.ParserListener() {
			public void onItemParsed(RssItem item) {
				items.add(item);
			}
		};
	}

	public void testParseEmptyFeed() throws Exception {
		parser.parse(listener);
		assertEquals(0, items.size());
	}

	public void testParseFeedWithEntry() throws Exception {
		feedSource.setFeedContent("<entry></entry>");
		parser.parse(listener);
		assertEquals(1, items.size());
	}

	public void testExtractEntryTitle() throws Exception {
		feedSource.setFeedContent("<entry><title>Entry title</title></entry>");
		parser.parse(listener);
		assertEquals(1, items.size());
		RssItem item = items.get(0);
		assertEquals("Entry title", item.title);
	}
	
	public void testExtractEntryLink() throws Exception {
		feedSource.setFeedContent("<entry>" +
				"<link href=\"http://entry-link\" />" + 
				"</entry>");
		parser.parse(listener);
		assertEquals(1, items.size());
		RssItem item = items.get(0);
		assertEquals("http://entry-link", item.link);
	}
}
