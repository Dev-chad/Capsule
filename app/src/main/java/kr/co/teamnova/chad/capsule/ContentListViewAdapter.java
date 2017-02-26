package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static kr.co.teamnova.chad.capsule.Content.MODE_TIME_ABSOLUTE;

/**
 * Created by Chad on 2017-02-03.
 */

public class ContentListViewAdapter extends BaseAdapter {

    private static final String TAG = "ContentListViewAdapter";
    private HomeFragment fragment;
    private SearchFragment fragment2;
    private User loginUser;
    private boolean animCheck = true;
    private ArrayList<Integer> positionArray = new ArrayList<>();


    public class ViewHolder {
        public ImageView imageViewPublisher;
        public ImageView imageViewContent;
        public TextView textViewPublisher;
        public TextView textViewContent;
        public TextView textViewDate;
        public ImageButton ibtnMenu;
        public LinearLayout layoutLocation;
        public TextView textViewLocation;
        public TextView textViewContentDetail;
        public TextView textViewLastContent;
        public ImageButton ibtnLike;
        public ImageButton ibtnReply;
        public LinearLayout layoutReply;
        public LinearLayout layoutLike;
        public TextView textLikeCount;
        public TextView textReplyCount;
        public ImageView imageFeeling;
        public ImageView imageWeather;
        public int mPosition;
    }

    private ArrayList<Content> listViewContentList = new ArrayList<Content>();
    private ViewHolder viewHolder;

    public ContentListViewAdapter(HomeFragment fragment, User loginUser) {
        this.fragment = fragment;
        this.loginUser = loginUser;
    }

    public ContentListViewAdapter(SearchFragment fragment, User loginUser) {
        this.fragment2 = fragment;
        this.loginUser = loginUser;
    }

    @Override
    public int getCount() {
        return listViewContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();

        final Content content = listViewContentList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_content, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageViewPublisher = (ImageView) convertView.findViewById(R.id.image_publisher);
            viewHolder.imageViewContent = (ImageView) convertView.findViewById(R.id.image_content);
            viewHolder.layoutLocation = (LinearLayout) convertView.findViewById(R.id.layout_location);
            viewHolder.textViewLocation = (TextView) convertView.findViewById(R.id.text_location);
            viewHolder.textViewPublisher = (TextView) convertView.findViewById(R.id.text_publisher);
            viewHolder.textViewContent = (TextView) convertView.findViewById(R.id.text_content);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.text_date);
            viewHolder.ibtnMenu = (ImageButton) convertView.findViewById(R.id.ibtn_menu);
            viewHolder.textViewContentDetail = (TextView) convertView.findViewById(R.id.text_content_detail);
            viewHolder.textViewLastContent = (TextView) convertView.findViewById(R.id.text_content_last);
            viewHolder.ibtnLike = (ImageButton) convertView.findViewById(R.id.ibtn_like);
            viewHolder.ibtnReply = (ImageButton) convertView.findViewById(R.id.ibtn_reply);
            viewHolder.layoutLike = (LinearLayout) convertView.findViewById(R.id.layout_like);
            viewHolder.layoutReply = (LinearLayout) convertView.findViewById(R.id.layout_reply);
            viewHolder.textLikeCount = (TextView) convertView.findViewById(R.id.text_like_count);
            viewHolder.textReplyCount = (TextView) convertView.findViewById(R.id.text_reply_count);
            viewHolder.imageFeeling = (ImageView) convertView.findViewById(R.id.image_feeling);
            viewHolder.imageWeather = (ImageView) convertView.findViewById(R.id.image_weather);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mPosition = position;

        // 게시물 프로필
        viewHolder.imageViewPublisher.setImageURI(content.getPublisherProfileImage());
        viewHolder.textViewPublisher.setText(content.getPublisherName());

        // 게시물 이미지

