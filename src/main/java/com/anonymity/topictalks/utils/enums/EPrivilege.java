package com.anonymity.topictalks.utils.enums;

/**
 * The {@code EPrivilege} enum represents various privileges that can be assigned to users or roles in the application.
 * Privileges define different levels of access or actions that users or roles are allowed to perform.
 *
 * - {@code READ_PRIVILEGE}: Grants the privilege to read or view resources or data.
 * - {@code WRITE_PRIVILEGE}: Grants the privilege to create or modify resources or data.
 * - {@code DELETE_PRIVILEGE}: Grants the privilege to delete or remove resources or data.
 * - {@code UPDATE_PRIVILEGE}: Grants the privilege to update or edit existing resources or data.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.utils.enums
 * - Created At: 14-09-2023 14:56:03
 * @since 1.0 - version of class
 */
public enum EPrivilege {

    /**
     * Represents the privilege to read or view resources or data.
     */
    READ_PRIVILEGE,
    /**
     * Represents the privilege to create or modify resources or data.
     */
    WRITE_PRIVILEGE,
    /**
     * Represents the privilege to delete or remove resources or data.
     */
    DELETE_PRIVILEGE,
    /**
     * Represents the privilege to update or edit existing resources or data.
     */
    UPDATE_PRIVILEGE

}
