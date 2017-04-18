package cn.edu.hfuu.service;

import cn.edu.hfuu.dao.UserDao;
import cn.edu.hfuu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Repository
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public void updataroles(String roles,String name){
        userDao.updateUserRoles(roles,name);
    }

    @Transactional
    public void deleteuserByName(String name){
        userDao.deleteUser(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UsernameNotFoundException("[" + username + "] not found!!!");
    }
}
