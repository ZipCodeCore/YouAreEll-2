package models;

/* 
 * POJO for an Id object
 */
public class Id {
    String name;
    String github;
    String userid;

    private Id(){}

    public Id (String name, String githubId) {
        this.name = name;
        this.github = githubId;
        this.userid = null;
    }

    public Id (String name, String githubId, String userid) {
        this.name = name;
        this.github = githubId;
        this.userid = userid;
    }

    public Id (Id id){
        this(id.getName(), id.getGithub(), id.getUserid());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}