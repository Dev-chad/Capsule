package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    }

    private ArrayList<ListViewContent> listViewContentList = new ArrayList<ListViewContent>();

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
        final ViewHolder viewHolder;
        final ListViewContent listViewContent = listViewContentList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_content, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageViewPublisher = (ImageView) convertView.findViewById(R.id.image_publisher);
            viewHolder.imageViewContent = (ImageView) convertView.findViewById(R.id.image_content);
            viewHolder.layoutLocation = (LinearLayout) convertView.findViewById(R.id.layout_location);
            viewHolder.textViewLocation = (TextView)convertView.findViewById(R.id.text_location);
            viewHolder.textViewPublisher = (TextView) convertView.findViewById(R.id.text_publisher);
            viewHolder.textViewContent = (TextView) convertView.findViewById(R.id.text_content);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.text_date);
            viewHolder.ibtnMenu = (ImageButton) convertView.findViewById(R.id.ibtn_menu);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (listViewContent.getContentImage() == null) {
            viewHolder.imageViewContent.setVisibility(View.GONE);
        } else {
            viewHolder.imageViewContent.setVisibility(View.VISIBLE);
            viewHolder.imageViewContent.setImageURI(listViewContent.getContentImage());
        }

        viewHolder.imageViewPublisher.setImageURI(listViewContent.getPublisherProfileImage());
        viewHolder.textViewPublisher.setText(listViewContent.getPublisherName());
        viewHolder.textViewContent.setText(listViewContent.getContentDesc());
        if(viewHolder.textViewContent.length() == 0){
            viewHolder.textViewContent.setVisibility(View.GONE);
        } else {
            viewHolder.textViewContent.setVisibility(View.VISIBLE);
        }

        viewHolder.textViewDate.setText(getTime(listViewContent.getDateToMillisecond()));
        if(listViewContent.getLocation() != null && !listViewContent.getLocation().equals("")){
            viewHolder.layoutLocation.setVisibility(View.VISIBLE);
            viewHolder.textViewLocation.setText(listViewContent.getLocation());
        }else{
            viewHolder.layoutLocation.setVisibility(View.GONE);
            viewHolder.textViewLocation.setText("");
        }

        viewHolder.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewContent.getTimeMode() == listViewContent.MODE_TIME_ABSOLUTE && viewHolder.textViewDate.getText().toString().contains("전")) {
                    listViewContent.setTimeMode(ListViewContent.MODE_TIME_RELATIVE);
                }

                if (listViewContent.getTimeMode() == ListViewContent.MODE_TIME_RELATIVE) {
                    listViewContent.setTimeMode(ListViewContent.MODE_TIME_ABSOLUTE);
                    viewHolder.textViewDate.setText(listViewContent.getDate());
                } else {
                    listViewContent.setTimeMode(ListViewContent.MODE_TIME_RELATIVE);
                    viewHolder.textViewDate.setText(getTime(listViewContent.getDateToMillisecond()));
                }
            }
        });

        if(!listViewContent.getPublisherEmail().equals(loginUser.getEmail())){
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
                                fragment.editContent(listViewContent);
                                break;
                            case R.id.menu_delete:
                                File descFile = new File("/data/data/" + context.getPackageName() + "/User/" + listViewContent.getPublisherEmail() + "/Contents/" + listViewContent.getFileName() + ".txt");
                                if (descFile.exists()) {
                                    descFile.delete();
                                } else {
                                    Log.e(TAG, "descFile does not exist " + descFile.getPath());
                                }

                                File imageFile = new File("/data/data/" + context.getPackageName() + "/User/" + listViewContent.getPublisherEmail() + "/Contents/" + listViewContent.getFileName() + ".jpg");
                                if (imageFile.exists()) {
                                    imageFile.delete();
                                }
                                listViewContentList.remove(position);

                                SharedPreferences sp = context.getSharedPreferences(listViewContent.getPublisherEmail(), Context.MODE_PRIVATE);
                                SharedPreferences.Editor spEditor = sp.edit();

                                int numOfContent = sp.getInt("num_of_content", -1);
                                numOfContent--;
                                spEditor.putInt("num_of_content", numOfContent);
                                spEditor.apply();

                                Log.d(TAG, "Position: " + position + "  size(): " + listViewContentList.size());
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



        return convertView;
    }

    public void addItem(Uri contentImage, String contentDesc, Uri publisherImage, String publisherName, String publisherEmail, long date, String fileName, String location) {
        ListViewContent content = new ListViewContent();

        if (contentImage != null) {
            content.setImage(contentImage);
        }

        content.setDateFromLong(date);
        content.setDesc(contentDesc);
        content.setPublisher(publisherName);
        content.setPublisherEmail(publisherEmail);
        content.setPublisherProfileImage(publisherImage);
        content.setFileName(fileName);
        if(!location.equals("")){
            content.setLocation(location);
        }

        listViewContentList.add(0, content);
        notifyDataSetChanged();
    }

    public void addItemFromArray(ArrayList<ListViewContent> list){
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

}
