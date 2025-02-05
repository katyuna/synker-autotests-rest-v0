package data.dto;

public class UserMappingDto {
    private String createdBy;
    private String id;
    private String created;
    private String sourceUser;
    private String targetUser;
    private String comment;

    // Конструктор без параметров
    public UserMappingDto() {
    }

    // Конструктор с шестью параметрами
    public UserMappingDto(String id, String sourceUser, String targetUser, String comment, String created, String createdBy) {
        this.id = id;
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.comment = comment;
        this.created = created;
        this.createdBy = createdBy;
    }

    // Конструктор с пятью параметрами (без id)
    public UserMappingDto(String sourceUser, String targetUser, String comment, String created, String createdBy) {
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.comment = comment;
        this.created = created;
        this.createdBy = createdBy;
    }

    // Геттеры и сеттеры
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
