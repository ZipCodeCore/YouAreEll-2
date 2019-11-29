package models;

/* 
 * POJO for an Id object
 */
public class Id {
    String name;
    String githubId;
    String userId;
    
    public Id (String name, String githubId) {
        this.name = name;
        this.githubId = githubId;
        this.userId = null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubId() {
        return this.githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}