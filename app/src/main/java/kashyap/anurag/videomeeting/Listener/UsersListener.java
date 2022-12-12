package kashyap.anurag.videomeeting.Listener;

import kashyap.anurag.videomeeting.Models.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);

    void initiateAudioMeeting(User user);

    void onMultipleUsersActions(Boolean isMultipleUserSelected);

}
