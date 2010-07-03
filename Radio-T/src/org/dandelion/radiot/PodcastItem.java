package org.dandelion.radiot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.text.Html;

public class PodcastItem implements Cloneable {
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
	private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

	private String number;
	private Date pubDate;
	private String showNotes;
	private Uri audioUri;

	public PodcastItem(int number, Date issueDate, String showNotes,
			String audioLink) {
		this.number = Integer.toString(number);
		this.pubDate = issueDate;
		this.showNotes = showNotes;
		this.audioUri = Uri.parse(audioLink);
	}

	public PodcastItem(int number) {
		this(number, null, null, "");
	}

	public Uri getAudioUri() {
		return audioUri;
	}

	public PodcastItem() {
	}

	public Date getPubDate() {
		return pubDate;
	}

	public String getShowNotes() {
		return showNotes;
	}

	public String getNumber() {
		return number;
	}

	public PodcastItem copy() {
		try {
			return (PodcastItem) clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void extractPodcastNumber(String value) {
		Matcher matcher = NUMBER_PATTERN.matcher(value);
		if (matcher.find()) {
			number = "#" + matcher.group();
		} else {
			number = value;
		}

	}

	public void extractPubDate(String value) {
		try {
			pubDate = DATE_FORMATTER.parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public void extractShowNotes(String value) {
		String noHtml = Html.fromHtml(value).toString();
		showNotes = noHtml.replaceAll("\n", " ");
	}

	public void extractAudioUri(String value) {
		audioUri = Uri.parse(value);
	}
}
