package com.feather.authserver.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.feather.authserver.model.Authority;
import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.Group;
import com.feather.authserver.model.GroupAuthority;
import com.feather.authserver.model.Role;
import com.feather.authserver.repository.AuthorityRepository;
import com.feather.authserver.repository.GroupAuthorityRepository;
import com.feather.authserver.repository.GroupRepository;
import com.feather.authserver.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitilaizeData implements CommandLineRunner {

    private final List<String> crudPrefiixList = List.of("CREATE_", "READ_", "UPDATE_", "DELETE_");
    private final List<String> serviceAuthorities = List.of("CHAT", "USER", "PROFILE");

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final GroupRepository groupRepository;
    private final GroupAuthorityRepository groupAuthorityRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!roleRepository.existsByName(FeatherRole.ROLE_ADMIN)) {
            roleRepository.save(new Role(FeatherRole.ROLE_ADMIN));
            roleRepository.save(new Role(FeatherRole.ROLE_STAFF));
            roleRepository.save(new Role(FeatherRole.ROLE_USER));

            Group adminGroup = groupRepository.save(new Group("GROUP_ADMIN"));
            Group staffGroup = groupRepository.save(new Group("GROUP_STAFF"));
            Group userGroup = groupRepository.save(new Group("GROUP_USER"));

            List<Authority> authorityList = new ArrayList<>();
            for (String crudPrefix : crudPrefiixList) {
                for (String serviceAuth : serviceAuthorities) {
                    Authority authority = authorityRepository.save(new Authority(crudPrefix + serviceAuth));
                    authorityList.add(authority);
                }
            }

            List<GroupAuthority> adminGroupAuthorities = new ArrayList<>();
            List<GroupAuthority> staffGroupAuthorities = new ArrayList<>();
            List<GroupAuthority> userGroupAuthorities = new ArrayList<>();

            for (Authority authority : authorityList) {
                adminGroupAuthorities.add(new GroupAuthority(adminGroup, authority));

                if (!authority.getName().startsWith("DELETE")) {
                    staffGroupAuthorities.add(new GroupAuthority(staffGroup, authority));
                }

                if (!authority.getName().endsWith("USER")) {
                    userGroupAuthorities.add(new GroupAuthority(userGroup, authority));
                }
            }

            groupAuthorityRepository.saveAll(adminGroupAuthorities);
            groupAuthorityRepository.saveAll(staffGroupAuthorities);
            groupAuthorityRepository.saveAll(userGroupAuthorities);

            adminGroup.setGroupAuthorities(adminGroupAuthorities);
            staffGroup.setGroupAuthorities(staffGroupAuthorities);
            userGroup.setGroupAuthorities(userGroupAuthorities);

            groupRepository.save(adminGroup);
            groupRepository.save(staffGroup);
            groupRepository.save(userGroup);
        }

    }

}
