package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-21.
 */

public class ReplyListAdapter extends BaseAdapter {

    public class ViewHolder {
        ImageView imageProfile;
        ImageView imageProfileSmall;
        TextView textNickname;
        TextView textReply;
        TextView textTime;
        TextView textAddReply;
        TextView textViewBeforeReply;
        TextView textLastReply;
        RelativeLayout layoutReplySmall;
        ImageButton ibtnMenu;
        TextView textLastReplyNickname;
    }

    private ReplyActivity activity;
    private ViewHolder viewHolder;
    private ArrayList<Reply> replyList;
    private User loginUser;
    private Content content;

    public ReplyListAdapter(ReplyActivity activity, User loginUser, Content content) {
        this.loginUser = loginUser;
        this.content = content;
        replyList = content.getReplyList();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return replyList.size();
    }

    @Override
    public Object getItem(int position) {
        return replyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();
        final Reply reply = replyList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_reply, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.imageProfileSmall = (ImageView) convertView.findViewById(R.id.image_profile_reply);
            viewHolder.textNickname = (TextView) convertView.findViewById(R.id.text_nickname);
            viewHolder.textReply = (TextView) convertView.findViewById(R.id.text_reply);
            viewHolder.textTime = (TextView) convertView.findViewById(R.id.text_time);
            viewHolder.textAddReply = (TextView) convertView.findViewById(R.id.text_add_reply);
            viewHolder.textLastReply = (TextView) convertView.findViewById(R.id.text_last_reply);
            viewHolder.textViewBeforeReply = (TextView) convertView.findViewById(R.id.text_reply_before);
            viewHolder.layoutReplySmall = (RelativeLayout) convertView.findViewById(R.id.layout_reply_small);
            viewHolder.ibtnMenu = (ImageButton) convertView.findViewById(R.id.ibtn_menu);
            viewHolder.textLastReplyNickname = (TextView) convertView.findViewById(R.id.text_last_reply_nickname);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageProfile.setImageURI(reply.getUser().getUriProfileImage());
        viewHolder.textNickname.setText(reply.getUser().getNickname());
        viewHolder.textReply.setText(reply.getDesc());

        viewHolder.textTime.setText(Utils.getTime(reply.getDateMilliSec()));

        if (reply.getUser().getNickname().equals(loginUser.getNickname())) {
            viewHolder.ibtnMenu.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ibtnMenu.setVisibility(View.GONE);
        }

        if (reply.getReplyList().size() > 0) {
            viewHolder.layoutReplySmall.setVisibility(View.VISIBLE);
            viewHolder.imageProfileSmall.setImageURI(reply.getReplyList().get(reply.getReplyList().size() - 1).getUser().getUriProfileImage());
            viewHolder.textLastReply.setText(reply.getReplyList().get(reply.getReplyList().size() - 1).getDesc());
            viewHolder.textLastReplyNickname.setText(reply.getReplyList().get(reply.getReplyList().size() - 1).getUser().getNickname());
            if (reply.getReplyList().size() > 1) {
                viewHolder.textViewBeforeReply.setVisibility(View.VISIBLE);
                viewHolder.textViewBeforeReply.setText("이전 답글 " + (reply.getReplyList().size() - 1) + "개");
            } else {
                viewHolder.textViewBeforeReply.setVisibility(View.GONE);
            }
        } else {
            viewHolder.layoutReplySmall.setVisibility(View.GONE);
            viewHolder.textViewBeforeReply.setVisibility(View.GONE);
        }

        viewHolder.textAddReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                                Intent intent = new Intent(context, ReplyEditActivity.class);
                                intent.putExtra("content", content);
                                intent.putExtra("reply", reply);
                                intent.putExtra("position", position);
                                intent.putExtra("mode", "댓글");
                                intent.putExtra("publisher", content.getPublisherEmail());
                                activity.startActivityForResult(intent, 1);
                                break;
                            case R.id.menu_delete:
                                SharedPreferences spContents = context.getSharedPreferences("contents", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spContentsEditor = spContents.edit();

                                String[] strTotalContents = spContents.getString(content.getPublisherEmail(), "").split(",");
                                String[] strContentsDetail = {};
                                int contentPosition = 0;

                                for (String strContentFind : strTotalContents) {
                                    String[] strContentFindDetail = strContentFind.split("::");
                                    if (strContentFindDetail[Const.CONTENT_TIME].equals(String.valueOf(content.getDateToMillisecond()))) {
                                        strContentsDetail = strContentFindDetail;
                                        break;
                                    }
                                    contentPosition++;
                                }

                                String[] strTotalReply = strContentsDetail[Const.CONTENT_REPLY].split("\\+");
                                String updatedReply = "";

                                for (String strReply : strTotalReply) {
                                    String[] strReplyDetail = strReply.split("/");

                                    if (!strReplyDetail[Const.REPLY_TIME].equals(String.valueOf(reply.getDateMilliSec()))) {
                                        if (updatedReply.equals("")) {
                                            updatedReply = strReply;
                                        } else {
                                            updatedReply += ("+" + strReply);
                                        }
                                    }
                                }

                                if (updatedReply.equals("")) {
                                    updatedReply = " ";
                                }

                                strContentsDetail[Const.CONTENT_REPLY] = updatedReply;
                                String updatedContent = strContentsDetail[0];
                                for (int i = 1; i < strContentsDetail.length; i++) {
                                    updatedContent += ("::" + strContentsDetail[i]);
                                }

                                strTotalContents[contentPosition] = updatedContent;
                                String updatedTotalContent = strTotalContents[0];
                                for (int i = 1; i < strTotalContents.length; i++) {
                                    updatedTotalContent += ("," + strTotalContents[i]);
                                }

                                spContentsEditor.putString(content.getPublisherEmail(), updatedTotalContent);
                                spContentsEditor.apply();

                                replyList.remove(position);
                                activity.setChanged(true);
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

        viewHolder.textAddReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Reply2Activity.class);
                intent.putExtra("login_user", loginUser);
                intent.putExtra("position", position);
                intent.putExtra("content", content);
                intent.putExtra("reply", reply);
                activity.startActivityForResult(intent, 0);
            }
        });

        viewHolder.textViewBeforeReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Reply2Activity.class);
                intent.putExtra("login_user", loginUser);
                intent.putExtra("position", position);
                intent.putExtra("content", content);
                intent.putExtra("reply", reply);
                activity.startActivityForResult(intent, 0);
            }
        });

        return convertView;
    }

    public void addReply(Reply reply) {
        replyList.add(reply);
        notifyDataSetChanged();
    }

}
