package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;
import java.util.List;

public interface UserDAO {
    List<User> getAllUsers();

    User save(User user);

    User findByEmail(String email);

    User findByID(int theId);

    void deleteById(int theId);

    void updateUserEnabledStatus(int id, boolean enabled);
}
