package gov.nist.hla.federates.database;

import gov.nist.hla.gateway.GatewayFederateConfig;

public class GatewayConfiguration extends GatewayFederateConfig {
    private String username = "root";
    private String password = "root";
    private String ipAddress = "localhost";
    private String port = "3306";
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getIpAddress() {
        return this.ipAddress;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public String getPort() {
        return this.port;
    }
}
