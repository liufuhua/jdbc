
自己写的JDBC封装类 
------------------

适用场景及特点：

  1.喜欢更自由地写SQL。<br>
  2.更轻量级的数据访问方法，相对于Hibernate、iBatis等ORM框架，可代替DAO层。<br>
  3.数据全部静态，线程安全。<br>
  4.数据连接交由使用者控制，提高数据连接的使用效率。<br>
  5.支持事务。<br>
  
  使用示例：
  
        Connection conn = jdbc.getConn();

        List<String> sqlList = new LinkedList<String>();
        sqlList.add("update cat set ODERBY=1022 where id=1");
        sqlList.add("update cat set NAME='SSSS' where id=3");

        ResultSet rs = jdbc.query(conn,"select * from cat");
        while (rs.next()){
            System.out.print(rs.getString("NAME") +"\n");
        }
        rs.close();

        // jdbc.sql(conn,"delete from cat where id=4");
        // jdbc.StartTransaction(conn,sqlList);

        jdbc.closeConn(conn);
        
