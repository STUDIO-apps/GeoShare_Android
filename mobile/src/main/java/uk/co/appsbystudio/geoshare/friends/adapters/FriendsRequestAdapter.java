package uk.co.appsbystudio.geoshare.friends.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.appsbystudio.geoshare.R;
import uk.co.appsbystudio.geoshare.utils.ProfileUtils;
import uk.co.appsbystudio.geoshare.utils.firebase.listeners.GetUserFromDatabase;

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.ViewHolder>{

    private final Context context;

    private final ArrayList userIncoming;
    private final DatabaseReference databaseReference;

    public interface Callback {
        void onAcceptReject(Boolean accept, String uid);
    }

    private final Callback callback;

    public FriendsRequestAdapter(Context context, ArrayList userIncoming, DatabaseReference databaseReference, Callback callback) {
        this.context = context;
        this.userIncoming = userIncoming;
        this.callback = callback;
        this.databaseReference = databaseReference;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friends_request_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        databaseReference.addListenerForSingleValueEvent(new GetUserFromDatabase(userIncoming.get(position).toString(), holder.friend_name));

        if (!userIncoming.isEmpty()) ProfileUtils.setProfilePicture(userIncoming.get(position).toString(), holder.friends_pictures, context.getCacheDir().toString());

        holder.accept_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAcceptReject(true, userIncoming.get(holder.getAdapterPosition()).toString());
            }
        });

        holder.decline_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAcceptReject(false, userIncoming.get(holder.getAdapterPosition()).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userIncoming.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final TextView friend_name;
        final CircleImageView friends_pictures;
        final ImageView accept_request;
        final ImageView decline_request;

        ViewHolder(View itemView) {
            super(itemView);
            friend_name = itemView.findViewById(R.id.friend_name);
            friends_pictures = itemView.findViewById(R.id.friend_profile_image);
            accept_request = itemView.findViewById(R.id.friend_accept);
            decline_request = itemView.findViewById(R.id.friend_reject);
        }
    }
}