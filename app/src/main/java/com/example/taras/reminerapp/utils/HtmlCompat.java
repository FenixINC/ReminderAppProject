package com.example.taras.reminerapp.utils;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by Oleh Mereshchuk on 4/12/2018
 */
public class HtmlCompat {
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static Spanned fromHtml(String html, Html.TagHandler tagHandler) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(html));
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, null, tagHandler);
        } else {
            result = Html.fromHtml(html, null, tagHandler);
        }
        return result;
    }
}
