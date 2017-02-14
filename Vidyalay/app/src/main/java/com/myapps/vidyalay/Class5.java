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
public final class Class5 extends Activity implements OnFullscreenListener {

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

        setContentView(R.layout.video_list_class5);

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
            list.add(new VideoEntry("Class 5 English Chapter 1", "qL4lZn6fRG4"));
			list.add(new VideoEntry("Class 5 English Chapter 2", "O1A8e8bbmuE"));
			list.add(new VideoEntry("Class 5 English Chapter 3 Part 1", "ro_eB86cAUs"));
			list.add(new VideoEntry("Class 5 English Chapter 3 Part 2", "f2Tcbd2XCFA"));
			list.add(new VideoEntry("Class 5 English Chapter 4", "FOk7Aj71kMo"));
			list.add(new VideoEntry("Class 5 English Chapter 5", "_PhHJSoAuVE"));
			list.add(new VideoEntry("Class 5 English Chapter 6", "HE76RCWVJU8"));
			list.add(new VideoEntry("Class 5 English Chapter 7 Part 1", "gaEUQGBrgl0"));
			list.add(new VideoEntry("Class 5 English Chapter 8 Part 1", "NWXVwEF59xc"));
			list.add(new VideoEntry("Class 5 English Chapter 9", "fJgXBncaid4"));
			list.add(new VideoEntry("Class 5 English Chapter 10 Part 1", "h4rlPZyVBJo"));
			list.add(new VideoEntry("Class 5 English Chapter 10 Part 2", "S4A3vPKTN-0"));
			list.add(new VideoEntry("Class 5 English Chapter 11", "jesY8nrUdFs"));
			list.add(new VideoEntry("Class 5 English Chapter 12", "2TSmPvkVtkE"));
			list.add(new VideoEntry("Class 5 English Chapter 13", "ufz8BTAyxfQ"));
			list.add(new VideoEntry("Class 5 English Chapter 14", "BQ3XC28kuoo"));
			list.add(new VideoEntry("Class 5 English Chapter 15 Part 1", "HxwF72juoWo"));
			list.add(new VideoEntry("Class 5 English Chapter 16", "2e-LfVaOyoM"));
			list.add(new VideoEntry("Class 5 English Chapter 17 Patr 1", "j283JRcjwus"));
			list.add(new VideoEntry("Class 5 English Chapter 18 Patr 1", "afyrkP2Q5is"));

			list.add(new VideoEntry("Class 5 Hindi Chapter 1", "D8y_Xrbuue4"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 2 Part 1", "t271c3Ou15o"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 2 Part 2", "cbnWc2hWyz4"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 2 Part 3", "J9fI9JXWjRU"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 3 Part 1", "npwO7m5MRz4"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 3 Part 2", "fvkrZ2qG5hY"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 4 Part 1", "KB4bswn1yPc"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 4 Part 2", "TlEBtCcKz9k"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 5 Part 1", "wyhVOPzO5X8"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 5 Part 2", "jv0MxyCOEIg"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 6", "zkl1dTjHhic"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 7", "MHuMg4BIQIk"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 8 Part 1", "4KGDEW1Y1kQ"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 8 Part 2", "Z-vAZHwG66A"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 9", "awiqQv5fyu0"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 10", "-k5kCr4dm4E"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 11 Part 1", "fqtFTxw0h7I"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 11 Part 2", "RDbx6k9A80I"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 12 Part 1", "VAaJ8qQT1GY"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 12 Part 2", "6z7ULPgzGeE"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 13 Part 1", "eEtxUUaJ35U"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 13 Part 2", "od3nNDKAAP8"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 14 Part 1", "xa5TZUgq7KE"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 14 Part 2", "bdc49nqCh78"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 15 Part 1", "OFhM_Rujam0"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 15 Part 2", "qskGvh49ipA"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 16", "u_utlxElPZI"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 17", "9kjTXfTtoXA"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 18", "7S18Pkfz2zs"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 19 Part 1", "EedSK0c_wCo"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 19 Part 2", "HDk2MFSrhk8"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 20", "2Hx2-HClRcc"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 21 Part 1", "7EOdSpVOmMs"));
			list.add(new VideoEntry("Class 5 Hindi Chapter 21 Part 2", "6qMTJi-wa4k"));

