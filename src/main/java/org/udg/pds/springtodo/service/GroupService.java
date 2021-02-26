package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.GroupRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    public GroupRepository crud() {
        return groupRepository;
    }

    public Collection<Group> getGroups(Long id) {
        return userService.getUser(id).getGroups();
    }

    public Group getGroupAsMember(Long userId, Long id) {
        Optional<Group> g = groupRepository.findById(id);
        if (!g.isPresent()) throw new ServiceException("Group does not exists");
        if (g.get().getUser().getId() != userId)
            throw new ServiceException("User does not own this task");
        return g.get();
    }

    public Group getGroup(Long id) {
        Optional<Group> g = groupRepository.findById(id);
        if (!g.isPresent()) throw new ServiceException("Group does not exists");
        return g.get();
    }

    @Transactional
    public IdObject addGroup(Long ownerId, String name, String description) {
        try {
            User user = userService.getUser(ownerId);

            Group group = new Group(name,description);

            group.setUser(user);

            /**-----------------*/
            group.addUser(user);
            /**-----------------*/

            user.addGroup(group);

            groupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }
    /**------------------------------------------------------------------------
    @Transactional
    public void addUsersToGroup(Long userId, Long groupId, Collection<Long> members) {
        Group g = this.getGroupAsMember(userId,groupId);

        if (g.getUser().getId() != userId)
            throw new ServiceException("This user is not in the group");

        try {
            for (Long memberId : members) {
                User member = userService.getUser(userId);
                g.addUser(member);
            }
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }
    /**------------------------------------------------------------------------*/

    @Transactional
    public void addUserToGroup(Long userId, Long groupId) {
        Group g = this.getGroup(groupId);

        try {
                User member = userService.getUser(userId);
                g.addUser(member);
                member.addGroup(g);

        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    public Collection<User> getGroupMembers(Long userId, Long id) {
        Group g = this.getGroupAsMember(userId,id);
        User u = g.getUser();

        if (u.getId() != userId)
            throw new ServiceException("Logged user does not own the task");

        return g.getMembers();
    }
    /**------------------------------------------------------------------------*/


}
