package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

/**
 * Represents a document in the virtual file system.
 * A document has a name, type, and content, and its size is calculated based on its content length.
 */
public class Document extends File implements Serializable {
    private String type;
    private final String content;

    /**
     * Constructs a new document with the specified name, type, and content.
     *
     * @param name    The name of the document.
     * @param type    The type of the document (e.g., txt, java, html, css).
     * @param content The content of the document.
     * @throws StateChangeCommandFailed If the document type is invalid.
     */
    public Document(String name, String type, String content){
        super(name);
        this.setType(type);
        this.content=content;
    }

    /**
     * Sets the type of the document. The type must be one of the allowed values: txt, java, html, css.
     *
     * @param type The type of the document.
     * @throws StateChangeCommandFailed If the document type is not one of the allowed values.
     */
    private void setType(String type){
        if (!type.matches("^(txt|java|html|css)$")) {
            throw new StateChangeCommandFailed("File type must be one of the following: txt, java, html, css.");
        }
        this.type=type;
    }

    /**
     * Retrieves the type of the document.
     *
     * @return The type of the document.
     */
    public String getType(){
        return type;
    }

    /**
     * Calculates and retrieves the size of the document.
     * The size is calculated as {@code content length * 2 + DEFAULT_SIZE}.
     *
     * @return The size of the document in bytes.
     */
    @Override
    public int getSize(){
        return content.length()*2+DEFAULT_SIZE;
    }

    /**
     * Returns a string representation of the document, including its name, type, and size.
     *
     * @return A string representing the document.
     */
    @Override
    public String toString() {
        return getName() + "." + getType() + ", Size: " + getSize();
    }
}