			list.add(new VideoEntry("Class 5 Hindi Lal Bahadur Shastri Part 1", "Juu3VJLZ3SE"));
			list.add(new VideoEntry("Class 5 Hindi Lal Bahadur Shastri Part 2", "Bkb-DMg0wn8"));
			list.add(new VideoEntry("Class 5 Hindi Lal Bahadur Shastri Part 4", "3I7ILCIAom0"));
			list.add(new VideoEntry("Class 5 Hindi Panch Parmeshwar Part 2", "5m4B6VN1H9s"));
			list.add(new VideoEntry("Class 5 Hindi Panch Parmeshwar Part 3", "uFMQQnhd9mM"));
			list.add(new VideoEntry("Class 5 Hindi Panch Parmeshwar Part 4", "rlMHYkQ__iw"));
			list.add(new VideoEntry("Class 5 Hindi Panch Parmeshwar Part1", "cIfKCDb2RXI"));
			list.add(new VideoEntry("Class 5 Hindi Vimal Indu Ki Vishal Kirane Part 1", "HpKdZFYNNgA"));
			list.add(new VideoEntry("Class 5 Hindi Vimal Indu Ki Vishal Kirane Part 2", "itPeCkl___8"));

			list.add(new VideoEntry("Class 5 Maths Chapter 1", "lGhXMiJElks"));
			list.add(new VideoEntry("Class 5 Maths Chapter 2 Part 1", "3skM19CAZQI"));
			list.add(new VideoEntry("Class 5 Maths Chapter 2 Part 2", "6y0uURIKqXs"));
			list.add(new VideoEntry("Class 5 Maths Chapter 3", "dF9ALwtR9Io"));
			list.add(new VideoEntry("Class 5 Maths Chapter 4 Part 1", "zMmf_meQaMI"));
			list.add(new VideoEntry("Class 5 Maths Chapter 4 Part 2", "kWdEqNB4Zqw"));
			list.add(new VideoEntry("Class 5 Maths Chapter 5", "Wzzx4TnmHQ0"));
			list.add(new VideoEntry("Class 5 Maths Chapter 6", "91xnP74NTvc"));
			list.add(new VideoEntry("Class 5 Maths Chapter 7", "07XkN6gbF2w"));
			list.add(new VideoEntry("Class 5 Maths Chapter 8", "ynbPbw9Ys_E"));
			list.add(new VideoEntry("Class 5 Maths Chapter 9 Part 1", "02olYVNAtTI"));
			list.add(new VideoEntry("Class 5 Maths Chapter 9 Part 2", "HkohbQyq5lA"));
			list.add(new VideoEntry("Class 5 Maths Chapter 10", "pnYrr-WIF_w"));
			list.add(new VideoEntry("Class 5 Maths Chapter 11", "xXlUBVcTEb8"));
			list.add(new VideoEntry("Class 5 Maths Chapter 12 Part 1", "1NcGLIp49v0"));
			list.add(new VideoEntry("Class 5 Maths Chapter 12 Part 2", "uLtazqOxz1c"));
			list.add(new VideoEntry("Class 5 Maths Chapter 13 Part 1", "BQY1hDZxC_4"));
			list.add(new VideoEntry("Class 5 Maths Chapter 13 Part 2", "b8cyDi1jfsc"));
			list.add(new VideoEntry("Class 5 Maths Chapter 13 Part 3", "iIbTH019FE4"));
			list.add(new VideoEntry("Class 5 Maths Chapter 14 Part 1", "PXWsbQBmeeE"));
			list.add(new VideoEntry("Class 5 Maths Chapter 14 Part 2", "fNSdPEVtdX4"));
			list.add(new VideoEntry("Class 5 Maths Chapter 15", "eF1-V9mDvd8"));
			list.add(new VideoEntry("Class 5 Maths Chapter 16 Part 1", "byVxFvSfESA"));
			list.add(new VideoEntry("Class 5 Maths Chapter 16 Part 2", "Rz3ExUWcXsE"));
			list.add(new VideoEntry("Class 5 Maths Chapter 17", "0v_bI2Qzdw0"));
			list.add(new VideoEntry("Class 5 Maths Chapter 18", "UjC5mo2tZ2Y"));
			list.add(new VideoEntry("Class 5 Maths Chapter 19", "EpAuJHcy2tU"));
			list.add(new VideoEntry("Class 5 Maths Chapter 20", "mRvrdiIW9o4"));
			list.add(new VideoEntry("Class 5 Maths Chapter 20 Part 1", "YMq792UhPl4"));

