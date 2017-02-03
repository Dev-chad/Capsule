package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-03.
 */

public class ContentListViewAdapter extends BaseAdapter {

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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_content, parent, false);
        }

        ImageView imageViewPublisher = (ImageView) convertView.findViewById(R.id.image_publisher);
        ImageView imageViewContent = (ImageView) convertView.findViewById(R.id.image_content);

        TextView textViewPublisher = (TextView) convertView.findViewById(R.id.text_publisher);
        TextView textViewContent = (TextView) convertView.findViewById(R.id.text_content);
        final TextView textViewDate = (TextView) convertView.findViewById(R.id.text_date);

        final ListViewContent listViewContent = listViewContentList.get(position);

        imageViewPublisher.setImageURI(listViewContent.getPublisherProfileImage());

        if (listViewContent.getContentImage() == null) {
            imageViewContent.setVisibility(View.GONE);
        } else {
            imageViewContent.setVisibility(View.VISIBLE);
            imageViewContent.setImageURI(listViewContent.getContentImage());
        }

        textViewPublisher.setText(listViewContent.getPublisherName());
        textViewContent.setText(listViewContent.getContentDesc());
        textViewDate.setText(listViewContent.getDate());

        ImageButton ibtnMenu = (ImageButton) convertView.findViewById(R.id.ibtn_menu);
        ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

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
