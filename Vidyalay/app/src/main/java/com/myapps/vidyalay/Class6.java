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
public final class Class6 extends Activity implements OnFullscreenListener {

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

        setContentView(R.layout.video_list_class6);

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
			list.add(new VideoEntry("Class 6 English Chapter 1 Part 1", "4gq2D0bYyXE"));
			list.add(new VideoEntry("Class 6 English Chapter 1 Part 2", "mR5PWvUCWTU"));
			list.add(new VideoEntry("Class 6 English Chapter 2 Part 1", "VAlWBM_WduQ"));
			list.add(new VideoEntry("Class 6 English Chapter 2 Part 2", "1VEbE1JHn6A"));
			list.add(new VideoEntry("Class 6 English Chapter 3", "jPxReGzB2Pg"));
			list.add(new VideoEntry("Class 6 English Chapter 4 Part 1", "7jIjbRA90EE"));
			list.add(new VideoEntry("Class 6 English Chapter 4 Part 2", "FA7ezx699Js"));
			list.add(new VideoEntry("Class 6 English Chapter 5", "rUhl1MU0eCw"));
			list.add(new VideoEntry("Class 6 English Chapter 6", "5LbQwLzPkD4"));
			list.add(new VideoEntry("Class 6 English Chapter 6 Part 1", "CERXMI8hexc"));
			list.add(new VideoEntry("Class 6 English Chapter 7 Part 1", "UUwzI09GqB8"));
			list.add(new VideoEntry("Class 6 English Chapter 7 Part 2", "l83kPVsy9pI"));
			list.add(new VideoEntry("Class 6 English Chapter 8 Part 1", "wNMIsLFrYKs"));
			list.add(new VideoEntry("Class 6 English Chapter 8 Part 2", "S-R6QUkG10Y"));
			list.add(new VideoEntry("Class 6 English Chapter 9", "AmAaifvpEzE"));
			list.add(new VideoEntry("Class 6 English Chapter 10 Part 1", "5E2compP-zY"));
			list.add(new VideoEntry("Class 6 English Chapter 10 Part 2", "H9NWJZTHUmw"));
			list.add(new VideoEntry("Class 6 English Chapter 11 Part 1", "FZavS6YOAqI"));
			list.add(new VideoEntry("Class 6 English Chapter 11 Part 2", "9MdoaYfkcIM"));
			list.add(new VideoEntry("Class 6 English Chapter 13 Part 1", "bGTlJO9Y1aY"));
			list.add(new VideoEntry("Class 6 English Chapter 13 Part 2", "v-UdRWOmnW8"));
			list.add(new VideoEntry("Class 6 English Chapter 14 Part 1", "3fBRA9YRsU8"));
			list.add(new VideoEntry("Class 6 English Chapter 14 Part 2", "cR7G24WVmqA"));
			list.add(new VideoEntry("Class 6 English Chapter 15", "eFIjvTTJCww"));
			list.add(new VideoEntry("Class 6 English Chapter 16 Part 1", "CwIn5fPQTcw"));
			list.add(new VideoEntry("Class 6 English Chapter 16 Part 2", "CwzIAF1I6gY"));
			list.add(new VideoEntry("Class 6 English Chapter 17 Part 1", "hnIJw2GUplM"));

			list.add(new VideoEntry("Class 6 Geography Chapter 1 Part 1", "JOb7d34nLis"));
			list.add(new VideoEntry("Class 6 Geography Chapter 2 Part 1", "Y1p4CywoNps"));
			list.add(new VideoEntry("Class 6 Geography Chapter 3 Part 1", "_2zZwHbmc-k"));
			list.add(new VideoEntry("Class 6 Geography Chapter 4 Part 1", "SrZ8WNDJBTM"));
			list.add(new VideoEntry("Class 6 Geography Chapter 4 Part 2", "iHgBLM-n22A"));
			list.add(new VideoEntry("Class 6 Geography Chapter 5 Part 1", "TwkZ_Lp1TeU"));
			list.add(new VideoEntry("Class 6 Geography Chapter 6 Part 1", "98Xw9yzYcvA"));
			list.add(new VideoEntry("Class 6 Geography Chapter 6 Part 2", "1mrHa-FqIWo"));
			list.add(new VideoEntry("Class 6 Geography Chapter 7 Part 1", "HpqmZFDJWBI"));
			list.add(new VideoEntry("Class 6 Geography Chapter 8 Part 1", "5m91D78BTh4"));
			list.add(new VideoEntry("Class 6 Geography Chapter 8 Part 2", "UJvPUoGkuwM"));
			list.add(new VideoEntry("Class 6 Geography Chapter 9 Part 1", "-4uY7_TOP50"));
			list.add(new VideoEntry("Class 6 Geography Chapter 10 Part 1", "UFb_-HV-Dac"));
			list.add(new VideoEntry("Class 6 Geography Chapter 10 Part 2", "o0bhWL1Z-fI"));

