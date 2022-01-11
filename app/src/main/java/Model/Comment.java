package Model;

public class Comment {
    private String comment, publisher, postuid, commentuid;
    private Long timestamp;

    public Comment(String comment, String publisher, Long timestamp, String postuid, String commentuid) {
        this.comment = comment;
        this.publisher = publisher;
        this.timestamp = timestamp;
        this.postuid = postuid;
        this.commentuid = commentuid;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getTimestamp() { return timestamp; }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostuid() {
        return postuid;
    }

    public void setPostuid(String postuid) {
        this.postuid = postuid;
    }

    public String getCommentuid() {
        return commentuid;
    }

    public void setCommentuid(String commentuid) {
        this.commentuid = commentuid;
    }
}
