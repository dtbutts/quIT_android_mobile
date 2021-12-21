package Model;

public class Post {
    private String thePost, publisher, title, postuid;

    public Post(String title, String thePost, String publisher, String postuid){
        this.thePost = thePost;
        this.publisher= publisher;
        this.title= title;
        this.postuid = postuid;
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

    public String getPostuid() {
        return postuid;
    }

    public void setPostuid(String postuid) {
        this.postuid = postuid;
    }
}
