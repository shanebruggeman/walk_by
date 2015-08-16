package cloud_controller.user.crud_operations;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

import java.util.List;

public interface UserEncounteredUsersCallback {
    public void userEncounteredUsersCallback(List<WalkbyUser> encountered);
}