			list.add(new VideoEntry("Class 5 Science Chapter 1 Part 1", "vBcpRgOy4xk"));
			list.add(new VideoEntry("Class 5 Science Chapter 1 Part 2", "pn12r_yNZls"));
			list.add(new VideoEntry("Class 5 Science Chapter 2 Part 1", "PhaapxS-9zI"));
			list.add(new VideoEntry("Class 5 Science Chapter 2 Part 2", "pRnq708CiNQ"));
			list.add(new VideoEntry("Class 5 Science Chapter 2 Part 3", "PN4vUKZurIs"));
			list.add(new VideoEntry("Class 5 Science Chapter 2 Part 4", "Ve9b2MsJBo4"));
			list.add(new VideoEntry("Class 5 Science Chapter 3 Part 1", "Dt0uAIf51hI"));
			list.add(new VideoEntry("Class 5 Science Chapter 3 Part 2", "c6d2NeDtx2o"));
			list.add(new VideoEntry("Class 5 Science Chapter 4 Part 1", "TabVPIsN9hI"));
			list.add(new VideoEntry("Class 5 Science Chapter 4 Part 2", "Pz5lOoh6BW8"));
			list.add(new VideoEntry("Class 5 Science Chapter 4 Part 3", "U2UdmRMxbtQ"));
			list.add(new VideoEntry("Class 5 Science Chapter 4 Part 4", "Tkd2ZRD01zw"));
			list.add(new VideoEntry("Class 5 Science Chapter 4 Part 5", "DeO5TCmDYEc"));
			list.add(new VideoEntry("Class 5 Science Chapter 5 Part 1", "XQqM1Ydqcr0"));
			list.add(new VideoEntry("Class 5 Science Chapter 5 Part 2", "60zOW4vxR5g"));
			list.add(new VideoEntry("Class 5 Science Chapter 5 Part 3", "a9d5B6e1b1E"));
			list.add(new VideoEntry("Class 5 Science Chapter 6 Part 1", "j4Jkeb1sjxE"));
			list.add(new VideoEntry("Class 5 Science Chapter 6 Part 2", "wgF2AuZGNGo"));
			list.add(new VideoEntry("Class 5 Science Chapter 7 Part 1", "eBl6TtELS-Y"));
			list.add(new VideoEntry("Class 5 Science Chapter 7 Part 2", "Jei5Fs-ZFJE"));
			list.add(new VideoEntry("Class 5 Science Chapter 8 Part 1", "GT9APN66pbM"));
			list.add(new VideoEntry("Class 5 Science Chapter 8 Part 2", "lKmfak0eU1Q"));
			list.add(new VideoEntry("Class 5 Science Chapter 9 Part 1", "sr8_Gd3sPNg"));
			list.add(new VideoEntry("Class 5 Science Chapter 9 Part 2", "k15lrkB-liQ"));
			list.add(new VideoEntry("Class 5 Science Chapter 10", "5SFqzp-1EEU"));
			list.add(new VideoEntry("Class 5 Science Chapter 11 Part 1", "5LqpslmRr6U"));
			list.add(new VideoEntry("Class 5 Science Chapter 11 Part 2", "yNzQlG4O4Hc"));
			list.add(new VideoEntry("Class 5 Science Chapter 12 Part 1", "i9ZJX1zaz_U"));
			list.add(new VideoEntry("Class 5 Science Chapter 12 Part 2", "4k7kKBRYIVw"));
			list.add(new VideoEntry("Class 5 Science Chapter 12 Part 3", "D8W8VeoxmME"));
			
