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
public final class Class8 extends Activity implements OnFullscreenListener {

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

        setContentView(R.layout.video_list_class8);

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
            list.add(new VideoEntry("Class 8 English Chapter 1 Part 1", "CqJroxYNtBs"));
            list.add(new VideoEntry("Class 8 English Chapter 1 Part 2", "3pJn-vXlmAA"));
            list.add(new VideoEntry("Class 8 English Chapter 2 Part 1", "hh-GoxLWRHU"));
            list.add(new VideoEntry("Class 8 English Chapter 2 Part 2", "sYftA_Wz83g"));
            list.add(new VideoEntry("Class 8 English Chapter 3 Part 1", "sifBmv5gLBQ"));
            list.add(new VideoEntry("Class 8 English Chapter 3 Part 2", "bKKtKzlQMM0"));
            list.add(new VideoEntry("Class 8 English Chapter 4 Part 1", "Z9niVMZnTCo"));
            list.add(new VideoEntry("Class 8 English Chapter 4 Part 2", "UqU7PoGOGcc"));
            list.add(new VideoEntry("Class 8 English Chapter 5 Part 1", "NZq9ku41Hww"));
            list.add(new VideoEntry("Class 8 English Chapter 5 Part 2", "KM05FnZ5LLI"));
            list.add(new VideoEntry("Class 8 English Chapter 6", "d4XqOvHtIKI"));
            list.add(new VideoEntry("Class 8 English Chapter 7 Part 1", "YC114y6dERU"));
            list.add(new VideoEntry("Class 8 English Chapter 7 Part 1", "OfDOqMZCW6A"));
            list.add(new VideoEntry("Class 8 English Chapter 8", "vGKbP_Dtevs"));
            list.add(new VideoEntry("Class 8 English Chapter 9", "1PGKvFsGF4Y"));
            list.add(new VideoEntry("Class 8 English Chapter 10", "h-LWfUsqHYQ"));
            list.add(new VideoEntry("Class 8 English Chapter 11 Part 1", "FD_fFDJbBio"));
            list.add(new VideoEntry("Class 8 English Chapter 11 Part 2", "iQB4V6BgMgY"));
            list.add(new VideoEntry("Class 8 English Chapter 12", "xqgret5xATg"));
            list.add(new VideoEntry("Class 8 English Chapter 13", "DxIGmCPZNgY"));
            list.add(new VideoEntry("Class 8 English Chapter 14", "AZ1MYaOmKkE"));
            list.add(new VideoEntry("Class 8 English Chapter 16", "7Kz61E4Wb-c"));
            list.add(new VideoEntry("Class 8 English Chapter 17 Part 1", "pz4cumrfxFM"));
            list.add(new VideoEntry("Class 8 English Chapter 17 Part 1", "U9Hbh270rwo"));
            list.add(new VideoEntry("Class 8 English Chapter 18", "FnpB4XNpPAY"));
            list.add(new VideoEntry("Class 8 English Chapter 19", "gb6g7mll3b8"));

