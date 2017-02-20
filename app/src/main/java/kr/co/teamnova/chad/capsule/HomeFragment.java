package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chad on 2017-01-18.
 */

public class HomeFragment extends Fragment implements AbsListView.OnScrollListener {
    private static final String TAG = "HomeFragment";

    private TextView textContentCount;
    private TextView textNothingContent;

    private ListView listViewContent;
    private ContentListViewAdapter adapter;
    private User loginUser;

    private ArrayList<Content> totalContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        String loginUserEmail = getArguments().getString("login_user");

        SharedPreferences spAccount = getContext().getSharedPreferences("account", MODE_PRIVATE);
        String[] strUserData = spAccount.getString(loginUserEmail, "").split(",");
        loginUser = new User(loginUserEmail, strUserData);

        adapter = new ContentListViewAdapter(HomeFragment.this, loginUser);
        listViewContent = (ListView) view.findViewById(R.id.listView_content);
        listViewContent.setOnScrollListener(this);
        listViewContent.setAdapter(adapter);

        final RelativeLayout layoutProfile = (RelativeLayout) view.findViewById(R.id.layout_profile);
        final View viewLine = view.findViewById(R.id.view_line);

        TextView textNickname = (TextView) view.findViewById(R.id.text_nickname);
        ImageView imageProfile = (ImageView) view.findViewById(R.id.image_profile);
        imageProfile.setImageURI(loginUser.getUriProfileImage());

        textContentCount = (TextView) view.findViewById(R.id.text_content_count);

        textNothingContent = (TextView) view.findViewById(R.id.text_nothing_content_const);

        totalContent = new ArrayList<>();

        if (loginUser.getNumOfContent() > 0) {
            SharedPreferences spContent = getActivity().getSharedPreferences("contents", MODE_PRIVATE);
            String[] strTotalContent = spContent.getString(loginUser.getEmail(), "").split(",");

            for (String strContent : strTotalContent) {
                String[] strContentDetail = strContent.split("::");

                Uri uriContentImage;
                if (strContentDetail[Const.CONTENT_IMAGE_URI].equals(" ")) {
                    uriContentImage = null;
                } else {
                    uriContentImage = Uri.parse(strContentDetail[Const.CONTENT_IMAGE_URI]);
                }

                String strContentDesc = strContentDetail[Const.CONTENT_DESCRIPTION];
                if (strContentDesc.equals(" ")) {
                    strContentDesc = "";
                } else {
                    strContentDesc = getStringFromByteString(strContentDetail[Const.CONTENT_DESCRIPTION], "\\+");
                }

                String strLocation = strContentDetail[Const.CONTENT_LOCATION];
                if (strLocation.equals(" ")) {
                    strLocation = "";
                }

                String strLikeList = strContentDetail[Const.CONTENT_LIKE_USER];
                ArrayList<String> likeList = new ArrayList<>();
                if (!strLikeList.equals(" ")) {
                    Collections.addAll(likeList, strLikeList.split("\\+"));
                }

                String strReplyList = strContentDetail[Const.CONTENT_REPLY];
                Log.d(TAG, strReplyList);
                ArrayList<Reply> replyList = new ArrayList<>();
                if(!strReplyList.equals(" ")){
                    String[] strReplyArray = strReplyList.split("\\+");
                    for(String strReply:strReplyArray){
                        String[] strReplyDetail = strReply.split("/");
                        Reply replyItem = new Reply(new User(strReplyDetail[0], spAccount.getString(strReplyDetail[0], "").split(",")), Utils.getStringFromByteString(strReplyDetail[1], "#"), Long.valueOf(strReplyDetail[2]));
                        replyList.add(replyItem);
                    }
                }

                Content userContent = new Content(
                        uriContentImage,
                        strContentDesc,
                        loginUser.getUriProfileImage(),
                        loginUser.getNickname(),
                        loginUser.getEmail(),
                        Long.valueOf(strContentDetail[Const.CONTENT_TIME]),
                        strLocation,
                        likeList,
                        replyList);

                totalContent.add(userContent);
            }
        }

