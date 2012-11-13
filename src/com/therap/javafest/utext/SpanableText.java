package com.therap.javafest.utext;

import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.util.Linkify;

public class SpanableText {

	Context context;
	private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();

	public SpanableText(Context context) {
		this.context = context;
		emoticons.put(":@", R.drawable.ic_imagespan_angry);
		emoticons.put(":$", R.drawable.ic_imagespan_blushing);
		emoticons.put("|B", R.drawable.ic_imagespan_cool);
		emoticons.put(":'", R.drawable.ic_imagespan_crying);
		emoticons.put(":)", R.drawable.ic_imagespan_happy);
		emoticons.put(":D", R.drawable.ic_imagespan_laughing);
		emoticons.put(":B", R.drawable.ic_imagespan_nerd);
		emoticons.put(":(", R.drawable.ic_imagespan_sad);
		emoticons.put(":&", R.drawable.ic_imagespan_sick);
		emoticons.put(":O", R.drawable.ic_imagespan_surprised);
		emoticons.put(":P", R.drawable.ic_imagespan_tounge);
		emoticons.put(";)", R.drawable.ic_imagespan_winking);
	}

	public SpannableString convertToSpannableString(String text) {
		SpannableString builder = new SpannableString(text);
		for (int index = 0; index < builder.length(); index++) {
			for (Entry<String, Integer> entry : emoticons.entrySet()) {
				int length = entry.getKey().length();
				if (index + length > builder.length())
					continue;
				if (builder.subSequence(index, index + length).toString()
						.equals(entry.getKey())) {
					builder.setSpan(new ImageSpan(context, entry.getValue()),
							index, index + length,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					index += length - 1;
					break;
				}
			}
		}
		Linkify.addLinks(builder, Linkify.ALL);
		return builder;
	}
}