			list.add(new VideoEntry("Class 6 Hindi Chapter 1 Part 1", "EZf0tvmH-UA"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 2 Part 1", "p9ZQZut1q9c"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 3 Part 1", "WBuiPmeRDgs"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 4 Part 2", "xWTVZxZVq04"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 5 Part 1", "IcNkEfz4hcw"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 6 Part 1", "x71Hn94N20Y"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 7 Part 1", "4gQlb7uCnpI"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 8 Part 1", "3AsZ3sKzeu4"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 9 Part 1", "w8DBwiPHq8Q"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 10 Part 1", "gGtDNgP3uTI"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 11 Part 1", "ttvjKuOlccY"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 12 Part 1", "uPnSnJ3BmKI"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 13 Part 1", "_rbYVtgLUHc"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 14 Part 1", "khAv9aQGTto"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 15 Part 1", "xAjMDK-_j5Q"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 16 Part 1", "lRWInFMqnv4"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 17 Part 1", "peotny70M2A"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 19 Part 1", "QO7RNLH7naM"));
			list.add(new VideoEntry("Class 6 Hindi Chapter 20 Part 1", "b4RogsIdBXM"));

			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 1 Part 1", "apWwE76hirI"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 1 Part 2", "BT-g4k6Gorw"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 1 Part 3", "nhAzfie7O4s"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 1 Part 4", "P2FVsQYK2jM"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 2 Part 1", "phYF1l9l7fo"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 2 Part 2", "yURmH0RBEx8"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 2 Part 3", "VMdF_D1l9SY"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 2 Part 4", "jWoo3ZGPk1I"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 2 Part 5", "lsj4wprYEuE"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 3 Part 1", "5b-dz0JJgNI"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 3 Part 2", "AQvgGQfc34M"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 3 Part 3", "a27u8tnjYME"));
			list.add(new VideoEntry("Class 6 Maths Algebra Chapter 3 Part 4", "Cmh6Y2Crqos"));

