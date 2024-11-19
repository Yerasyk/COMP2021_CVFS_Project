package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

public class Document extends File implements Serializable {
    private String type;
    private String content;

    public Document(String name, String type, String content){
        super(name);
        this.setType(type);
        this.content=content;
    }

    private void setType(String type){
        if (!type.matches("^(txt|java|html|css)$")) {
            throw new StateChangeCommandFailed("File type must be one of the following: txt, java, html, css.");
        }
        this.type=type;
    }
    public String getType(){
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int getSize(){
        return content.length()*2+40;
    }
}
