package cloud_controller.user.crud_operations;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

import java.util.ArrayList;

public interface UserListCallback {
    public void userListCallback(ArrayList<WalkbyUser> walkbyUsers);
}
