package is103.lostluggage.Database;

import is103.lostluggage.Model.User;
import java.sql.*;
import java.util.Enumeration;

/**
 *
 * @author Template: Hva (MyJDBC)
 * @author Michael de Boer Clean up, Implimentations, First prepared statements
 * @author Arthur Krom SQL Dump created
 * @author Thijs Zijdel 5 Prepared statements
 *
 */
public class MyJDBC {
    //Part 1
    private static final String DB_DEFAULT_DATABASE = "sys";
    private static final String DB_DEFAULT_SERVER_URL = "localhost:3306";
    private static final String DB_DEFAULT_ACCOUNT = "root";
    private static final String DB_DEFAULT_PASSWORD = "root";

    private final static String DB_DRIVER_URL = "com.mysql.jdbc.Driver";
    private final static String DB_DRIVER_PREFIX = "jdbc:mysql://";
    private final static String DB_DRIVER_PARAMETERS = "?useSSL=false";
    
    private Connection connection = null;

    // set for verbose logging of all queries
    private boolean verbose = true;

    // remembers the first error message on the connection 
    private String errorMessage = null;

    // constructors
    public MyJDBC() {
        this(DB_DEFAULT_DATABASE, DB_DEFAULT_SERVER_URL, DB_DEFAULT_ACCOUNT, DB_DEFAULT_PASSWORD);
    }

    public MyJDBC(String dbName) {
        this(dbName, DB_DEFAULT_SERVER_URL, DB_DEFAULT_ACCOUNT, DB_DEFAULT_PASSWORD);
    }

    public MyJDBC(String dbName, String account, String password) {
        this(dbName, DB_DEFAULT_SERVER_URL, account, password);
    }

    public MyJDBC(String dbName, String serverURL, String account, String password) {
        try {
            // verify that a proper JDBC driver has been installed and linked
            if (!selectDriver(DB_DRIVER_URL)) {
                return;
            }

            if (password == null) {
                password = "";
            }

            // establish a connection to a named database on a specified server	
            String connStr = DB_DRIVER_PREFIX + serverURL + "/" + dbName + DB_DRIVER_PARAMETERS;
            log("Connecting " + connStr);
            this.connection = DriverManager.getConnection(connStr, account, password);

        } catch (SQLException eSQL) {
            error(eSQL);
            this.close();
        }
    }

    public final void close() {

        if (this.connection == null) {
            // db has been closed earlier already
            return;
        }
        try {
            this.connection.close();
            this.connection = null;
            this.log("Data base has been closed");
        } catch (SQLException eSQL) {
            error(eSQL);
        }
    }

    /**
     * *
     * elects proper loading of the named driver for database connections. This
     * is relevant if there are multiple drivers installed that match the JDBC
     * type
     *
     * @param driverName the name of the driver to be activated.
     * @return indicates whether a suitable driver is available
     */
    private Boolean selectDriver(String driverName) {
        try {
            Class.forName(driverName);
            // Put all non-prefered drivers to the end, such that driver selection hits the first
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                if (!d.getClass().getName().equals(driverName)) {   // move the driver to the end of the list
                    DriverManager.deregisterDriver(d);
                    DriverManager.registerDriver(d);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            error(ex);
            return false;
        }
        return true;
    }

    /**
     * *
     * Executes a DDL, DML or DCL query that does not yield a result set
     *
     * @param sql the full sql text of the query.
     * @return the number of rows that have been impacted, -1 on error
     */
    public int executeUpdateQuery(String sql) {
        try {
            Statement s = this.connection.createStatement();
            log(sql);
            int n = s.executeUpdate(sql);
            s.close();
            return (n);
        } catch (SQLException ex) {
            // handle exception
            error(ex);
            return -1;
        }
    }

    /**
     * *
     * Executes an SQL query that yields a ResultSet with the outcome of the
     * query. This outcome may be a single row with a single column in case of a
     * scalar outcome.
     *
     * @param sql the full sql text of the query.
     * @return a ResultSet object that can iterate along all rows
     * @throws SQLException
     */
    public ResultSet executeResultSetQuery(String sql) throws SQLException {
        Statement s = this.connection.createStatement();
        log(sql);
        ResultSet rs = s.executeQuery(sql);
        // cannot close the statement, because that also closes the resultset
        return rs;
    }

    /**
     * *
     * Executes query that is expected to return a single String value
     *
     * @param sql the full sql text of the query.
     * @return the string result, null if no result or error
     */
    public String executeStringQuery(String sql) {
        String result = null;
        try {
            Statement s = this.connection.createStatement();
            log(sql);
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString(1);
            }
            // close both statement and resultset
            s.close();
        } catch (SQLException ex) {
            error(ex);
        }

        return result;
    }

    /**
     * *
     * Executes query that is expected to return a list of String values
     *
     * @param sql the full sql text of the query.
     * @return the string result, null if no result or error
     */
    public String executeStringListQuery(String sql) {
        String result = null;
        try {
            Statement s = this.connection.createStatement();
            log(sql);
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString(1);
            }
            // close both statement and resultset
            s.close();
        } catch (SQLException ex) {
            error(ex);
        }

