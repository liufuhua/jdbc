package soodo.jdbcTool;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * JDBC封装类
 */
public class jdbc {
    private static String DriverClass = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;
    private static String buildFrom = "jdbc.properties";  //配置文件位置，默认位置：同一个包内

    private static Properties props = new Properties();

    static {
        try {
            props.load(jdbc.class.getResourceAsStream(buildFrom));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DriverClass = props.getProperty("jdbc.driverClass");
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        password = props.getProperty("jdbc.password");
        //注册驱动类
        try {
            Class.forName(DriverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个数据库连接
     *
     * @return 一个数据库连接
     */
    public static Connection getConn() {
        Connection conn = null;
        //创建数据库连接
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 执行一个SQL语句查询
     *
     * @param conn 数据库连接
     * @param sql SQL语句字符串
     * @return 返回查询结果集ResultSet对象
     */
    public static ResultSet query(Connection conn, String sql) {
        ResultSet rs = null;
        try {
            //创建执行SQL的对象
            Statement stmt = conn.createStatement();
            //执行SQL，并获取返回结果
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 执行一个SQL语句
     *
     * @param conn   据库连接
     * @param sql SQL语句字符串
     */
    public static void sql(Connection conn, String sql) {
        try {
            //创建执行SQL的对象
            Statement stmt = conn.createStatement();
            //执行SQL
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行一批SQL语句
     *
     * @param conn   数据库连接
     * @param sqlList SQL语句字符串集合
     */
    public static void BatchSQL(Connection conn, List<String> sqlList) {
        try {
            //创建执行SQL的对象
            Statement stmt = conn.createStatement();
            for (String sql : sqlList) {
                stmt.addBatch(sql);
            }
            //执行SQL
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 事务执行SQL
     * @param conn 数据库连接
     * @param sqlList SQL语句字符串集合

     */
    public static void StartTransaction(Connection conn, List<String> sqlList) {

        Statement stmt = null;
        try {
            // 事务开始
            conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库
            stmt = conn.createStatement(); // 创建Statement对象

            for (String sql : sqlList) {
                stmt.addBatch(sql);
            }
            //执行SQL
            stmt.executeBatch();
            conn.commit();   // 提交给数据库处理

        } catch (SQLException e) {
            try {

                conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
      * 关闭数据库连接
      * @param conn 数据库连接
    */
    public static void closeConn(Connection conn) {
        if (conn == null) return;
        try {
            if (!conn.isClosed()) {
                //关闭数据库连接
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
