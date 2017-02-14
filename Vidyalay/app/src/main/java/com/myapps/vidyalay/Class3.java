/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.myapps.vidyalay;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A sample Activity showing how to manage multiple YouTubeThumbnailViews in an adapter for display
 * in a List. When the list items are clicked, the video is played by using a YouTubePlayerFragment.
 * <p/>
 * The demo supports custom fullscreen and transitioning between portrait and landscape without
 * rebuffering.
 */
@TargetApi(13)
public final class Class3 extends Activity implements OnFullscreenListener {

    /**
     * The duration of the animation sliding up the video in portrait.
     */
    private static final int ANIMATION_DURATION_MILLIS = 300;
    /**
     * The padding between the video list and the video in landscape orientation.
     */
    private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;

    /**
     * The request code when calling startActivityForResult to recover from an API service error.
     */
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private VideoListFragment listFragment;
    private VideoFragment videoFragment;

    private String title;
    private static int  position;

    private View videoBox;
    private View closeButton;

    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_list_class3);

        title = getIntent().getStringExtra("title");
        //setIntentTitle(title2);
        position = getIntent().getIntExtra("position",-1);

        listFragment = (VideoListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        videoFragment =
                (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);

        videoBox = findViewById(R.id.video_box);
        closeButton = findViewById(R.id.close_button);

        videoBox.setVisibility(View.INVISIBLE);

        layout();

        checkYouTubeApi();
    }

    /*
    public void setIntentTitle(String title) {
        this.title = title;
    }
    */
    public String getIntentTitle() {
        return title;
    }

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            String errorMessage =
                    String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Recreate the activity if user performed a recovery action
            recreate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        layout();
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;

        layout();
    }

    /**
     * Sets up the layout programatically for the three different states. Portrait, landscape or
     * fullscreen+landscape. This has to be done programmatically because we handle the orientation
     * changes ourselves in order to get fluent fullscreen transitions, so the xml layout resources
     * do not get reloaded.
     */
    private void layout() {
        boolean isPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        listFragment.getView().setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
        listFragment.setLabelVisibility(isPortrait);
        closeButton.setVisibility(isPortrait ? View.VISIBLE : View.GONE);

        if (isFullscreen) {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
        } else if (isPortrait) {
            setLayoutSize(listFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM);
        } else {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
            setLayoutSize(listFragment.getView(), screenWidth / 4, MATCH_PARENT);
            int videoWidth = screenWidth - screenWidth / 4 - dpToPx(LANDSCAPE_VIDEO_PADDING_DP);
            setLayoutSize(videoFragment.getView(), videoWidth, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
    }

    public void onClickClose(@SuppressWarnings("unused") View view) {
        listFragment.getListView().clearChoices();
        listFragment.getListView().requestLayout();
        videoFragment.pause();
        ViewPropertyAnimator animator = videoBox.animate()
                .translationYBy(videoBox.getHeight())
                .setDuration(ANIMATION_DURATION_MILLIS);
        runOnAnimationEnd(animator, new Runnable() {
            @Override
            public void run() {
                videoBox.setVisibility(View.INVISIBLE);
            }
        });
    }

    @TargetApi(16)
    private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            animator.withEndAction(runnable);
        } else {
            animator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    runnable.run();
                }
            });
        }
    }

    /**
     * A fragment that shows a static list of videos.
     */
    public static final class VideoListFragment extends ListFragment {

        private static final List<VideoEntry> VIDEO_LIST;
        static {
            List<VideoEntry> list = new ArrayList<VideoEntry>();
            list.add(new VideoEntry("Class 3 EVS Chapter 1 Part 1", "kGZTkTPcS7Y"));
			list.add(new VideoEntry("Class 3 EVS Chapter 1 Part 2", "yZpRJaW1I9w"));
			list.add(new VideoEntry("Class 3 EVS Chapter 1 Part 3", "1dS9lLG8iFo"));
			list.add(new VideoEntry("Class 3 EVS Chapter 1 Part 4", "ijax1Qt5V18"));
			list.add(new VideoEntry("Class 3 EVS Chapter 2 Part 1", "lvN0w2TcRSg"));
			list.add(new VideoEntry("Class 3 EVS Chapter 2 Part 2", "8a0Iszu0G_o"));
			list.add(new VideoEntry("Class 3 EVS Chapter 2 Part 3", "toV12BMuW4g"));
			list.add(new VideoEntry("Class 3 EVS Chapter 3 Part 1", "SRmQLlec-y4"));
			list.add(new VideoEntry("Class 3 EVS Chapter 3 Part 2", "aAk34FzFm2g"));
			list.add(new VideoEntry("Class 3 EVS Chapter 3 Part 3", "KE5QMyI-81I"));
			list.add(new VideoEntry("Class 3 EVS Chapter 4 Part 1", "pJzpVQW2Jsg"));
			list.add(new VideoEntry("Class 3 EVS Chapter 4 Part 2", "PYj4fx_5QO8"));
			list.add(new VideoEntry("Class 3 EVS Chapter 5", "r9pwwWbfeRc"));
			list.add(new VideoEntry("Class 3 EVS Chapter 6 Part 1", "i2usNDr-0Gg"));
			list.add(new VideoEntry("Class 3 EVS Chapter 6 Part 2", "1IPiiDT4tu4"));
			list.add(new VideoEntry("Class 3 EVS Chapter 7 Part 1", "tizSmjTjwaw"));
			list.add(new VideoEntry("Class 3 EVS Chapter 7 Part 2", "Nxz6TUV6Bn0"));
			list.add(new VideoEntry("Class 3 EVS Chapter 7 Part 3", "U5iy5y0A28Y"));
			list.add(new VideoEntry("Class 3 EVS Chapter 7 Part 4", "4TGimDdCLQs"));
			list.add(new VideoEntry("Class 3 EVS Chapter 7 Part 5", "NUNzJWhMLWk"));
			list.add(new VideoEntry("Class 3 EVS Chapter 8", "pT3CMLfnNio"));
			list.add(new VideoEntry("Class 3 EVS Chapter 9", "L99lRSs8l6A"));
			list.add(new VideoEntry("Class 3 EVS Chapter 10", "kRq2C4nLa6Y"));
			list.add(new VideoEntry("Class 3 EVS Chapter 11 Part 1", "ucQxVyW2bpk"));
			list.add(new VideoEntry("Class 3 EVS Chapter 11 Part 2", "ayrKnHMJpfA"));
			list.add(new VideoEntry("Class 3 EVS Chapter 12", "xw9MMMBzD_8"));
			list.add(new VideoEntry("Class 3 EVS Chapter 14", "NlaqyyAKL6Y"));
			list.add(new VideoEntry("Class 3 EVS Chapter 15 Part 1", "8vTyASYFTWI"));
			list.add(new VideoEntry("Class 3 EVS Chapter 15 Part 2", "BQXoQAZleE0"));

			list.add(new VideoEntry("Class 3 English Chapter 1 Part 1", "ploK_fS8iIQ"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 2", "Gb3Axu2NJ3g"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 3", "M9rDatLnvA8"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 4", "Lu6_SOdhUHI"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 5", "nGjp06ignQs"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 6", "VxiDEoC1sB8"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 7", "ND5vR4nytBA"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 8", "5HGO7O2gMac"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 9", "1wRDzu_mQck"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 10", "a4p12O11wVA"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 11", "itZzuRAIGHQ"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 12", "wyrTL_1O1fA"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 13", "YhRHMvR5snk"));
			list.add(new VideoEntry("Class 3 English Chapter 1 Part 14", "jamhNFwOOdk"));
			list.add(new VideoEntry("Class 3 English Chapter 2", "Ambmj_k5rb4"));
			list.add(new VideoEntry("Class 3 English Chapter 3 Part 1", "pbB5XaAiZmk"));
			list.add(new VideoEntry("Class 3 English Chapter 3 Part 2", "XRaV0hu79Jw"));
			list.add(new VideoEntry("Class 3 English Chapter 4", "wVTE8S6LZKc"));
			list.add(new VideoEntry("Class 3 English Chapter 5", "n4naHCsYOPM"));
			list.add(new VideoEntry("Class 3 English Chapter 6", "aVjohYPCOds"));
			list.add(new VideoEntry("Class 3 English Chapter 7", "U4fLd2y5mJA"));
			list.add(new VideoEntry("Class 3 English Chapter 8", "FNMQfPPhr3s"));
			list.add(new VideoEntry("Class 3 English Chapter 9 Part 1", "045Gf7uALis"));
			list.add(new VideoEntry("Class 3 English Chapter 10", "sOvdDsQGosA"));
			list.add(new VideoEntry("Class 3 English Chapter 10 Part 1", "OFhwb-YmwuM"));
			list.add(new VideoEntry("Class 3 English Chapter 11", "SjsqWIh5Lqs"));
			list.add(new VideoEntry("Class 3 English Chapter 12", "miOctrBz6LM"));
			list.add(new VideoEntry("Class 3 English Chapter 13", "C1M2-gcJJAs"));
			list.add(new VideoEntry("Class 3 English Chapter 13 Part 1", "NsIHnoMPOLo"));
			list.add(new VideoEntry("Class 3 English Chapter 14", "G2YCc-kpvI0"));
			list.add(new VideoEntry("Class 3 English Chapter 15", "OSc2ILzriPE"));

			list.add(new VideoEntry("Class 3 Hindi Chapter 1", "AwEl_0wpO8k"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 2", "RcY9nV5pDbA"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 3", "QoKyDI3Fy7M"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 4", "OYOXyg9pkco"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 5", "pKuhjD-PyhI"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 6 Part 1", "MrqMwkOBCuE"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 6 Part 2", "0FqCdsTUh_U"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 7 Part 1", "RzI_zXCQ7_s"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 7 Part 2", "UTqt6x2-0ik"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 8", "ApdGX1bNSfI"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 9", "QOxY-wKBSKU"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 11 Part 1", "Ys0D8iA70BU"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 11 Part 2", "jQH6B-KHOHE"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 12", "gwahnhLzc5o"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 13", "wlhq_fwZ4Bw"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 14", "pl4GfgUJHwk"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 15", "Rb62_OaQPIo"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 16 Part 1", "YRy_BBUh2IY"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 16 Part 2", "A1yE6pdHvI0"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 17", "pyp43688uPY"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 18", "4uealdDe740"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 19", "i_ZEZgB_a8Y"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 20 Part 1", "gkv7-xVLi0Y"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 20 Part 2", "S7-3K81mARo"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 21 Part 1", "TBnF1HsHRfY"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 21 Part 2", "2j0h-kNgZPA"));
			list.add(new VideoEntry("Class 3 Hindi Chapter 22", "rsIRGhMj3Qk"));

			list.add(new VideoEntry("Class 3 Maths Chapter 1 Part 1", "AA44LunEdO0"));
			list.add(new VideoEntry("Class 3 Maths Chapter 2 Part 1", "M3eMFrKGMdE"));
			list.add(new VideoEntry("Class 3 Maths Chapter 3 Part 1", "zJtDtHW_yHQ"));
			list.add(new VideoEntry("Class 3 Maths Chapter 4 Part 1", "9RAwvv6gqmU"));
			list.add(new VideoEntry("Class 3 Maths Chapter 5 Part 1", "yi-4I_9ewC0"));
			list.add(new VideoEntry("Class 3 Maths Chapter 6 Part 1", "AXthPZ8krkY"));
			list.add(new VideoEntry("Class 3 Maths Chapter 7 Part 1", "gd64JMJlNgM"));
			list.add(new VideoEntry("Class 3 Maths Chapter 8 Part 1", "Q5WNYscJ6vA"));
			list.add(new VideoEntry("Class 3 Maths Chapter 9 Part 1", "BZpTopzoawo"));
			list.add(new VideoEntry("Class 3 Maths Chapter 9 Part 2", "bdJYUmR7ayM"));
			list.add(new VideoEntry("Class 3 Maths Chapter 10 Part 1", "5cF-cAxMkSU"));
			list.add(new VideoEntry("Class 3 Maths Chapter 10 Part 2", "eDFRCH7r04Y"));
			list.add(new VideoEntry("Class 3 Maths Chapter 11 Part 1", "5rJqhJ0z51s"));
			list.add(new VideoEntry("Class 3 Maths Chapter 11 Part 2", "aYbMrjRW-cw"));
			list.add(new VideoEntry("Class 3 Maths Chapter 12 Part 1", "_GFNEIgicgQ"));
			list.add(new VideoEntry("Class 3 Maths Chapter 12 Part 2", "Qkos1iKKhJ0"));
			list.add(new VideoEntry("Class 3 Maths Chapter 13 Part 1", "Tl36Zr5Wjo4"));
			list.add(new VideoEntry("Class 3 Maths Chapter 13 Part 2", "_62QlM-uPvM"));
			list.add(new VideoEntry("Class 3 Maths Chapter 14 Part 1", "8XFpOjIq_tQ"));
			list.add(new VideoEntry("Class 3 Maths Chapter 14 Part 2", "10syJvAaAIM"));
			list.add(new VideoEntry("Class 3 Maths Chapter 15 Part 1", "aWkvS4rnnIU"));
			list.add(new VideoEntry("Class 3 Maths Chapter 15 Part 2", "AHzHEglypp0"));
			list.add(new VideoEntry("Class 3 Maths Chapter 16 Part 1", "Djio5J4vf4g"));
			list.add(new VideoEntry("Class 3 Maths Chapter 16 Part 2", "Xv-i0z5TPpA"));
			list.add(new VideoEntry("Class 3 Maths Chapter 17 Part 1", "sbq6Jh4toZA"));
			list.add(new VideoEntry("Class 3 Maths Chapter 18 Part 1", "pE1ooimFUFo"));


            VIDEO_LIST = Collections.unmodifiableList(list);
        }

        private PageAdapter adapter;

        private View videoBox;

        private String title3;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Class3 obj = new Class3();
            title3 = obj.getIntentTitle();
            adapter = new PageAdapter(getActivity(), VIDEO_LIST);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            videoBox = getActivity().findViewById(R.id.video_box);
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            String videoId = VIDEO_LIST.get(position).videoId;

            VideoFragment videoFragment =
                    (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
            videoFragment.setVideoId(videoId);

            // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
            if (videoBox.getVisibility() != View.VISIBLE) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Initially translate off the screen so that it can be animated in from below.
                    videoBox.setTranslationY(videoBox.getHeight());
                }
                videoBox.setVisibility(View.VISIBLE);
            }

            // If the fragment is off the screen, we animate it in.
            if (videoBox.getTranslationY() > 0) {
                videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

            adapter.releaseLoaders();
        }

        public void setLabelVisibility(boolean visible) {
            adapter.setLabelVisibility(visible);
        }

    }

    /**
     * Adapter for the video list. Manages a set of YouTubeThumbnailViews, including initializing each
     * of them only once and keeping track of the loader of each one. When the ListFragment gets
     * destroyed it releases all the loaders.
     */
    private static final class PageAdapter extends BaseAdapter {

        private final List<VideoEntry> entries;
        private final List<View> entryViews;
        private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
        private final LayoutInflater inflater;
        private final ThumbnailListener thumbnailListener;

        private boolean labelsVisible;

        public PageAdapter(Context context, List<VideoEntry> entries) {
            this.entries = entries;

            entryViews = new ArrayList<View>();
            thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
            inflater = LayoutInflater.from(context);
            thumbnailListener = new ThumbnailListener();

            labelsVisible = true;
        }

        public void releaseLoaders() {
            for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
                loader.release();
            }
        }

        public void setLabelVisibility(boolean visible) {
            labelsVisible = visible;
            for (View view : entryViews) {
                view.findViewById(R.id.text).setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public VideoEntry getItem(int position) {
            return entries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            VideoEntry entry = entries.get(position);

            // There are three cases here
            if (view == null) {
                // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
                view = inflater.inflate(R.layout.video_list_item, parent, false);
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                thumbnail.setTag(entry.videoId);
                thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
            } else {
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(thumbnail);
                if (loader == null) {
                    // 2) The view is already created, and is currently being initialized. We store the
                    //    current videoId in the tag.
                    thumbnail.setTag(entry.videoId);
                } else {
                    // 3) The view is already created and already initialized. Simply set the right videoId
                    //    on the loader.
                    thumbnail.setImageResource(R.drawable.loading_thumbnail);
                    loader.setVideo(entry.videoId);
                }
            }
            TextView label = ((TextView) view.findViewById(R.id.text));
            label.setText(entry.text);
            label.setVisibility(labelsVisible ? View.VISIBLE : View.GONE);
            return view;
        }

        private final class ThumbnailListener implements
                YouTubeThumbnailView.OnInitializedListener,
                YouTubeThumbnailLoader.OnThumbnailLoadedListener {

            @Override
            public void onInitializationSuccess(
                    YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
                loader.setOnThumbnailLoadedListener(this);
                thumbnailViewToLoaderMap.put(view, loader);
                view.setImageResource(R.drawable.loading_thumbnail);
                String videoId = (String) view.getTag();
                loader.setVideo(videoId);
            }

            @Override
            public void onInitializationFailure(
                    YouTubeThumbnailView view, YouTubeInitializationResult loader) {
                view.setImageResource(R.drawable.no_thumbnail);
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
                view.setImageResource(R.drawable.no_thumbnail);
            }
        }

    }

    public static final class VideoFragment extends YouTubePlayerFragment
            implements OnInitializedListener {

        private YouTubePlayer player;
        private String videoId;

        public static VideoFragment newInstance() {
            return new VideoFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initialize(DeveloperKey.DEVELOPER_KEY, this);
        }

        @Override
        public void onDestroy() {
            if (player != null) {
                player.release();
            }
            super.onDestroy();
        }

        public void setVideoId(String videoId) {
            if (videoId != null && !videoId.equals(this.videoId)) {
                this.videoId = videoId;
                if (player != null) {
                    player.cueVideo(videoId);
                }
            }
        }

        public void pause() {
            if (player != null) {
                player.pause();
            }
        }

        @Override
        public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean restored) {
            this.player = player;
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            player.setOnFullscreenListener((Class3) getActivity());
            if (!restored && videoId != null) {
                player.cueVideo(videoId);
            }
        }

        @Override
        public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
            this.player = null;
        }

    }

    private static final class VideoEntry {
        private final String text;
        private final String videoId;

        public VideoEntry(String text, String videoId) {
            this.text = text;
            this.videoId = videoId;
        }
    }

    // Utility methods for layouting.

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static void setLayoutSize(View view, int width, int height) {
        LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

}