        return result;
    }

    /**
     * *
     * echoes a message on the system console, if run in verbose mode
     *
     * @param message
     */
    public void log(String message) {
        if (isVerbose()) {
            System.out.println("MyJDBC: " + message);
        }
    }

    /**
     * *
     * echoes an exception and its stack trace on the system console. remembers
     * the message of the first error that occurs for later reference. closes
     * the connection such that no further operations are possible.
     *
     * @param e
     */
    public final void error(Exception e) {
        String msg = "MyJDBC-" + e.getClass().getName() + ": " + e.getMessage();

        // capture the message of the first error of the connection
        if (this.errorMessage == null) {
            this.errorMessage = msg;
        }
        System.out.println(msg);
        e.printStackTrace();

        // if an error occurred, close the connection to prevent further operations
        this.close();
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /*
    
    Part 2 
    
     */
    /**
     * *
     * Executes an SQL query that yields a ResultSet with a user if the filled
     * in fields match a id and password in the user table. Else returns an
     * ResultSet
     *
     * Using a Prepared Statement. Prepared statements are used against SQL
     * injection
     *
     *
     * @param id the username from the user.
     * @param password the password that the user entered
     * @return a ResultSet object (User)
     */
    public User executeLogInQuery(String id, String password) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM employee  "
                + "WHERE employeeId = ? AND password = ?");
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        User currentUser = null;

        while (resultSet.next()) {
            String employeeId = resultSet.getString("employeeId");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String location = resultSet.getString("location");
            String role = resultSet.getString("role");
            String status = resultSet.getString("status");
            currentUser = new User(employeeId, lastname, firstname, location, role, status);

        }

        return currentUser;
    }

    public ResultSet executeSearchQuery(String searchInput) throws SQLException {

        PreparedStatement preparedStatement = this.connection.prepareStatement(
                "SELECT * "
                + "FROM employee WHERE employeeId LIKE ? ESCAPE '!' OR firstname "
                + "LIKE ? ESCAPE '!' OR lastname LIKE ? ESCAPE '!' OR location LIKE ? "
                + "ESCAPE '!' OR status LIKE ? ESCAPE '!' OR role LIKE ? ESCAPE '!'");
        String search = searchInput
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");

        preparedStatement.setString(1, "%" + search + "%");
        preparedStatement.setString(2, "%" + search + "%");
        preparedStatement.setString(3, "%" + search + "%");
        preparedStatement.setString(4, "%" + search + "%");
        preparedStatement.setString(5, "%" + search + "%");
        preparedStatement.setString(6, "%" + search + "%");

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;
    }
    
    public int executePasswordUpdateQuery(String id, String newPassword) throws SQLException {
     
        try {

            PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE employee SET "
                    + "password = ? WHERE employeeId = ?");

            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, id);

            int returnValue = preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println(returnValue);

            return returnValue;
        } catch (SQLException ex) {
            // handle exception
            error(ex);
            return -1;
        }

    }

    public int executeUserUpdateQuery(User user) throws SQLException {
        String firstname = user.getFirstName();
        String lastname = user.getLastName();
        String location = user.getLocation();
        String role = user.getRole();
        String status = user.getStatus();
        String employeeID = user.getId();

        System.out.println(firstname + lastname + location + role + status + employeeID);
        try {

            PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE employee SET "
                    + "firstname = ?, lastname = ?, "
                    + "location = ?, "
                    + "role = ?, status = ? WHERE employeeId = ?");

            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, role);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, employeeID);

            int returnValue = preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println(returnValue);

            return returnValue;
        } catch (SQLException ex) {
            // handle exception
            error(ex);
            return -1;
        }

    }

    // -------------------------------------------------------------------------
    
    /**
     * @author Thijs Zijdel - 500782165
     *
     * Execute update query for editing a fields of a luggage. 
     * Note: Prepared statement so the db is be protected against SQL Injection.
     *
     * @param table table of the field that will be updated
     * @param field field that need to be changed
     * @param value new value of the field
     * @param registrationNr of the luggage that will changed
     * @throws java.sql.SQLException updating data in the db
     **/
    public void executeUpdateLuggageFieldQuery(
                                    String table,
                                    String field,
                                    String value,
                                    String registrationNr) throws SQLException {
        //try to create en execute an prepared statment
        try (
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                        "UPDATE `"+table+"` SET  "
                        + " "+field+" = ? "
                        + "WHERE `registrationNr`= ? ;")) {
            //initializing the preparedstatement
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, registrationNr);

            //execute the prepared statement
            preparedStatement.executeUpdate();
            
            //close preparedStatement
            preparedStatement.close();
        }
    }
    /**
     * @author Thijs Zijdel - 500782165
     *
     * Execute update query for editing the fields of a passenger. 
     * Note: Prepared statement so the db is be protected against SQL Injection.
     *
     * @param table table of the field that will be updated
     * @param field field that need to be changed
     * @param where statement / field that will be checked on the @param id
     * @param value new value of the field
     * @param id of the row that will changed
     * @throws java.sql.SQLException updating data in the db
     **/
    public void executeUpdateQueryWhere(
                                    String table,
                                    String field,
                                    String where,
                                    String value,
                                    String id) throws SQLException {
        //try to create en execute an prepared statment
        try (
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                        "UPDATE `"+table+"` SET  "
                        + " "+field+" = ? "
                        + "WHERE `"+where+"`= ? ;")) {
            //initializing the preparedstatement
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, id);

            //execute the prepared statement
            preparedStatement.executeUpdate();
            
            //close preparedStatement
            preparedStatement.close();
        }
    }
    /**
     * @author Thijs Zijdel - 500782165
     *
     * Execute update query for editing the fields of a passenger. 
     * Note: Prepared statement so the db is be protected against SQL Injection.
     *
     *
     * //All the parameters are the values of the new fields
     * @param name
     * @param address
     * @param place
     * @param postalcode
     * @param country
     * @param email
     * @param phone
     *
     * @param passengerId -> the passenger where the values are set
     * @throws java.sql.SQLException because there will be a SQL query executed
     **/
    public void executeUpdatePassengerQuery(
                                    String name,
                                    String address,
                                    String place,
                                    String postalcode,
                                    String country,
                                    String email,
                                    String phone,
                                    String passengerId) throws SQLException {
        //try to create en execute an prepared statment
        try (
            PreparedStatement preparedStatement = 
                this.connection.prepareStatement(
                    "UPDATE `passenger` SET  "
                    + " name = ? ,"
                    + " address = ? ,"
                    + " place = ? ,"
                    + " postalcode = ? ,"
                    + " country = ? ,"
                    + " email = ? ,"
                    + " phone = ? "
                    + "WHERE `passengerId`= ? ;")) {
            //initializing the preparedstatement
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, place);
            preparedStatement.setString(4, postalcode);
            preparedStatement.setString(5, country);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, phone);
            preparedStatement.setString(8, passengerId);

            //execute the prepared statement
            preparedStatement.executeUpdate();
            
            //close preparedStatement
            preparedStatement.close();
        } 
    }

    /**
     * @author Thijs Zijdel - 500782165
     * 
     * Execute update query for editing almost all fields of a luggage. 
     * Note: Prepared statement so the db is be protected against SQL Injection.
     * 
     * //All the parameters are the values of the new fields
     * @param tag
     * @param brand
     * @param size
     * @param signatures
     * @param id
     * 
     * 
     * @param luggageTable  should be found luggage or lost luggage
     * @throws java.sql.SQLException because there will be data updated in the Db
     **/
    public void executeUpdateLuggageQuery(
                                        String tag,
                                        String brand,
                                        String size,
                                        String signatures,
                                        String id,
                                        String luggageTable) throws SQLException {
           //try to create en execute an prepared statment
        try (
            PreparedStatement preparedStatement = 
                this.connection.prepareStatement(
                    "UPDATE `"+luggageTable+"` SET  "
                    + " luggageTag = ? ,"
                    + " brand = ? ,"
                    + " size = ? ,"
                    + " otherCharacteristics = ? "
                    + "WHERE `registrationNr` =  ?  ;")) {
            //initializing the preparedstatement
            preparedStatement.setString(1, tag);
            preparedStatement.setString(2, brand);
            preparedStatement.setString(3, size);
            preparedStatement.setString(4, signatures);
            preparedStatement.setString(5, id);

            //execute the prepared statement
            preparedStatement.executeUpdate();
            
            //close preparedStatement
            preparedStatement.close();
        }     
    }

    /**
     * @author Thijs Zijdel - 500782165
     *
     * Execute insert query for inserting the fields of a match. Note: this is
     * an prepared statement so the db will be protected against SQL Injection.
     *
     *
     * //All the parameters are the values of the new fields
     * @param matchedId            //generated id for the match itself  PKey
     * @param foundRegistrationNr  //id of the matched found luggage    FKey
     * @param lostRegistrationNr   //id of the matched lost luggage     FKey
     * @param employeeId           //id of the employee that confirmed the match
     * @param dateMatched          //current date
     *
     * @throws java.sql.SQLException because there will be a SQL query executed
     **/
    public void executeInsertMatchQuery(
                                String matchedId, 
                                String foundRegistrationNr, 
                                String lostRegistrationNr, 
                                String employeeId, 
                                String dateMatched) throws SQLException {
        try (
            PreparedStatement preparedStatement = 
                this.connection.prepareStatement(
                    " INSERT INTO `matched` " 
                    + " (`matchedId`, `foundluggage`, `lostluggage`, `employeeId`, `dateMatched`, `delivery`) "
                    + " VALUES ( ? , ? , ? , ? , ? , '' ) ")){
            //initializing the preparedstatement
            preparedStatement.setString(1, matchedId);
            preparedStatement.setString(2, foundRegistrationNr);
            preparedStatement.setString(3, lostRegistrationNr);
            preparedStatement.setString(4, employeeId);
            preparedStatement.setString(5, dateMatched);

            //execute the prepared statement
            preparedStatement.executeUpdate();
            
            //close preparedStatement
            preparedStatement.close();
        } 
    }

    // -------------------------------------------------------------------------
    
    //Model made by Arthur implemented by Michael
    public static void createLostLuggageDatabase(String dbName) {

        System.out.println("Creating the " + dbName + " database...");

        // use the sys schema for creating another db
        MyJDBC sysJDBC = new MyJDBC("sys");
        sysJDBC.executeUpdateQuery("CREATE DATABASE IF NOT EXISTS " + dbName);
        sysJDBC.close();

        // create or truncate User table in the Airline database
        System.out.println("Creating the User table...");
        MyJDBC myJDBC = new MyJDBC(dbName);

        //User will be changed to Employee next Fase
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS User ("
                + " ID VARCHAR(10) NOT NULL PRIMARY KEY,"
                + " Firstname VARCHAR(45),"
                + " Lastname VARCHAR(45),"
                + " Location VARCHAR(45),"
                + " Status VARCHAR(10),"
                + " Role VARCHAR(20)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        // truncate Tables, in case some data was already there
        myJDBC.executeUpdateQuery("TRUNCATE TABLE User");

        myJDBC.executeUpdateQuery("INSERT INTO User VALUES ("
                + "'MB1', 'Michael', 'Boer de', 'Amsterdam', 'Active', 'Adminstrator' )");
        myJDBC.executeUpdateQuery("INSERT INTO User VALUES ("
                + "'AA1', 'Ahmet', 'Aksu', 'Amsterdam', 'Active', 'Manager' )");
        myJDBC.executeUpdateQuery("INSERT INTO User VALUES ("
                + "'AK1', 'Arthur', 'Krom', 'Amsterdam', 'Active', 'Service' )");
        myJDBC.executeUpdateQuery("INSERT INTO User VALUES ("
                + "'TZ1', 'Thijs', 'Zijdel', 'Amsterdam', 'Active', 'Service' )");
        myJDBC.executeUpdateQuery("INSERT INTO User VALUES ("
                + "'DO1', 'Daron', 'Özdemir', 'Amsterdam', 'Active', 'Manager' )");
        myJDBC.executeUpdateQuery("INSERT INTO User VALUES ("
                + "'PL1', 'Poek', 'Ligthart', 'Amsterdam', 'Active', 'Service' )");
        myJDBC.executeUpdateQuery("INSERT INTO User VALUES ("
                + "'MB2', 'Michael', 'Boer de', 'Amsterdam', 'Active', 'Manager' )");

        //Create Table Color 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS color ("
                + "  ralCode INT(11) NOT NULL,"
                + "  english VARCHAR(45) NOT NULL,"
                + "  dutch VARCHAR(45) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table Destination 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS destination ("
                + "  `IATAcode` VARCHAR(10) NOT NULL,"
                + "  `airport` VARCHAR(45) NOT NULL,"
                + "  `country` VARCHAR(45) NOT NULL,"
                + "  `timeZone` VARCHAR(45) NOT NULL,"
                + "  `daylightSaving` TINYINT(4) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table Employee 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `employee` ("
                + "  `employeeId` VARCHAR(45) NOT NULL,"
                + "  `firstname` VARCHAR(45) NOT NULL,"
                + "  `surname` VARCHAR(45) NOT NULL,"
                + "  `password` VARCHAR(45) NOT NULL,"
                + "  `department` VARCHAR(45) NOT NULL,"
                + "  `airport` VARCHAR(45) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table Flight 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS flight ("
                + "  `flightNr` varchar(45) NOT NULL,"
                + "  `airline` varchar(45) NOT NULL,"
                + "  `from` varchar(3) NOT NULL,"
                + "  `to` varchar(3) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table FoundLuggage 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `foundluggage` ("
                + "  `registrationNr` int(11) NOT NULL,"
                + "  `dateFound` date NOT NULL,"
                + "  `timeFound` varchar(45) NOT NULL,"
                + "  `luggageTag` varchar(45) DEFAULT NULL,"
                + "  `luggageType` int(11) NOT NULL,"
                + "  `brand` varchar(45) DEFAULT NULL,"
                + "  `mainColor` int(11) NOT NULL,"
                + "  `secondColor` int(11) DEFAULT NULL,"
                + "  `size` varchar(45) DEFAULT NULL,"
                + "  `weight` int(11) DEFAULT NULL,"
                + "  `otherCharacteristics` mediumtext,"
                + "  `arrivedWithFlight` varchar(45) DEFAULT NULL,"
                + "  `locationFound` int(11) DEFAULT NULL,"
                + "  `employeeId` varchar(45) DEFAULT NULL,"
                + "  `matchedId` int(11) DEFAULT NULL,"
                + "  `passengerId` int(11) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table Location 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `location` ("
                + "  `locationId` int(11) NOT NULL,"
                + "  `english` varchar(45) NOT NULL,"
                + "  `dutch` varchar(45) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table LostLuggage 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `lostluggage` ("
                + "  `registrationNr` int(11) NOT NULL,"
                + "  `dateLost` datetime NOT NULL,"
                + "  `timeLost` varchar(45) NOT NULL,"
                + "  `luggageTag` varchar(45) DEFAULT NULL,"
                + "  `brand` varchar(45) DEFAULT NULL,"
                + "  `mainColor` int(11) NOT NULL,"
                + "  `secondColor` int(11) DEFAULT NULL,"
                + "  `size` int(11) DEFAULT NULL,"
                + "  `weight` int(11) DEFAULT NULL,"
                + "  `otherCharacteristics` mediumtext,"
                + "  `flight` varchar(45) DEFAULT NULL,"
                + "  `employeeId` varchar(45) NOT NULL,"
                + "  `luggageType` int(11) NOT NULL,"
                + "  `matchedId` int(11) DEFAULT NULL,"
                + "  `passengerId` int(11) DEFAULT NULL)");

        //Create Table LuggageType 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `luggagetype` ("
                + "  `luggageTypeId` int(11) NOT NULL,"
                + "  `english` varchar(45) NOT NULL,"
                + "  `dutch` varchar(45) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table Matched 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `matched` ("
                + "  `matchedId` int(11) NOT NULL,"
                + "  `foundluggage` int(11) NOT NULL,"
                + "  `lostluggage` int(11) NOT NULL,"
                + "  `employeeId` varchar(45) NOT NULL,"
                + "  `dateMatched` varchar(45) NOT NULL,"
                + "  `delivery` varchar(45) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        //Create Table Passenger 
        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `passenger` ("
                + "  `passengerId` int(11) NOT NULL,"
                + "  `name` varchar(45) DEFAULT NULL,"
                + "  `address` varchar(45) DEFAULT NULL,"
                + "  `place` varchar(45) DEFAULT NULL,"
                + "  `postalcode` varchar(45) DEFAULT NULL,"
                + "  `country` varchar(45) DEFAULT NULL,"
                + "  `email` varchar(45) DEFAULT NULL,"
                + "  `phone` varchar(45) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        myJDBC.executeUpdateQuery("INSERT INTO `color` (`ralCode`, `english`, `dutch`) VALUES"
                + "(1003, 'Yellow', 'Geel'),"
                + "(1015, 'Cream', 'Crème'),"
                + "(1024, 'Olive', 'Olijf'),"
                + "(2004, 'Orange', 'Oranje'),"
                + "(3000, 'Red', 'Rood'),"
                + "(3005, 'Darkred', 'Donkerrood'),"
                + "(3017, 'Pink', 'Roze'),"
                + "(4005, 'Purple', 'Paars'),"
                + "(4010, 'Violet', 'Violet'),"
                + "(5002, 'Blue', 'Blauw'),"
                + "(5015, 'Lightblue', 'Lichtblauw'),"
                + "(5022, 'Darkblue', 'Donkerblauw'),"
                + "(6002, 'Green', 'Groen'),"
                + "(6004, 'Bluegreen', 'Blauwgroen'),"
                + "(6022, 'Darkgreen', 'Donkergroen'),"
                + "(6038, 'Lightgreen', 'Lichtgroen'),"
                + "(7000, 'Lightgray', 'Lichtgrijs'),"
                + "(7015, 'Gray', 'Grijs'),"
                + "(8002, 'Brown', 'Bruin'),"
                + "(8011, 'Darkbrown', 'Donkerbruin'),"
                + "(8023, 'Lightbrown', 'Lichtbruin'),"
                + "(9001, 'White', 'Wit'),"
                + "(9005, 'Black', 'Zwart'),"
                + "(9011, 'Darkgray', 'Donkergrijs');");
        myJDBC.executeUpdateQuery("INSERT INTO `flight` (`flightNr`, `airline`, `from`, `to`) VALUES"
                + "('CAI020', 'Corendon', 'AMS', 'AYT'),"
                + "('CAI021', 'Corendon', 'AYT', 'AMS'),"
                + "('CAI023', 'Corendon', 'AYT', 'AMS'),"
                + "('CAI040', 'Corendon', 'EIN', 'AYT'),"
                + "('CAI041', 'Corendon', 'AYT', 'EIN'),"
                + "('CAI1827', 'Corendon', 'AYT', 'BRU'),"
                + "('CAI1828', 'Corendon', 'BRU', 'AYT'),"
                + "('CAI201', 'Corendon', 'BJV', 'AMS'),"
                + "('CAI202', 'Corendon', 'AMS', 'BJV'),"
                + "('CAI421', 'Corendon', 'AYT', 'BRU'),"
                + "('CAI524', 'Corendon', 'BRU', 'AYT'),"
                + "('CAI723', 'Corendon', 'AYT', 'BRU'),"
                + "('CAI724', 'Corendon', 'BRU', 'AYT'),"
                + "('CAI805', 'Corendon', 'AYT', 'AMS'),"
                + "('CAI806', 'Corendon', 'AMS', 'AYT'),"
                + "('CND117', 'Corendon', 'AMS', 'AGP'),"
                + "('CND118', 'Corendon', 'AGP', 'AMS'),"
                + "('CND513', 'Corendon', 'AMS', 'HER'),"
                + "('CND593', 'Corendon', 'AMS', 'AGP'),"
                + "('CND594', 'Corendon', 'AGP', 'AMS'),"
                + "('CND712', 'Corendon', 'HER', 'AMS'),"
                + "('HV355', 'Transavia', 'AMS', 'BJV'),"
                + "('HV356', 'Transavia', 'BJV', 'AMS'),"
                + "('HV6115', 'Transavia', 'AMS', 'AGP'),"
                + "('HV6224', 'Transavia', 'AGP', 'AMS'),"
                + "('HV649', 'Transavia', 'AMS', 'AYT'),"
                + "('HV650', 'Transavia', 'AYT', 'AMS'),"
                + "('HV740', 'Transavia', 'AYT', 'AMS'),"
                + "('HV799', 'Transavia', 'AMS', 'AYT'),"
                + "('KL1039', 'KLM', 'AMS', 'AGP'),"
                + "('KL1040', 'KLM', 'AGP', 'AMS'),"
                + "('KL1041', 'KLM', 'AMS', 'AGP'),"
                + "('KL1042', 'KLM', 'AGP', 'AMS'),"
                + "('PC5665', 'Pegasus', 'AYT', 'AMS'),"
                + "('TK1823', 'Turkish Airlines', 'IST', 'PAR'),"
                + "('TK1824', 'Turkish Airlines', 'PAR', 'IST'),"
                + "('TK1827', 'Turkish Airlines', 'IST', 'PAR'),"
                + "('TK1830', 'Turkish Airlines', 'PAR', 'IST'),"
                + "('TK1938', 'Turkish Airlines', 'BRU', 'IST'),"
                + "('TK1939', 'Turkish Airlines', 'IST', 'BRU'),"
                + "('TK1942', 'Turkish Airlines', 'BRU', 'IST'),"
                + "('TK1943', 'Turkish Airlines', 'IST', 'BRU'),"
                + "('TK1951', 'Turkish Airlines', 'IST', 'AMS'),"
                + "('TK1952', 'Turkish Airlines', 'AMS', 'IST'),"
                + "('TK1955', 'Turkish Airlines', 'IST', 'AMS'),"
                + "('TK1958', 'Turkish Airlines', 'AMS', 'IST'),"
                + "('TK2409', 'Turkish Airlines', 'AYT', 'IST'),"
                + "('TK2414', 'Turkish Airlines', 'IST', 'AYT'),"
                + "('TK2425', 'Turkish Airlines', 'AYT', 'IST'),"
                + "('TK2430', 'Turkish Airlines', 'IST', 'AYT'),"
                + "('TK2505', 'Turkish Airlines', 'BJV', 'IST'),"
                + "('TK2510', 'Turkish Airlines', 'IST', 'BJV'),"
                + "('TO3002', 'Transavia', 'PAR', 'AGA'),"
                + "('TO3005', 'Transavia', 'AGA', 'PAR'),"
                + "('TO3160', 'Transavia', 'PAR', 'AGP'),"
                + "('TO3163', 'Transavia', 'AGP', 'PAR'),"
                + "('VY2150', 'Vueling', 'AGP', 'BRU'),"
                + "('VY2151', 'Vueling', 'BRU', 'AGP');");
        myJDBC.executeUpdateQuery("INSERT INTO `foundluggage` (`registrationNr`, `dateFound`, `timeFound`, `luggageTag`, `luggageType`, `brand`, `mainColor`, `secondColor`, `size`, `weight`, `otherCharacteristics`, `arrivedWithFlight`, `locationFound`, `employeeId`, `matchedId`, `passengerId`) VALUES"
                + "(1, '2016-06-17', '19:25', '1153481443', 4, '', 1015, NULL, '', 0, '', NULL, NULL, NULL, NULL, 1),"
                + "(2, '2016-09-07', '10:04', '1297047756', 1, 'Perry Mackin', 6004, 4010, '80x60x40', 0, 'holywood sticker', NULL, 6, NULL, NULL, 2),"
                + "(3, '2016-07-04', '20:05', '1321391290', 6, 'Eastsport', 3017, 8011, '', 0, 'red-bull sticker', 'CAI020', 1, NULL, NULL, 3),"
                + "(4, '2016-09-09', '13:18', '1557534916', 5, 'Baggallini', 1024, 3005, '60x30x30', 15, '', 'CAI724', 1, NULL, NULL, 4),"
                + "(5, '2015-11-25', '12:00', '1688722916', 2, 'Baggallini', 9005, 5002, '60x30x30', 15, 'Orange stripes', 'CAI1828', 2, NULL, NULL, 5),"
                + "(6, '2016-09-10', '16:30', '1957629307', 1, 'Ivy', 2004, NULL, '70x50x20', 0, '', NULL, 7, NULL, NULL, 6),"
                + "(7, '2016-09-09', '11:56', '1963627893', 1, 'Nautica', 1003, 6038, '80x60x30', 20, 'many scratches', NULL, 7, NULL, NULL, 7),"
                + "(8, '2015-10-20', '11:50', '2771896151', 6, 'Ivy', 5002, NULL, '50x40x15', 10, '', 'TK2414', 0, NULL, NULL, 8),"
                + "(9, '2016-09-08', '11:29', '2973839061', 4, '', 4010, NULL, '60x40x30', 15, 'chain lock', 'HV799', 0, NULL, NULL, 9),"
                + "(10, '2016-08-23', '7:30', '3217712035', 1, 'Travel Gear', 8023, 1003, '100x60x40', 30, 'ajax stickers', NULL, NULL, NULL, NULL, 10),"
                + "(11, '2016-03-13', '19:23', '3260024106', 3, 'Hedgren', 3000, 1015, '', 0, 'football stickers', 'TK2430', 1, NULL, NULL, 11),"
                + "(12, '2016-09-02', '9:25', '3299609395', 5, 'Fjallraven', 8023, NULL, '60x30x30', 0, '', 'HV799', 2, NULL, NULL, NULL),"
                + "(13, '2016-01-17', '14:13', '3794786696', 1, 'Glove It', 4010, 9001, '80x60x30', 15, '', 'CAI724', 5, NULL, NULL, 12),"
                + "(14, '2016-09-04', '9:40', '4497537549', 6, 'Glove It', 4005, NULL, '50x40x15', 10, '', 'CAI1828', 0, NULL, NULL, 13),"
                + "(15, '2016-08-24', '8:10', '4811246270', 2, 'Fjallraven', 7015, NULL, '50x40x15', 10, 'Orange stripes', 'HV649', 3, NULL, NULL, 14),"
                + "(16, '2016-08-31', '8:11', '5364334705', 3, 'Travel Gear', 9001, NULL, '60x30x30', 15, '', NULL, NULL, NULL, NULL, NULL),"
                + "(17, '2016-07-19', '21:05', '5703242384', 2, 'Samsonite', 8002, NULL, '', 0, 'Bicycle stickers', NULL, 8, NULL, NULL, NULL),"
                + "(18, '2016-08-11', '23:00', '5877130095', 6, 'Baggallini', 5022, NULL, '', 0, 'red-bull sticker', 'CAI040', 0, NULL, NULL, 15),"
                + "(19, '2016-07-25', '22:00', '5941005772', 4, '', 6038, NULL, '', 0, '', 'CAI806', 4, NULL, NULL, NULL),"
                + "(20, '2016-07-15', '20:35', '5955243509', 1, 'Everest', 5015, NULL, '60x40x20', 15, '', 'TK2430', 5, NULL, NULL, 16),"
                + "(21, '2016-08-06', '22:15', '6175011250', 5, 'Samsonite', 8011, NULL, '', 0, '', NULL, 8, NULL, NULL, 17),"
                + "(22, '2016-09-08', '10:17', '6327958189', 3, 'Perry Mackin', 6002, NULL, '60x30x30', 10, '', NULL, 6, NULL, NULL, NULL),"
                + "(23, '2016-09-10', '16:00', '6377992003', 6, 'Everest', 5015, 3017, '50x40x15', 10, '', 'CAI806', 0, NULL, NULL, 18),"
                + "(24, '2016-06-18', '19:40', '6895742082', 5, 'Briggs', 3005, NULL, '', 0, '', 'HV649', 1, NULL, NULL, NULL),"
                + "(25, '2016-05-24', '18:44', '7620963089', 1, 'Hedgren', 1015, NULL, '70x50x20', 10, '', 'CAI806', 2, NULL, NULL, 19),"
                + "(26, '2016-09-10', '14:28', '7686938228', 2, 'AmeriLeather', 5022, 7015, '60x30x30', 10, 'Olympic rings', 'HV799', 4, NULL, NULL, NULL),"
                + "(27, '2016-04-13', '17:17', '7975308223', 2, 'Delsey', 5002, 9005, '', 0, 'Olympic rings', 'CAI524', 5, NULL, NULL, 20),"
                + "(28, '2016-09-10', '15:50', '9896064347', 5, 'AmeriLeather', 8011, 6004, '60x30x30', 10, 'BRT television sticker', 'CAI524', 3, NULL, NULL, NULL),"
                + "(29, '2016-09-01', '9:10', '', 4, '', 3017, NULL, '60x40x30', 15, '', 'HV649', 5, NULL, NULL, NULL),"
                + "(30, '2016-09-06', '9:58', '', 7, '', 6038, 4005, '100x60x40', 30, '', NULL, 8, NULL, NULL, NULL),"
                + "(31, '2016-09-07', '10:13', '', 2, 'Eastsport', 9011, 1024, '50x40x15', 0, 'Orange stripes', 'CAI724', 3, NULL, NULL, 21),"
                + "(32, '2016-09-08', '11:43', '', 5, 'Eastsport', 9001, NULL, '60x30x30', 10, '', 'CAI040', 2, NULL, NULL, 22),"
                + "(33, '2016-09-09', '11:54', '', 7, '', 7015, 6022, '80x60x40', 25, 'red name tag', NULL, 8, NULL, NULL, NULL),"
                + "(34, '2016-09-09', '12:01', '', 3, 'Nautica', 1024, 2004, '60x30x30', 0, '', NULL, 7, NULL, NULL, 23),"
                + "(35, '2015-12-25', '12:04', '', 4, '', 7000, 3000, '60x40x30', 0, '', 'TK2430', 3, NULL, NULL, 24),"
                + "(36, '2016-09-09', '13:21', '', 6, 'Hedgren', 8002, NULL, '50x40x15', 10, 'ajax stickers', 'CAI020', 3, NULL, NULL, 25),"
                + "(37, '2016-09-10', '13:37', '', 7, '', 4005, 8023, '80x60x30', 0, '', 'CAI020', 5, NULL, NULL, NULL),"
                + "(38, '2016-09-10', '15:31', '', 3, 'Glove It', 6004, 5015, '60x30x30', 10, '', 'CAI040', 4, NULL, NULL, 26),"
                + "(39, '2016-09-10', '15:40', '', 4, '', 9011, 5022, '', 0, 'frech national flag sticker', NULL, 8, NULL, NULL, 27),"
                + "(40, '2016-02-10', '16:22', '', 7, '', 7000, NULL, '80x60x30', 15, '', NULL, 8, NULL, NULL, NULL),"
                + "(41, '2016-04-27', '17:34', '', 3, 'Ivy', 3000, 9011, '', 0, '', NULL, 7, NULL, NULL, 28),"
                + "(42, '2016-04-30', '17:44', '', 4, '', 9005, 7000, '', 0, 'broken lock', NULL, NULL, NULL, NULL, NULL),"
                + "(43, '2015-12-25', '18:09', '', 5, 'Delsey', 6002, 8002, '', 0, '', 'CAI1828', 4, NULL, NULL, 29),"
                + "(44, '2016-05-03', '18:10', '', 6, 'Fjallraven', 2004, 6002, '', 0, '', 'CAI524', 1, NULL, NULL, NULL),"
                + "(45, '2016-05-14', '18:37', '', 7, '', 6022, NULL, '70x50x20', 20, 'duvel sticker', NULL, 6, NULL, NULL, NULL),"
                + "(46, '2016-06-04', '19:20', '', 2, 'Briggs', 1003, NULL, '', 0, 'Olympic rings', 'TK2414', 2, NULL, NULL, 30),"
                + "(47, '2016-07-09', '20:18', '', 7, '', 3005, NULL, '70x50x20', 10, '', NULL, 7, NULL, NULL, 31),"
                + "(48, '2016-08-17', '21:45', '', 3, 'Everest', 6022, NULL, '', 0, '', 'TK2414', 4, NULL, NULL, 32);");
//        INSERT INTO `lostluggage`.`lostluggage` (`registrationNr`, `dateLost`, `timeLost`, `luggageTag`, `brand`, `mainColor`, `secondColor`, `size`, `weight`, `otherCharacteristics`, `employeeId`, `luggageType`) VALUES ('4', '2015-02-12', '8:00', '1928310232', 'BrandName', '9005', '9005', '10x80x20', '20', 'Blue dots', 'tz', '8');

        System.out.println("started");
        myJDBC.executeUpdateQuery("INSERT INTO `lostluggage` (`registrationNr`, `dateLost`, `timeLost`, `luggageTag`, `brand`, `mainColor`, `secondColor`, `size`, `weight`, `otherCharacteristics`, `employeeId`, `luggageType`, `matchedId`, `passengerId`) VALUES "
                + "(1, '2015-03-11', '8:20', '2771896151', 'Delsey', 8002, 6022, '10', 20, 'duvel sticker', 'tz', 7, NULL, 5);");
        myJDBC.executeUpdateQuery("INSERT INTO `lostluggage` (`registrationNr`, `dateLost`, `timeLost`, `luggageTag`, `brand`, `mainColor`, `secondColor`, `size`, `weight`, `otherCharacteristics`, `employeeId`, `luggageType`, `matchedId`, `passengerId`) VALUES "
                + "(2, '2015-10-10', '8:20', '6377992003', 'Fjallraven', 8002, 6002, '30', 20, 'Blue spots', 'tz', 4, NULL, 10);");
        myJDBC.executeUpdateQuery("INSERT INTO `lostluggage` (`registrationNr`, `dateLost`, `timeLost`, `luggageTag`, `brand`, `mainColor`, `secondColor`, `size`, `weight`, `otherCharacteristics`, `employeeId`, `luggageType`, `matchedId`, `passengerId`) VALUES "
                + "(3, '2015-11-09', '8:20', '', 'Everest', 9005, 9005, '30', 20, 'Olympic rings', 'tz', 2, NULL, 3);");
        myJDBC.executeUpdateQuery("INSERT INTO `lostluggage` (`registrationNr`, `dateLost`, `timeLost`, `luggageTag`, `brand`, `mainColor`, `secondColor`, `size`, `weight`, `otherCharacteristics`, `employeeId`, `luggageType`, `matchedId`, `passengerId`) VALUES "
                + "(4, '2015-04-07', '8:20', '', 'Briggs', 1003, 9005, '30', 20, '', 'tz', 1, NULL, 2);");
        myJDBC.executeUpdateQuery("INSERT INTO `lostluggage` (`registrationNr`, `dateLost`, `timeLost`, `luggageTag`, `brand`, `mainColor`, `secondColor`, `size`, `weight`, `otherCharacteristics`, `employeeId`, `luggageType`, `matchedId`, `passengerId`) VALUES "
                + "(5, '2015-03-12', '8:20', '', 'Everest', 8002, 9005, '30', 20, '', 'tz', 6, NULL, 1);");
        System.out.println("ended");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `lostluggage` ("
                + "  `registrationNr` int(11) NOT NULL,"
                + "  `dateLost` datetime NOT NULL,"
                + "  `timeLost` varchar(45) NOT NULL,"
                + "  `luggageTag` varchar(45) DEFAULT NULL,"
                + "  `brand` varchar(45) DEFAULT NULL,"
                + "  `mainColor` int(11) NOT NULL,"
                + "  `secondColor` int(11) DEFAULT NULL,"
                + "  `size` int(11) DEFAULT NULL,"
                + "  `weight` int(11) DEFAULT NULL,"
                + "  `otherCharacteristics` mediumtext,"
                + "  `flight` varchar(45) DEFAULT NULL,"
                + "  `employeeId` varchar(45) NOT NULL,"
                + "  `luggageType` int(11) NOT NULL,"
                + "  `matchedId` int(11) DEFAULT NULL,"
                + "  `passengerId` int(11) DEFAULT NULL)");

        myJDBC.executeUpdateQuery("INSERT INTO `location` (`locationId`, `english`, `dutch`) VALUES"
                + "(0, 'belt-06', 'band-06'),"
                + "(1, 'belt-05', 'band-05'),"
                + "(2, 'belt-04', 'band-04'),"
                + "(3, 'belt-03', 'band-03'),"
                + "(4, 'belt-02', 'band-02'),"
                + "(5, 'belt-01', 'band-01'),"
                + "(6, 'departure hall', 'vertrekhal'),"
                + "(7, 'arrival hall', 'aankomsthal'),"
                + "(8, 'toilet', 'toilet');");
        myJDBC.executeUpdateQuery("INSERT INTO `luggagetype` (`luggageTypeId`, `english`, `dutch`) VALUES"
                + "(1, 'Suitcase', 'Koffer'),"
                + "(2, 'Bag', 'Tas'),"
                + "(3, 'Bagpack', 'Rugzak'),"
                + "(4, 'Box', 'Doos'),"
                + "(5, 'Sports Bag', 'Sporttas'),"
                + "(6, 'Business Case', 'Zakenkoffer'),"
                + "(7, 'Case', 'Kist'),"
                + "(8, 'Other', 'Anders');");
        myJDBC.executeUpdateQuery("INSERT INTO `passenger` (`passengerId`, `name`, `address`, `place`, `postalcode`, `country`, `email`, `phone`) VALUES"
                + "(1, 'P. Curie', 'Versaille', NULL, NULL, NULL, NULL, NULL),"
                + "(2, 'R. Hauer', 'Bussum', NULL, NULL, NULL, NULL, NULL),"
                + "(3, 'M. Verstappen', 'Monaco', NULL, NULL, NULL, NULL, NULL),"
                + "(4, 'S. Appelmans', 'De Panne', NULL, NULL, NULL, NULL, NULL),"
                + "(5, 'A. van Buren', 'Wassenaar', NULL, NULL, NULL, NULL, NULL),"
                + "(6, 'D. Kuyt', 'Rotterdam', NULL, NULL, NULL, NULL, NULL),"
                + "(7, 'C. van Houten', 'Naarden', NULL, NULL, NULL, NULL, NULL),"
                + "(8, 'M. Messi', 'Barcelona', NULL, NULL, NULL, NULL, NULL),"
                + "(9, 'N. Bonaparte', 'Paris', NULL, NULL, NULL, NULL, NULL),"
                + "(10, 'M. van Basten', 'Alkmaar', NULL, NULL, NULL, NULL, NULL),"
                + "(11, 'F. van der Elst', 'Brussel', NULL, NULL, NULL, NULL, NULL),"
                + "(12, 'R. van Persie', 'Rotterdam', NULL, NULL, NULL, NULL, NULL),"
                + "(13, 'M. Rutte', 'den Haag', NULL, NULL, NULL, NULL, NULL),"
                + "(14, 'W.A. van Buren', 'Wassenaar', NULL, NULL, NULL, NULL, NULL),"
                + "(15, 'J. Verstappen', 'Oss', NULL, NULL, NULL, NULL, NULL),"
                + "(16, 'A. Gerritse', 'Ilpendam', NULL, NULL, NULL, NULL, NULL),"
                + "(17, 'D. de Munck', 'Amsterdam', NULL, NULL, NULL, NULL, NULL),"
                + "(18, 'R. de Boer', 'Southhampton', NULL, NULL, NULL, NULL, NULL),"
                + "(19, 'S. Kramer', 'Heerenveen', NULL, NULL, NULL, NULL, NULL),"
                + "(20, 'I. de Bruijn', 'Leiden', NULL, NULL, NULL, NULL, NULL),"
                + "(21, 'M. van Buren', 'Wassenaar', NULL, NULL, NULL, NULL, NULL),"
                + "(22, 'E. Gruyaert', 'Antwerpen', NULL, NULL, NULL, NULL, NULL),"
                + "(23, 'Mw. Hollande', 'Paris', NULL, NULL, NULL, NULL, NULL),"
                + "(24, 'G. Esting', 'Paris', NULL, NULL, NULL, NULL, NULL),"
                + "(25, 'F. de Boer', 'Amsterdam', NULL, NULL, NULL, NULL, NULL),"
                + "(26, 'Mw. Zoetemelk', 'Lyon', NULL, NULL, NULL, NULL, NULL),"
                + "(27, 'F. Mitterand', 'Paris', NULL, NULL, NULL, NULL, NULL),"
                + "(28, 'L.. Van Moortsel', 'Breda', NULL, NULL, NULL, NULL, NULL),"
                + "(29, 'E. Leyers', 'Turnhout', NULL, NULL, NULL, NULL, NULL),"
                + "(30, 'P. van den Hoogenband', 'Eindhoven', NULL, NULL, NULL, NULL, NULL),"
                + "(31, 'E. de Munck', 'Brugge', NULL, NULL, NULL, NULL, NULL),"
                + "(32, 'F. van der Sande', 'Wuustwezel', NULL, NULL, NULL, NULL, NULL);");

        myJDBC.executeUpdateQuery("ALTER TABLE `color`"
                + "  ADD PRIMARY KEY (`ralCode`);");
        myJDBC.executeUpdateQuery("ALTER TABLE `destination`"
                + "  ADD PRIMARY KEY (`IATAcode`);");
        myJDBC.executeUpdateQuery("ALTER TABLE `employee`"
                + "  ADD PRIMARY KEY (`employeeId`),"
                + "  ADD UNIQUE KEY `employeeId_UNIQUE` (`employeeId`),"
                + "  ADD KEY `works at airport_idx` (`airport`);");

        myJDBC.executeUpdateQuery("ALTER TABLE `flight`"
                + "  ADD PRIMARY KEY (`flightNr`),"
                + "  ADD KEY `destination_start_idx` (`from`),"
                + "  ADD KEY `destination_end_idx` (`to`);");

        myJDBC.executeUpdateQuery("ALTER TABLE `foundluggage`"
                + "  ADD PRIMARY KEY (`registrationNr`),"
                + "  ADD UNIQUE KEY `registrationNr_UNIQUE` (`registrationNr`),"
                + "  ADD KEY `the type of luggage_idx` (`luggageType`),"
                + "  ADD KEY `the main color of found luggage_idx` (`mainColor`),"
                + "  ADD KEY `the second color of found luggage_idx` (`secondColor`),"
                + "  ADD KEY `arrived with flight_idx` (`arrivedWithFlight`),"
                + "  ADD KEY `is found at location_idx` (`locationFound`),"
                + "  ADD KEY `formulier has been submitted by_idx` (`employeeId`),"
                + "  ADD KEY `matched with_idx` (`matchedId`),"
                + "  ADD KEY `Belongs to a passenger like this_idx` (`passengerId`);");

        myJDBC.executeUpdateQuery("ALTER TABLE `lostluggage` "
                + "  ADD PRIMARY KEY (`registrationNr`),"
                + "  ADD KEY `the main color of the luggage_idx` (`mainColor`),"
                + "  ADD KEY `the second color of the luggage_idx` (`secondColor`),"
                + "  ADD KEY `form has been submitted by employee_idx` (`employeeId`),"
                + "  ADD KEY `type of luggage_idx` (`luggageType`),"
                + "  ADD KEY `should have arrived with this flight` (`flight`),"
                + "  ADD KEY `matched with_idx` (`matchedId`),"
                + "  ADD KEY `belongs to this passenger_idx` (`passengerId`);");

        myJDBC.executeUpdateQuery("ALTER TABLE `luggagetype`"
                + "  ADD PRIMARY KEY (`luggageTypeId`);");

        myJDBC.executeUpdateQuery("ALTER TABLE `matched`"
                + "  ADD PRIMARY KEY (`matchedId`),"
                + "  ADD KEY `submitted by employee_idx` (`employeeId`),"
                + "  ADD KEY `lostluggage form_idx` (`lostluggage`),"
                + "  ADD KEY `foundluggage form_idx` (`foundluggage`);");
        myJDBC.executeUpdateQuery("ALTER TABLE `passenger`"
                + "  ADD PRIMARY KEY (`passengerId`);");
        myJDBC.executeUpdateQuery("ALTER TABLE `foundluggage`"
                + "  MODIFY `registrationNr` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;");
        myJDBC.executeUpdateQuery("ALTER TABLE `lostluggage`"
                + "  MODIFY `registrationNr` int(11) NOT NULL AUTO_INCREMENT");
        myJDBC.executeUpdateQuery("ALTER TABLE `passenger`"
                + "  MODIFY `passengerId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;");
        myJDBC.executeUpdateQuery("ALTER TABLE `employee`"
                + "  ADD CONSTRAINT `works at airport` FOREIGN KEY (`airport`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION;");

        //These Foreign keys dont work yet, will be implemented in next fase
//        myJDBC.executeUpdateQuery("ALTER TABLE `flight`"
//                + "  ADD CONSTRAINT `this flight leaves from` FOREIGN KEY (`from`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `this flight arrives at` FOREIGN KEY (`to`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION;");
//
//        myJDBC.executeUpdateQuery("ALTER TABLE `foundluggage`"
//                + "  ADD CONSTRAINT `this lugage is of this type` FOREIGN KEY (`luggageType`) REFERENCES `luggagetype` (`luggageTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `This luggage has this main color` FOREIGN KEY (`mainColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `This luggage has this second color` FOREIGN KEY (`secondColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `this luggage has arrived with flight` FOREIGN KEY (`arrivedWithFlight`) REFERENCES `flight` (`flightNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `this luggage has been found at` FOREIGN KEY (`locationFound`) REFERENCES `location` (`locationId`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `formulier has been submitted by` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `matched with` FOREIGN KEY (`matchedId`) REFERENCES `matched` (`matchedId`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `Belongs to a passenger like this` FOREIGN KEY (`passengerId`) REFERENCES `passenger` (`passengerId`) ON DELETE NO ACTION ON UPDATE NO ACTION;");
//        myJDBC.executeUpdateQuery("ALTER TABLE `lostluggage`"
//                + "  ADD CONSTRAINT `belongs to this passenger` FOREIGN KEY (`passengerId`) REFERENCES `passenger` (`passengerId`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `form has been submitted by employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `matched with found` FOREIGN KEY (`matchedId`) REFERENCES `matched` (`matchedId`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `should have arrived with this flight` FOREIGN KEY (`flight`) REFERENCES `flight` (`flightNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `the main color of the luggage` FOREIGN KEY (`mainColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `the second color of the luggage` FOREIGN KEY (`secondColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
//                + "  ADD CONSTRAINT `type of luggage` FOREIGN KEY (`luggageType`) REFERENCES `luggagetype` (`luggageTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;");
        myJDBC.executeUpdateQuery("ALTER TABLE `matched`"
                + "  ADD CONSTRAINT `foundluggage form` FOREIGN KEY (`foundluggage`) REFERENCES `foundluggage` (`registrationNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
                + "  ADD CONSTRAINT `lostluggage form` FOREIGN KEY (`lostluggage`) REFERENCES `lostluggage` (`registrationNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,"
                + "  ADD CONSTRAINT `submitted by employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;");

        System.out.println("Known User in time zone 1:");
        try {
            ResultSet rs = myJDBC.executeResultSetQuery(
                    "SELECT ID, Firstname FROM User WHERE Status='Active'");
            while (rs.next()) {
                // echo the info of the next airport found
                System.out.println(
                        rs.getString("ID")
                        + " " + rs.getString("Firstname"));
            }
            // close and release the resources
            rs.close();

        } catch (SQLException ex) {
            myJDBC.error(ex);
        }

        // close the connection with the database
        myJDBC.close();
    }

}
