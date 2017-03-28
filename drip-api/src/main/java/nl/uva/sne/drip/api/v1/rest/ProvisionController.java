/*
 * Copyright 2017 S. Koulouzis, Wang Junchao, Huan Zhou, Yang Hu 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.uva.sne.drip.api.v1.rest;

import nl.uva.sne.drip.commons.v1.types.ProvisionRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import nl.uva.sne.drip.api.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import nl.uva.sne.drip.api.exception.NotFoundException;
import nl.uva.sne.drip.api.service.ProvisionService;
import nl.uva.sne.drip.api.service.UserService;
import nl.uva.sne.drip.commons.v1.types.ProvisionResponse;
import org.json.JSONException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This controller is responsible for obtaining resources from cloud providers
 * based the plan generated by the planner
 *
 * @author S. Koulouzis
 */
@RestController
@RequestMapping("/user/v1.0/provisioner")
@Component
public class ProvisionController {

    @Autowired
    private ProvisionService provisionService;

    /**
     * Gets the ProvisionRequest
     *
     * @param id. The id of the ProvisionRequest
     * @return the requested ProvisionRequest
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RolesAllowed({UserService.USER, UserService.ADMIN})
    public @ResponseBody
    ProvisionResponse get(@PathVariable("id") String id) {
        return provisionService.findOne(id);
    }

    /**
     * Deletes the ProvisionRequest
     *
     * @param id. The ID of the ProvisionRequest to be deleted
     * @return the ID of the deleted ProvisionRequest
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({UserService.USER, UserService.ADMIN})
    public @ResponseBody
    String delete(@PathVariable("id") String id) {
        ProvisionRequest provPlan = provisionService.findOne(id);
        if (provPlan != null) {
            provisionService.delete(id);
            return "Deleted : " + id;
        }
        throw new NotFoundException();
    }

    @RequestMapping(value = "/all", method = RequestMethod.DELETE)
    @RolesAllowed({UserService.ADMIN})
    public @ResponseBody
    String deleteAll() {
        provisionService.deleteAll();
        return "Done";

    }

    /**
     * Gets the IDs of all the stored ProvisionRequest
     *
     * @return a list of IDs
     */
    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    @RolesAllowed({UserService.USER, UserService.ADMIN})
    public @ResponseBody
    List<String> getIds() {
        List<ProvisionResponse> all = provisionService.findAll();
        List<String> ids = new ArrayList<>(all.size());
        for (ProvisionRequest pi : all) {
            ids.add(pi.getId());
        }
        return ids;
    }

    /**
     * Provison the resources specified by a plan.
     *
     * @param req. The ProvisionRequest. This is a container the plan ID, cloud
     * credent ID, etc.
     * @return The ID of the provisioned ProvisionRequest
     */
    @RequestMapping(value = "/provision", method = RequestMethod.POST)
    @RolesAllowed({UserService.USER, UserService.ADMIN})
    public @ResponseBody
    String provision(@RequestBody ProvisionRequest req) {
        if (req.getCloudCredentialsID() == null) {
            throw new BadRequestException();
        }
        if (req.getPlanID() == null) {
            throw new BadRequestException();
        }
        try {
            req = provisionService.provisionResources(req);

            return req.getId();

        } catch (IOException | TimeoutException | JSONException | InterruptedException ex) {
            Logger.getLogger(ProvisionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
