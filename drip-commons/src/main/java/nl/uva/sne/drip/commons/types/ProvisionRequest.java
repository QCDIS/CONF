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
package nl.uva.sne.drip.commons.types;

/**
 *
 * @author S. Koulouzis
 */
public class ProvisionRequest {

    private String cloudConfID;

    private String planID;

    private String userScriptID;

    private String userKeyID;

    /**
     * @return the cloudConfID
     */
    public String getCloudConfID() {
        return cloudConfID;
    }

    /**
     * @param cloudConfID the cloudConfID to set
     */
    public void setCloudConfID(String cloudConfID) {
        this.cloudConfID = cloudConfID;
    }

    /**
     * @return the planID
     */
    public String getPlanID() {
        return planID;
    }

    /**
     * @param planID the planID to set
     */
    public void setPlanID(String planID) {
        this.planID = planID;
    }

    /**
     * @return the userScriptID
     */
    public String getUserScriptID() {
        return userScriptID;
    }

    /**
     * @param userScriptID the userScriptID to set
     */
    public void setUserScriptID(String userScriptID) {
        this.userScriptID = userScriptID;
    }

    /**
     * @return the userKeyID
     */
    public String getUserKeyID() {
        return userKeyID;
    }

    /**
     * @param userKeyID the userKeyID to set
     */
    public void setUserKeyID(String userKeyID) {
        this.userKeyID = userKeyID;
    }

}
