package views;

import models.Id;

public class IdTextView {
    Id id;

    public IdTextView(Id idToDisplay) {
        this.id = idToDisplay;
    }
    @Override public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(String.format(
                "\tname:\t\t%s\n" +
                "\tgithub:\t\t%s\n" +
                "\tuserid:\t\t%s\n", id.getName(), id.getGithubId(), id.getUserId()));
        return out.toString();
    } 
}