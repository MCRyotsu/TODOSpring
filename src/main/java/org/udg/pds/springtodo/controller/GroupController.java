package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.controller.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Tag;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.serializer.JsonDateDeserializer;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@RequestMapping(path="/groups")
@RestController
public class GroupController extends BaseController {

    @Autowired
    GroupService groupService;

    @GetMapping(path="/{id}")
    public Group getGroup(HttpSession session,
                        @PathVariable("id") Long id) {
        Long userId = getLoggedUser(session);

        return groupService.getGroupAsMember(userId, id);
    }

    @GetMapping
    @JsonView(Views.Private.class)
    public Collection<Group> listAllGroups(HttpSession session,
                                         @RequestParam(value = "from", required = false) Date from) {
        Long userId = getLoggedUser(session);

        return groupService.getGroups(userId);
    }

    @PostMapping(consumes = "application/json")
    public IdObject addGroup(HttpSession session, @Valid @RequestBody R_Group group) {
        Long ownerId = getLoggedUser(session);

        return groupService.addGroup(ownerId,group.name,group.description);
    }

    @DeleteMapping(path="/{id}")
    public String deleteGroup(HttpSession session,
                             @PathVariable("id") Long groupId) {
        getLoggedUser(session);
        groupService.crud().deleteById(groupId);
        return BaseController.OK_MESSAGE;
    }

    /**----------------------------------
    @PostMapping(path="/{id}/members")
    public String addMembers(@RequestBody Collection<Long> members, HttpSession session,
                          @PathVariable("id") Long groupId) {

        Long userId = getLoggedUser(session);
        groupService.addUsersToGroup(userId, groupId, members);
        return BaseController.OK_MESSAGE;
    }
    /**----------------------------------
    @PostMapping(path="/{id}/members")
    public String addMember(@RequestBody GroupID id, HttpSession session) {

        Long userId = getLoggedUser(session);
        //groupService.addUsersToGroup(userId, groupId, members);
        groupService.addUserToGroup(userId, id.id);
        return BaseController.OK_MESSAGE;
    }
    /**----------------------------------*/

    @PostMapping(path="/{id}/join")
    public String addMember(@PathVariable("id") Long groupId, HttpSession session) {
        Long userId = getLoggedUser(session);
        //groupService.addUsersToGroup(userId, groupId, members);
        groupService.addUserToGroup(userId, groupId);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/{id}/members")
    public Collection<User> getGroupMembers(HttpSession session,
                                       @PathVariable("id") Long groupId) {

        Long userId = getLoggedUser(session);
        return groupService.getGroupMembers(userId, groupId);
    }

    /**----------------------------------*/


    static class R_Group {

        @NotNull
        public String name;

        @NotNull
        public String description;

    }

    static class GroupID{
        @NotNull
        public Long id;
    }

}
