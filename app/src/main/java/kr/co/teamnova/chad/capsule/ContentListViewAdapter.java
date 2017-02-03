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
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-03.
 */

public class ContentListViewAdapter extends BaseAdapter {

    public class ViewHolder {
        public ImageView imageViewPublisher;
        public ImageView imageViewContent;
        public TextView textViewPublisher;
        public TextView textViewContent;
        public TextView textViewDate;
        public ImageButton ibtnMenu;
    }

    public interface OnClickBtnListener {
        public void btnClickEvent();
    }

    OnClickBtnListener mCallback = null;
    private ArrayList<ListViewContent> listViewContentList = new ArrayList<ListViewContent>();

    public ContentListViewAdapter() {

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
        ViewHolder viewHolder;
        final ListViewContent listViewContent = listViewContentList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_content, parent, false);
            viewHolder = new ViewHolder();

            try {
                mCallback = (OnClickBtnListener) context;

            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + "must implement OnArticleSelectedListener");
            }

            viewHolder.imageViewPublisher = (ImageView) convertView.findViewById(R.id.image_publisher);
            viewHolder.imageViewContent = (ImageView) convertView.findViewById(R.id.image_content);

            viewHolder.textViewPublisher = (TextView) convertView.findViewById(R.id.text_publisher);
            viewHolder.textViewContent = (TextView) convertView.findViewById(R.id.text_content);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.text_date);
            viewHolder.ibtnMenu = (ImageButton) convertView.findViewById(R.id.ibtn_menu);
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
                                    break;
                                case R.id.menu_delete:
                                    File descFile = new File("/data/data/" + context.getPackageName() + "/User/" + listViewContent.getPublisherEmail() + "/Contents/" + listViewContent.getFileName() + ".txt");
                                    if (descFile.exists()) {
                                        descFile.delete();
                                    } else {
                                        Log.e("error", "descFile does not exist " + descFile.getPath());
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

                                    mCallback.btnClickEvent();
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
        viewHolder.textViewDate.setText(listViewContent.getDate());

        return convertView;
    }

    public void addItem(Uri contentImage, String contentDesc, Uri publisherImage, String publisherName, String publisherEmail, String date, String fileName) {
        ListViewContent content = new ListViewContent();

        if (contentImage != null) {
            content.setImage(contentImage);
        }

        content.setDate(date);
        content.setDesc(contentDesc);
        content.setPublisher(publisherName);
        content.setPublisherEmail(publisherEmail);
        content.setPublisherProfileImage(publisherImage);
        content.setFileName(fileName);

        listViewContentList.add(content);
    }
}
