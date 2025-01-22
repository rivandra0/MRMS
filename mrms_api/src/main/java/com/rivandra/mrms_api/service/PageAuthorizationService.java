package com.rivandra.mrms_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PageAuthorizationService {
    private final List<String[]> roleEndpointMappings = new ArrayList<>();

    //NOTE: in the future we can move this list to database and create some config page to adjust the authorization, in case we have tons of UserRoles
    public PageAuthorizationService() {
        roleEndpointMappings.add(new String[] {"/my-requests__My Requests", "PRODUCTION_USER"});
        roleEndpointMappings.add(new String[] {"/my-requests/history__My Requests History", "PRODUCTION_USER"});

        roleEndpointMappings.add(new String[] {"/manage-requests__Manage Requests", "WAREHOUSE_ADMIN"});
        roleEndpointMappings.add(new String[] {"/manage-requests/history__Manage Requests History", "WAREHOUSE_ADMIN"});

    }

    public List<String> getAuthorizedPagesByUser(String role) {
        List<String> authorizedPages = new ArrayList<>();

        // Iterate through mappings to find matching roles
        for (String[] mapping : roleEndpointMappings) {
            if (mapping[1].equals(role)) {
                authorizedPages.add(mapping[0]); // Add the endpoint to the result
            }
        }

        // Convert the result list to an array and return
        return authorizedPages;
    }

}