        if (loginUser.getFollowList().size() > 0) {
            for (String email : loginUser.getFollowList()) {
                SharedPreferences spContent = getActivity().getSharedPreferences("contents", MODE_PRIVATE);
                String[] strTotalContent = spContent.getString(email, "").split(",");

                if (!strTotalContent[0].equals("")) {
                    strUserData = getContext().getSharedPreferences("account", MODE_PRIVATE).getString(email, "").split(",");
                    for (String strContent : strTotalContent) {
                        String[] strContentDetail = strContent.split("::");

                        Uri uriContentImage;
                        if (strContentDetail[Const.CONTENT_IMAGE_URI].equals(" ")) {
                            uriContentImage = null;
                        } else {
                            uriContentImage = Uri.parse(strContentDetail[Const.CONTENT_IMAGE_URI]);
                        }

                        String strContentDesc = strContentDetail[Const.CONTENT_DESCRIPTION];
                        if (strContentDesc.equals(" ")) {
                            strContentDesc = "";
                        } else {
                            strContentDesc = getStringFromByteString(strContentDetail[Const.CONTENT_DESCRIPTION], "\\+");
                        }

                        String strLocation = strContentDetail[Const.CONTENT_LOCATION];
                        if (strLocation.equals(" ")) {
                            strLocation = "";
                        }

                        String strLikeList = strContentDetail[Const.CONTENT_LIKE_USER];
                        ArrayList<String> likeList = new ArrayList<>();
                        if (!strLikeList.equals(" ")) {
                            Collections.addAll(likeList, strLikeList.split("\\+"));
                        }

                        String strReplyList = strContentDetail[Const.CONTENT_REPLY];
                        Log.d(TAG, strReplyList);
                        ArrayList<Reply> replyList = new ArrayList<>();
                        if(!strReplyList.equals(" ")){
                            String[] strReplyArray = strReplyList.split("\\+");
                            for(String strReply:strReplyArray){
                                String[] strReplyDetail = strReply.split("/");
                                Reply replyItem = new Reply(new User(strReplyDetail[0], spAccount.getString(strReplyDetail[0], "").split(",")), Utils.getStringFromByteString(strReplyDetail[1], "#"), Long.valueOf(strReplyDetail[2]));
                                replyList.add(replyItem);
                            }
                        }

                        Content userContent = new Content(
                                uriContentImage,
                                strContentDesc,
                                Uri.parse(strUserData[Const.INDEX_PROFILE_IMAGE]),
                                strUserData[Const.INDEX_NICKNAME],
                                email,
                                Long.valueOf(strContentDetail[Const.CONTENT_TIME]),
                                strLocation,
                                likeList,
                                replyList);

                        totalContent.add(userContent);
                    }
                }
            }
        }

        Collections.sort(totalContent, new FileNameSort());
        Collections.reverse(totalContent);
        adapter.addItemFromArray(totalContent);

        if (totalContent.size() == 0) {
            textNothingContent.setVisibility(View.VISIBLE);
        }

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final ImageButton ibtnOpen = (ImageButton) view.findViewById(R.id.ibtn_open);
        ibtnOpen.bringToFront();
        ibtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutProfile.getVisibility() == View.VISIBLE) {
                    layoutProfile.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    ibtnOpen.setBackgroundResource(R.mipmap.image_arrow_bottom);
                } else {
                    layoutProfile.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                    ibtnOpen.setBackgroundResource(R.mipmap.image_arrow_above);
                }
            }
        });

        textNickname.setText(loginUser.getNickname());
        textContentCount.setText(String.valueOf(loginUser.getNumOfContent()));
        return view;
    }

    public void updateContentCount(int position) {
        textContentCount.setText(String.valueOf(loginUser.getNumOfContent()));
        adapter.notifyDataSetChanged();
        listViewContent.setSelection(position);
    }

    class FileNameSort implements Comparator<Content> {
        public int compare(Content f1, Content f2) {
            return f1.getDateToMillisecond().compareTo(f2.getDateToMillisecond());
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public static String getStringFromByteString(String target, String regex) {
        String[] strArray = target.split(regex);

        byte[] byteArray = new byte[strArray.length];

        for (int i = 0; i < strArray.length; i++) {
            byteArray[i] = Byte.valueOf(strArray[i]);
        }

        return new String(byteArray);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                int position = data.getIntExtra("position", -1);
                Log.d(TAG, String.valueOf(position));
                Content editContent = data.getParcelableExtra("edit_content");
                totalContent.remove(position);
                totalContent.add(position, editContent);
                adapter.notifyDataSetChanged();
            } else if (requestCode == 1){
                int position = data.getIntExtra("position", 0);
                totalContent.remove(position);
                totalContent.add(position, (Content)data.getParcelableExtra("content"));
                adapter.notifyDataSetChanged();
            }
        }
    }
}