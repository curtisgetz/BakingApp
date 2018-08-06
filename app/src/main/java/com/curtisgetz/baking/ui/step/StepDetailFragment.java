package com.curtisgetz.baking.ui.step;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.database.AppDataBase;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.Step;
import com.curtisgetz.baking.ui.recipe.RecipeViewModel;
import com.curtisgetz.baking.ui.recipe.RecipeViewModelFactory;
import com.curtisgetz.baking.utils.BakingApp;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class StepDetailFragment extends Fragment{

    private static final String TAG = StepDetailFragment.class.getSimpleName();


    private SimpleExoPlayer mPlayer;
    private boolean noButtons;
    private boolean twoPane;
    private long mPlayerPosition;
    private int mStepNum;
    private int mRecipeId;
    private String mMediaString;

    private AppDataBase mDb;
    private RecipeViewModel mViewModel;

    @BindView(R.id.simple_player) PlayerView mPlayerView;
    @BindView(R.id.no_video_imageview) ImageView mNoVideoImageView;
    @Nullable
    @BindView(R.id.step_detail_description) TextView mStepDetailTV;
    @Nullable
    @BindView(R.id.previous_button) Button mPrevButton;
    @Nullable
    @BindView(R.id.next_button) Button mNextButton;


    public StepDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "Fragment Created");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        mDb = AppDataBase.getsInstance(getActivity().getApplicationContext());

        twoPane =(view.findViewById(R.id.step_detail_fragment_sw600_land0) != null);
        noButtons = (view.findViewById(R.id.button_layout) == null);



        if(savedInstanceState == null) {
            Bundle bundle = getArguments();
            mRecipeId = bundle.getInt(getString(R.string.recipe_id_extra), BakingApp.DEFAULT_RECIPE_ID);
            mStepNum = bundle.getInt(getString(R.string.step_number_extra), BakingApp.DEFAULT_STEP_NUM);

        }else {
            mRecipeId = savedInstanceState.getInt(getString(R.string.recipe_id_extra), BakingApp.DEFAULT_RECIPE_ID);
            mStepNum = savedInstanceState.getInt(getString(R.string.saved_step_num), BakingApp.DEFAULT_STEP_NUM);
            mPlayerPosition = savedInstanceState.getLong(getString(R.string.saved_player_position), 0L);
        }

        RecipeViewModelFactory factory = new RecipeViewModelFactory(mDb, mRecipeId);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(RecipeViewModel.class);
        mViewModel.getmRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                populateUI();
            }
        });

        return view;
    }




    private void setupButtons(){
        //If button layout is null, return. Otherwise, check which button(s) should be enabled.
        if(noButtons) return;
        mPrevButton.setEnabled(!(mStepNum == 0));
        mNextButton.setEnabled(!(mStepNum == (mViewModel.getmRecipe().getValue()
                .getSteps().size() - 1)));
    }


    private void clearPlayerDetails(){
        mMediaString = null;
        mPlayerPosition = 0L;
    }

    @Optional
    @OnClick(R.id.previous_button)
    public void onPrevStepClick() {
        clearPlayerDetails();
        mStepNum--;
        populateUI();
    }

    @Optional
    @OnClick(R.id.next_button)
    public void onNextStepClick() {
        clearPlayerDetails();
        mStepNum++;
        populateUI();
    }


    private void populateUI(){
        releasePlayer();
        setupButtons();
        Recipe recipe =  mViewModel.getmRecipe().getValue();
        getActivity().setTitle(recipe.getName());
        Log.e(TAG, "POPULATE UI");
        Step step = recipe.getSteps().get(mStepNum);
        Log.e(TAG, String.valueOf(noButtons));
        Log.e(TAG, String.valueOf(twoPane));
        if((noButtons == twoPane)){
            mStepDetailTV.setText(step.getDescription());
        }

        //if there is no video url, attempt to load thumbnail from thumbnailUrl. Picasso will
        //show no_video_image if there is an error loading image.
        //if there is videourl, attempt to play
        mPlayerView.setVisibility(View.INVISIBLE);
        mNoVideoImageView.setVisibility(View.INVISIBLE);
        if(step.getVideoURL().isEmpty()){
            showNoVideoImageView(step.getThumbnailURL());
        }else{
            mMediaString = step.getVideoURL();
            initializePlayer(mMediaString);
        }

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(getString(R.string.saved_step_num), mStepNum);
        outState.putInt(getString(R.string.recipe_id_extra), mRecipeId);
        outState.putLong("saved_player_position", mPlayerPosition);
        super.onSaveInstanceState(outState);
        if(mPlayer != null) {
            Log.e(TAG, String.valueOf(mPlayer.getCurrentPosition()));
            outState.putLong("saved_player_pos", mPlayer.getCurrentPosition());
        }
    }



    private void showNoVideoImageView(String imageUrl){
        mPlayerView.setVisibility(View.INVISIBLE);
        //attempt to load "thumbnailURL" image. If error, display 'no video' image
        if(imageUrl.isEmpty() || imageUrl.trim().length() == 0){
            //Picasso will throw exception with empty path, Tell it to load no_video_image if empty
            Picasso.get().load(R.drawable.no_video_image).into(mNoVideoImageView);
        }else {
            //if path isn't empty try to load. On error display the same no_video_image
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.no_video_image)
                    .into(mNoVideoImageView);

        }
        mNoVideoImageView.setVisibility(View.VISIBLE);
    }



    private void initializePlayer(String mediaString){
        if(mediaString.isEmpty()) showNoVideoImageView(mediaString);
        Log.i(TAG, "Initializing Player");
        mPlayerView.setVisibility(View.VISIBLE);
        mNoVideoImageView.setVisibility(View.INVISIBLE);

        if(mPlayer == null){
            Uri mediaUri = Uri.parse(mediaString);

            //create instance of player
            mPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()), new DefaultTrackSelector(), new DefaultLoadControl());

            String userAgent = Util.getUserAgent(getActivity(), "Baking");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            mPlayer.prepare(mediaSource);
            mPlayerView.setControllerShowTimeoutMs(800);
            mPlayerView.setPlayer(mPlayer);
            if(mPlayerPosition != 0L) mPlayer.seekTo(mPlayerPosition);
            mPlayer.setPlayWhenReady(true);

        }
    }



    private void releasePlayer(){
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void pausePlayer(){
        if(mPlayer != null){
            mPlayerPosition = mPlayer.getCurrentPosition();
            releasePlayer();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mMediaString != null) initializePlayer(mMediaString);
    }


    @Override
    public void onPause() {
        super.onPause();
        //pause, save state, and release player if activity is not in foreground to save resources
        pausePlayer();
    }


}

