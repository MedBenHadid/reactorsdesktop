package Packages.UserManagementPackage.Models;

import Packages.UserManagementPackage.Models.enums.AccessType;

import java.sql.Timestamp;

class Membership {
    private Timestamp joinDate;
    private String fonction,description;
    private AccessType access;
}
