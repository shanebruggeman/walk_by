package cloud_controller.user.crud_operations;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

public interface UserRetrievedCallback {
    public void userHasBeenRetrieved(WalkbyUser user);
}