			list.add(new VideoEntry("Class 5 Hindi Aatm Kath Dr Rajendra Prashad Part 1", "OhJ7BE2j-Vc"));
			list.add(new VideoEntry("Class 5 Hindi Aatm Kath Dr Rajendra Prashad Part 2", "rEijkOCQhH0"));
			list.add(new VideoEntry("Class 5 Hindi Abraham Lincoln Part 1", "s9Y-9s4ywGk"));
			list.add(new VideoEntry("Class 5 Hindi Abraham Lincoln Part 2", "Cl0MamdapLE"));
			list.add(new VideoEntry("Class 5 Hindi Abraham Lincoln Part 3", "HFvYzNVjN_o"));
			list.add(new VideoEntry("Class 5 Hindi Abraham Lincoln Part 4", "3LZoOcWNg2k"));
			list.add(new VideoEntry("Class 5 Hindi Hariyali Part 1", "caC6vB3iMV0"));
			list.add(new VideoEntry("Class 5 Hindi Kantoo Mein Raah Banate Hain Part 1", "Xa9FwMVQjzc"));
			list.add(new VideoEntry("Class 5 Hindi Kantoo Mein Raah Banate Hain Part 2", "EeYdMMgmqV0"));
			list.add(new VideoEntry("Class 5 Hindi Koi Laake Mujhe De Part 1", "LmIP73rWxto"));
			list.add(new VideoEntry("Class 5 Hindi Koi Laake Mujhe De Part 2", "RoFt13ZlCRk"));
			list.add(new VideoEntry("Class 5 Hindi Maharishi Baalmiki", "RG0yEuinCgo"));
			list.add(new VideoEntry("Class 5 Hindi Mai Aur Mera Desh Part 1", "OONLP2wGwJI"));
			list.add(new VideoEntry("Class 5 Hindi Mai Aur Mera Desh Part 2", "dsrxVcMQnhE"));
			list.add(new VideoEntry("Class 5 Hindi Mai Aur Mera Desh Part 3", "64IbUGsKAzM"));
			list.add(new VideoEntry("Class 5 Hindi Mai Aur Mera Desh Part 4", "fSV7rtKrbXs"));
			list.add(new VideoEntry("Class 5 Hindi Olympic Khel Part 1", "t4peLdPLFBU"));
			list.add(new VideoEntry("Class 5 Hindi Sarita Part 1", "dKpyK-ILJNo"));
			list.add(new VideoEntry("Class 5 Hindi Sarita Part 2", "drPJgc-TLAM"));
			list.add(new VideoEntry("Class 5 Hindi Vallabh Bhi Patel Part 2", "rFTnk5nn75U"));
			list.add(new VideoEntry("Class 5 Math Addition and Subtraction Part 1", "d8yaxp3SZNo"));
			list.add(new VideoEntry("Class 5 Math Addition and Subtraction Part 2", "RnC4Txh1jaU"));
			list.add(new VideoEntry("Class 5 Math Addition and Subtraction Part 3", "6fjHJ4PWb5E"));
			list.add(new VideoEntry("Class 5 Math Addition and Subtraction Part 4", "CdD3HlW9Eko"));
			list.add(new VideoEntry("Class 5 Math Addition and Subtraction Part 5", "yuZDs5XdKt8"));
			list.add(new VideoEntry("Class 5 Math Area of Rectangle and Square Part 1", "GGM2FpGHit4"));
			list.add(new VideoEntry("Class 5 Math Area of Rectangle and Square Part 2", "7xyKHMCYNdA"));
			list.add(new VideoEntry("Class 5 Math BODMAS Part 2", "SE7htT8bE4k"));
			list.add(new VideoEntry("Class 5 Math Circle Part 1", "Qi-oX5Hgg9I"));
			list.add(new VideoEntry("Class 5 Math Circle Part 2", "aexIp1TQAxY"));
			list.add(new VideoEntry("Class 5 Math Construction of Triangle Part 1", "1vhFPxSufyA"));
			list.add(new VideoEntry("Class 5 Math Construction of Triangle Part 2", "xdy0j4seD7I"));
			list.add(new VideoEntry("Class 5 Math Decimal Part 1", "-6tJeR_oHa0"));
			list.add(new VideoEntry("Class 5 Math Decimal Part 2", "nlxZKVwU8DU"));
			list.add(new VideoEntry("Class 5 Math Decimal on Multiple Part 1", "bYPabtk0rzI"));
			list.add(new VideoEntry("Class 5 Math Decimal on Multiple and Division Part 2", "3xM1VfQd2R0"));
			list.add(new VideoEntry("Class 5 Math Division Part 3", "RFvX2jU_Jqs"));
			list.add(new VideoEntry("Class 5 Math Fraction Part 1", "hbafgR4QIWQ"));
			list.add(new VideoEntry("Class 5 Math Fraction Part 2", "YMNdonlhaIo"));
			list.add(new VideoEntry("Class 5 Math Fraction Part 3", "W1FtUF2FNak"));
			list.add(new VideoEntry("Class 5 Math Fraction Part 4", "umEPcpEMtx4"));
			list.add(new VideoEntry("Class 5 Math Fraction Part 6", "prSo0cCktyE"));
			list.add(new VideoEntry("Class 5 Math Graph Part 1", "u_s3jEi5DBE"));
			list.add(new VideoEntry("Class 5 Math Graph Part 2", "0zqcwpgLxF8"));
			list.add(new VideoEntry("Class 5 Math Graph Part 3", "wxHuQEI8NCg"));
			list.add(new VideoEntry("Class 5 Math HCF Part 2", "izgEKkew2M4"));
			list.add(new VideoEntry("Class 5 Math LCM Part 1", "dE94X0mX9KA"));
			list.add(new VideoEntry("Class 5 Math LCM Part 2", "J6CWxWy210w"));
			list.add(new VideoEntry("Class 5 Math LCM Part 3", "MFULwEt9Rzc"));
			list.add(new VideoEntry("Class 5 Math Line and Angle Part 1", "00BnIJPnV_c"));
			list.add(new VideoEntry("Class 5 Math Line and Angle Part 2", "z59kgsoSSHw"));
			list.add(new VideoEntry("Class 5 Math Multiple And Division Part 2", "2PeiUYxOG3M"));
			list.add(new VideoEntry("Class 5 Math Multiple Part 1", "Y5HTWNSD6DA"));
			list.add(new VideoEntry("Class 5 Math Number Part 1", "3ztPcw1AkOI"));
			list.add(new VideoEntry("Class 5 Math Number Part 2", "Z9WaeJ4A7oA"));
			list.add(new VideoEntry("Class 5 Math Parallel & Perpendicular Line Part1", "y3B8sj_Lhsw"));
			list.add(new VideoEntry("Class 5 Math Parallel & Perpendicular Line Part2", "azHHJU3Xuns"));
			list.add(new VideoEntry("Class 5 Math Percentage Part 1", "a2X1UY1m-lE"));
			list.add(new VideoEntry("Class 5 Math Percentage Part 2", "18hyCA2fP9M"));
			list.add(new VideoEntry("Class 5 Math Perimeter Part 1", "DWKQCDmF80g"));
			list.add(new VideoEntry("Class 5 Math Perimeter Part 2", "GH6GS9wE1k0"));
			list.add(new VideoEntry("Class 5 Math Profit And Loss Part 1", "qRY1h0ZWoyk"));
			list.add(new VideoEntry("Class 5 Math Profit And Loss Part 2", "yYdRwWlw7J0"));
			list.add(new VideoEntry("Class 5 Math Rectangle and Square Part 1", "Kuzz9MFnXK0"));
			list.add(new VideoEntry("Class 5 Math Rectangle and Square Part 2", "qHjguu84hcw"));
			list.add(new VideoEntry("Class 5 Math Simple Interest Part 1", "rWMmiMtFGRk"));
			list.add(new VideoEntry("Class 5 Math Simple Interest Part 2", "EboK8fVSOaQ"));
			list.add(new VideoEntry("Class 5 Math Simple Interest Part 3", "4aSgpzjYL3U"));
			list.add(new VideoEntry("Class 5 Math Simple Interest Part 4", "17MDCB-vpMo"));
			list.add(new VideoEntry("Class 5 Math Time Table Part 1", "q2yZw9sfOfM"));
			list.add(new VideoEntry("Class 5 Math Time Table Part 2", "lQVlhQ-pnhY"));
			list.add(new VideoEntry("Class 5 Math Time Table Part 3", "a1vehnXtZm0"));
			list.add(new VideoEntry("Class 5 Math Triangle Part 1", "5DZtaY6BGgw"));
			list.add(new VideoEntry("Class 5 Math Triangle Part 1", "nL8Re4XtyrM"));
			list.add(new VideoEntry("Class 5 Math Triangle Part 2", "3GqeAUwbAdQ"));
			list.add(new VideoEntry("Class 5 Math Triangle Part 2", "7SZN1wNNc_c"));
			list.add(new VideoEntry("Class 5 Math Unitary Method Part 1", "aEafBiTH7VI"));
			list.add(new VideoEntry("Class 5 Math Unitary Method Part 2", "92p6DoEXMSI"));
			list.add(new VideoEntry("Class 5 Math Volume Part 1", "eQwcszY_D18"));
			list.add(new VideoEntry("Class 5 Math Volume Part 2", "Wp9f9GNfH6I"));
			list.add(new VideoEntry("Class 5 Science Adaption in Animals Part 1", "jq7zfxw3Xms"));
			list.add(new VideoEntry("Class 5 Science Adaption in Plants and Animals Part 1", "XQjGPdOCLHw"));
			list.add(new VideoEntry("Class 5 Science Adaption in Plants and Animals Part 2", "bNR-4QScxSk"));
			list.add(new VideoEntry("Class 5 Science Adaption in Plants and Animals Part 3", "mUZ_mG3Qcdk"));
			list.add(new VideoEntry("Class 5 Science Air Part 1", "my7GZHtmAaM"));
			list.add(new VideoEntry("Class 5 Science Air Part 2", "aoo9-V1odgM"));
			list.add(new VideoEntry("Class 5 Science Air Part 3", "OzM2M6firwg"));
			list.add(new VideoEntry("Class 5 Science Air Part 4", "Z1eW6IFVhCA"));
			list.add(new VideoEntry("Class 5 Science Air Pollution Part 1", "1H9qjyqaebA"));
			list.add(new VideoEntry("Class 5 Science Air Pollution Part 2", "pUJojNd8zmM"));
			list.add(new VideoEntry("Class 5 Science Bilance Diet Part 1", "dC1QOQbPKQs"));
			list.add(new VideoEntry("Class 5 Science Bilance Diet Part 2", "0RBhXVyOawI"));
			list.add(new VideoEntry("Class 5 Science Bones Of Human Body Part 1", "7bTlbIXdkjk"));
			list.add(new VideoEntry("Class 5 Science Bones Of Human Body Part 2", "8gst5KF1YVw"));
			list.add(new VideoEntry("Class 5 Science Common Diseases Part 1", "2ATFknFdJzo"));
			list.add(new VideoEntry("Class 5 Science Common Diseases Part 2", "pZSiUMli3YY"));
			list.add(new VideoEntry("Class 5 Science Communicable Disease Part 1", "LhAVChHupto"));
			list.add(new VideoEntry("Class 5 Science Communicable Disease Part 2", "UEjwyiJO190"));
			list.add(new VideoEntry("Class 5 Science Germination Of Seed Part 1", "-cUJMRtMW0E"));
			list.add(new VideoEntry("Class 5 Science Germination Of Seed Part 2", "f9kOsYN-3_s"));
			list.add(new VideoEntry("Class 5 Science Germination Of Seed Part 4", "hTH0nB9Dcg0"));
			list.add(new VideoEntry("Class 5 Science Joint and Ligament Part 1", "4oPl7GqHVl0"));
			list.add(new VideoEntry("Class 5 Science Joint and Ligament Part 2", "Yzg8oEYIU74"));
			list.add(new VideoEntry("Class 5 Science Living Thing Part 2", "F5kOAUKT8wM"));
			list.add(new VideoEntry("Class 5 Science Living Thing Part1", "uALSvS2aFA4"));
			list.add(new VideoEntry("Class 5 Science Migration of birds and extinct animals Part1", "GMFadjq8iMs"));
			list.add(new VideoEntry("Class 5 Science Migration of birds and extinct animals Part2", "Roizaejjyvg"));
			list.add(new VideoEntry("Class 5 Science Muscles and bones Part 1", "vbdKXt8cztU"));
			list.add(new VideoEntry("Class 5 Science Muscles and bones Part 2", "E7MiksBbpFo"));
			list.add(new VideoEntry("Class 5 Science Similarity & Difference Between Plants & Animals Part 2", "A-obfGopAs8"));
			list.add(new VideoEntry("Class 5 Science Simple Machine Part 1", "AWZ-zMGz75c"));
			list.add(new VideoEntry("Class 5 Science Simple Machine Part 1", "pQtIWk2LzdA"));
			list.add(new VideoEntry("Class 5 Science Simple Machine Part 2", "u7xqiCO8sHk"));
			list.add(new VideoEntry("Class 5 Science Simple Machine Part 2", "Gb-UiL4aN_0"));
			list.add(new VideoEntry("Class 5 Science Simple Machine Part 3", "spp--ZAyJkU"));
			list.add(new VideoEntry("Class 5 Science Simple Machine Part 4", "C5rNclSDi6s"));
			list.add(new VideoEntry("Class 5 Science Skeletal System Part 1", "jGtDsCmhcXc"));
			list.add(new VideoEntry("Class 5 Science Skeletal System Part 2", "SQ2afkkS_y4"));
			list.add(new VideoEntry("Class 5 Science Soil Erosion Part 1", "GkUeaWDs0LI"));
			list.add(new VideoEntry("Class 5 Science Soil Erosion Part 2", "4Pevcrz5_-w"));
			list.add(new VideoEntry("Class 5 Science Soil Part 1", "z1-CbXCEvJM"));
			list.add(new VideoEntry("Class 5 Science Soil Part 2", "8eK1hpeVcJQ"));
			list.add(new VideoEntry("Class 5 Science Water Borne Diseases Part 1", "WB7n3FEiRBU"));
			list.add(new VideoEntry("Class 5 Science Water Borne Diseases Part 2", "dazloSA5c2s"));
			list.add(new VideoEntry("Class 5 Science Work,Powwer and Energy Part 2", "njxn-jsPP48"));
			list.add(new VideoEntry("Class 5 Science Work,Powwer and Energy Part1", "2brVhXr41Kk"));

            VIDEO_LIST = Collections.unmodifiableList(list);
        }

        private PageAdapter adapter;

        private View videoBox;

        private String title3;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Class5 obj = new Class5();
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
            player.setOnFullscreenListener((Class5) getActivity());
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
