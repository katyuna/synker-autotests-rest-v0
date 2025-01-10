package data.dto;


public class UserMappingDto {
    String createdBy;
    String id;
    String created;
    String sourceUser;
    String targetUser;
    String comment;
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getSourceUser() {
        return sourceUser;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public String getComment() {
        return comment;
    }
}
