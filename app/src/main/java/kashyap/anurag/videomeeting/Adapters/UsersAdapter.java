package kashyap.anurag.videomeeting.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.videomeeting.Listener.UsersListener;
import kashyap.anurag.videomeeting.Models.User;
import kashyap.anurag.videomeeting.R;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;
    private UsersListener usersListener;
    private List<User> selectedUsers;

    public UsersAdapter(List<User> users, UsersListener usersListener) {
        this.users = users;
        this.usersListener = usersListener;
        selectedUsers = new ArrayList<>();
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textFirstChar, textUserName, textEmail;
        ImageView imageAudioMeeting, imageVideoMeeting, imageSelected;
        RelativeLayout userContainer;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            textFirstChar = itemView.findViewById(R.id.textFirstChar);
            textUserName = itemView.findViewById(R.id.textUserName);
            textEmail = itemView.findViewById(R.id.textEmail);
            imageAudioMeeting = itemView.findViewById(R.id.imageAudioMeeting);
            imageVideoMeeting = itemView.findViewById(R.id.imageVideoMeeting);
            userContainer = itemView.findViewById(R.id.userContainer);
            imageSelected = itemView.findViewById(R.id.imageSelected);
        }

        void setUserData(User user) {
            textFirstChar.setText(user.firstName.substring(0, 1));
            textUserName.setText(String.format("%s %s", user.firstName, user.firstName, user.lastName));
            textEmail.setText(user.email);
            imageAudioMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usersListener.initiateAudioMeeting(user);
                }
            });
            imageVideoMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usersListener.initiateVideoMeeting(user);
                }
            });

            userContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (imageSelected.getVisibility() != View.VISIBLE){
                        selectedUsers.add(user);
                        imageSelected.setVisibility(View.VISIBLE);
                        imageAudioMeeting.setVisibility(View.GONE);
                        imageVideoMeeting.setVisibility(View.GONE);
                        textFirstChar.setText(null);
                        usersListener.onMultipleUsersActions(true);
                    }
                    return true;
                }
            });

            userContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageSelected.getVisibility() == View.VISIBLE) {
                        selectedUsers.remove(user);
                        imageSelected.setVisibility(View.GONE);
                        imageAudioMeeting.setVisibility(View.VISIBLE);
                        imageVideoMeeting.setVisibility(View.VISIBLE);
                        if (selectedUsers.size() == 0) {
                            usersListener.onMultipleUsersActions(false);
                        } else {
                            if (selectedUsers.size() > 0) {
                                selectedUsers.add(user);
                                imageSelected.setVisibility(View.VISIBLE);
                                imageVideoMeeting.setVisibility(View.GONE);
                                imageAudioMeeting.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        }

    }
}
