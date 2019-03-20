open module com.shepherdjerred.capstone.storage {
  requires static lombok;
  requires com.zaxxer.hikari;
  requires java.sql;
  requires com.shepherdjerred.capstone.logic;
  requires gson;
  exports com.shepherdjerred.capstone.storage.save;
}