        if (content.getContentImage() == null) {
            viewHolder.imageViewContent.setVisibility(View.GONE);
        } else {
            if(animCheck){
                if(viewHolder.imageViewContent.getDrawable() != null){
                    viewHolder.imageViewContent.setImageAlpha(0);
                }
            } else {
                positionArray.add(position);
            }
            viewHolder.imageViewContent.setVisibility(View.VISIBLE);
            BitmapWorkerTask task = new BitmapWorkerTask(context, viewHolder.imageViewContent, position, viewHolder);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, content.getContentImage());
        }

        // 게시물 내용 표시 및 더 보기(접기) 글씨 표시 여부
        if (content.getContentDesc().length() == 0) {
            viewHolder.textViewContent.setVisibility(View.GONE);
            viewHolder.textViewContentDetail.setVisibility(View.GONE);
            viewHolder.textViewLastContent.setVisibility(View.GONE);
        } else {
            viewHolder.textViewContent.setVisibility(View.VISIBLE);
            String[] strContent = content.getContentDesc().split("\\n");
            if (strContent.length > 2) {
                viewHolder.textViewLastContent.setVisibility(View.VISIBLE);
                viewHolder.textViewContentDetail.setVisibility(View.VISIBLE);
                if (content.isContentDetailCheck()) {
                    int maxLine = content.getContentDesc().split("\\n").length;
                    viewHolder.textViewContent.setMaxLines(maxLine - 1);
                    viewHolder.textViewContentDetail.setText("   - 접기 -");
                    String strUpper = strContent[0];
                    for (int i = 1; i < strContent.length - 1; i++) {
                        strUpper += ('\n' + strContent[i]);
                    }
                    String strLower = strContent[strContent.length - 1];
                    viewHolder.textViewContent.setText(strUpper);
                    viewHolder.textViewLastContent.setText(strLower);
                } else {
                    viewHolder.textViewContent.setText(strContent[0]);
                    viewHolder.textViewLastContent.setText(strContent[1]);
                    viewHolder.textViewContentDetail.setText(" ...더 보기");
                }
            } else {
                viewHolder.textViewContent.setText(content.getContentDesc());
                viewHolder.textViewLastContent.setVisibility(View.GONE);
                viewHolder.textViewContentDetail.setVisibility(View.GONE);
            }
        }

        // 위치
        if (content.getLocation().length() > 0) {
            viewHolder.layoutLocation.setVisibility(View.VISIBLE);
            viewHolder.textViewLocation.setText(content.getLocation());
        } else {
            viewHolder.layoutLocation.setVisibility(View.GONE);
            viewHolder.textViewLocation.setText("");
        }

        if (content.getFeeling().equals(" ")) {
            viewHolder.imageFeeling.setVisibility(View.GONE);
        } else {
            viewHolder.imageFeeling.setVisibility(View.VISIBLE);
            viewHolder.imageFeeling.setBackgroundResource(Integer.valueOf(content.getFeeling()));
        }

        if (content.getWeather().equals(" ")) {
            viewHolder.imageWeather.setVisibility(View.GONE);
        } else {
            viewHolder.imageWeather.setVisibility(View.VISIBLE);
            viewHolder.imageWeather.setBackgroundResource(Integer.valueOf(content.getWeather()));
        }

        // 게시물 작성 시간
        if (content.getTimeMode() == Content.MODE_TIME_ABSOLUTE) {
            viewHolder.textViewDate.setText(content.getDate());
        } else {
            viewHolder.textViewDate.setText(getTime(content.getDateToMillisecond()));

        }

        // 게시물 메뉴 표시 여부
        if (!content.getPublisherEmail().equals(loginUser.getEmail())) {
            viewHolder.ibtnMenu.setVisibility(View.GONE);
        } else {
            viewHolder.ibtnMenu.setVisibility(View.VISIBLE);
        }

        // 좋아요 버튼 색
        if (content.getLikeUserList().contains(loginUser.getEmail())) {
            viewHolder.ibtnLike.setBackgroundResource(R.mipmap.image_like_red);
        } else {
            viewHolder.ibtnLike.setBackgroundResource(R.mipmap.image_like);
        }

        // 좋아요 개수 표시 여부
        if (content.getLikeCount() > 0) {
            viewHolder.layoutLike.setVisibility(View.VISIBLE);
            viewHolder.textLikeCount.setText(content.getLikeCount() + "개");
        } else {
            viewHolder.layoutLike.setVisibility(View.GONE);
        }

        // 댓글 개수 표시 여부
        if (content.getReplyCount() > 0) {
            viewHolder.layoutReply.setVisibility(View.VISIBLE);
            viewHolder.textReplyCount.setText(content.getReplyCount() + "개");
        } else {
            viewHolder.layoutReply.setVisibility(View.GONE);
        }


        // 시간 터치
        viewHolder.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animCheck = false;
                if (content.getTimeMode() == Content.MODE_TIME_ABSOLUTE) {
                    content.setTimeMode(Content.MODE_TIME_RELATIVE);
                } else {
                    content.setTimeMode(MODE_TIME_ABSOLUTE);
                }
                notifyDataSetChanged();
            }
        });


        // 게시물 메뉴 터치
        viewHolder.ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(parent.getContext(), v);
                p.getMenuInflater().inflate(R.menu.popup_menu_listview, p.getMenu());
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Intent intent = new Intent(context, AddContentActivity.class);
                                intent.putExtra("edit_content", content);
                                intent.putExtra("login_user", loginUser.getEmail());
                                intent.putExtra("position", position);
                                if (fragment == null) {
                                    fragment2.startActivityForResult(intent, 0);
                                } else {
                                    fragment.startActivityForResult(intent, 0);
                                }
                                break;
                            case R.id.menu_delete:
                                File imageFile = new File(context.getFilesDir() + "/contents/" + loginUser.getEmail() + '/' + content.getDateToMillisecond() + ".jpg");
                                if (imageFile.exists()) {
                                    imageFile.delete();
                                }

                                SharedPreferences spContents = context.getSharedPreferences("contents", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spContentsEditor = spContents.edit();
                                SharedPreferences spAccount = context.getSharedPreferences("account", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spAccountEditor = spAccount.edit();

                                String[] strUserData = spAccount.getString(loginUser.getEmail(), "").split(",");

                                int currentNumOfContent = loginUser.getNumOfContent();
                                loginUser.setNumOfContent(--currentNumOfContent);
                                strUserData[Const.INDEX_NUM_OF_CONTENT] = String.valueOf(currentNumOfContent);

                                String strEditUserData = strUserData[0];
                                for (int i = 1; i < strUserData.length; i++) {
                                    strEditUserData += (',' + strUserData[i]);
                                }

                                spAccountEditor.putString(loginUser.getEmail(), strEditUserData);
                                spAccountEditor.apply();

                                String strEditTotalContents = "";
                                String[] strTotalContents = spContents.getString(loginUser.getEmail(), "").split(",");

                                for (String strFind : strTotalContents) {
                                    String[] strDetail = strFind.split("::");
                                    if (!strDetail[Const.CONTENT_TIME].equals(String.valueOf(content.getDateToMillisecond()))) {
                                        if (strEditTotalContents.length() == 0) {
                                            strEditTotalContents = strFind;
                                        } else {
                                            strEditTotalContents += ("," + strFind);
                                        }
                                    }
                                }

                                spContentsEditor.putString(loginUser.getEmail(), strEditTotalContents);
                                spContentsEditor.apply();

                                listViewContentList.remove(position);
                                notifyDataSetChanged();
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
                p.show();

            }
        });

        // 좋아요 아이콘 터치
        viewHolder.ibtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animCheck = false;
                // sp에 저장된 게시물 리스트에서 현재 게시물이 어디에 위치한지 알기 위한 변수
                int contentPosition = 0;

                SharedPreferences spAccount = context.getSharedPreferences("account", Context.MODE_PRIVATE);
                SharedPreferences.Editor spAccountEditor = spAccount.edit();

                SharedPreferences spContent = context.getSharedPreferences("contents", Context.MODE_PRIVATE);
                SharedPreferences.Editor spContentEditor = spContent.edit();

                //로그인 유저의 data
                String[] strLoginUserArray = spAccount.getString(loginUser.getEmail(), "").split(",");

                //현재 게시물 작성자의 전체 content data
                String[] strTotalContentArray = spContent.getString(content.getPublisherEmail(), "").split(",");

                //현재 게시물 데이터를 저장할 어레이
                String[] strContentArray = {};

                for (String strFindContent : strTotalContentArray) {
                    String[] strFindContentDetail = strFindContent.split("::");
                    if (strFindContentDetail[Const.CONTENT_TIME].equals(String.valueOf(content.getDateToMillisecond()))) {
                        //현재 게시물의 작성시간과 일치하면 현재 게시물에 대한 데이터
                        strContentArray = strFindContentDetail;
                        break;
                    }
                    contentPosition++;
                }

                String updateLikeContent = "";
                String updateLikeUser = "";
                if (content.getLikeUserList().contains(loginUser.getEmail())) {
                    viewHolder.ibtnLike.setBackgroundResource(R.mipmap.image_like);
                    content.getLikeUserList().remove(loginUser.getEmail());
                    loginUser.getLikeContentList().remove(content.getPublisherEmail() + "+" + content.getDateToMillisecond());

                    if (loginUser.getLikeContentList().size() > 0) {
                        updateLikeContent = loginUser.getLikeContentList().get(0);
                        for (int i = 1; i < loginUser.getLikeContentList().size(); i++) {
                            updateLikeContent += ("::" + loginUser.getLikeContentList().get(i));
                        }
                    } else {
                        updateLikeContent = " ";
                    }

                    if (content.getLikeCount() > 0) {
                        updateLikeUser = content.getLikeUserList().get(0);
                        for (int i = 1; i < content.getLikeUserList().size(); i++) {
                            updateLikeUser += ("+" + content.getLikeUserList().get(i));
                        }
                    } else {
                        updateLikeUser = " ";
                    }
                } else {
                    viewHolder.ibtnLike.setBackgroundResource(R.mipmap.image_like_red);
                    content.getLikeUserList().add(loginUser.getEmail());
                    loginUser.getLikeContentList().add(content.getPublisherEmail() + "+" + content.getDateToMillisecond());

                    updateLikeContent = loginUser.getLikeContentList().get(0);
                    for (int i = 1; i < loginUser.getLikeContentList().size(); i++) {
                        updateLikeContent += ("::" + loginUser.getLikeContentList().get(i));
                    }

                    updateLikeUser = content.getLikeUserList().get(0);
                    for (int i = 1; i < content.getLikeUserList().size(); i++) {
                        updateLikeUser += ("+" + content.getLikeUserList().get(i));
                    }
                }

                strLoginUserArray[Const.INDEX_LIKE_CONTENT] = updateLikeContent;
                String updateLoginUser = strLoginUserArray[0];
                for (int i = 1; i < strLoginUserArray.length; i++) {
                    updateLoginUser += ("," + strLoginUserArray[i]);
                }

                strContentArray[Const.CONTENT_LIKE_USER] = updateLikeUser;
                String updateContent = strContentArray[0];

                for (int i = 1; i < strContentArray.length; i++) {
                    updateContent += ("::" + strContentArray[i]);
                }

                strTotalContentArray[contentPosition] = updateContent;
                String updateTotalContent = strTotalContentArray[0];

                for (int i = 1; i < strTotalContentArray.length; i++) {
                    updateTotalContent += ("," + strTotalContentArray[i]);
                }

                spAccountEditor.putString(loginUser.getEmail(), updateLoginUser);
                spContentEditor.putString(content.getPublisherEmail(), updateTotalContent);

                Log.d(TAG, "Login user: " + updateLoginUser);
                Log.d(TAG, "Content: " + updateContent);

                spAccountEditor.apply();
                spContentEditor.apply();

                notifyDataSetChanged();
            }
        });

        // 좋아요 글씨 터치
        viewHolder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikeActivity.class);
                intent.putExtra("mode", "좋아요");
                intent.putExtra("login_user", loginUser.getEmail());
                intent.putExtra("like_user_list", content.getLikeUserList());
                context.startActivity(intent);
            }
        });

        // 댓글 글씨 터치
        viewHolder.layoutReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("content", content);
                intent.putExtra("login_user", loginUser);
                intent.putExtra("position", position);
                if (fragment == null) {
                    fragment2.startActivityForResult(intent, 1);

                } else {
                    fragment.startActivityForResult(intent, 1);
                }
            }
        });

        // 댓글 아이콘 터치
        viewHolder.ibtnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("content", content);
                intent.putExtra("login_user", loginUser);
                intent.putExtra("position", position);
                if (fragment == null) {
                    fragment2.startActivityForResult(intent, 1);

                } else {
                    fragment.startActivityForResult(intent, 1);
                }
            }
        });

        // 더 보기(접기) 터치
        viewHolder.textViewContentDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content.isContentDetailCheck()) {
                    content.setContentDetailCheck(false);
                } else {
                    content.setContentDetailCheck(true);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    public void addItem(Uri contentImage, String contentDesc, Uri publisherImage, String publisherName, String publisherEmail, long date, String location) {
        Content content = new Content();

        if (contentImage != null) {
            content.setImage(contentImage);
        }

        content.setDateFromLong(date);
        content.setDesc(contentDesc);
        content.setPublisher(publisherName);
        content.setPublisherEmail(publisherEmail);
        content.setPublisherProfileImage(publisherImage);
        if (!location.equals("")) {
            content.setLocation(location);
        }

        listViewContentList.add(0, content);
        notifyDataSetChanged();
    }

    public void addItemFromArray(ArrayList<Content> list) {
        listViewContentList = list;
        notifyDataSetChanged();
    }

    public String getTime(long savedTime) {
        String date = "";
        long currentTime = System.currentTimeMillis();
        long subTime = currentTime - savedTime;

        if (subTime / 60000 < 1) {
            date = "방금 전";
        } else if (subTime / 60000 < 60) {
            date = (subTime / 60000) + "분 전";
        } else if (subTime / 3600000 < 24) {
            date = (subTime / 3600000) + "시간 전";
        } else if (subTime / 86400000 < 7) {
            date = (subTime / 86400000) + "일 전";
        } else {
            date = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(new Date(savedTime));
        }
        return date;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth) {
        // Raw height and width of image
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromUri(Context context, Uri uriImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriImage), null, options);
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
//            Log.d(TAG, "Original: " + options.outWidth + "x" + options.outHeight);
//            Log.d(TAG, "View width: " + dm.widthPixels);
            options.inSampleSize = calculateInSampleSize(options, dm.widthPixels);
            options.inJustDecodeBounds = false;
//            Log.d(TAG, "inSampleSize: " + options.inSampleSize);
            Bitmap bp = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriImage), null, options);
