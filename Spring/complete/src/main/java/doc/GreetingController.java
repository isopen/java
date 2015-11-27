package doc;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import org.springframework.boot.json.BasicJsonParser;

@RestController
public class GreetingController {

    private JdbcTemplate jdbcTemplate = this.getConn();

    private JdbcTemplate getConn() {
      SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
      dataSource.setDriverClass(org.h2.Driver.class);
      dataSource.setUsername("sa");
      dataSource.setUrl("jdbc:h2:./data/db");
      dataSource.setPassword("");
      return new JdbcTemplate(dataSource);
    }

    private Map getJsonObject(String json) {
      BasicJsonParser basicJsonParser = new BasicJsonParser();
      Map<String,Object> map = new HashMap<String,Object>();
      map = basicJsonParser.parseMap(json);
      return map;
    }

    @RequestMapping(value = "/setdoc", method = RequestMethod.POST)
    public void setdoc(@RequestParam(value="document", required=false) String document){

      System.out.println("Set documents");

      Map<String,Object> map = this.getJsonObject(document);

      long unixTime = System.currentTimeMillis() / 1000L;

      String query = "INSERT INTO documents(name_doc,status_doc,createdate_doc,updatedate_doc,description_doc) values(?,0,?,?,?)";

      this.jdbcTemplate.update(query,map.get("name_doc"),unixTime,unixTime,map.get("description_doc"));

      return;
    }

    @RequestMapping("/getdoc")
    public String getdoc(@RequestParam(value="id", required=false) String idDoc){

      System.out.println("Get Document");

      List<CustomerDocument> results = this.jdbcTemplate.query(
        "select * from documents where id_doc = ?",new Object[]{idDoc},
        new RowMapper<CustomerDocument>() {
          @Override
          public CustomerDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CustomerDocument(
              rs.getString("name_doc"),
              rs.getInt("status_doc"),
              rs.getInt("createdate_doc"),
              rs.getInt("updatedate_doc"),
              rs.getString("description_doc")
            );
          }
        });
      return results+"";
    }

    @RequestMapping("/getdocs")
    public String getdocs() {

      System.out.println("Get Documents");

      List<Customer> results = this.jdbcTemplate.query(
        "select name_doc,status_doc,createdate_doc,id_doc from documents order by id_doc",
        new RowMapper<Customer>() {
          @Override
          public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Customer(
              rs.getString("name_doc"),
              rs.getInt("status_doc"),
              rs.getInt("createdate_doc"),
              rs.getInt("id_doc")
            );
          }
        });

      String listDocs = "{";
      int i = 0;
      for (Customer customer : results) {
        if(i > 0){
          listDocs += ",";
        }
        listDocs += "\""+customer.getId()+"\":"+customer;
        System.out.println(customer);
        i++;
      }
      listDocs += "}";

      return listDocs;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, readOnly = false)
    @RequestMapping("/statusConfirm")
    public void statusConfirm(@RequestParam(value="id", required=false) String idDoc){
      this.jdbcTemplate.update("update documents set status_doc = 1 where id_doc = ?", idDoc);
      return;
    }

    @RequestMapping("/delDoc")
    public void delDoc(@RequestParam(value="id", required=false) String idDoc){
      this.jdbcTemplate.update("delete from documents where id_doc = ? and status_doc = 0", idDoc);
      return;
    }

}
