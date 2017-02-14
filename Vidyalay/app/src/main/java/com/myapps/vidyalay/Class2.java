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
public final class Class2 extends Activity implements OnFullscreenListener {

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

        setContentView(R.layout.video_list_class2);

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
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 1", "VCd_oKvudi0"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 2", "_e5GOln8L4o"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 3", "E9PXx4YDX0A"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 4", "g-NV-qA6tvo"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 5", "COUhyWJR2nE"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 6", "XrmdIBic7Sg"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 7", "zfAdmL50nWM"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 8", "hF2sVWZ3uno"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 9", "qT-UHeXfna4"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 10", "vqMXCqmymjA"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 11", "n88UAczVN3M"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 12", "reAsCEu_ju8"));
			list.add(new VideoEntry("Class 2 English Chapter 1 Part 13", "-bQvbPIcqdE"));
			list.add(new VideoEntry("Class 2 English Chapter 2 Part 1", "T-Bf6xQtQl4"));
			list.add(new VideoEntry("Class 2 English Chapter 3 Part 1", "opr5g4A1Uhw"));
			list.add(new VideoEntry("Class 2 English Chapter 3 Part 2", "0gupFbEq9tg"));
			list.add(new VideoEntry("Class 2 English Chapter 3 Part 3", "dG6GtVzGoXc"));
			list.add(new VideoEntry("Class 2 English Chapter 4 Part 1", "mAKiJljoVqE"));
			list.add(new VideoEntry("Class 2 English Chapter 4 Part 2", "dexwn27oNS4"));
			list.add(new VideoEntry("Class 2 English Chapter 5 Part 1", "N_l0jwUUBLI"));
			list.add(new VideoEntry("Class 2 English Chapter 5 Part 2", "XpWXl3hmtwo"));
			list.add(new VideoEntry("Class 2 English Chapter 6 Part 1", "9BSNhHYzTH8"));
			list.add(new VideoEntry("Class 2 English Chapter 6 Part 2", "a1SpY_WRMoY"));
			list.add(new VideoEntry("Class 2 English Chapter 6 Part 3", "TtYJ0lssci0"));
			list.add(new VideoEntry("Class 2 English Chapter 6 Part 4", "qlYuEiuTu6A"));
			list.add(new VideoEntry("Class 2 English Chapter 6 Part 5", "0CinFVyi-H0"));
			list.add(new VideoEntry("Class 2 English Chapter 7 Part 1", "3uMfg5ad23k"));
			list.add(new VideoEntry("Class 2 English Chapter 8 Part 1", "hFpqXgkEl-I"));
			list.add(new VideoEntry("Class 2 English Chapter 8 Part 2", "Nt8gbfBnhSI"));
			list.add(new VideoEntry("Class 2 English Chapter 8 Part 3", "9v7CfOOU6tk"));
			list.add(new VideoEntry("Class 2 English Chapter 9 Part 1", "oarRGjK_Fo0"));
			list.add(new VideoEntry("Class 2 English Chapter 10 Part 1", "Y-_3YJtJ06M"));
			list.add(new VideoEntry("Class 2 English Chapter 10 Part 2", "wBgaP01ycYQ"));
			list.add(new VideoEntry("Class 2 English Chapter 11 Part 1", "YCHiTKhYrF4"));
			list.add(new VideoEntry("Class 2 English Chapter 11 Part 2", "_TOWm_-MH7A"));
			list.add(new VideoEntry("Class 2 English Chapter 12 Part 1", "JkJLhMHC-xk"));
			list.add(new VideoEntry("Class 2 English Phonetic's Video Part 1", "5AARiFcUMWc"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 1", "dDFkV7SPTVo"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 2", "AByN5K3j4G0"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 3", "3d28V4p_22E"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 4", "EdkcXofmPR8"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 5", "3mS_oizjBZc"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 6", "ACmY9otiI2s"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 7", "rrRbYRbxTeE"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 8 Part 1", "XTHw7zHBxFc"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 8 Part 2", "9x_4aqLnGBs"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 9", "B-g-pNNzvUA"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 10", "IkJ8H5GP2D4"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 11", "3iuxGnC4Rpc"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 12", "mTEXEkELCWk"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 13", "n2S2FoTJA3A"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 14 Part 1", "oek4TjmbK4Q"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 14 Part 2", "mOMVwZgHrfI"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 15", "a101DNiXaT0"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 16", "TB761e8raDY"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 17 Part 1", "QWZ6NLOvsRk"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 17 Part 2", "ajXSnZhRgc8"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 18", "ump_eELHvEU"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 19 Part 1", "mfcbXzPgNcU"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 19 Part 2", "UKS7-G4pa1o"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 20", "9X0axC4TjZY"));
			list.add(new VideoEntry("Class 2 Hindi Chapter 21", "vkVmdUMmOUw"));

			list.add(new VideoEntry("Class 2 Maths Chapter 3 Part 4", "89UxBNmCPv0"));
			list.add(new VideoEntry("Class 2 Maths Chapter 4 Part 1", "cFrmoFWTS6w"));
			list.add(new VideoEntry("Class 2 Maths Chapter 4 Part 1", "fxk60Afglic"));
			list.add(new VideoEntry("Class 2 Maths Chapter 4 Part 2.", "kvmW2C2BOc8"));
			list.add(new VideoEntry("Class 2 Maths Chapter 5 Part 1", "yN5-oCgIEFk"));
			list.add(new VideoEntry("Class 2 Maths Chapter 6 Part 1", "MdpRpGL6hUg"));
			list.add(new VideoEntry("Class 2 Maths Chapter 7 Part 1", "w8xdTPeo2yc"));
			list.add(new VideoEntry("Class 2 Maths Chapter 8 Part 1", "5cpZKSOTQj8"));
			list.add(new VideoEntry("Class 2 Maths Chapter 8 Part 2", "_4636ThMJtw"));
			list.add(new VideoEntry("Class 2 Maths Chapter 9 Part 1", "fS0c4V7MlgA"));
			list.add(new VideoEntry("Class 2 Maths Chapter 10 Part 1", "dqwev8SNha0"));
			list.add(new VideoEntry("Class 2 Maths Chapter 10 Part 2", "TuTVzZLkKiY"));
			list.add(new VideoEntry("Class 2 Maths Chapter 11 Part 1", "J_VtsrWUM40"));
			list.add(new VideoEntry("Class 2 Maths Chapter 11 Part 2", "16i9Nb97buo"));
			list.add(new VideoEntry("Class 2 Maths Chapter 13 Part 1", "BF4tlm3tHVc"));
			list.add(new VideoEntry("Class 2 Maths Chapter 13 Part 2", "e5nnmEPwKLk"));
			list.add(new VideoEntry("Class 2 Maths Chapter 14 Part 1", "eb8IHjNYU20"));
			list.add(new VideoEntry("Class 2 Maths Chapter 14 Part 2", "DBC8_sR8q3w"));
			list.add(new VideoEntry("Class 2 Maths Chapter 15 Part 1", "GHnpZFrpiPc"));
			list.add(new VideoEntry("Class 2 Maths Chapter 15 Part 2", "iPAW2tS8bzo"));
			list.add(new VideoEntry("Class 2 Maths Chapter 16 Part 1", "W39uLbvBKkY"));
			list.add(new VideoEntry("Class 2 Maths Chapter 17 Part 1", "M851PxDvDUI"));
			list.add(new VideoEntry("Class 2 Maths Chapter 17 Part 2", "Cv5AXcGOWpY"));
            VIDEO_LIST = Collections.unmodifiableList(list);
        }
        private PageAdapter adapter;

        private View videoBox;

        private String title3;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Class2 obj = new Class2();
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
            player.setOnFullscreenListener((Class2) getActivity());
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