//            Log.d(TAG, "Sampled: " + bp.getWidth() + "x" + bp.getHeight());
            return bp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private Uri data = null;
        private Context context;
        private int setPosition;
        private ViewHolder mHolder;

        public BitmapWorkerTask(Context context, ImageView imageView, int setPosition, ViewHolder mHolder) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.context = context;
            this.setPosition = setPosition;
            this.mHolder = mHolder;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Uri... params) {
            data = params[0];
            return decodeSampledBitmapFromUri(context, data);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(animCheck){
                imageViewReference.get().setImageAlpha(255);
                if (fragment != null) {
                    if (fragment.isAdded() && bitmap != null) {
                        final ImageView imageView = imageViewReference.get();
                        if (imageView != null && setPosition == mHolder.mPosition) {
                            Animation flowAnimation;
                            flowAnimation = AnimationUtils.loadAnimation(fragment.getActivity(), R.anim.alpha);
                            imageView.startAnimation(flowAnimation);
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                } else if (fragment2 != null) {
                    if (fragment2.isAdded() && bitmap != null) {
                        final ImageView imageView = imageViewReference.get();
                        if (imageView != null && setPosition == mHolder.mPosition) {
                            Animation flowAnimation;
                            flowAnimation = AnimationUtils.loadAnimation(fragment2.getActivity(), R.anim.alpha);
                            imageView.startAnimation(flowAnimation);
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }
            } else {
                positionArray.remove(Integer.valueOf(setPosition));
                if(fragment != null){
                    Log.d(TAG, String.valueOf(setPosition));
                    if(fragment.isLastVisibleList(setPosition)){
                        if (positionArray.size() == 0){
                            animCheck = true;
                        }
                    }
                } else if(fragment2 != null){
                    if(fragment2.isLastVisibleList(setPosition)){
                        if (positionArray.size() == 0){
                            animCheck = true;
                        }
                    }

                }
            }

        }
    }

    public void editList(int position, Content content) {
        listViewContentList.remove(position);
        listViewContentList.add(position, content);
//        Log.d(TAG, listViewContentList.get(position).getContentDesc());
        notifyDataSetChanged();
    }

    private int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
