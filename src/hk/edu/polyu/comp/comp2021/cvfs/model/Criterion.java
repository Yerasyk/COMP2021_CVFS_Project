package hk.edu.polyu.comp.comp2021.cvfs.model;

public abstract class Criterion {
    private String name;

    public Criterion(String criName){
        if (!(criName.matches("^[a-zA-Z]{2}$") || criName.equals("IsDocument"))) {
            throw new IllegalArgumentException("criName must contain exactly two English letters.");
        }
        setName(criName);
    }
    private void setName(String criName){
        this.name=criName;
    }

    public String getName() {
        return name;
    }

    public abstract boolean matches(File file);

    @Override
    public abstract String toString();
}
