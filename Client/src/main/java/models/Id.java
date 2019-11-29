package models;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.JsonUtils;

/*
 * POJO for an Id object
 */
public class Id {
    String name;
    String githubid;
    String userid;
    
    public Id (String name, String githubId) {
        this.name = name;
        this.githubid = githubId;
        this.userid = null;
    }
    public Id (String name, String githubId, String userId) {
        this.name = name;
        this.githubid = githubId;
        this.userid = userId;
    }

    public Id (Id id) {
        this(id.getName(), id.getGithubId(), id.getUserId());
    }

    public Id (String json) {
        this(JsonUtils.stringToId(json));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubId() {
        return this.githubid;
    }

    public void setGithubId(String githubId) {
        this.githubid = githubId;
    }

    public String getUserId() {
        return this.userid;
    }

    public void setUserId(String userId) {
        this.userid = userId;
    }

}