            list.add(new VideoEntry("Class 8 Science Chapter 1 Part 1", "uu2DS1LCFoM"));
            list.add(new VideoEntry("Class 8 Science Chapter 1 Part 2", "-9A3hGU95bc"));
            list.add(new VideoEntry("Class 8 Science Chapter 1 Part 3", "9rwMTCGQdaM"));
            list.add(new VideoEntry("Class 8 Science Chapter 2 Part 1", "fvgLlUFRJuY"));
            list.add(new VideoEntry("Class 8 Science Chapter 2 Part 2", "VhcFj9XoXeQ"));
            list.add(new VideoEntry("Class 8 Science Chapter 2 Part 3", "VRdolSn-hfA"));
            list.add(new VideoEntry("Class 8 Science Chapter 2 Part 4", "DEiKC63SulM"));
            list.add(new VideoEntry("Class 8 Science Chapter 2 Part 4", "dr-6x_lF0wo"));
            list.add(new VideoEntry("Class 8 Science Chapter 2 Part 5", "Ie_soDHY6a0"));
            list.add(new VideoEntry("Class 8 Science Chapter 3 Part 1", "KhhhyAFjaJQ"));
            list.add(new VideoEntry("Class 8 Science Chapter 3 Part 2", "V5JsiLTHjls"));
            list.add(new VideoEntry("Class 8 Science Chapter 3 Part 3", "HTAGO6lt2vI"));
            list.add(new VideoEntry("Class 8 Science Chapter 3 Part 4", "5EsshK5ch3Y"));
            list.add(new VideoEntry("Class 8 Science Chapter 3 Part 5", "ab94fJA_JLU"));
            list.add(new VideoEntry("Class 8 Science Chapter 3 Part 6", "kThYCmrsapM"));
            list.add(new VideoEntry("Class 8 Science Chapter 3 Part 7", "SLWyHJHDzX0"));
            list.add(new VideoEntry("Class 8 Science Chapter 4 Part 1", "9wAYXK8YRVI"));
            list.add(new VideoEntry("Class 8 Science Chapter 4 Part 2", "E_WHRh5Pd-U"));
            list.add(new VideoEntry("Class 8 Science Chapter 4 Part 3", "oGlNDRs3-g4"));
            list.add(new VideoEntry("Class 8 Science Chapter 4 Part 4", "JSvOr0ZQ3Vg"));
            list.add(new VideoEntry("Class 8 Science Chapter 4 Part 5", "IN6PQHHx43A"));
            list.add(new VideoEntry("Class 8 Science Chapter 4 Part 6", "e1sUyqTqoGI"));
            list.add(new VideoEntry("Class 8 Science Chapter 5 Part 1", "EcJ-PtR6Inw"));
            list.add(new VideoEntry("Class 8 Science Chapter 5 Part 2", "TLP4LHhhH4U"));
            list.add(new VideoEntry("Class 8 Science Chapter 5 Part 3", "jHVJBC3gpsg"));
            list.add(new VideoEntry("Class 8 Science Chapter 6 Part 1", "nO0fRa1IG50"));
            list.add(new VideoEntry("Class 8 Science Chapter 6 Part 2", "JmetlhG72Tc"));
            list.add(new VideoEntry("Class 8 Science Chapter 6 Part 3", "nFyeQFgsdO4"));
            list.add(new VideoEntry("Class 8 Science Chapter 6 Part 4", "Le3M5Es1EHg"));
            list.add(new VideoEntry("Class 8 Science Chapter 7 Part 1", "6TrKdaULVd4"));
            list.add(new VideoEntry("Class 8 Science Chapter 7 Part 2", "8vqLRNEnzr0"));
            list.add(new VideoEntry("Class 8 Science Chapter 7 Part 3", "4a6wjUVdjiM"));
            list.add(new VideoEntry("Class 8 Science Chapter 8 Part 1", "_1-GoWsHO6c"));
            list.add(new VideoEntry("Class 8 Science Chapter 8 Part 2", "sUk27GkqMOU"));
            list.add(new VideoEntry("Class 8 Science Chapter 8 Part 3", "Wsqrbe3BW4E"));
            list.add(new VideoEntry("Class 8 Science Chapter 8 Part 4", "JwUGITUTTis"));
            list.add(new VideoEntry("Class 8 Science Chapter 9 Part 1", "PIoV6iDKVmQ"));
            list.add(new VideoEntry("Class 8 Science Chapter 9 Part 2", "MUQMeYgZU4w"));
            list.add(new VideoEntry("Class 8 Science Chapter 9 Part 3", "gY5yR52f4X8"));
            list.add(new VideoEntry("Class 8 Science Chapter 9 Part 4", "rO9Bm0aLHtE"));
            list.add(new VideoEntry("Class 8 Science Chapter 10 Part 1", "1eDUP5Yj2s8"));
            list.add(new VideoEntry("Class 8 Science Chapter 10 Part 2", "sgwyErFApVY"));
            list.add(new VideoEntry("Class 8 Science Chapter 10 Part 3", "n-gH6Q7CN8g"));
            list.add(new VideoEntry("Class 8 Science Chapter 10 Part 4", "jQDvmxddjRk"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 1", "OW1zuYNrPT0"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 2", "ng7siO9byyM"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 3", "MCTLJfgSJYs"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 4", "VXgQS28D6tA"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 5", "t2YiLAwiZAU"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 6", "FtB6PXmtAMI"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 7", "xWGlWRhzfHA"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 8", "qoI857MOxqs"));
            list.add(new VideoEntry("Class 8 Science Chapter 11 Part 9", "7t9xn-67f-Q"));
            list.add(new VideoEntry("Class 8 Science Chapter 12 Part 1", "X9dzPt5ynFE"));
            list.add(new VideoEntry("Class 8 Science Chapter 12 Part 1", "-XSxa9-6rew"));
            list.add(new VideoEntry("Class 8 Science Chapter 12 Part 2", "biRaYkw-gHI"));
            list.add(new VideoEntry("Class 8 Science Chapter 12 Part 3", "QMl9zsvYX8Y"));
            VIDEO_LIST = Collections.unmodifiableList(list);
        }
        private PageAdapter adapter;

        private View videoBox;

        private String title3;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Class8 obj = new Class8();
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
            player.setOnFullscreenListener((Class8) getActivity());
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
