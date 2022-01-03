package Model;

public class Post {
    private String thePost, publisher, title, postuid, date, imageUri;

    public Post(String title, String thePost, String publisher, String postuid, String date, String imageUri){
        this.thePost = thePost;
        this.publisher= publisher;
        this.title= title;
        this.postuid = postuid;
        this.date = date;
        this.imageUri = imageUri;
    }
    public Post(){

    }
    public String getThePost(){
        return thePost;
    }

    public void setThePost(String thePost){
        this.thePost=thePost;
    }

    public String getPublisher(){
        return publisher;
    }

    public void setPublisher(String publisher){
        this.publisher=publisher;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getPostuid() { return postuid; }

    public void setPostuid(String postuid) {
        this.postuid = postuid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
