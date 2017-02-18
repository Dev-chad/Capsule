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
    private User loginUser;

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

    }

    private ArrayList<Content> listViewContentList = new ArrayList<Content>();
    private ViewHolder viewHolder;

    public ContentListViewAdapter(HomeFragment fragment, User loginUser) {
        this.fragment = fragment;
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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (content.getContentImage() == null) {
            viewHolder.imageViewContent.setVisibility(View.GONE);
        } else {
            viewHolder.imageViewContent.setVisibility(View.VISIBLE);
            BitmapWorkerTask task = new BitmapWorkerTask(context, viewHolder.imageViewContent);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, content.getContentImage());
//            task.execute(content.getContentImage());
        }

        viewHolder.imageViewPublisher.setImageURI(content.getPublisherProfileImage());
        viewHolder.textViewPublisher.setText(content.getPublisherName());

        if (content.getContentDesc().length() == 0) {
            viewHolder.textViewContent.setVisibility(View.GONE);
            viewHolder.textViewContentDetail.setVisibility(View.GONE);
            viewHolder.textViewLastContent.setVisibility(View.GONE);
        } else {
            String[] strContent = content.getContentDesc().split("\\n");
            if (strContent.length > 2) {
                viewHolder.textViewContent.setVisibility(View.VISIBLE);
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

        if (content.getTimeMode() == Content.MODE_TIME_ABSOLUTE) {
            viewHolder.textViewDate.setText(content.getDate());
        } else {
            viewHolder.textViewDate.setText(getTime(content.getDateToMillisecond()));

        }
        if (content.getLocation().length() > 0) {
            viewHolder.layoutLocation.setVisibility(View.VISIBLE);
            viewHolder.textViewLocation.setText(content.getLocation());
        } else {
            viewHolder.layoutLocation.setVisibility(View.GONE);
            viewHolder.textViewLocation.setText("");
        }

        viewHolder.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content.getTimeMode() == Content.MODE_TIME_ABSOLUTE) {
                    content.setTimeMode(Content.MODE_TIME_RELATIVE);
                } else {
                    content.setTimeMode(MODE_TIME_ABSOLUTE);
                }
                notifyDataSetChanged();
            }
        });

        if (!content.getPublisherEmail().equals(loginUser.getEmail())) {
            viewHolder.ibtnMenu.setVisibility(View.GONE);
        } else {
            viewHolder.ibtnMenu.setVisibility(View.VISIBLE);
        }
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
                                fragment.editContent(content, position);
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
                                for (int i = 1; i <= 7; i++) {
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

                                if (position == 0) {
                                    fragment.updateContentCount(0);
                                } else {
                                    fragment.updateContentCount(position - 1);
                                }
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

        viewHolder.ibtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ibtnLike.setBackgroundResource(R.mipmap.image_like_red);
            }
        });

        viewHolder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikeActivity.class);
                context.startActivity(intent);
            }
        });

        viewHolder.layoutReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                context.startActivity(intent);
            }
        });

        viewHolder.ibtnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                context.startActivity(intent);
            }
        });

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
            Log.d(TAG, "Original: " + options.outWidth + "x" + options.outHeight);
            Log.d(TAG, "View width: " + dm.widthPixels);
            options.inSampleSize = calculateInSampleSize(options, dm.widthPixels);
            options.inJustDecodeBounds = false;
            Log.d(TAG, "inSampleSize: " + options.inSampleSize);
            Bitmap bp = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriImage), null, options);
            Log.d(TAG, "Sampled: " + bp.getWidth() + "x" + bp.getHeight());
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

        public BitmapWorkerTask(Context context, ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.context = context;
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
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
