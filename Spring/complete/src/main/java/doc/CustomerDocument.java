package doc;

public class CustomerDocument {
    private String nameDoc, descriptionDoc;
    private int statusDoc, createdateDoc, updatedateDoc;

    public CustomerDocument(String nameDoc, int statusDoc, int createdateDoc, int updatedateDoc,String descriptionDoc) {
      this.nameDoc = nameDoc;
      this.statusDoc = statusDoc;
      this.createdateDoc = createdateDoc;
      this.updatedateDoc = updatedateDoc;
      this.descriptionDoc = descriptionDoc;
    }

    @Override
    public String toString() {
      return String.format(
        "{\"nameDoc\":\"%s\", \"statusDoc\":\"%d\", \"createdateDoc\":\"%d\", \"updatedateDoc\":\"%d\",\"descriptionDoc\":\"%s\"}",
        nameDoc, statusDoc, createdateDoc, updatedateDoc, descriptionDoc
      );
    }
}
