package com.example.parking.client.sql;

public class SqlConstants {
  public static final String SQL_CREATE_ENTRIES =
      "CREATE TABLE FAVORITE ( NAME TEXT PRIMARY KEY)";

  public static final String SQL_DELETE_ENTRIES =
      "DROP TABLE IF EXISTS FAVORITE";

  public static final String SQL_SELECT_ALL =
      "SELECT * FROM FAVORITE";

  public static final String SQL_SELECT_NAME_CONDITION =
          "Select * FROM favorite where name = %s";

}
