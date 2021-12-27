package Model;

public class Comment {
    private String comment, publisher;
    private Long timestamp;

    public Comment(String comment, String publisher, Long timestamp) {
        this.comment = comment;
        this.publisher = publisher;
        this.timestamp = timestamp;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
