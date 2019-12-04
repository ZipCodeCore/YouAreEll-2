package views;

import models.Id;

public class IdTextView {
    Id id;

    public IdTextView(Id idToDisplay) {
        this.id = idToDisplay;
    }

    @Override
    public String toString() {
        return String.format("User:\n" +
                "\tname:\t\t%s\n" +
                "\tgithub:\t\t%s\n" +
                "\tuserid:\t\t%s\n", id.getName(), id.getGithubId(), id.getUserId());
    } 
}