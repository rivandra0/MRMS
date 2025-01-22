package com.rivandra.mrms_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ApiAuthorizationService {
    private final List<String[]> roleEndpointMappings = new ArrayList<>();

    //NOTE: in the future we can move this list to database and create some config page to adjust the authorizatoin, in case we have tons of UserRoles
    public ApiAuthorizationService() {
        roleEndpointMappings.add(new String[] {"GET", "/admin/material-requests", "WAREHOUSE_ADMIN"});
        roleEndpointMappings.add(new String[] {"GET", "/user/material-requests", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"GET", "/admin/material-request", "WAREHOUSE_ADMIN"});
        roleEndpointMappings.add(new String[] {"GET", "/user/material-request", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"POST", "/user/material-request", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"PUT", "/admin/material-request", "WAREHOUSE_ADMIN"});
        roleEndpointMappings.add(new String[] {"PUT", "/user/material-request", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"DELETE", "/user/material-request", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"GET", "/admin/material-request/approve", "WAREHOUSE_ADMIN"});
        roleEndpointMappings.add(new String[] {"GET", "/admin/material-request/reject", "WAREHOUSE_ADMIN"});


        roleEndpointMappings.add(new String[] {"POST", "/user/material-request-item", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"PUT", "/admin/material-request-item", "WAREHOUSE_ADMIN"});
        roleEndpointMappings.add(new String[] {"PUT", "/user/material-request-item", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"DELETE", "/user/material-request-item", "PRODUCTION_USER"});
    }

    public boolean isAccessGranted(String endpointType, String path, String userRole) {
        for (String[] mapping : roleEndpointMappings) {
            // Check if endpoint type, path, and role match
            if (mapping[0].equals(endpointType) && mapping[1].equals(path) && mapping[2].equals(userRole)) {
                return true;
            }
        }
        return false;  // Default: deny access if no match is found
    }
}
