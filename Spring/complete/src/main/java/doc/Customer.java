package doc;

public class Customer {
    private String nameDoc;
    private int statusDoc, createdateDoc, idDoc;

    public Customer(String nameDoc, int statusDoc, int createdateDoc, int idDoc) {
      this.nameDoc = nameDoc;
      this.statusDoc = statusDoc;
      this.createdateDoc = createdateDoc;
      this.idDoc = idDoc;
    }

    public int getId(){
      return idDoc;
    }

    @Override
    public String toString() {
      return String.format(
        "{\"nameDoc\":\"%s\", \"statusDoc\":\"%d\", \"createdateDoc\":\"%d\"}",
        nameDoc, statusDoc, createdateDoc
      );
    }
}