			list.add(new VideoEntry("Class 6 Maths Chapter 1 Part 1", "S3o01DHMrvw"));
			list.add(new VideoEntry("Class 6 Maths Chapter 1 Part 2", "iF0KiQ2pXZ4"));
			list.add(new VideoEntry("Class 6 Maths Chapter 1 Part 3", "IlBgqLsDjPQ"));
			list.add(new VideoEntry("Class 6 Maths Chapter 2 Part 1", "8VwYg8Nj4gg"));
			list.add(new VideoEntry("Class 6 Maths Chapter 2 Part 2", "BrGzx14ooBU"));
			list.add(new VideoEntry("Class 6 Maths Chapter 2 Part 3", "bNB62NY_V5A"));
			list.add(new VideoEntry("Class 6 Maths Chapter 3 Part 1", "9bhDpuE56t4"));
			list.add(new VideoEntry("Class 6 Maths Chapter 3 Part 2", "xQEEPKGCxdA"));
			list.add(new VideoEntry("Class 6 Maths Chapter 3 Part 3", "0ivh9cdhndc"));
			list.add(new VideoEntry("Class 6 Maths Chapter 3 Part 4", "YROJ_q-xdTk"));
			list.add(new VideoEntry("Class 6 Maths Chapter 3 Part 5", "HYWAd2c7zkE"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 1", "HYtq8EZ4RYk"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 10", "l5BGT93TlFg"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 11", "P8voffRz9SE"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 12", "_FHxCJlpnPQ"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 2", "WEpQ9nQkPTU"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 3", "qcYFCYaR5lA"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 4", "oBYK4IqX1tU"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 5", "ZpU0HxeqCKs"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 6", "sCVHl7voRJA"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 7", "BqMJ_AFC_NE"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 8", "YXHDToa2tRw"));
			list.add(new VideoEntry("Class 6 Maths Chapter 4  Part 9", "syU7PqqMwFc"));
			list.add(new VideoEntry("Class 6 Maths Chapter 5", "3PEVDC-Q1Oo"));

			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 1", "mcp-slYYxKo"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 2 Part 1", "cbBpWWd-H7o"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 2 Part 2", "DisGOcDK1sE"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 3 Part 1", "4oMs9Cu8DhI"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 3 Part 2", "X-opyxPKUpU"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 4 Part 1", "j_L9g1oLmvw"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 4 Part 2", "XJGcg5Yprzk"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 5 Part 1", "TKt5qJC8BkI"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 5 Part 2", "BdUmFCt6sAg"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 6 Part 1", "Q5TvWYvhct8"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 6 Part 2", "yjYtogoBvlM"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 6 Part 3", "RSYCfX51zwc"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 7 Part 1", "oNYglfoEoZA"));
			list.add(new VideoEntry("Class 6 Maths Geometry Chapter 7 Part 2", "Rs606BfeV3Q"));
			list.add(new VideoEntry("Class 6 Maths geometry Chapter 5 Part 1", "P7uqWr3NlYM"));

			list.add(new VideoEntry("Class 6 Science Chapter 1 Part 1", "iP0zyOxEGSY"));
			list.add(new VideoEntry("Class 6 Science Chapter 1 Part 1", "TLLLeBoPiOg"));
			list.add(new VideoEntry("Class 6 Science Chapter 1 Part 2", "3N83Yhfr7ZY"));
			list.add(new VideoEntry("Class 6 Science Chapter 1 Part 2", "AvVIwoqlf0Q"));
			list.add(new VideoEntry("Class 6 Science Chapter 2 Part 1", "KUF2sQQBnvM"));
			list.add(new VideoEntry("Class 6 Science Chapter 2 Part 2", "1Z94wgWEGiU"));
			list.add(new VideoEntry("Class 6 Science Chapter 3 Part 1", "TiNkbQfLQLk"));
			list.add(new VideoEntry("Class 6 Science Chapter 3 Part 2", "zm8Kj_1L6Ok"));
			list.add(new VideoEntry("Class 6 Science Chapter 3 Part 3", "JKPY11IjcE0"));
			list.add(new VideoEntry("Class 6 Science Chapter 4 Part 1", "nmHUZm2GYIc"));
			list.add(new VideoEntry("Class 6 Science Chapter 4 Part 2", "uKBdFHQGKWg"));
			list.add(new VideoEntry("Class 6 Science Chapter 5", "2g8068l3x9c"));
			list.add(new VideoEntry("Class 6 Science Chapter 6 Part 1", "RvLGi9aCNns"));
			list.add(new VideoEntry("Class 6 Science Chapter 6 Part 2", "zYVXpUpwB4U"));
			list.add(new VideoEntry("Class 6 Science Chapter 6 Part 3", "9B3Ye1m_DM4"));
			list.add(new VideoEntry("Class 6 Science Chapter 6 Part 4", "uAcS8zgzJbk"));
			list.add(new VideoEntry("Class 6 Science Chapter 6 Part 5", "6ZbGRXcIFXw"));
			list.add(new VideoEntry("Class 6 Science Chapter 6 Part 5", "J3yEHi5z7mY"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 1", "zKrmNKHWGFg"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 2", "HcPCCeuEbiA"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 3", "NiV234LBzow"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 4", "w2HC85hS678"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 5", "feqn5apgsXY"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 6", "eticgTw8o4Q"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 7", "7l0Je9ug2RE"));
			list.add(new VideoEntry("Class 6 Science Chapter 7 Part 8", "Z6wSNpZymB4"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 1", "VyzwGaPPCdo"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 10", "--y2bqMd6mk"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 2", "_Cb4lxIsbz4"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 3", "nCz3tVpWvU8"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 4", "AFoPpzTzP_8"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 5", "s-XPFtrw3Qs"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 6", "O0-LgCLtlb8"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 7", "v2rEms3hIzY"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 8", "xD7A8KdRlsY"));
			list.add(new VideoEntry("Class 6 Science Chapter 8 Part 9", "fxH7pfgmBtU"));
			list.add(new VideoEntry("Class 6 Science Chapter 9 Part 1", "n_F1ATv0XGg"));
			list.add(new VideoEntry("Class 6 Science Chapter 9 Part 2", "RkavaRrxODk"));
			list.add(new VideoEntry("Class 6 Science Chapter 10 Part 1", "JMtr8pNDbm8"));
			list.add(new VideoEntry("Class 6 Science Chapter 10 Part 2", "bJeoZjY-z2c"));
			list.add(new VideoEntry("Class 6 Science Chapter 10 Part 3", "hytXI5gcxvs"));
			list.add(new VideoEntry("Class 6 Science Chapter 11 Part 1", "cgDflRGycKE"));
			list.add(new VideoEntry("Class 6 Science Chapter 11 Part 2", "lEekkRwQVJ4"));
			list.add(new VideoEntry("Class 6 Science Chapter 12 Part 1", "HLZ6YBQjy3k"));
			list.add(new VideoEntry("Class 6 Science Chapter 12 Part 2", "qjeNq8k6geE"));
			list.add(new VideoEntry("Class 6 Science Chapter 12 Part 3", "BL1PVh2QjiQ"));
            VIDEO_LIST = Collections.unmodifiableList(list);
        }
        private PageAdapter adapter;

        private View videoBox;

        private String title3;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Class6 obj = new Class6();
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
            player.setOnFullscreenListener((Class6) getActivity());
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
