package in.desireplace.waytogo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.desireplace.waytogo.R;
import in.desireplace.waytogo.models.Notifications;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Callback mCallback;

    private List<Notifications> mNotifications;

    private ProgressDialog dialog;

    public NotificationsAdapter(Callback callback, Context context) {
        mCallback = callback;
        mNotifications = new ArrayList<>();
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_item_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Notifications notifications = mNotifications.get(position);
        holder.mHeadingTextView.setText(notifications.getHeading());
        holder.mBodyTextView.setText(notifications.getBody());
        holder.mContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(notifications);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public interface Callback {
        void onItemClick(Notifications notifications);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mHeadingTextView;
        private TextView mBodyTextView;
        private View mContainerView;
        public ViewHolder(View itemView) {
            super(itemView);
            mHeadingTextView = itemView.findViewById(R.id.heading_text_view);
            mBodyTextView = itemView.findViewById(R.id.body_text_view);
            mContainerView = itemView.findViewById(R.id.container_card_view);
        }
    }
}
