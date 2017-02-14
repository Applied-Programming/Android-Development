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
public final class Class4 extends Activity implements OnFullscreenListener {

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

        setContentView(R.layout.video_list_class4);

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
			list.add(new VideoEntry("Class 4 EVS Chapter 1 Part 1", "u3ctiasD3ak"));
			list.add(new VideoEntry("Class 4 EVS Chapter 1 Part 2", "BWT7SX4XMU0"));
			list.add(new VideoEntry("Class 4 EVS Chapter 1 Part 3", "Dx052euaEzo"));
			list.add(new VideoEntry("Class 4 EVS Chapter 1 Part 4", "iYRjovATux4"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 1", "uWChmU7R_Bk"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 2", "uCGfgWzYZIg"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 3", "0V8DWlBjP3M"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 4", "lubDQaCqOxk"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 5", "5JNMONcuwMo"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 6", "zceTNbxvhGc"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 7", "Z247i4egrgA"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 8", "evyAjVO9v90"));
			list.add(new VideoEntry("Class 4 EVS Chapter 2 Part 9", "swi-xhtUgA4"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 1", "PsOgl0II3xI"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 2", "GBIjrVltDsU"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 3", "MyO-azwkGdU"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 4", "aNEkpe6soT0"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 5", "ixPjOGCiSNw"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 6", "ebKum53uG0g"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 7", "bskS4N3LGXc"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 8", "Fq9oQssn20E"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 9", "GlIbwbkiu8k"));
			list.add(new VideoEntry("Class 4 EVS Chapter 3 Part 10", "tmaeucbL97c"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 1", "Z9gPayQOwGc"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 2", "3RRLtWeQidU"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 3", "0WbKpSuuEac"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 4", "o8FWUgtd1Zk"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 5", "DizTEnVMNOs"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 6", "IkaqZ98hPbc"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 7", "Doyw3TX-AR0"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 8", "dP4Cb2Yyj5w"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 9", "dNOVKUtq2dk"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 10", "ZVVCfmrxNwQ"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 11", "yKYR7yOACUM"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 12", "0-978f0d770"));
			list.add(new VideoEntry("Class 4 EVS Chapter 4 Part 13", "5sMvoWABsDk"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 1", "WYLFWyjldGI"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 2", "5OfmuJ1RTCE"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 3", "xJ2MNorwAXg"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 4", "AyIl8avBJY8"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 5", "BJqUlKDfo3U"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 6", "-AvpB7kxYwc"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 7", "PGEqR6Ac0fw"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 8", "kQf3e_ro6qY"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 9", "ZkAkjnnFW1s"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 10", "fUJjxB0FOiU"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 11", "hbd3vVWIYdU"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 12", "FZenbdqRsJM"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 13", "EiMUWZYXYuc"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 14", "v_CYa0xSIhM"));
			list.add(new VideoEntry("Class 4 EVS Chapter 5 Part 15", "G-CmpFjgesw"));
			list.add(new VideoEntry("Class 4 EVS Chapter 6 Part 1", "MYodxhEiiNY"));
			list.add(new VideoEntry("Class 4 EVS Chapter 6 Part 2", "Qt-6kgCzknY"));
			list.add(new VideoEntry("Class 4 EVS Chapter 6 Part 3", "G70iIeQlCZs"));
			list.add(new VideoEntry("Class 4 EVS Chapter 6 Part 4", "L4sYQ9klUfE"));
			list.add(new VideoEntry("Class 4 EVS Chapter 6 Part 5", "XW178zI1pSo"));
			list.add(new VideoEntry("Class 4 EVS Chapter 6 Part 6", "61bw-U4W_RQ"));
			list.add(new VideoEntry("Class 4 EVS Chapter 7 Part 1", "4DyKnKv3Gng"));
			list.add(new VideoEntry("Class 4 EVS Chapter 7 Part 2", "tjUpPx2rF1U"));
			list.add(new VideoEntry("Class 4 EVS Chapter 7 Part 3", "avAGJvw8XkU"));
			list.add(new VideoEntry("Class 4 English Chapter 1", "hjik__yk8-M"));
			list.add(new VideoEntry("Class 4 English Chapter 3", "4_ZfubA3Nl4"));
			list.add(new VideoEntry("Class 4 English Chapter 4", "hzD8nu8JlXc"));
			list.add(new VideoEntry("Class 4 English Chapter 5", "MhiqxN4NjoI"));
			list.add(new VideoEntry("Class 4 English Chapter 6", "8jI9Z-3U8PI"));
			list.add(new VideoEntry("Class 4 English Chapter 7", "nALf_qrOklc"));
			list.add(new VideoEntry("Class 4 English Chapter 8 Part 1", "5FwaFteOsdo"));
			list.add(new VideoEntry("Class 4 English Chapter 8 Part 2", "ACFtPFX2wbw"));
			list.add(new VideoEntry("Class 4 English Chapter 8 Part 3", "jU9aJ2hjS_A"));
			list.add(new VideoEntry("Class 4 English Chapter 8 Part 4", "o0T_k5Rn5vo"));
			list.add(new VideoEntry("Class 4 English Chapter 9", "vEZr77rdOBk"));
			list.add(new VideoEntry("Class 4 English Chapter 11", "sia4GmjPu88"));
			list.add(new VideoEntry("Class 4 English Chapter 12", "tbimf7KrDaI"));
			list.add(new VideoEntry("Class 4 English Chapter 13 Part 1", "J9EgyZ4mCPQ"));
			list.add(new VideoEntry("Class 4 English Chapter 13 Part 2", "eYqMaLFSHOA"));
			list.add(new VideoEntry("Class 4 English Chapter 14", "2M9_Yvx3JM8"));
			list.add(new VideoEntry("Class 4 English Chapter 15", "_qh8ZGC0wxI"));
			list.add(new VideoEntry("Class 4 English Chapter 16", "3IhfsMqql7A"));
			list.add(new VideoEntry("Class 4 English Chapter 17 Part 1", "vn43lzyWNOM"));
			list.add(new VideoEntry("Class 4 English Chapter 17 Part 2", "xRtD2LB4Cw0"));
			list.add(new VideoEntry("Class 4 English Chapter 18 Part 1", "fcYMaWlXPug"));
			list.add(new VideoEntry("Class 4 English Chapter 18 Part 2", "IeTjNrQSf5g"));

			list.add(new VideoEntry("Class 4 Hindi Chapter 1", "Wg9CV_FogHA"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 2 Part 1", "Efo_y3xCRHQ"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 2 Part 2", "rXvyEZaHSqM"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 2 Part 2", "AINpbC5GU_w"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 3 Part 1", "ztayJoWDi8s"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 3 Part 2", "j0_9G1btCWw"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 4 Part 1", "-4plSqACb3E"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 4 Part 2", "ZCv2VknjsqQ"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 4 Part 2", "pf_b7VdQfGQ"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 4Part 1", "n9BeVJTOjYk"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 5 Part 1", "015OItGHoJM"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 5 Part 2", "epNdYw_wv6k"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 6 Part 1", "Rn1qyZjoaAQ"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 6 Part 2", "Sqq-Uj_ird4"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 6 Part 3", "7r6A9Ejfp2A"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 7 Part 1", "Ude53L-p72E"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 7 Part 2", "dIpeET4nME8"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 7 Part 3", "1gPDcPn-N4o"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 8 Part 1", "x8t6mbPW1DI"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 8 Part 2", "CvHfQjSwo0Q"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 9 Part 1", "8fcNS-gEt5g"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 9 Part 2", "rg1ioOAZMuM"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 9 Part 3", "08WTvjcy1bk"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 10 Part 1", "iz9oXo61DKQ"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 10 Part 2", "LQ5uSiLCM4A"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 11", "JwhW3S3emrU"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 12 Part 1", "Cib6WB3sDlU"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 12 Part 2", "q3ezlRc3Bqc"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 13 Part 1", "B4NO6sCe-R0"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 13 Part 2", "WckjocHz8yM"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 13 Part 3", "dM0LTkwJcXY"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 14", "SNm9jYgDVqE"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 15", "EziGy45V0gY"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 16", "nmDwjrrhaZQ"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 17 Part 1", "3XxZMDsAeUA"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 17 Part 2", "UzCyCSFyLn8"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 18 Part 1", "qP9jT_yTvQw"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 18 Part 2", "sqiUHySqXKk"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 18 Part 3", "yGzsGfg0U70"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 19 Part 1", "Ig-rrnrM1yw"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 19 Part 2", "P-StcQ7TY0A"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 20", "jp99HTxhhpU"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 21 Part 1", "3WFd0QjijHw"));
			list.add(new VideoEntry("Class 4 Hindi Chapter 21 Part 2", "GqpIPK-LF1M"));

			list.add(new VideoEntry("Class 4 Maths Chapter 1 Part 1", "p9k3PSsBpPc"));
			list.add(new VideoEntry("Class 4 Maths Chapter 1 Part 2", "RVzaCFi1N_g"));
			list.add(new VideoEntry("Class 4 Maths Chapter 1 Part 3", "T2mhHIsN7es"));
			list.add(new VideoEntry("Class 4 Maths Chapter 2 Part 1", "beSFVEGC85I"));
			list.add(new VideoEntry("Class 4 Maths Chapter 2 Part 2", "k4lvmoft-qo"));
			list.add(new VideoEntry("Class 4 Maths Chapter 3 Part 1", "ZV5za3Vj84o"));
			list.add(new VideoEntry("Class 4 Maths Chapter 3 Part 2", "KMIbCuX_0So"));
			list.add(new VideoEntry("Class 4 Maths Chapter 4 Part 1", "r60WDonCQZY"));
			list.add(new VideoEntry("Class 4 Maths Chapter 4 Part 2", "IihdCLuWT_k"));
			list.add(new VideoEntry("Class 4 Maths Chapter 5 Part 1", "YkzWx35hIjM"));
			list.add(new VideoEntry("Class 4 Maths Chapter 5 Part 2", "RYUtvMpKkYo"));
			list.add(new VideoEntry("Class 4 Maths Chapter 6 Part 1", "MbPF4BzEJpA"));
			list.add(new VideoEntry("Class 4 Maths Chapter 6 Part 2", "k42DyMWdCgg"));
			list.add(new VideoEntry("Class 4 Maths Chapter 7", "-E_lKRTjm9M"));
			list.add(new VideoEntry("Class 4 Maths Chapter 8 Part 1", "m-_VWjK3usQ"));
			list.add(new VideoEntry("Class 4 Maths Chapter 8 Part 2", "_6ppeOsedMI"));
			list.add(new VideoEntry("Class 4 Maths Chapter 9", "PyO1UCh6G9A"));
			list.add(new VideoEntry("Class 4 Maths Chapter 10", "BLhI3kQAfX8"));
			list.add(new VideoEntry("Class 4 Maths Chapter 11", "M9l1p13hiug"));
			list.add(new VideoEntry("Class 4 Maths Chapter 12 Part 1", "mocJ08aopyw"));
			list.add(new VideoEntry("Class 4 Maths Chapter 12 Part 2", "uG51g3kzle4"));
			list.add(new VideoEntry("Class 4 Maths Chapter 13 Part 1", "_HP5R73epPY"));
			list.add(new VideoEntry("Class 4 Maths Chapter 13 Part 2", "asZVtxrOn-g"));
			list.add(new VideoEntry("Class 4 Maths Chapter 14", "jws6pVR7o48"));
			list.add(new VideoEntry("Class 4 Maths Chapter 15", "1nGrfd4Bpdg"));
			list.add(new VideoEntry("Class 4 Maths Chapter 16 Part 1", "qzQr05uwk7k"));
			list.add(new VideoEntry("Class 4 Maths Chapter 16 Part 2", "G3MeM0YVwZU"));
			list.add(new VideoEntry("Class 4 Maths Chapter 17", "YxeGYpzwv0c"));
			list.add(new VideoEntry("Class 4 Maths Chapter 18", "gJ5IrxVeNLU"));

			list.add(new VideoEntry("Class 4 Science Chapter 1 Experiment Part 1", "CSDdOz4zXC4"));
			list.add(new VideoEntry("Class 4 Science Chapter 1 Experiment Part 2", "UJshgkJGBZs"));
			list.add(new VideoEntry("Class 4 Science Chapter 1 Part 1", "etUK3jGCj7A"));
			list.add(new VideoEntry("Class 4 Science Chapter 1 Part 2", "ipQTXOtEbKg"));
			list.add(new VideoEntry("Class 4 Science Chapter 1 Part 3", "U0b13EQ7nQ0"));
			list.add(new VideoEntry("Class 4 Science Chapter 1 Part 4", "n2pIoRHf2UI"));
			list.add(new VideoEntry("Class 4 Science Chapter 2 Part 1", "gZku_X-3DCs"));
			list.add(new VideoEntry("Class 4 Science Chapter 2 Part 2", "N-uQTt5YCaE"));
			list.add(new VideoEntry("Class 4 Science Chapter 3 Part 1", "m23EjPyJpUg"));
			list.add(new VideoEntry("Class 4 Science Chapter 3 Part 1", "f3xwr_HY4m4"));
			list.add(new VideoEntry("Class 4 Science Chapter 3 Part 2", "fR0XnnQLw6Q"));
			list.add(new VideoEntry("Class 4 Science Chapter 3 Part 2", "xaWaNbvnSzo"));
			list.add(new VideoEntry("Class 4 Science Chapter 3 Part 3", "8eFfASq9Ib8"));
			list.add(new VideoEntry("Class 4 Science Chapter 3 Part 4", "8_xAJlFHTlQ"));
			list.add(new VideoEntry("Class 4 Science Chapter 3 Part 5", "0CwnTfudnYs"));
			list.add(new VideoEntry("Class 4 Science Chapter 4 Part 1", "MLiyzVEJd7g"));
			list.add(new VideoEntry("Class 4 Science Chapter 4 Part 1", "oWvfN3hraSY"));
			list.add(new VideoEntry("Class 4 Science Chapter 4 Part 2", "w7oct0x0ORg"));
			list.add(new VideoEntry("Class 4 Science Chapter 4 Part 2", "-0zVowmDlbI"));
			list.add(new VideoEntry("Class 4 Science Chapter 4 Part 3", "DBkukmt6EYA"));
			list.add(new VideoEntry("Class 4 Science Chapter 4 Part 3", "Ed7occYX62Y"));
			list.add(new VideoEntry("Class 4 Science Chapter 5", "PAMR3I5Emow"));
			list.add(new VideoEntry("Class 4 Science Chapter 6 Experiment Part 1", "2ADirQ4nRjU"));
			list.add(new VideoEntry("Class 4 Science Chapter 6 Part 1", "3Vtyhp_JTfI"));
			list.add(new VideoEntry("Class 4 Science Chapter 6 Part 2", "sUWQbl2ZRfc"));
			list.add(new VideoEntry("Class 4 Science Chapter 6 Part 2", "DmTONUH0ZJ0"));
			list.add(new VideoEntry("Class 4 Science Chapter 6 Part 3", "3EMV-oSKI3w"));
			list.add(new VideoEntry("Class 4 Science Chapter 6 Part 3", "A0_X-2PuaDs"));
			list.add(new VideoEntry("Class 4 Science Chapter 7", "bWu7ww1duDI"));
			list.add(new VideoEntry("Class 4 Science Chapter 8", "X0fkTwvJ-CM"));
			list.add(new VideoEntry("Class 4 Science Chapter 8 Part 1", "HXw1lQqTXcU"));
			list.add(new VideoEntry("Class 4 Science Chapter 8 Part 2", "z3VQy8bz5kM"));
			list.add(new VideoEntry("Class 4 Science Chapter 9 Part 1", "rXTyoYcRjxo"));
			list.add(new VideoEntry("Class 4 Science Chapter 9 Part 2", "igZLiW-Pdik"));
			list.add(new VideoEntry("Class 4 Science Chapter 10", "u2aQAMOdH1w"));
			list.add(new VideoEntry("Class 4 Science Chapter 10 Part 1", "FafQIow5C1k"));
			list.add(new VideoEntry("Class 4 Science Chapter 10 Part 2", "kZmEclin_Fk"));
			list.add(new VideoEntry("Class 4 Science Chapter 10 Part 3", "6fFheqG9YUE"));
			list.add(new VideoEntry("Class 4 Science Chapter 11 Part 1", "5bSI9yk7w7E"));
			list.add(new VideoEntry("Class 4 Science Chapter 11 Part 2", "CuCeEE_eW7s"));
			list.add(new VideoEntry("Class 4 Science Chapter 12 Part 1", "4aXkoxp3ApM"));
			list.add(new VideoEntry("Class 4 Science Chapter 12 Part 2", "E-7Oz_rt3V8"));
			list.add(new VideoEntry("Class 4 Science Chapter 12 Part 3", "JEvFQDhOa-I"));

            VIDEO_LIST = Collections.unmodifiableList(list);
        }
        private PageAdapter adapter;

        private View videoBox;

        private String title3;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Class4 obj = new Class4();
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
            player.setOnFullscreenListener((Class4) getActivity());
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
