package erixe.android.videoplayer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erixe.android.videoplayer.Adapters.CommentListItemAdapter;
import erixe.android.videoplayer.Adapters.SimpleVideoListItemAdapter;
import erixe.android.videoplayer.EVideoInformationModels.EQueueInformation;
import erixe.android.videoplayer.EVideoInformationModels.EVideoInformation;
import erixe.android.videoplayer.EVideoInformationModels.EVideoInformationComment;
import erixe.android.videoplayer.EWebServices.*;
import erixe.android.videoplayer.Utilities.EVideoView;
import erixe.android.videoplayer.R;

public class MainActivity extends Activity implements GetShowVideoTask.OnTaskCompleteListener, GetIndexVideosTask.OnTaskCompleteListener,
        GetQueueVideosTask.OnTaskCompleteListener, PostAddVideoToQueueTask.OnTaskCompleteListener,
        PostRemoveVideoFromQueueTask.OnTaskCompleteListener, PostUnlikeOnVideoTask.OnTaskCompleteListener,
        PostLikeOnVideoTask.OnTaskCompleteListener, PostCommentOnVideoTask.OnTaskCompleteListener,
        GetSearchVideosTask.OnTaskCompleteListener{

    public static final int LAYOUT_STATE_CONTENT_PAGE = 1;
    public static final int LAYOUT_STATE_HOME_PAGE = 2;

    SharedPreferences prefs;

    ActionBarViewHolder abvh = new ActionBarViewHolder();
    HomePageViewHolder hpvh = new HomePageViewHolder();
    ContentPageViewHolder cpvh = new ContentPageViewHolder();

    SimpleVideoListItemAdapter homePageListViewAdapter;
    CommentListItemAdapter commentListItemAdapter;
    List<EVideoInformation> indexVideosInformation = new ArrayList<>();
    List<EQueueInformation> queueVideosInformation = new ArrayList<>();
    EVideoInformation showVideoInformation;

    int layoutState = LAYOUT_STATE_HOME_PAGE, screenHeight, screenWidth, marginTopForLayout;
    String username, password;

    @Override
    public void onBackPressed() {
        if(layoutState == LAYOUT_STATE_CONTENT_PAGE) {
            cpvh.contentPageEVideoView.pauseMedia();
            changeLayoutStateTo(LAYOUT_STATE_HOME_PAGE);
        }
        else
            super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            marginTopForLayout = screenWidth;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            marginTopForLayout = screenHeight;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            layoutState = savedInstanceState.getInt("layoutState");
            if (layoutState == 0)
                layoutState = LAYOUT_STATE_HOME_PAGE;
        }

        setViewHolders();
        changeLayoutStateTo(layoutState);
        getAuthentication();

        new GetIndexVideosTask(MainActivity.this, null).execute(username, password);
    }

    public void getAuthentication()
    {
        prefs = getApplicationContext().getSharedPreferences("MyPref", 0);

        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        if(username != null && password != null)
        {
            prefs.edit().putString("username", username).apply();
            prefs.edit().putString("password", password).apply();
        }

        username = prefs.getString("username", null);
        password = prefs.getString("password", null);
        if(username == null && password == null)
        {
            username = "hedayati.emad@gmail.com";
            password = "36233623";
            abvh.actionBarAuthenticationPanel.setVisibility(View.VISIBLE);
            abvh.actionBarUserPanel.setVisibility(View.GONE);
        }
        else
        {
            abvh.actionBarAuthenticationPanel.setVisibility(View.GONE);
            abvh.actionBarUserPanel.setVisibility(View.VISIBLE);
        }
    }

    public void toast(String message)
    {
        if(message == null || message.equals(""))
            message = "null";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void populateListView()
    {
        homePageListViewAdapter = new SimpleVideoListItemAdapter(getApplicationContext(), indexVideosInformation);
        hpvh.homePageVideoList.setAdapter(homePageListViewAdapter);

        hpvh.homePageVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                changeLayoutStateTo(LAYOUT_STATE_CONTENT_PAGE);
                cpvh.contentPageEVideoView.resetMedia();
                fillContentPage(null);
                indexVideosInformation.get(i).views = String.valueOf(Integer.parseInt(indexVideosInformation.get(i).views) + 1);
                homePageListViewAdapter.notifyDataSetChanged();
                new GetShowVideoTask(MainActivity.this, null).execute(username, password, indexVideosInformation.get(i).id);
            }
        });
    }

    public void changeLayoutStateTo(int layoutState)
    {
        switch (layoutState){
            case LAYOUT_STATE_HOME_PAGE:
                abvh.actionBarTitle.setText("Home");
                abvh.actionBar.setVisibility(View.VISIBLE);
//                hpvh.homePage.setVisibility(View.VISIBLE);
//                vh.contentPage.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) hpvh.homePage.getLayoutParams();
                lp1.setMargins(0, 0, 0, 0);
                hpvh.homePage.setLayoutParams(lp1);
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) cpvh.contentPageEVideoView.getLayoutParams();
                lp2.setMargins(0, marginTopForLayout, 0, 0);
                cpvh.contentPageEVideoView.setLayoutParams(lp2);
                break;
            case LAYOUT_STATE_CONTENT_PAGE:
                abvh.actionBarTitle.setText("Video");
                abvh.actionBar.setVisibility(View.GONE);
//                hpvh.homePage.setVisibility(View.INVISIBLE);
//                vh.contentPage.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) hpvh.homePage.getLayoutParams();
                lp3.setMargins(0, marginTopForLayout, 0, 0);
                hpvh.homePage.setLayoutParams(lp3);
                LinearLayout.LayoutParams lp4 = (LinearLayout.LayoutParams) cpvh.contentPageEVideoView.getLayoutParams();
                lp4.setMargins(0, 0, 0, 0);
                cpvh.contentPageEVideoView.setLayoutParams(lp4);
                break;
        }

        this.layoutState = layoutState;
    }

    @Override
    public void onGetSearchVideosTaskComplete(WebServiceRespond webServiceRespond, List<EVideoInformation> indexVideosInformation) {
        if(webServiceRespond.ok) {
            this.indexVideosInformation = indexVideosInformation;
            populateListView();
        }
    }

    @Override
    public void onGetShowVideoTaskComplete(WebServiceRespond webServiceRespond, EVideoInformation showVideoInformation) {
        if(webServiceRespond.ok) {
            this.showVideoInformation = showVideoInformation;
            cpvh.contentPageEVideoView.setVideoQualities(showVideoInformation.qualities);
            fillContentPage(showVideoInformation);
            try {
                if(showVideoInformation.qualities.size() > 0)
                    cpvh.contentPageEVideoView.setMediaPath(EWebServiceStrings.SERVER_BASE_URL +
                                    showVideoInformation.qualities.get(0).file);
                cpvh.contentPageEVideoView.prepareMedia(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            toast(webServiceRespond.description);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            }, 2000);
        }
    }

    @Override
    public void onGetIndexVideosTaskComplete(WebServiceRespond webServiceRespond, List<EVideoInformation> indexVideosInformation) {
        if(webServiceRespond.ok) {
            this.indexVideosInformation = indexVideosInformation;
            populateListView();
        }
    }

    @Override
    public void onGetQueueVideosTaskComplete(WebServiceRespond webServiceRespond, List<EQueueInformation> queueVideosInformation) {
        if(webServiceRespond.ok) {
            this.queueVideosInformation = queueVideosInformation;
            List<EVideoInformation> tmp = new ArrayList<>();
            for(EQueueInformation eQueueInformation : queueVideosInformation)
                tmp.add(eQueueInformation.video);

//            populateListView();
        }
    }

    @Override
    public void onPostCommentOnVideoTaskComplete(WebServiceRespond webServiceRespond, EVideoInformationComment eVideoInformationComment) {
        if(webServiceRespond.ok)
        {
            showVideoInformation.comments.add(eVideoInformationComment);
//            CommentListItemAdapter commentListItemAdapter = new CommentListItemAdapter(getApplicationContext(), showVideoInformation.comments);
//            cpvh.contentPageVideoList.setAdapter(commentListItemAdapter);
            commentListItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPostAddVideoToQueueTaskComplete(WebServiceRespond webServiceRespond) {
        if(webServiceRespond.ok) {
            showVideoInformation.queued = "true";
            cpvh.contentPageVideoAddToQueue.setImageResource(R.drawable.remove_from_queue);
            toast("Video added to queue");
        }
        else
            toast(webServiceRespond.description);
    }

    @Override
    public void onPostRemoveVideoFromQueueTaskComplete(WebServiceRespond webServiceRespond) {
        if(webServiceRespond.ok) {
            showVideoInformation.queued = "false";
            cpvh.contentPageVideoAddToQueue.setImageResource(R.drawable.add_to_queue);
            toast("Video removed from queue");
        }
        else
            toast(webServiceRespond.description);
    }

    @Override
    public void onPostLikeOnVideoTaskComplete(WebServiceRespond webServiceRespond, String likes) {
        if(webServiceRespond.ok)
        {
            showVideoInformation.liked = "true";
            cpvh.contentPageVideoLike.setImageResource(R.drawable.liked);
            cpvh.contentPageVideoLikesNumber.setText(likes);

            for(EVideoInformation videoInformation : indexVideosInformation)
                if(videoInformation.id == showVideoInformation.id)
                {
                    videoInformation.likes = String.valueOf(Integer.parseInt(videoInformation.likes) + 1);
                    homePageListViewAdapter.notifyDataSetChanged();
                }
        }
        else
            toast(webServiceRespond.description);
    }

    @Override
    public void onPostUnlikeOnVideoTaskComplete(WebServiceRespond webServiceRespond, String likes) {
        if(webServiceRespond.ok)
        {
            showVideoInformation.liked = "false";
            cpvh.contentPageVideoLike.setImageResource(R.drawable.like);
            cpvh.contentPageVideoLikesNumber.setText(likes);

            for(EVideoInformation videoInformation : indexVideosInformation)
                if(videoInformation.id == showVideoInformation.id)
                {
                    videoInformation.likes = String.valueOf(Integer.parseInt(videoInformation.likes) + 1);
                    homePageListViewAdapter.notifyDataSetChanged();
                }
        }
        else
            toast(webServiceRespond.description);
    }

    public static class ActionBarViewHolder{
        LinearLayout actionBar;
        LinearLayout actionBarAuthenticationPanel;
        LinearLayout actionBarUserPanel;
        TextView actionBarTitle;
        TextView actionBarLogin;
        TextView actionBarSignUp;
        ImageButton actionBarNavigationBar;
        ImageButton actionBarQueue;
    }

    public static class HomePageViewHolder{
        LinearLayout homePage;
        EditText homePageSearchEditText;
        ListView homePageVideoList;
    }

    public static class ContentPageViewHolder{
        LinearLayout contentPage;
        LinearLayout contentPageContent;
        RelativeLayout contentPageLoadingPanel;
        EVideoView contentPageEVideoView;
        TextView contentPageVideoDescription;
        TextView contentPageVideoTitle;
        TextView contentPageVideoLikesNumber;
        TextView contentPageVideoCommentsNumber;
        TextView contentPageVideoViews;
        ListView contentPageVideoList;
        ImageButton contentPageVideoLike;
        ImageButton contentPageVideoAddToQueue;
        EditText contentPageCommentText;
        Button contentPageCommentSend;
    }

    public void setViewHolders()
    {
        abvh.actionBar = (LinearLayout) findViewById(R.id.actionBar);
        abvh.actionBarAuthenticationPanel = (LinearLayout) findViewById(R.id.actionBarAuthenticationPanel);
        abvh.actionBarUserPanel = (LinearLayout) findViewById(R.id.actionBarUserPanel);
        abvh.actionBarTitle = (TextView) findViewById(R.id.actionBarTitle);
        abvh.actionBarLogin = (TextView) findViewById(R.id.actionBarLogin);
        abvh.actionBarSignUp = (TextView) findViewById(R.id.actionBarSignUp);
        abvh.actionBarNavigationBar = (ImageButton) findViewById(R.id.actionBarNavigationBar);
        abvh.actionBarQueue = (ImageButton) findViewById(R.id.actionBarQueue);
        setActionBarListeners();

        hpvh.homePage = (LinearLayout) findViewById(R.id.homePage);
        hpvh.homePageSearchEditText = (EditText) findViewById(R.id.homePageSearchEditText);
        hpvh.homePageVideoList = (ListView) findViewById(R.id.homePageVideoList);
        setHomePageListeners();

        cpvh.contentPage = (LinearLayout) findViewById(R.id.contentPage);
        cpvh.contentPageContent = (LinearLayout) findViewById(R.id.contentPageContent);
        cpvh.contentPageLoadingPanel = (RelativeLayout) findViewById(R.id.contentPageLoadingPanel);
        cpvh.contentPageEVideoView = (EVideoView) findViewById(R.id.contentPageEVideoView);
        cpvh.contentPageVideoList = (ListView) findViewById(R.id.contentPageVideoList);
        cpvh.contentPageVideoTitle = (TextView) findViewById(R.id.contentPageVideoTitle);
        cpvh.contentPageVideoLikesNumber = (TextView) findViewById(R.id.contentPageVideoLikesNumber);
        cpvh.contentPageVideoCommentsNumber = (TextView) findViewById(R.id.contentPageVideoCommentsNumber);
        cpvh.contentPageVideoViews = (TextView) findViewById(R.id.contentPageVideoViews);
        cpvh.contentPageVideoDescription = (TextView) findViewById(R.id.contentPageVideoDescription);
        cpvh.contentPageVideoLike = (ImageButton) findViewById(R.id.contentPageVideoLike);
        cpvh.contentPageVideoAddToQueue = (ImageButton) findViewById(R.id.contentPageVideoAddToQueue);
        cpvh.contentPageCommentText = (EditText) findViewById(R.id.contentPageCommentText);
        cpvh.contentPageCommentSend = (Button) findViewById(R.id.contentPageCommentSend);
        setContentPageListeners();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            marginTopForLayout = screenWidth;
            cpvh.contentPageEVideoView.changeToFullScreen();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            marginTopForLayout = screenHeight;
        }
    }

    public void setActionBarListeners()
    {
        abvh.actionBarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        abvh.actionBarSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        abvh.actionBarNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().remove("username").apply();
                prefs.edit().remove("password").apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        abvh.actionBarQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetQueueVideosTask(MainActivity.this, null).execute(username, password);
            }
        });

        abvh.actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(abvh.actionBarTitle.getText().toString().equals("Home"))
                    new GetIndexVideosTask(MainActivity.this, null).execute(username, password);
            }
        });
    }

    public void setHomePageListeners()
    {
        hpvh.homePageSearchEditText.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        hpvh.homePageSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("q", hpvh.homePageSearchEditText.getText().toString());
                    new GetSearchVideosTask(MainActivity.this, paramsMap).execute(username, password);
                    toast("searching");
                    Utilities.hideKeyboard(getApplicationContext());
                }
                return true;
            }
        });
    }

    public void setContentPageListeners() {
        cpvh.contentPageVideoLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showVideoInformation.liked.equals("true"))
                    new PostLikeOnVideoTask(MainActivity.this, null).execute(username, password, showVideoInformation.id);
                else
                    new PostUnlikeOnVideoTask(MainActivity.this, null).execute(username, password, showVideoInformation.id);
            }
        });

        cpvh.contentPageVideoAddToQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showVideoInformation.queued.equals("true"))
                    new PostAddVideoToQueueTask(MainActivity.this, null).execute(username, password, showVideoInformation.id);
                else
                    new PostRemoveVideoFromQueueTask(MainActivity.this, null).execute(username, password, showVideoInformation.id);
            }
        });

        cpvh.contentPageCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> paramsMap = new HashMap<String, String>();
                if(cpvh.contentPageCommentText.getText().toString().length() != 0 && showVideoInformation != null) {
                    paramsMap.put("body", cpvh.contentPageCommentText.getText().toString());
                    cpvh.contentPageCommentText.setText("");
                    Utilities.hideKeyboard(getApplicationContext());
                    new PostCommentOnVideoTask(MainActivity.this, paramsMap).execute(username, password, showVideoInformation.id);
                }
            }
        });
    }

    public void fillContentPage(EVideoInformation eVideoInformation)
    {
        if(eVideoInformation != null) {
            cpvh.contentPageLoadingPanel.setVisibility(View.GONE);
            cpvh.contentPageContent.setVisibility(View.VISIBLE);

            if (eVideoInformation.liked.equals("true"))
                cpvh.contentPageVideoLike.setImageResource(R.drawable.liked);
            else
                cpvh.contentPageVideoLike.setImageResource(R.drawable.like);

            if (eVideoInformation.queued.equals("true"))
                cpvh.contentPageVideoAddToQueue.setImageResource(R.drawable.remove_from_queue);
            else
                cpvh.contentPageVideoAddToQueue.setImageResource(R.drawable.add_to_queue);

            cpvh.contentPageVideoLikesNumber.setText(eVideoInformation.likes);
            cpvh.contentPageVideoViews.setText(eVideoInformation.views + " views");
            cpvh.contentPageVideoCommentsNumber.setText(eVideoInformation.comments.size() + " comments");
            cpvh.contentPageVideoTitle.setText(eVideoInformation.title);
            if(eVideoInformation.description.equals(""))
                cpvh.contentPageVideoDescription.setVisibility(View.GONE);
            else
            {
                cpvh.contentPageVideoDescription.setVisibility(View.VISIBLE);
                cpvh.contentPageVideoDescription.setText(eVideoInformation.description);
            }

            commentListItemAdapter = new CommentListItemAdapter(getApplicationContext(), eVideoInformation.comments);
            cpvh.contentPageVideoList.setAdapter(commentListItemAdapter);
        }
        else
        {
            cpvh.contentPageLoadingPanel.setVisibility(View.VISIBLE);
            cpvh.contentPageEVideoView.openLoadingPanelBlack();
            cpvh.contentPageContent.setVisibility(View.GONE);
//            cpvh.contentPageVideoLike.setImageResource(R.drawable.like);
//            cpvh.contentPageVideoAddToQueue.setImageResource(R.drawable.add_to_queue);
//            cpvh.contentPageVideoLikesNumber.setText("");
//            cpvh.contentPageVideoTitle.setText("");
//            cpvh.contentPageVideoDescription.setText("");
//
//            cpvh.contentPageVideoList.setAdapter(null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("layoutState", layoutState);
        super.onSaveInstanceState(outState);
    }
}