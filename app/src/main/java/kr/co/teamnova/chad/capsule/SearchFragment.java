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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chad on 2017-01-18.
 */

public class SearchFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "SearchFragment";

    private User loginUser;
    private ContentListViewAdapter adapter;
    private ListView listViewContent;

    private TextView textNothingContent;
    private ArrayList<Content> totalContent;
    private TextView textContentCount;
    private TextView textFollowCount;
    private TextView textFollowerCount;

    private String loginUserEmail;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search, container, false);

        loginUserEmail = getArguments().getString("login_user");

        SharedPreferences spAccount = getContext().getSharedPreferences("account", MODE_PRIVATE);
        String[] strUserData = spAccount.getString(loginUserEmail, "").split(",");
        loginUser = new User(loginUserEmail, strUserData);

        adapter = new ContentListViewAdapter(this, loginUser);
        listViewContent = (ListView) view.findViewById(R.id.list_search);
        listViewContent.setAdapter(adapter);

        textNothingContent = (TextView) view.findViewById(R.id.text_no_content);
        textContentCount = (TextView) view.findViewById(R.id.text_content_count);
        textFollowCount = (TextView) view.findViewById(R.id.text_follow_count);
        textFollowerCount = (TextView) view.findViewById(R.id.text_follower_count);
        ImageView imageProfile = (ImageView) view.findViewById(R.id.image_profile);
        imageProfile.setImageURI(loginUser.getUriProfileImage());
        TextView textNickname = (TextView)view.findViewById(R.id.text_nickname);
        textNickname.setText(loginUser.getNickname());

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
                    strContentDesc = Utils.getStringFromByteString(strContentDetail[Const.CONTENT_DESCRIPTION], "\\+");
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
                        String strReply2 = strReplyDetail[Const.REPLY_REPLY];
                        ArrayList<Reply> reply2List = new ArrayList<>();
                        if(!strReply2.equals(" ")){
                            String[] strTotalReply2 = strReply2.split("#");
                            for(String strInnerReply : strTotalReply2){
                                String[] strInnerReplyDetail = strInnerReply.split("&");
                                Reply innerReply = new Reply(new User(strInnerReplyDetail[Const.REPLY_NICKNAME], spAccount.getString(strInnerReplyDetail[Const.REPLY_NICKNAME], "").split(",")), Utils.getStringFromByteString(strInnerReplyDetail[Const.REPLY_DECS], "\\*"), Long.valueOf(strInnerReplyDetail[Const.REPLY_TIME]));
                                reply2List.add(innerReply);
                            }
                        }
                        Reply replyItem = new Reply(new User(strReplyDetail[0], spAccount.getString(strReplyDetail[0], "").split(",")), Utils.getStringFromByteString(strReplyDetail[1], "#"), Long.valueOf(strReplyDetail[2]), reply2List);
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
                        strContentDetail[Const.CONTENT_FEELING],
                        strContentDetail[Const.CONTENT_WEATHER],
                        likeList,
                        replyList);

                totalContent.add(userContent);

            }
        }

        Collections.sort(totalContent, new FileNameSort());
        Collections.reverse(totalContent);
        adapter.addItemFromArray(totalContent);

        if (totalContent.size() == 0) {
            textNothingContent.setVisibility(View.VISIBLE);
        }

        textFollowerCount.setText(String.valueOf(loginUser.getFollowerList().size()));
        textFollowCount.setText(String.valueOf(loginUser.getFollowerList().size()));
        textContentCount.setText(String.valueOf(totalContent.size()));

        textFollowCount.setOnClickListener(this);
        textFollowerCount.setOnClickListener(this);

        return view;
    }
    class FileNameSort implements Comparator<Content> {
        public int compare(Content f1, Content f2) {
            return f1.getDateToMillisecond().compareTo(f2.getDateToMillisecond());
        }
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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences spAccount = getContext().getSharedPreferences("account", MODE_PRIVATE);
        String[] strUserData = spAccount.getString(loginUserEmail, "").split(",");
        loginUser = new User(loginUserEmail, strUserData);

        textContentCount.setText(String.valueOf(loginUser.getNumOfContent()));
        textFollowCount.setText(String.valueOf(loginUser.getFollowList().size()));
        textFollowerCount.setText(String.valueOf(loginUser.getFollowerList().size()));
    }

    public boolean isLastVisibleList(int position){
        return listViewContent.getLastVisiblePosition() == position;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(getContext(), LikeActivity.class);
        switch(id){
            case R.id.text_follow_count:
                intent.putExtra("mode", "팔로우");
                intent.putStringArrayListExtra("like_user_list",loginUser.getFollowList());
                break;
            case R.id.text_follower_count:
                intent.putExtra("mode", "팔로워");
                intent.putStringArrayListExtra("like_user_list", loginUser.getFollowerList());
                break;
        }

        startActivity(intent);
    }
}
